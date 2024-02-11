import java.io.FileOutputStream;
import java.io.IOException;

public class Decompressor {

    public Decompressor(String inputFileName, String outputFileName) {
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
    }

    private final String inputFileName;
    private final String outputFileName;

    public void decompress() {
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFileName)) {
            FileReader fileReader = new FileReader(inputFileName);
            String[] codes = new String[65536];
            Node root = new Node();

            fileReader.recreateTree(root);
            Node.generateCodes(codes, new StringBuilder(), root);
            fileReader.decompress(root, fileOutputStream);
            fileReader.close();
        } catch (IOException e) {
            System.err.println("Error while opening file!");
            System.exit(2);
        }

    }
}
