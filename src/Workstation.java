public class Workstation extends Thread{
    private Buffer buffer1;
    private Buffer buffer2;
    private Integer countProduct =0; //a counter that keeps track of the # products produced by a workstation.
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
    public Integer getCountProduct(){
        return countProduct;
    }

    /**
     * For WS1: Take right away
     *
     * For WS2/WS3: Don't proceed until you have both
     */
    public void run(){


        while (!isInterrupted()){
            long startTime = System.nanoTime();
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

            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;

            if(ID == 1){
                simulation.getWs1().add(timeElapsed);
                countProduct = countProduct + 1;
                System.out.println("In this iteration, WS1 produced " + countProduct);
                simulation.getWs1ProductTracker().add(countProduct);
            } else if(ID == 2){
                simulation.getWs2().add(timeElapsed);
                countProduct = countProduct + 1;
                System.out.println("In this iteration, WS2 produced "+ countProduct);
                simulation.getWs2ProductTracker().add(countProduct);
            } else {
                simulation.getWs3().add(timeElapsed);
                countProduct = countProduct + 1;
                System.out.println("In this iteration, WS3 produced "+ countProduct);
                simulation.getWs3ProductTracker().add(countProduct);
            }

        }
    }


    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }
}
