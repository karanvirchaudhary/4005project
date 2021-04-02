import java.util.ArrayList;
import java.util.List;
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

    public Component getComponent() {
        return component;
    }

    /**
     * Inspectors are gonna create their own components
     */
    public void run() {

        //generate 300 components
        while (run < 10) {

            if (ID == 1) { //only for inspector 1
                component = new Component(Type.C1);
            } else {
                component = new Component(Type.getRandomType()); //generate a random component

                //no use for C1 if we are inspector 2
                while (component.getComponentType() == Type.C1) {
                    component = new Component(Type.getRandomType()); //generate a random component

                }

            }
            double sleepTime = 0;

            //component is c1 and this inspector is inspector 1
            if (component.getComponentType() == Type.C1 && ID == 1) {
                sleepTime = simulation.getExponential(lambdaValueOne);
            } else if (component.getComponentType() == Type.C2 && ID == 2) {
                sleepTime = simulation.getExponential(lambdaValueOne);
            } else if (component.getComponentType() == Type.C3 && ID == 2) {
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


            ArrayList<Integer> bufferLengths = new ArrayList<>();
            for (int i = 0; i < buffer.size(); i++) {
                bufferLengths.add(buffer.get(i).getSize());
            }

            int index = getIndexOfMinArray(bufferLengths);

            if (ID != 1) {
                //specifically for Inspector 2

                System.out.println("Inspector 2 putting " + component.getComponentType().toString() + " into buffer ");
                if(index == 0){
                    System.out.print("WS2 \n");
                } else{
                    System.out.print("WS3 \n");
                }
                if (buffer.get(index).getBufferComponentType() == component.getComponentType()) {
                    buffer.get(index).put(component);
                } else {

                    if (index == 0) {

                        buffer.get(index + 1).put(component);
                    } else if (index == 1) {
                        buffer.get(0).put(component);
                    }

                }

            } else {
                System.out.println("Inspector 1 putting into buffer ");
                if(index == 0){
                    System.out.print("WS1 \n");
                } else if (index == 1){
                    System.out.print("WS2 \n");
                } else {
                    System.out.print("WS3 \n");
                }

                buffer.get(index).put(component);
            }
            run++;

        }

        System.out.println("Inspector 1 putting into buffer");
    }

    private int getIndexOfMinArray(ArrayList<Integer> array) {
        if (array.size() == 0)
            return -1;

        int index = 0;
        int min = array.get(index);

        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) < min) {
                min = array.get(i);
                index = i;
            }
        }
        return index;
    }


}