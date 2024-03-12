import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class WriteToFile {
    static File file = new File("output.txt");
    static FileWriter fw;

    static {
        try {
            fw = new FileWriter(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static PrintWriter pw = new PrintWriter(fw);

    public WriteToFile() throws IOException {
    }

    public static void print(String input){
        pw.write(input);
    }

    public static void closeFile(){
        pw.close();
    }
}
