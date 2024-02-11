import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;


public class CompressionDecompressionTest {
    private final String compressedFilePath = "src\\test\\resources\\compressed.txt";
    private final String decompressedFilePath = "src\\test\\resources\\decompressed.txt";

    private boolean areFilesEqual(String filePath1, String filePath2) {
        Path path1 = Paths.get(filePath1);
        Path path2 = Paths.get(filePath2);

        try {
            byte[] file1Content = Files.readAllBytes(path1);
            byte[] file2Content = Files.readAllBytes(path2);

            return Arrays.equals(file1Content, file2Content);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @BeforeEach
    public void clearFiles() {
        try {
            FileOutputStream fos1 = new FileOutputStream(compressedFilePath);
            FileOutputStream fos2 = new FileOutputStream(decompressedFilePath);
            fos1.close();
            fos2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void should_CorrectlyCompressAndDecompress_WhenInputIsOneLetter() {
        //given
        String inputFilePath = "src\\test\\resources\\one_letter.txt";
        Compressor compressor = new Compressor(inputFilePath, compressedFilePath);
        Decompressor decompressor = new Decompressor(compressedFilePath, decompressedFilePath);

        //when
        compressor.compress();
        decompressor.decompress();

        //then
        assertThat(areFilesEqual(inputFilePath, decompressedFilePath)).isTrue();
    }

    @Test
    public void should_CorrectlyCompressAndDecompress_WhenInputIsTwoLetters() {
        //given
        String inputFilePath = "src\\test\\resources\\two_letters.txt";
        Compressor compressor = new Compressor(inputFilePath, compressedFilePath);
        Decompressor decompressor = new Decompressor(compressedFilePath, decompressedFilePath);

        //when
        compressor.compress();
        decompressor.decompress();

        //then
        assertThat(areFilesEqual(inputFilePath, decompressedFilePath)).isTrue();
    }

    @Test
    public void should_CorrectlyCompressAndDecompress_WhenInputIsSpecificSymbols() {
        //given
        String inputFilePath = "src\\test\\resources\\different_letters.txt";
        Compressor compressor = new Compressor(inputFilePath, compressedFilePath);
        Decompressor decompressor = new Decompressor(compressedFilePath, decompressedFilePath);

        //when
        compressor.compress();
        decompressor.decompress();

        //then
        assertThat(areFilesEqual(inputFilePath, decompressedFilePath)).isTrue();
    }

    @Test
    public void should_CorrectlyCompressAndDecompress_WhenInputIsBig() {
        //given
        String inputFilePath = "src\\test\\resources\\big.txt";
        Compressor compressor = new Compressor(inputFilePath, compressedFilePath);
        Decompressor decompressor = new Decompressor(compressedFilePath, decompressedFilePath);

        //when
        compressor.compress();
        decompressor.decompress();

        //then
        assertThat(areFilesEqual(inputFilePath, decompressedFilePath)).isTrue();
    }

    @Test
    public void should_CorrectlyCompressAndDecompress_WhenInputConsistOfOneLetter() {
        //given
        String inputFilePath = "src\\test\\resources\\plain.txt";
        Compressor compressor = new Compressor(inputFilePath, compressedFilePath);
        Decompressor decompressor = new Decompressor(compressedFilePath, decompressedFilePath);

        //when
        compressor.compress();
        decompressor.decompress();

        //then
        assertThat(areFilesEqual(inputFilePath, decompressedFilePath)).isTrue();
    }

    @Test
    public void should_CorrectlyCompressAndDecompress_WhenInputIsImage() {
        //given
        String inputFilePath = "src\\test\\resources\\duck.png";
        Compressor compressor = new Compressor(inputFilePath, compressedFilePath);
        Decompressor decompressor = new Decompressor(compressedFilePath, decompressedFilePath);

        //when
        compressor.compress();
        decompressor.decompress();

        //then
        assertThat(areFilesEqual(inputFilePath, decompressedFilePath)).isTrue();
    }

    @Test
    public void should_ThrowException_WhenInputIsEmpty() {
        //given
        String inputFilePath = "src\\test\\resources\\empty.txt";
        Compressor compressor = new Compressor(inputFilePath, compressedFilePath);
        Decompressor decompressor = new Decompressor(compressedFilePath, decompressedFilePath);

        //when
        Throwable exception = catchThrowable(() -> {
            compressor.compress();
            decompressor.decompress();
        });

        //then
        assertThat(exception).isInstanceOf(NullPointerException.class).hasMessage("File to compress cannot be empty!");
    }

}
