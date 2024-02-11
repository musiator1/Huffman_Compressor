import org.apache.commons.cli.*;

public class Main {
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("h", "help", false, "Display help message");
        options.addOption("f", "file", true, "Input file path");
        options.addOption("o", "output", true, "Output file path");
        options.addOption("c", "compress", false, "Compress");
        options.addOption("d", "decompress", false, "Decompress");

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
            if (cmd.hasOption("h") || (cmd.hasOption("c") == cmd.hasOption("d"))) {
                formatter.printHelp("Huffman compressor", options);
                System.exit(1);
            }

            String inputFile = cmd.getOptionValue("f");
            String outputFile = cmd.getOptionValue("o");

            if (cmd.hasOption("c")) {
                Compressor compressor = new Compressor(inputFile, outputFile);
                compressor.compress();
                System.exit(0);
            }

            if (cmd.hasOption("d")) {
                Decompressor decompressor = new Decompressor(inputFile, outputFile);
                decompressor.decompress();
                System.exit(0);
            }

        } catch (ParseException | NullPointerException e) {
            System.out.println("Error parsing command line arguments: " + e.getMessage());
            formatter.printHelp("Huffman compressor", options);
            System.exit(1);
        }
    }
}
