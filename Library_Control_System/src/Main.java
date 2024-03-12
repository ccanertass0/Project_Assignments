
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Library library1 = new Library(args[0], args[1]);
        library1.operate();
    }
}
