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

    //Dont' want to use nextDouble
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

        double lambdaSP1 = simulation.getLambda("servinsp1.dat"); //Inspector 1 serving time
        double lambdaSP22 = simulation.getLambda("servinsp22.dat"); //Inspector 2 serving time for component 2
        double lambdaSP23 = simulation.getLambda("servinsp23.dat"); //Inspector 2 serving time for component 3

        double lambdaWs1 = simulation.getLambda("ws1.dat");
        double lambdaWs2 = simulation.getLambda("ws2.dat");
        double lambdaWs3 = simulation.getLambda("ws3.dat");

        double getVariate = simulation.getExponential(rand,lambdaWs1);

        System.out.println("Inspector 1 serving lamba is" + lambdaSP1);
        System.out.println(getVariate);
        System.out.println(lambdaWs1);


        ArrayList<Buffer> inspectorOneBuffers = new ArrayList<>();
        ArrayList<Buffer> inspectorTwoBuffers = new ArrayList<>();

        Buffer bufferOne = new Buffer(); //buffer for inspector one and ws1
        Buffer bufferOneTwo = new Buffer();
        Buffer bufferOneThree = new Buffer();
        Buffer bufferTwoTwo = new Buffer();
        Buffer bufferTwoThree = new Buffer();

        Workstation workstationOne = new Workstation(bufferOne, null);
        Workstation workstationTwo = new Workstation(bufferOneTwo, bufferTwoTwo);
        Workstation workstationThree = new Workstation(bufferOneThree, bufferTwoThree);

        Inspector inspectorOne = new Inspector(inspectorOneBuffers, 1, lambdaSP1); //handles component 1
        Inspector inspectorTwoTwo = new InspectorTwo(inspectorTwoBuffers, 2, lambdaSP22, lambdaSP23); //handles component 2
        //Inspector inspectorTwoThree = new Inspector(bufferFive, 3); //handles components 3

        for(int i = 0; i < 10; i++){
            for (int j = 0; j < 300; j++){

                //create random component


            }
        }
    }
}
