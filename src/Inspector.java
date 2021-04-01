import java.util.ArrayList;

public class Inspector{
    private ArrayList<Component> inventory;
    private boolean blocked;
    private int ID;

    public Inspector(boolean blocked, int ID){
        this.ID = ID;
        this.blocked = blocked;
    }
}