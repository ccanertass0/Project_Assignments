import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class ReadFile {

    public String[] inputArray = new String[0];   // each line is an element of this array
    public File file;
    public String arg;

    public ReadFile() {
    }

    public ReadFile(String arg) {
        this.arg = arg;
    }

    public void readAndAppend() throws FileNotFoundException {
        file = new File(arg);
        Scanner scanner = new Scanner(file);
        int lineCounter = 0;
        while (scanner.hasNextLine()){
            inputArray = Arrays.copyOf(inputArray, inputArray.length+1);
            inputArray[lineCounter] = scanner.nextLine();
            lineCounter++;
        }

    }

    public String[] getInputArray() {
        int columnNum = inputArray[0].length()/2 + 1;
        for(int line = 0; line < inputArray.length; line++){
            String newLine = "";
            for(int column = 0; column < columnNum; column++){
                newLine += inputArray[line].charAt(2*column);
            }
            inputArray[line] = newLine;
        }
        return inputArray;
    }

    public int[] getPositionOfTheBall(){
        int[] positions = {0, 0};
        for(int line = 0; line < inputArray.length; line++ ){
            for(int column = 0; column < inputArray[0].length(); column++){
                if(inputArray[line].charAt(column) == '*'){
                    positions[0] = line + 1;
                    positions[1] = column + 1;
                    return positions;
                }
            }
        }
        return positions;
    }
}
