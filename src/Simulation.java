import java.util.Random;

public class Simulation {

    public double getNext(Random random, Double lambda) {
        return  Math.log(1-random.nextDouble())/(-lambda);
    }

    public static void main(String[] args){

    }
}
