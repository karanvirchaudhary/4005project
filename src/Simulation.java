import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Simulation {

    /*The follow arrays are used to keep track of the time for each iteration.
    Each index represents an iteration, and the value of the index indicates the total inspection time for that inspector and the
    component they were working with.
    Additionally, ws1, ws2 and ws3 track the timing of each workstation for an iteration.
    */
    private  ArrayList<Long> servinsp1 = new ArrayList<Long>();
    private  ArrayList<Long> inspectorOneBlocked = new ArrayList<Long>(); //Keeps track of how long inspector 1 was blocked for during an iteration
    private  ArrayList<Long> servinsp22 = new ArrayList<Long>();
    private  ArrayList<Long> inspectorTwoBlocked = new ArrayList<Long>();  //Keeps track of how long inspector 2 with C2 was blocked for during an iteration
    private  ArrayList<Long> servinsp23 = new ArrayList<Long>();
    private  ArrayList<Long> servinsp23Blocked = new ArrayList<Long>(); //Keeps track of how long inspector 2 with C3 was blocked for during an iteration

    private  ArrayList<Long> ws1 = new ArrayList<Long>();
    private  ArrayList<Long> ws2 = new ArrayList<Long>();
    private  ArrayList<Long> ws3 = new ArrayList<Long>();


    /*
    * Next we are creating a ArrayLists similar to the ones above but to keep track of the number of products produced by each workstation.
    * Index 0 would contain the # of products produced by that workstation during the 1st iteration, and so on.
    * This will help calculate the throughput for the system.
    */
    private ArrayList<Integer> ws1ProductTracker = new ArrayList<Integer>();
    private  ArrayList<Integer> ws2ProductTracker = new ArrayList<Integer>();
    private  ArrayList<Integer> ws3ProductTracker = new ArrayList<Integer>();

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

    public ArrayList<Integer> getWs1ProductTracker() {return ws1ProductTracker;}
    public ArrayList<Integer> getWs2ProductTracker() {return ws2ProductTracker;}
    public ArrayList<Integer> getWs3ProductTracker() {return ws3ProductTracker;}




    /**
     * Gets the average in a list of timings
     * @param array
     * @return
     */
    public double getAverage(ArrayList<Long> array){
        if(array.size() == 0){
            return 0.0;
        }

        Long average = 0L;
        for(Long num: array){
            average += num;
        }
        double avg = (double)(average / (long) array.size()) / 1000000000L;
        return avg;
    }
    public double getTotal(ArrayList<Long> array){
        if(array.size() == 0){
            return (long) 0.0;
        }

        Long total = 0L;
        for(Long num: array){
            total += num/1000000000L;
        }
        return total;
    }

    public void printTimeStatistics(HashMap<Integer, Double> averageMap){
        Iterator it = averageMap.entrySet().iterator();
        Long average = 0L;
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println("iteration " + pair.getKey() + " average time: " + pair.getValue());
        }
    }
    public void printVarianceStats(HashMap<Integer, Double> varianceMap){
        Iterator it = varianceMap.entrySet().iterator();
        Long average = 0L;
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println("iteration " + pair.getKey() + " Variance: " + pair.getValue());
        }
    }
    public double calculateVariance(ArrayList<Long>array, double mean){
        if(array.size() != 0) {
            double variance = 0;
            for (double a : array) {
                variance += (a - mean) * (a - mean);
            }
            return variance / (array.size() - 1);
        }else{
            return 0.0;
        }

    }

    /*
    This function will be used to print out the product collection statistics for each workstation.
     */
    public void printProductStatistics(HashMap<Integer, Integer> productMap){
        for(Integer i =0; i<productMap.size();i++){
            System.out.println("iteration: " + i + " Products Produced: " + productMap.get(i));
        }
    }

    /*
    The following function is used to calculate the total products at the very end of the simulation.
     */
    public Integer getFinalProduction(HashMap<Integer, Integer> productMap){
        Integer total =0;
        for(Integer i =0; i<productMap.size();i++){
            total = total+ productMap.get(i);
        }
        return total;
    }

    /**
     *
     * @param productMap products produce
     * @return
     */
    public double calculateThroughput(HashMap<Integer, Integer> productMap, int simTime){
        double productTotal = getFinalProduction(productMap);
        System.out.println("---------------------- Printing Throughput ------------");
        return productTotal/simTime;
    }

    public static void main(String[] args) throws FileNotFoundException {

        Simulation simulation = new Simulation();
        int iterations = 3;
        int currentIteration = 0;
        int simTimeMinutes = 5; //in minutes
        int simTime = simTimeMinutes * 60000;

        //collection for each repetition. They keep track of the average time for each iteration.
        HashMap<Integer, Double> inspector1Performance = new HashMap<>();
        HashMap<Integer, Double> inspector2Performance = new HashMap<>();

        HashMap<Integer, Double> inspector1Blocked = new HashMap<>();
        HashMap<Integer, Double> inspector2Blocked = new HashMap<>();
        HashMap<Integer, Double> inspector23Performance = new HashMap<>();

        HashMap<Integer, Double> ws1Performance = new HashMap<>();
        HashMap<Integer, Double> ws2Performance = new HashMap<>();
        HashMap<Integer, Double> ws3Performance = new HashMap<>();

        //The following collections will be used to keep track of the number of products produced by each workstation in an iteration
        HashMap<Integer, Integer> ws1ProductResults = new HashMap<>();
        HashMap<Integer, Integer> ws2ProductResults = new HashMap<>();
        HashMap<Integer, Integer> ws3ProductResults = new HashMap<>();

        //The following collections will be used to keep track of the sample variance of each entity for an iteration
        HashMap<Integer, Double> ws1VarianceResults = new HashMap<>();
        HashMap<Integer, Double> ws2VarianceResults = new HashMap<>();
        HashMap<Integer, Double> ws3VarianceResults = new HashMap<>();
        HashMap<Integer, Double> insp1VarianceResults = new HashMap<>();
        HashMap<Integer, Double> insp22VarianceResults = new HashMap<>();
        HashMap<Integer, Double> insp23VarianceResults = new HashMap<>();

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

            Buffer bufferOne = new Buffer(Type.C1, 0); //buffer for inspector one and ws1
            Buffer bufferOneTwo = new Buffer(Type.C1, 1);
            Buffer bufferOneThree = new Buffer(Type.C1,2);

            Buffer bufferTwoTwo = new Buffer(Type.C2, 0);
            Buffer bufferTwoThree = new Buffer(Type.C3,0);

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

            inspectorOne.setEnd(true);
            inspectorTwo.setEnd(true);

            workstationOne.interrupt();
            workstationTwo.interrupt();
            workstationThree.interrupt();

            workstationOne.setEnd(true);
            workstationTwo.setEnd(true);
            workstationThree.setEnd(true);

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

            //get time results for inspectors
            ArrayList<Long> insp1 = simulation.getServinsp1();
            ArrayList<Long> insp22 = simulation.getServinsp22();
            ArrayList<Long> insp23 = simulation.getServinsp23();

            //get time results for workstations
            ArrayList<Long> ws1 = simulation.getWs1();
            ArrayList<Long> ws2 = simulation.getWs2();
            ArrayList<Long> ws3 = simulation.getWs3();

            //Calculating the average and total for the time results for the iteration.
            double averageInsp1 = simulation.getAverage(insp1); //gets the average for inspector 1
            double averageInsp22 = simulation.getAverage(insp22);
            double averageInsp23 = simulation.getAverage(insp23);

            double averageWs1 = simulation.getAverage(ws1);
            double averageWs2 = simulation.getAverage(ws2);
            double averageWs3 = simulation.getAverage(ws3);

            double varianceWs1 = simulation.calculateVariance(ws1, averageWs1);
            System.out.println("WS1 VARIANCE IS: " + varianceWs1);
            double varianceWs2 = simulation.calculateVariance(ws2, averageWs2);
            System.out.println("WS2 VARIANCE IS: " + varianceWs2);
            double varianceWs3 = simulation.calculateVariance(ws3, averageWs3);
            System.out.println("WS3 VARIANCE IS: " + varianceWs3);
            double varianceInsp1 = simulation.calculateVariance(insp1, averageInsp1);
            System.out.println("INSP1 VARIANCE IS: " + varianceInsp1);
            double varianceInsp22 = simulation.calculateVariance(insp22, averageInsp22);
            System.out.println("INSP1 VARIANCE IS: " + varianceInsp22);
            double varianceInsp23 = simulation.calculateVariance(insp23, averageInsp23);
            System.out.println("INSP1 VARIANCE IS: " + varianceInsp23);


            //Next we will be retrieving the total amount of products produced by a workstation during that iteration
            ArrayList<Integer> totalWS1Products = simulation.getWs1ProductTracker();
            Integer WS1Products = totalWS1Products.size();

            ArrayList<Integer> totalWS2Products = simulation.getWs2ProductTracker();
            Integer WS2Products = totalWS2Products.size();

            ArrayList<Integer> totalWS3Products = simulation.getWs3ProductTracker();
            Integer WS3Products = totalWS3Products.size();

            //Printing out the average time for the 5 entities.
            System.out.println("The inspection time for inspector 1 was " + "Avg: "+ averageInsp1 + " seconds " );
            System.out.println("The inspection time for inspector 2 w C2 was " + "Avg: "+ averageInsp22 + " seconds ");
            System.out.println("The inspection time for inspector 2 w C3 was " + "Avg: "+ averageInsp23 + " seconds ");
            System.out.println("--------------------------------------------------------------------");
            System.out.println("Workstation 1: " + "Average Time: " + averageWs1 + " seconds" + "Toys Produced:" + WS1Products);
            System.out.println("Workstation 2: " + "Average Time: " + averageWs2 + " seconds" + "Toys Produced:" + WS2Products);
            System.out.println("Workstation 3: " + "Average Time: " + averageWs3 + " seconds" + "Toys Produced:" + WS3Products);

            //Storing this iteration's data into the hashmap. The Hashmap will be used to print out
            //the final set of statistics at the end of the simulation.
            inspector1Performance.put(currentIteration,averageInsp1);
            inspector2Performance.put(currentIteration, averageInsp22);
            inspector23Performance.put(currentIteration, averageInsp23);
            ws1Performance.put(currentIteration,  averageWs1);
            ws2Performance.put(currentIteration,  averageWs2);
            ws3Performance.put(currentIteration,  averageWs3);

            ws1ProductResults.put(currentIteration, WS1Products);
            ws2ProductResults.put(currentIteration, WS2Products);
            ws3ProductResults.put(currentIteration, WS3Products);

            ws1VarianceResults.put(currentIteration, varianceWs1);
            ws2VarianceResults.put(currentIteration, varianceWs2);
            ws3VarianceResults.put(currentIteration, varianceWs3);
            insp1VarianceResults.put(currentIteration, varianceInsp1);
            insp22VarianceResults.put(currentIteration, varianceInsp22);
            insp23VarianceResults.put(currentIteration, varianceInsp23);

            /*
            inspector1Blocked.put(currentIteration, simulation.getAverage(simulation.getinspectorOneBlocked()));
            inspector2Blocked.put(currentIteration, simulation.getAverage(simulation.getinspectorTwoBlocked()));
*/
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentIteration++;

            simulation.getWs1().clear();
            simulation.getWs2().clear();
            simulation.getWs3().clear();
            simulation.getServinsp1().clear();
            simulation.getServinsp22().clear();
            simulation.getServinsp23().clear();
            simulation.getWs1ProductTracker().clear();
            simulation.getWs2ProductTracker().clear();
            simulation.getWs3ProductTracker().clear();
        }

        System.out.println("SIMULATION FINISHED, GENERATING RESULTS");
        System.out.println("==============================================================================");

        System.out.println("Inspector one work time: ");
        simulation.printTimeStatistics(inspector1Performance);
        simulation.printVarianceStats(insp1VarianceResults);
        System.out.println("Inspector two with C2 work time: ");
        simulation.printTimeStatistics(inspector2Performance);
        simulation.printVarianceStats(insp22VarianceResults);
        System.out.println("Inspector two with C3 work time: ");
        simulation.printTimeStatistics(inspector23Performance);
        simulation.printVarianceStats(insp23VarianceResults);

        //Workstation 1
        System.out.println("Workstation 1 Production: ");
        simulation.printProductStatistics(ws1ProductResults);
        System.out.println(ws1ProductResults.get(0));
        Integer ws1TotalResult = simulation.getFinalProduction(ws1ProductResults);
        System.out.println("Total products produced: " + ws1TotalResult);
        simulation.printVarianceStats(ws1VarianceResults);

        double ws1Throughput = simulation.calculateThroughput(ws1ProductResults,simTime);
        System.out.println("Workstation 1 throughput: " + ws1Throughput);


        //Workstation 2
        System.out.println("Workstation 2 Production: ");
        simulation.printProductStatistics(ws2ProductResults);
        Integer ws2TotalResult = simulation.getFinalProduction(ws2ProductResults);
        System.out.println("Total products produced: " + ws2TotalResult);
        simulation.printVarianceStats(ws2VarianceResults);

        double ws2Throughput = simulation.calculateThroughput(ws2ProductResults,simTime);
        System.out.println("Workstation 2 throughput: " + ws2Throughput);

        //Workstation 3
        System.out.println("Workstation 3 Production: ");
        simulation.printProductStatistics(ws3ProductResults);
        Integer ws3TotalResult = simulation.getFinalProduction(ws3ProductResults);
        System.out.println("Total products produced: " + ws3TotalResult);
        simulation.printVarianceStats(ws1VarianceResults);

        double ws3Throughput = simulation.calculateThroughput(ws3ProductResults,simTime);
        System.out.println("Workstation 3 throughput: " + ws3Throughput);

    }
}
