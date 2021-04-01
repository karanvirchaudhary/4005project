import java.util.ArrayList;

public class Inspector{
    private ArrayList<Component> inventory;
    private boolean blocked;
    private int ID;
    private ArrayList<Buffer> buffer;

    public Inspector(ArrayList<Buffer> buffer, int ID){

        this.buffer = buffer;
        this.ID = ID;
    }
}