import java.util.Random;

public enum Type {
    C1, C2, C3;

    public static Type getRandomType(){
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}
