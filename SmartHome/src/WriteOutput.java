import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class WriteOutput {
    private String outFileName;

    private File file;
    private FileWriter fw;
    private PrintWriter pw;

    public WriteOutput(String outFileName) {
        this.outFileName = outFileName;
    }

    public void createWriter() throws IOException {
        file = new File(outFileName);
        fw = new FileWriter(file);
        pw = new PrintWriter(fw);
    }

    public void write(String string){
        pw.write(string);

    }
    public void closeFile(){
        pw.close();
    }
    public void writef(String string, double value){
        pw.format(string, value);
    }




}
