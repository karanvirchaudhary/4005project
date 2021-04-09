import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Simulation {

    private  ArrayList<Long> servinsp1 = new ArrayList<Long>();
    private  ArrayList<Long> inspectorOneBlocked = new ArrayList<Long>();
    private  ArrayList<Long> servinsp22 = new ArrayList<Long>();
    private  ArrayList<Long> inspectorTwoBlocked = new ArrayList<Long>();
    private  ArrayList<Long> servinsp23 = new ArrayList<Long>();
    private  ArrayList<Long> servinsp23Blocked = new ArrayList<Long>();

    private  ArrayList<Long> ws1 = new ArrayList<Long>();
    private  ArrayList<Long> ws2 = new ArrayList<Long>();
    private  ArrayList<Long> ws3 = new ArrayList<Long>();

    public double getExponential(Double lambda) {

        Random random = new Random();

        return Math.log(1 - random.nextDouble()) / (-lambda);
    }

    /**
     * Specifically for the files
     *
     * @param fileName file name
     * @throws FileNotFoundException
     */
    public double getLambda(String fileName) throws FileNotFoundException {
        //get the lambda of each data file
        double sum = 0;
        File file = new File("./src/data/" + fileName);

        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.length() < 2) {
                continue;
            }
            sum += Double.parseDouble(line);
        }

        double mean = sum / 300;
        double lambda = 1 / mean;

        return lambda;
    }

    public Simulation() {}

    public ArrayList<Long> getServinsp1() {
        return servinsp1;
    }

    public ArrayList<Long> getinspectorOneBlocked() {
        return inspectorOneBlocked;
    }

    public ArrayList<Long> getServinsp22() {
        return servinsp22;
    }

    public ArrayList<Long> getinspectorTwoBlocked() {
        return inspectorTwoBlocked;
    }

    public ArrayList<Long> getServinsp23() {
        return servinsp23;
    }

    public ArrayList<Long> getServinsp23Blocked() {
        return servinsp23Blocked;
    }

    public ArrayList<Long> getWs1() {
        return ws1;
    }

    public ArrayList<Long> getWs2() {
        return ws2;
    }

    public ArrayList<Long> getWs3() {
        return ws3;
    }


    public static void main(String[] args) throws FileNotFoundException {

        Simulation simulation = new Simulation();
        int iterations = 3;
        int currentIteration = 0;
        int simTimeMinutes = 25; //in minutes
        int simTime = simTimeMinutes * 60000;

        //collection for each repetition
        HashMap<Integer, Long> inspector1Performance = new HashMap<>();
        HashMap<Integer, Long> inspector2Performance = new HashMap<>();
        HashMap<Integer, Long> ws1Performance = new HashMap<>();

        System.out.println("SIMULATION STARTING FOR " + (simTimeMinutes * iterations) + " MINUTES.");
        System.out.println("==============================================================================");

        double lambdaSP1 = simulation.getLambda("servinsp1.dat"); //Inspector 1 serving time
        double lambdaSP22 = simulation.getLambda("servinsp22.dat"); //Inspector 2 serving time for component 2
        double lambdaSP23 = simulation.getLambda("servinsp23.dat"); //Inspector 2 serving time for component 3

        double lambdaWs1 = simulation.getLambda("ws1.dat");
        double lambdaWs2 = simulation.getLambda("ws2.dat");
        double lambdaWs3 = simulation.getLambda("ws3.dat");

        while(currentIteration < iterations){

            Random rand = new Random();
            ArrayList<Buffer> inspectorOneBuffers = new ArrayList<>();
            ArrayList<Buffer> inspectorTwoBuffers = new ArrayList<>();

            Buffer bufferOne = new Buffer(Type.C1); //buffer for inspector one and ws1
            Buffer bufferOneTwo = new Buffer(Type.C1);
            Buffer bufferOneThree = new Buffer(Type.C1);

            Buffer bufferTwoTwo = new Buffer(Type.C2);
            Buffer bufferTwoThree = new Buffer(Type.C3);

            inspectorOneBuffers.add(bufferOne);
            inspectorOneBuffers.add(bufferOneTwo);
            inspectorOneBuffers.add(bufferOneThree);

            inspectorTwoBuffers.add(bufferTwoTwo);
            inspectorTwoBuffers.add(bufferTwoThree);

            Workstation workstationOne = new Workstation(bufferOne, null, simulation, lambdaWs1, 1);
            workstationOne.setName("Workstation 1");
            Workstation workstationTwo = new Workstation(bufferOneTwo, bufferTwoTwo, simulation, lambdaWs2, 2);
            workstationTwo.setName("Workstation 2");
            Workstation workstationThree = new Workstation(bufferOneThree, bufferTwoThree, simulation, lambdaWs3, 3);
            workstationThree.setName("Workstation 3");

            Inspector inspectorOne = new Inspector(inspectorOneBuffers, 1, lambdaSP1, simulation); //handles component 1
            inspectorOne.setName("Inspector 1");
            Inspector inspectorTwo = new Inspector(inspectorTwoBuffers, 2, lambdaSP22, lambdaSP23, simulation);
            inspectorTwo.setName("Inspector 2");

            System.out.println("STARTING ITERATION " + currentIteration + ".");
            System.out.println("==============================================================================");

            inspectorOne.start();
            inspectorTwo.start();

            workstationOne.start();
            workstationTwo.start();
            workstationThree.start();

            //let the simulation run for simTimeMinutes amount of time
            try {
                Thread.sleep(simTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }



            inspectorOne.interrupt();
            inspectorTwo.interrupt();

            workstationOne.interrupt();
            workstationTwo.interrupt();
            workstationThree.interrupt();

            System.out.println("Waiting for all programs to terminate...");
            while (inspectorOne.getState() != Thread.State.TERMINATED
                    && inspectorTwo.getState() != Thread.State.TERMINATED
                    && workstationOne.getState() != Thread.State.TERMINATED
                    && workstationTwo.getState() != Thread.State.TERMINATED
                    && workstationThree.getState() != Thread.State.TERMINATED) {
            }
            System.out.println("All programs terminated");

            System.out.println("ITERATION " + currentIteration + " COMPLETED, COLLECTING RESULTS.");
            System.out.println("==============================================================================");

            //get results for inspectors
            ArrayList<Long> insp1 = simulation.getServinsp1();
            ArrayList<Long> insp22 = simulation.getServinsp22();
            ArrayList<Long> insp23 = simulation.getServinsp23();

            //get results for workstations
            ArrayList<Long> ws1 = simulation.getWs1();
            ArrayList<Long> ws2 = simulation.getWs2();
            ArrayList<Long> ws3 = simulation.getWs3();

            Long average = 0L;
            for(Long num: insp1){
                average += num;
            }
            double avg = (double)(average / (long) insp1.size()) / 1000000000L;


            System.out.println("The average inspection time for inspector 1 was " + avg + " seconds");
            inspector1Performance.put(currentIteration,(long)avg);

            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        System.out.println("SIMULATION FINISHED, GENERATING RESULTS");
        System.out.println("==============================================================================");

        System.out.println("Inspector one work time: ");
        Iterator it = inspector1Performance.entrySet().iterator();
        Long average = 0L;
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println("iteration " + pair.getKey() + " average time: " + pair.getValue());
        }
        currentIteration++;
    }
}
