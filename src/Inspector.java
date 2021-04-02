import java.util.ArrayList;
import java.util.Random;

/*
    This class represents the first inspector and is extended by the InspectorTwo class
    As they both share similar properties, but InspectorTwo needs 2 lambda values as it is
    reponsible for servicing two components (C2 and C3)
 */
public class Inspector extends Thread {

    private ArrayList<Component> inventory;
    private boolean blocked;
    private int ID;
    private ArrayList<Buffer> buffer;
    private double lambdaValueOne = 0;
    private double lambdaValueTwo = 0;
    private Simulation simulation;

    private int run;

    private Component component; //current component being handled

    public Inspector(ArrayList<Buffer> buffer, int ID, double lambda, Simulation simulation) {
        this.buffer = buffer;
        this.ID = ID;
        this.lambdaValueOne = lambda;
        this.simulation = simulation;
    }

    public Inspector(ArrayList<Buffer> buffer, int ID, double lambdaOne, double lambdaTwo, Simulation simulation) {
        this.buffer = buffer;
        this.ID = ID;
        this.lambdaValueOne = lambdaOne;
        this.simulation = simulation;
        this.lambdaValueTwo = lambdaTwo;
    }


    public void setComponent(Component component) {
        this.component = component;
    }

    public Component getComponent(){
        return component;
    }

    /**
     * Inspectors are gonna create their own components
     */
    public void run() {

        //generate 300 components
        while (true) {

            if(ID == 1){ //only for inspector 1
                component = new Component(Type.C1);
            } else {
                component = new Component(Type.getRandomType()); //generate a random component

                //no use for C1 if we are inspector 2
                while (component.getComponentType() == Type.C1){
                    component = new Component(Type.getRandomType()); //generate a random component

                }

            }
            double sleepTime = 0;

            //component is c1 and this inspector is inspector 1
            if(component.getComponentType() == Type.C1 && ID == 1){
                sleepTime = simulation.getExponential(lambdaValueOne);
            } else if(component.getComponentType() == Type.C2 && ID == 2){
                sleepTime = simulation.getExponential(lambdaValueOne);
            } else if (component.getComponentType() == Type.C3 && ID == 2){
                sleepTime = simulation.getExponential(lambdaValueTwo);
            } else {
                System.out.println("Error");
            }

            try {
                Thread.sleep((long) sleepTime * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }

            //for each buffer this inspector has access to add the component to it
            int smallest = 0;
            for(int i = 0; i < buffer.size(); i++){
                if(buffer.get(i).getSize() <= smallest && (buffer.get(i).getBufferComponentType() == component.getComponentType())){
                    smallest = buffer.get(i).getSize();
                    System.out.println("adding to buffer");
                    buffer.get(i).put(component); //smallest buffer
                    break;
                }
            }



            /**
            for(int i = 0; i < buffer.size(); i++){

                //get the smallest buffer that accepts this component type



                if(buffer.get(i).getBufferComponentType() == component.getComponentType()){
                    //has space to add and is the correct buffer

                    if(buffer.get(i).getSize() == 0){
                        buffer.get(i).put(component);
                    } else if(buffer.get(i).getSize() == 1) {
                        buffer.get(i).put(component);
                    } else {
                        System.out.println("This buffer is full");
                        continue; //buffer is full
                    }

                    System.out.println("put into buffer");
                    break;
                } else {
                    System.out.println("wrong buffer");
                }
            }
             **/
            run++;

        }

    }

}