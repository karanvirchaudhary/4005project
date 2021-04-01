public class Workstation extends Thread{
    private Buffer buffer1;
    private Buffer buffer2;
    private int countProduct;
    private int ID;

    public Workstation(Buffer buffer1, Buffer buffer2){
        this.buffer1 = buffer1;
        this.buffer2 = buffer2;
    }



}
