import java.util.Stack;

public class Buffer{

    Stack<Component> components;
    boolean isEmpty = true;

    public Buffer(){
        components = new Stack<Component>();
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
        isEmpty = false;
        notifyAll();

    }

    public synchronized Component take(){

        while (isEmpty){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Component component = components.pop();
        if(components.size() == 0){
            isEmpty = true;
        }
        notifyAll();
        return component;
    }



}