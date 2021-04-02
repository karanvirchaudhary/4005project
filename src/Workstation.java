public class Workstation extends Thread{
    private Buffer buffer1;
    private Buffer buffer2;
    private int countProduct;
    private int ID;
    private Simulation simulation;
    private double lambda;

    //only 300 runs
    private int run = 0;

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
                System.out.println("inspector 1 taking from buffer");
            } else {
                //WS2 and 3

                //if you're WS2 buffer 1 just has component 1 in it and buffer 2 is just C2
                //you need both



                while(buffer1.getSize() == 0 && buffer2.getSize() == 0){
                    //just wait lol
                }

                if(buffer1.getSize() != 0 && buffer2.getSize() != 0){
                    Component componentTwo = buffer2.take();
                    Component componentOne = buffer1.take();
                    System.out.print("Workstation taking " + componentOne.getComponentType().toString() + " from buffer and");
                    System.out.println("workstation taking " + componentTwo.getComponentType().toString() + " from buffer");
                }


                //only when one item from both are taken can the program continue
            }

            double sleepTime = simulation.getExponential(lambda);

            try {
                Thread.sleep((long)sleepTime * 1000);
            } catch (InterruptedException e ) {
                e.printStackTrace();
                System.exit(1);
            }

            run ++;
        }

    }



}
