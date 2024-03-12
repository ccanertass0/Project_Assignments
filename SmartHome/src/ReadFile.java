import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class ReadFile {
    private File file;
    private Scanner scanner;
    private String arg;
    private String[] inputArray = new String[0];

    public ReadFile(String arg) {
        this.arg = arg;
    }

    public void readAndAppend() throws FileNotFoundException {
        file = new File(arg);
        scanner = new Scanner(file);
        int line = 0;
        while(scanner.hasNextLine()){
            inputArray = Arrays.copyOf(inputArray, inputArray.length + 1);
            inputArray[line++] = scanner.nextLine();
        }
    }

    public String[] getInputArray() {
        return inputArray;
    }

}
