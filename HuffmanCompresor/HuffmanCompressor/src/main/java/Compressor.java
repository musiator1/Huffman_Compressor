import java.io.FileInputStream;
import java.io.IOException;

public class Compressor {

    private final PriorityQueue<Node> forest = new PriorityQueue<>();
    private final Node[] nodes = new Node[256];
    private final String[] codes = new String[256];
    private final String inputFileName;
    private final String outputFileName;

    public Compressor(String inputFileName, String outputFileName) {
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
    }

    public void compress() {

        int uniqueSymbols = initNodes();
        if (uniqueSymbols == 0) {
            throw new NullPointerException("File to compress cannot be empty!");
        } else if (uniqueSymbols == 1) {
            nodes[2] = new Node((char) 21, null, null);
        }

        for (int i = 0; i < 256; i++) {
            if (nodes[i] != null) forest.add(nodes[i]);
        }

        Node root = Node.makeTree(forest);
        Node.generateCodes(codes, new StringBuilder(), root);

        try (FileInputStream fileInputStream = new FileInputStream(inputFileName)) {
            FileWriter fileWriter = new FileWriter(outputFileName);
            fileWriter.writeTreeToFile(root);
            fileWriter.compress(codes, fileInputStream);
            fileWriter.close();
        } catch (IOException e) {
            System.err.println("Error while opening file!");
            System.exit(2);
        }
    }

    private int initNodes() {
        int buffer;
        int uniqueSymbolsCounter = 0;
        try (FileInputStream inputStream = new FileInputStream(inputFileName)) {
            while ((buffer = inputStream.read()) != -1) {
                if (nodes[buffer] == null) {
                    nodes[buffer] = new Node((char) buffer, null, null);
                    uniqueSymbolsCounter++;
                }
                nodes[buffer].incrementCounter();
            }
        } catch (IOException e) {
            System.err.println("Error while opening file!");
            System.exit(2);
        }
        return uniqueSymbolsCounter;
    }
}

