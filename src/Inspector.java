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

    public void run() {

        while (run < 300) {

            //don't proceed until this inspector has a component
            while (component == null) {
                //just hold on here
            }

            double sleepTime = 0;
            //first inspector or second inspector
            if (component.getComponentType() == Type.C1 || component.getComponentType() == Type.C2) {
                sleepTime = simulation.getExponential(lambdaValueOne);
            } else {

                //only second inspector has access to C3
                sleepTime = simulation.getExponential(lambdaValueTwo);
            }

            try {
                Thread.sleep((long) sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }

            //for each buffer this inspector has access to add the component to it
            for(Buffer temp: buffer){
                if(temp.getSpace()){
                    //has space to add
                    temp.put(component);
                    System.out.println("put into buffer");
                    break;
                } else {
                    System.out.println("this buffer is full");
                }
            }

            component = null;
            run++;

        }

    }

}