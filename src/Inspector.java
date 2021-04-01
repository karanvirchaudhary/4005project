import java.util.ArrayList;

public class Inspector extends Thread{
    private ArrayList<Component> inventory;
    private boolean blocked;
    private int ID;
    private ArrayList<Buffer> buffer;
    private double lambdaValue;

    public Inspector(ArrayList<Buffer> buffer, int ID){
        this.buffer = buffer;
        this.ID = ID;
    }
}