import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Simulation {

    //10 replications with 300 items each
    private ArrayList<ArrayList<Double>> servinsp1 = new ArrayList<ArrayList<Double>>();
    private ArrayList<ArrayList<Double>> servinsp22 = new ArrayList<ArrayList<Double>>();
    private ArrayList<ArrayList<Double>> servinsp23 = new ArrayList<ArrayList<Double>>();

    private ArrayList<ArrayList<Double>> ws1 = new ArrayList<ArrayList<Double>>();
    private ArrayList<ArrayList<Double>> ws2 = new ArrayList<ArrayList<Double>>();
    private ArrayList<ArrayList<Double>> ws3 = new ArrayList<ArrayList<Double>>();

    public double getExponential(Random random, Double lambda) {
        return Math.log(1-random.nextDouble())/(-lambda);
    }

    /**
     * Specifically for the files
     * @param fileName file name
     * @throws FileNotFoundException
     */
    public double getLambda(String fileName) throws FileNotFoundException {
        //get the lambda of each data file
        double sum = 0;
        File file = new File("./src/data/" + fileName);

        Scanner scanner = new Scanner(file);
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            if(line.length() < 2) {
                continue;
            }
            sum += Double.parseDouble(line);
        }

        double mean = sum / 300;
        double lambda = 1 /mean;

        return lambda;
    }

    public Simulation(){}

    public static void main(String[] args) throws FileNotFoundException {

        Simulation simulation = new Simulation();
        Random rand = new Random();

        double lambdaSP1 = simulation.getLambda("servinsp1.dat");
        double lambdaSP22 = simulation.getLambda("servinsp22.dat");
        double lambdaSP23 = simulation.getLambda("servinsp23.dat");

        double lambdaWs1 = simulation.getLambda("ws1.dat");
        double lambdaWs2 = simulation.getLambda("ws2.dat");
        double lambdaWs3 = simulation.getLambda("ws3.dat");

        double getVariate = simulation.getExponential(rand,lambdaWs1);
        System.out.println(getVariate);
        System.out.println(lambdaWs1);
    }
}
