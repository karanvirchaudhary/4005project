import java.util.Stack;

public class Buffer{

    Stack<Component> components;
    private boolean hasSpace = true;

    public Buffer(){
        components = new Stack<Component>();
    }

    public synchronized boolean getSpace(){

        return components.size() != 2;

    }

    public synchronized void put(Component component){

        //if its not full
        while (components.size() == 2){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        components.add(component);

        //if it now has no space
        if(components.size() == 2){
            hasSpace = false;
        }

        notifyAll();

    }

    public synchronized Component take(){

        //cant take if it is empty
        while (components.size() == 0){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Component component = components.pop();
        hasSpace = true;
        notifyAll();
        return component;
    }



}