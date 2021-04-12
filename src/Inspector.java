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
    private int ID;
    private ArrayList<Buffer> buffer;
    private double lambdaValueOne = 0;
    private double lambdaValueTwo = 0;
    private Simulation simulation;
    private long blockedTime = 0L;


    private boolean end = false;
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

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public Component getComponent() {
        return component;
    }

    /**
     * Inspectors are gonna create their own components
     */
    public void run() {

        long startTimeTotal = System.nanoTime();

        while (!isInterrupted() || !end) {
            long startTime = System.nanoTime();
            //generating its own components
            if (ID == 1) { //only for inspector 1
                component = new Component(Type.C1);
            } else {
                component = new Component(Type.getRandomType()); //generate a random component

                //no use for C1 if we are inspector 2
                while (component.getComponentType() == Type.C1) {
                    component = new Component(Type.getRandomType()); //generate a random component if component is C1
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

            long testTime = System.nanoTime();

            try {
                Thread.sleep((long) sleepTime * 1000); //here to change scale
            } catch (InterruptedException e) {
                System.out.println(this.getName() + " interrupted");
                end = true;
                break;
            }

            long endTest = System.nanoTime();

            long totalT = endTest - testTime;

            if (ID != 1) {
                //specifically for Inspector 2

                /**
                 * Inspector 2 doesn't need to actually check for the smallest buffer, it just needs to put
                 * the component in the right buffer
                 */

                //find the buffer with the right component type
                for(Buffer b: buffer){
                    if(b.getBufferComponentType() == component.getComponentType()){
                        long time = b.put(component);
                        System.out.println("Inspector 2 putting " + component.getComponentType().toString() + " into buffer ");
                        if(time > 0){
                            //simulation.getinspectorTwoBlocked().add(time);
                            blockedTime += time;
                        }
                        break;
                    }
                }

            } else {

                /**
                 * Inspector 1 has to place C1 into the smallest C1 buffer with the first buffer being the priority
                 */

                ArrayList<Integer> bufferLengths = new ArrayList<>();
                for (int i = 0; i < buffer.size(); i++) {
                    bufferLengths.add(buffer.get(i).getSize());
                }

                //get buffer with smallest size
                int index = getIndexOfMinArray(bufferLengths);

                System.out.print("Inspector 1 putting into buffer ");
                if(index == 0){
                    System.out.print("WS1 \n");
                } else if (index == 1){
                    System.out.print("WS2 \n");
                } else if (index ==2){
                    System.out.print("WS3 \n");
                } else{
                    System.out.print("There is a tie. Giving component to WS3 \n");
                }

                long time = buffer.get(index).put(component);
                if(time > 0){
                    //simulation.getinspectorOneBlocked().add(time);
                    blockedTime += time;
                }

            }

            if(end){
                break;
            }

            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime; //time to create a component and place it

            if(component.getComponentType() == Type.C1){
                //inspector 1
                simulation.getServinsp1().add(timeElapsed);
            } else if(component.getComponentType() == Type.C2) {
                simulation.getServinsp22().add(timeElapsed);
            } else {
                simulation.getServinsp23().add(timeElapsed);
            }

        }

        long endTimeTotal = System.nanoTime();

        long total = endTimeTotal - startTimeTotal;

        //double proportion = (double) (blockedTime / total);

        if(ID == 1){
            simulation.getinspectorOneBlocked().add(blockedTime);
        } else {
            simulation.getinspectorTwoBlocked().add(blockedTime);
        }
    }

    /**
     * Give priority to WS3, then WS2, then WS1
     * @param array
     * @return
     */
    private int getIndexOfMinArray(ArrayList<Integer> array) {
        if (array.size() == 0)
            return -1;

        int index = 0;
        int min = array.get(index);


        if((array.get(0) == 0 & array.get(1) == 0 & array.get(2) == 0) ||
                (array.get(0) == 1 & array.get(1) == 1 & array.get(2) == 1)) {
            return 2;
        }


        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) < min) {
                min = array.get(i);
                index = i;
            }
        }
        return index;
    }
}