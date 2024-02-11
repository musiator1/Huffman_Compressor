import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileWriter {
    private final RandomAccessFile outputFile;
    private final byte[] buff = new byte[20];
    private int buffCounter = 0;
    private byte xorByte = (byte) 0xFF;

    public FileWriter(String outputFileName) throws IOException {
        outputFile = new RandomAccessFile(outputFileName, "rw");
        this.outputFile.seek(2);
    }

    public void writeTreeToFile(Node node) throws IOException {
        if (node != null) {
            if (node.left != null) {
                saveOneBit(0);
                writeTreeToFile(node.left);
                writeTreeToFile(node.right);
            } else {
                saveOneBit(1);
                saveManyBits(node.value, 8);
            }
        }
    }

    public void compress(String[] codes, FileInputStream fileInputStream) throws IOException {
        int buffor;
        while ((buffor = fileInputStream.read()) != -1) {
            saveManyBits(Integer.parseInt(codes[buffor], 2), codes[buffor].length());
        }
        fileInputStream.close();

        outputFile.write(buff, 0, 1);
        xorByte ^= buff[0];

        outputFile.seek(0);
        outputFile.write(buffCounter);
        outputFile.write(xorByte);
    }

    public void close() throws IOException {
        this.outputFile.close();
    }

    private void saveOneBit(int bit) throws IOException {
        int bitPosition = 7 - buffCounter % 8;
        int arrayPosition = buffCounter / 8;

        byte mask = (byte) ((1 & bit) << bitPosition);
        buff[arrayPosition] |= mask;

        buffCounter++;
        updateBuff();
    }

    private void saveManyBits(int value, int length) throws IOException {
        int bitsLeft = length;
        int bitsWritten = 0;

        while (bitsLeft > 0) {
            int bitsToWrite = Math.min(8 - buffCounter % 8, bitsLeft);
            int arrayPosition = buffCounter / 8;
            int bitPosition = 7 - buffCounter % 8;
            bitsWritten += bitsToWrite;

            byte mask = (byte) ((1 << bitsToWrite) - 1);
            mask &= (byte) (value >> length - bitsWritten);
            buff[arrayPosition] |= (byte) (mask << bitPosition - bitsToWrite + 1);

            bitsLeft -= bitsToWrite;
            buffCounter += bitsToWrite;
        }
        updateBuff();
    }

    private void updateBuff() throws IOException {
        int arrayPosition = buffCounter / 8;
        if (arrayPosition >= 1) {
            for (int i = 0; i < arrayPosition; i++) {
                outputFile.write(buff[0]);
                xorByte ^= buff[0];
                buffCounter -= 8;
                System.arraycopy(buff, 1, buff, 0, buff.length - 1);
                buff[buff.length - 1] = 0;
            }
        }
    }
}
