public class Buffer{
    private boolean bufferCapacityFull =false;
    private Inspector inspector;
    private Component[] components;

    public Buffer(Inspector aInspector){
        this.inspector = aInspector;
    }

    public boolean isFull(){
        return bufferCapacityFull;
    }
}