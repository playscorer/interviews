package ICE.merger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

/**
 * An utility class for file generators containing ordered sequences.
 * 
 * @author Filipe
 *
 */
public class GenerateFileWithLettersSeq {
    
    private final int BASE = 26;
    
    private String filename1;
    
    private String filename2;
    
    public GenerateFileWithLettersSeq(String filename1, String filename2) {
	this.filename1 = filename1;
	this.filename2 = filename2;
    }

    private String convert(int number, int base) {
	StringBuilder builder = new StringBuilder();

	do {
	    int digit = number % base;
	    builder.insert(0, (char) (digit + 'A'));
	    number = number / base;
	} while (number != 0);
	
	return builder.toString();
    }

    private String padString(String str, int length) {
	StringBuilder builder = new StringBuilder(str);
	while (builder.length() < length) {
	    builder.insert(0, 'A');
	}
	return builder.toString();
    }

    /**
     *  This method generates a file containing the alphabetical sequence AAAA, AAAB... depending on the length provided
     *  if sequenceLength=6 then AAAAAA, AAAAAB... will be generated
     * 
     * @param sequenceLength
     */
    public void generateFileWithSortedSeq(int sequenceLength) {
	Path path = Paths.get(filename1);
	Charset charset = Charset.forName("US-ASCII");
	try (BufferedWriter writer = Files.newBufferedWriter(path, charset)) {
	    for (int i=0; i<Math.pow(BASE, sequenceLength); i++) {
		String str = convert(i, BASE);
		str = padString(str, sequenceLength);
		writer.write(str);
		writer.newLine();
	    }
	    
	    System.out.println("File with ordered letters sequence generated: " + filename1);
	    
	} catch (IOException x) {
	    System.err.format("IOException: %s%n", x);
	}
    }
    
    /**
     * This method generates a file containing per each letter from the alphabet a random sequence
     * ie: ADFGD, BRTDS, etc...
     * 
     * @param sequenceLength
     */
    public void generateFileWithRandomSortedSeq(int sequenceLength) {
	int leftLimit = 65; // A
	int rightLimit = 90; // Z
	Random random = new Random();
	Path path = Paths.get(filename2);
	Charset charset = Charset.forName("US-ASCII");
	
	try (BufferedWriter writer = Files.newBufferedWriter(path, charset)) {
	    for (int i = 0; i < BASE; i++) {
		StringBuilder buffer = new StringBuilder(sequenceLength);
		buffer.append((char) (leftLimit + i));
		for (int j = 1; j < sequenceLength; j++) {
		    int randomLimitedInt = leftLimit + random.nextInt((rightLimit - leftLimit + 1));
		    buffer.append((char) randomLimitedInt);
		}
		writer.write(buffer.toString());
		writer.newLine();
	    }
	    
	    System.out.println("File with random ordered letters sequence generated: " + filename2);
	    
	} catch (IOException x) {
	    System.err.format("IOException: %s%n", x);
	}
    }
    
    public static void main(String[] args) {
	if (args.length != 2) {
	    System.err.println("The command line needs one parameter: ./java GenerateFileWithLettersSeq output_file1.txt output_file2.txt");
	    System.exit(0);
	}
	
	GenerateFileWithLettersSeq generator = new GenerateFileWithLettersSeq(args[0], args[1]);
	generator.generateFileWithSortedSeq(4);
	generator.generateFileWithRandomSortedSeq(10);
    }

}
