public class Workstation extends Thread{
    private Buffer buffer1;
    private Buffer buffer2;
    private int countProduct;
    private int ID;
    private Simulation simulation;
    private double lambda;

    public Workstation(Buffer buffer1, Buffer buffer2, Simulation simulation, Double lambda){
        this.buffer1 = buffer1;
        this.buffer2 = buffer2;
        this.simulation = simulation;
        this.lambda = lambda;
    }

    /**
     * For WS1: Take right away
     *
     * For WS2/WS3: Don't proceed until you have both
     */
    public void run(){

        while (true){

            //this is workstation 1
            if(buffer2 == null){
                Component component = buffer1.take();
            } else {
                //WS2
                Component componentOne = buffer1.take();
                Component componentTwo = buffer2.take();
            }

            double sleepTime = simulation.getExponential(lambda);

            try {
                Thread.sleep((long)sleepTime);
            } catch (InterruptedException e ) {
                e.printStackTrace();
                System.exit(1);
            }


        }

    }



}
