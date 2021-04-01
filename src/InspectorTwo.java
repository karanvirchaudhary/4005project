import java.util.ArrayList;

public class InspectorTwo extends Inspector {
    private double secondLambda; //This is the lambda value for servicing component 3.

    public InspectorTwo(ArrayList<Buffer> buffer, int ID, double lambdaTwo, double lambdaThree) {
        super(buffer, ID, lambdaTwo);
        this.secondLambda = lambdaThree;
    }
}
