import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ReadFile {
    public static int[] getData(String filename) {
        ArrayList<Integer> flowDurations = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            line = br.readLine();
            System.out.println(line);
            while ((line = br.readLine()) != null) {
                // Add each line to the ArrayList
                int flowDuration = Integer.parseInt(line.split(",")[line.split(",").length - 1]);
                flowDurations.add(flowDuration);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int[] flowDurationsArray = flowDurations.stream().mapToInt(Integer::intValue).toArray();

        return flowDurationsArray;

    }
}
