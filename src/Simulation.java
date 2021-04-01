import java.util.ArrayList;
import java.util.Random;

public class Simulation {

    //10 replications with 300 items each
    private ArrayList<ArrayList<Double>> servinsp1 = new ArrayList<ArrayList<Double>>();
    private ArrayList<ArrayList<Double>> servinsp22 = new ArrayList<ArrayList<Double>>();
    private ArrayList<ArrayList<Double>> servinsp23 = new ArrayList<ArrayList<Double>>();

    private ArrayList<ArrayList<Double>> ws1 = new ArrayList<ArrayList<Double>>();
    private ArrayList<ArrayList<Double>> ws2 = new ArrayList<ArrayList<Double>>();
    private ArrayList<ArrayList<Double>> ws3 = new ArrayList<ArrayList<Double>>();

    public double getExponential(Random random, Double lambda) {
        return  Math.log(1-random.nextDouble())/(-lambda);
    }

    public void readFile(String fileName){
        
    }

    public static void main(String[] args){




    }
}
