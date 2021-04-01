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

    private ArrayList<ArrayList<Double>> throughPut = new ArrayList<ArrayList<Double>>();

    public double getExponential(Double lambda) {

        Random random = new Random();

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

        ArrayList<Buffer> inspectorOneBuffers = new ArrayList<>();

        ArrayList<Buffer> inspectorTwoBuffers = new ArrayList<>();

        Buffer bufferOne = new Buffer(); //buffer for inspector one and ws1
        Buffer bufferOneTwo = new Buffer();
        Buffer bufferOneThree = new Buffer();
        Buffer bufferTwoTwo = new Buffer();
        Buffer bufferTwoThree = new Buffer();

        inspectorOneBuffers.add(bufferOne);
        inspectorOneBuffers.add(bufferOneTwo);
        inspectorOneBuffers.add(bufferOneThree);

        inspectorTwoBuffers.add(bufferTwoTwo);
        inspectorTwoBuffers.add(bufferTwoThree);

        Workstation workstationOne = new Workstation(bufferOne, null, simulation, lambdaWs1);
        Workstation workstationTwo = new Workstation(bufferOneTwo, bufferTwoTwo, simulation, lambdaWs2);
        Workstation workstationThree = new Workstation(bufferOneThree, bufferTwoThree, simulation, lambdaWs3);

        Inspector inspectorOne = new Inspector(inspectorOneBuffers, 1, lambdaSP1, simulation); //handles component 1
        Inspector inspectorTwo = new Inspector(inspectorTwoBuffers, 2, lambdaSP22, lambdaSP23, simulation);
        //handles component 2

        //create a random component and give it to an inspector
        //for(int i = 0; i < 10; i++){

            inspectorOne.start();
            inspectorTwo.start();

            workstationOne.start();
            workstationTwo.start();
            workstationThree.start();


          //  for (int j = 0; j < 300; j++){

                //create random component

                Component component = new Component(Type.C1);
                inspectorOne.setComponent(component);

            //}
       // }
    }
}
