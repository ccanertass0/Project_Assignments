import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        SmartHome smartHome = new SmartHome(args[0], args[1]);
        smartHome.process();

    }
}