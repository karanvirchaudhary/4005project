import java.util.ArrayList;
import java.util.Random;

/*
    This class represents the first inspector and is extended by the InspectorTwo class
    As they both share similar properties, but InspectorTwo needs 2 lambda values as it is
    reponsible for servicing two components (C2 and C3)
 */
public class Inspector extends Thread{

    private ArrayList<Component> inventory;
    private boolean blocked;
    private int ID;
    private ArrayList<Buffer> buffer;
    private double lambdaValue;

    public Inspector(ArrayList<Buffer> buffer, int ID, double lambda){
        this.buffer = buffer;
        this.ID = ID;
        this.lambdaValue = lambda;
    }

    //Used to generate the random variate for the inspector
    public double generateRandomVariate(Random random, Double lambda) {
        return Math.log(1-random.nextDouble())/(-lambda);
    }
}