import java.util.Stack;

public class Buffer{

    Stack<Component> components;
    private boolean hasSpace = true;
    private Type bufferType;

    public Buffer(Type type){
        components = new Stack<Component>();
        this.bufferType = type;
    }

    public synchronized boolean getSpace(){
        notifyAll();
        return components.size() != 2;
    }

    public synchronized int getSize(){
        return components.size();
    }

    public Type getBufferComponentType(){
        return bufferType;
    }

    /**
     * Put a component into the buffer, if the process that invokes this has to go into the wait then
     * time the amount of time in wait
     * @param component
     */
    public synchronized long put(Component component){

        //if its not full
        long time = 0;
        long start = 0;
        while (components.size() == 2){

            try {
                start = System.nanoTime();
                wait();
                time = System.nanoTime() - start;
            } catch (InterruptedException e) {
                //e.printStackTrace();
                System.out.println("Process interrupted");
                return 0;
            }
        }
        components.add(component);
        //if it now has no space
        if(components.size() == 2){
            hasSpace = false;
        }

        notifyAll();

        if(time == 0){
            //means that the program didn't wait
            return 0;
        } else {
            return time;
        }
    }

    public synchronized Component take(){

        //cant take if it is empty
        while (components.size() == 0){
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Process interrupted");
                return null;
            }
        }

        Component component = components.pop();
        hasSpace = true;
        notifyAll();
        return component;
    }



}