public class Workstation extends Thread{
    private Buffer buffer1;
    private Buffer buffer2;
    private int countProduct;
    private int ID;
    private Simulation simulation;
    private double lambda;
    private boolean end;

    public Workstation(Buffer buffer1, Buffer buffer2, Simulation simulation, Double lambda, int id){
        this.buffer1 = buffer1;
        this.buffer2 = buffer2;
        this.simulation = simulation;
        this.lambda = lambda;
        this.ID = id;
    }

    /**
     * For WS1: Take right away
     *
     * For WS2/WS3: Don't proceed until you have both
     */
    public void run(){

        long startTime = System.nanoTime();
        while (!end){
            //this is workstation 1
            if(buffer2 == null){
                Component component = buffer1.take();
                System.out.println("Workstation 1 taking from buffer");
            } else {
                //WS2 and 3

                //if you're WS2 buffer 1 just has component 1 in it and buffer 2 is just C2
                //you need both



                Component componentTwo = buffer2.take();
                Component componentOne = buffer1.take();

                if(componentTwo == null || componentOne == null){
                    //means that the program go interrupted
                    end = true;
                    return;
                }

                System.out.println(this.getName() + " taking " + componentOne.getComponentType().toString() + " and " +
                        componentTwo.getComponentType().toString() + " from buffer");
                //only when one item from both are taken can the program continue
            }


            double sleepTime = simulation.getExponential(lambda);

            try {
                Thread.sleep((long)sleepTime * 1000);
            } catch (InterruptedException e ) {
                System.out.println(this.getName() + " interrupted");
                end = true;
                return;
            }
        }


        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        if(ID == 1){
            simulation.getWs1().add(timeElapsed);
        } else if(ID == 2){
            simulation.getWs2().add(timeElapsed);
        } else {
            simulation.getWs3().add(timeElapsed);
        }

    }


    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }
}
