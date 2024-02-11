import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileReader {
    private final FileInputStream fileInputStream;
    private final byte[] buff = new byte[20];
    private int bitsInBuffCounter;
    private int cursorBitPosition = 7;  //7 - MSB, (...), 0 - LSB
    private int cursorArrayPosition;
    private boolean canRead = true;
    private boolean startCount = false;
    private final int bitsInLastByte;
    private byte xorByte;
    private int bitsLeft;

    public FileReader(String inputFileName) throws IOException {
        this.fileInputStream = new FileInputStream(inputFileName);
        bitsInLastByte = fileInputStream.read();
        xorByte = (byte) fileInputStream.read();
    }

    public void recreateTree(Node root) throws IOException {
        if (canRead) {
            int bit = readOneBit();
            if (bit == 0) {
                root.left = new Node();
                recreateTree(root.left);
                root.right = new Node();
                recreateTree(root.right);
            } else if (bit == 1) {
                root.value = (char) readManyBits(8);
            }
        }
    }

    public void decompress(Node root, FileOutputStream fileOutputStream) throws IOException {
        Node nodeTmp = root;
        int bit;
        while (canRead) {
            bit = readOneBit();
            if (bit == 0) {
                nodeTmp = nodeTmp.left;
            } else {
                nodeTmp = nodeTmp.right;
            }

            if (nodeTmp.left == null) {
                fileOutputStream.write(nodeTmp.value);
                nodeTmp = root;
            }
        }

        xorByte ^= buff[0];
        if (xorByte != (byte) 0xFF) {
            System.err.println("Compressed file is damaged! Decompression failed!");
            System.exit(3);
        }
    }

    public void close() throws IOException {
        this.fileInputStream.close();
    }


    private int readOneBit() throws IOException {
        if (cursorArrayPosition * 8 + (7 - cursorBitPosition) >= bitsInBuffCounter) {
            bitsLeft = fillBuff();
            if (bitsLeft != -1) {
                startCount = true;
            }
        }

        int retValue = (buff[cursorArrayPosition] >> cursorBitPosition) & 1;
        cursorBitPosition--;
        if (cursorBitPosition < 0) {
            cursorBitPosition = 7;
            cursorArrayPosition++;
        }

        if (startCount) {
            bitsLeft--;
            if (bitsLeft <= 0) {
                canRead = false;
            }
        }

        updateBuff();
        return retValue;
    }

    private int readManyBits(int amount) throws IOException {
        if (cursorArrayPosition * 8 + (7 - cursorBitPosition) + amount > bitsInBuffCounter) {
            bitsLeft = fillBuff();
            if (bitsLeft != -1) {
                startCount = true;
            }
        }

        int bitsLeft = amount;
        int retValue = 0;

        while (bitsLeft > 0) {
            int bitsToRead = Math.min(bitsLeft, cursorBitPosition + 1);
            byte mask = (byte) ((1 << bitsToRead) - 1);

            mask = (byte) (mask << (cursorBitPosition - bitsToRead + 1));
            retValue = retValue << bitsToRead;
            mask = (byte) (mask & buff[cursorArrayPosition]);
            mask = (byte) (mask >>> (cursorBitPosition - bitsToRead + 1));
            mask &= ((1 << 8 - (cursorBitPosition - bitsToRead + 1)) - 1); // :)

            retValue |= mask;

            cursorBitPosition -= bitsToRead;
            bitsLeft -= bitsToRead;

            if (cursorBitPosition < 0) {
                cursorBitPosition = 7;
                cursorArrayPosition++;
            }
        }

        if (startCount) {
            this.bitsLeft -= amount;
        }
        updateBuff();
        return retValue;
    }

    //Returns amount of bits left to decompress or -1 when it is unknown.
    private int fillBuff() throws IOException {
        int bitsLeftToRead = -1;
        if (!startCount) {
            int startIndex = bitsInBuffCounter / 8;
            int length = buff.length - startIndex;
            int bytesRead = fileInputStream.read(buff, startIndex, length);

            if (bytesRead == -1) {
                bitsLeftToRead = 0;
            } else if (bytesRead < length) {
                bitsLeftToRead = (bytesRead - 1) * 8 + bitsInLastByte;
                bitsInBuffCounter += bitsLeftToRead;
            } else {
                bitsInBuffCounter += 8 * bytesRead;
            }
        }
        return bitsLeftToRead;
    }

    private void updateBuff() {
        while (cursorArrayPosition > 0) {
            xorByte ^= buff[0];
            System.arraycopy(buff, 1, buff, 0, buff.length - 1);
            buff[buff.length - 1] = 0;
            cursorArrayPosition--;
            bitsInBuffCounter -= 8;
        }
    }
}