# Huffman_Compressor
## 1. Usage instructions.
### You need to know this before using:
* You can only use this application by the aid of bash shell and path to a ".jar" file is: HuffmanCompressor/out/artifacts/HuffmanCompressor_jar/HuffmanCompressor.jar 
* To use this software you need to have installed Java version 17 or higher. You can download it [here](https://www.java.com/pl/download/manual.jsp).
* You can compress and decompress without a loss all types of binary files, but big files (> 1GB) can take some time to compress/decompress.
* Software was tested on Linux and Windows and it is not guaranteed that it will work on other OS.

### Compression:
* To compress you have to type in bash shell ```java -jar HuffmanCompressor.jar -c -f <path_to_input_file> -o <path_to_output_file>```
* If an argument after -o is path to non-existing file it will be created a new file.

### Decompression:
* To deompress you have to type in bash shell ```java -jar HuffmanCompressor.jar -d -f <path_to_compressed_file> -o <path_to_decompressed_file>```
* If an argument after -o is path to non-existing file it will be created a new file.

## 2. All command-line flags:
* ```-h``` Displaying a help message
* ```-f <input_file_path>``` Input file path
* ```-o <output_file_path>``` Output file path
* ```-c``` Flag set if you want to compress
* ```-d``` Flag set if you want to decompress

## 3. Program ending codes:
* 0 - compression/decompression completed successfully
* 1 - ending program with displaying a help message
* 2 - file access error or file does not exist
* 3 - compressed file is damaged and decompression is impossible
