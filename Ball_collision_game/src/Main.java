import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        MoveMaker game1 = new MoveMaker(args[0], args[1]);
        game1.main();
    }
}