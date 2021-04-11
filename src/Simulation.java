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

    public Integer getProduction(ArrayList<Integer> array){
        if(array.size()== 0){
            return 0;
        }
        Integer total =0;
        for(Integer num: array){
            total += num;
        }
        return total;
    }

    public void printTimeStatistics(HashMap<Integer, Long> performanceMap){
        Iterator it = performanceMap.entrySet().iterator();
        Long average = 0L;
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println("iteration " + pair.getKey() + " average time: " + pair.getValue());
        }
    }
    public Integer printProductStatistics(HashMap<Integer, Integer> productMap){
        Integer total =0;
        for(Integer i =0; i<productMap.size();i++){
            total = total + productMap.get(i);
            System.out.println("iteration: " + i + " Products Produced: " + total);
        }
        return total;
    }
    public long getTotalTime(HashMap<Integer, Long> productMap){
        long total =0;
        for(Integer i =0; i<productMap.size();i++){
            total = total + productMap.get(i);
            System.out.println("iteration: " + i + " Products Produced: " + total);
        }
        return total;
    }
    public double calculateThroughput(HashMap<Integer, Integer> productMap, HashMap<Integer, Long>totalMap){
        double productTotal = printProductStatistics(productMap);
        Long totalTime = getTotalTime(totalMap);
        return productTotal/totalTime;
    }


    public static void main(String[] args) throws FileNotFoundException {

        Simulation simulation = new Simulation();
        int iterations = 3;
        int currentIteration = 0;
        int simTimeMinutes = 2; //in minutes
        int simTime = simTimeMinutes * 60000;

        //collection for each repetition. They keep track of the average time for each iteration.
        HashMap<Integer, Long> inspector1Performance = new HashMap<>();
        HashMap<Integer, Long> inspector2Performance = new HashMap<>();
        HashMap<Integer, Long> ws1Performance = new HashMap<>();
        HashMap<Integer, Long> ws2Performance = new HashMap<>();
        HashMap<Integer, Long> ws3Performance = new HashMap<>();

        //collection for each repetition. They keep track of the total time for each iteration.
        HashMap<Integer, Long> inspector1TotalPerformance = new HashMap<>();
        HashMap<Integer, Long> inspector2TotalPerformance = new HashMap<>();
        HashMap<Integer, Long> ws1TotalPerformance = new HashMap<>();
        HashMap<Integer, Long> ws2TotalPerformance = new HashMap<>();
        HashMap<Integer, Long> ws3TotalPerformance = new HashMap<>();

        //The following collections will be used to keep track of the number of products produced by each workstation in an iteration
        HashMap<Integer, Integer> ws1ProductResults = new HashMap<>();
        HashMap<Integer, Integer> ws2ProductResults = new HashMap<>();
        HashMap<Integer, Integer> ws3ProductResults = new HashMap<>();

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

            double totalInsp1 = simulation.getTotal(insp1);
            double totalInsp22 = simulation.getTotal(insp22);
            double totalInsp23 = simulation.getTotal(insp23);

            double totalWs1 = simulation.getTotal(ws1);
            double totalWs2 = simulation.getTotal(ws2);
            double totalWs3 = simulation.getTotal(ws3);
            System.out.println(totalInsp1);


            //Next we will be retrieving the total amount of products produced by a workstation during that iteration
            ArrayList<Integer> totalWS1Products = simulation.getWs1ProductTracker();
            Integer WS1Products = totalWS1Products.size();

            ArrayList<Integer> totalWS2Products = simulation.getWs2ProductTracker();
            Integer WS2Products = totalWS2Products.size();

            ArrayList<Integer> totalWS3Products = simulation.getWs3ProductTracker();
            Integer WS3Products = totalWS3Products.size();


            //Printing out the average time for the 5 entities.
            System.out.println("The inspection time for inspector 1 was " + "Avg: "+ averageInsp1 + " seconds " + "Total: "+totalInsp1);
            System.out.println("The inspection time for inspector 2 w C2 was " + "Avg: "+ averageInsp22 + " seconds " + "Total: "+totalInsp22);
            System.out.println("The inspection time for inspector 2 w C3 was " + "Avg: "+ averageInsp23 + " seconds " + "Total: "+totalInsp23);
            System.out.println("--------------------------------------------------------------------");
            System.out.println("Workstation 1: " + "Average Time: " + averageWs1 + " seconds" + "Toys Produced:" + WS1Products);
            System.out.println("Workstation 2: " + "Average Time: " + averageWs2 + " seconds" + "Toys Produced:" + WS2Products);
            System.out.println("Workstation 3: " + "Average Time: " + averageWs3 + " seconds" + "Toys Produced:" + WS3Products);

            //Getting the time performance statistics for all 5 entities
            inspector1Performance.put(currentIteration,(long)averageInsp1);
            inspector2Performance.put(currentIteration, (long)averageInsp22);
            ws1Performance.put(currentIteration, (long) averageWs1);
            ws2Performance.put(currentIteration, (long) averageWs2);
            ws3Performance.put(currentIteration, (long) averageWs3);

            inspector1TotalPerformance.put(currentIteration, (long)totalInsp1);
            inspector2TotalPerformance.put(currentIteration, (long)totalInsp22);
            ws1TotalPerformance.put(currentIteration, (long) totalWs1);
            ws2TotalPerformance.put(currentIteration, (long) totalWs2);
            ws3TotalPerformance.put(currentIteration, (long) totalWs3);

            ws1ProductResults.put(currentIteration,(Integer) WS1Products);
            ws2ProductResults.put(currentIteration,(Integer) WS2Products);
            ws3ProductResults.put(currentIteration,(Integer) WS3Products);


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
        System.out.println("Inspector two work time: ");
        simulation.printTimeStatistics(inspector2Performance);

        //Workstation 1
        System.out.println("Workstation 1 Production: ");
        Integer ws1Final = simulation.printProductStatistics(ws1ProductResults);
        System.out.println("Total products produced: " + ws1Final);
        double ws1Throughput = simulation.calculateThroughput(ws1ProductResults,inspector1TotalPerformance);
        System.out.println("Workstation 1 throughput: " + ws1Throughput);

        //Workstation 2
        System.out.println("Workstation 2 Production: ");
        Integer ws2Final = simulation.printProductStatistics(ws2ProductResults);
        System.out.println("Total products produced: " + ws2Final);
        double ws2Throughput = simulation.calculateThroughput(ws2ProductResults,inspector2TotalPerformance);
        System.out.println("Workstation 2 throughput: " + ws2Throughput);

        //Workstation 3
        System.out.println("Workstation 3 Production: ");
        Integer ws3Final =simulation.printProductStatistics(ws3ProductResults);
        System.out.println("Total products produced: " + ws3Final);
        double ws3Throughput = simulation.calculateThroughput(ws3ProductResults,inspector2TotalPerformance);
        System.out.println("Workstation 3 throughput: " + ws3Throughput);

    }
}
