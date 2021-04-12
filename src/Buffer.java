import java.util.Stack;

public class Buffer{

    Stack<Component> components;
    private boolean hasSpace = true;
    private Type bufferType;
    private int priority; //0 being the lowest and 2 being the highest.

    public Buffer(Type type, int priority){
        components = new Stack<Component>();
        this.bufferType = type;
        this.priority = priority;
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
        long time = 0L;
        long start = 0L;
        while (components.size() == 2){

            try {
                start = System.nanoTime();
                wait();
                time = System.nanoTime() - start;
            } catch (InterruptedException e) {
                //e.printStackTrace();
                time = System.nanoTime() - start;
                System.out.println("Process interrupted");
                return time;
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