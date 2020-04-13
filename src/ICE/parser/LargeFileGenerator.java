package ICE.parser;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

/**
 * A class that generates a large file containing prices for a list of cusips
 * @author Filipe
 */
public class LargeFileGenerator {
    
    private static int NB_OF_CUSIPS = 100000;
    
    private static int NB_OF_PRICES_PER_CUSIP = 1000;

    private String filename;
    
    public LargeFileGenerator(String filename) {
	this.filename = filename;
    }

    private String generateCusip() {
	int leftLimit = 65; // A
	int rightLimit = 90; // Z
	int targetStringLength = 8;
	Random random = new Random();
	StringBuilder buffer = new StringBuilder(targetStringLength);
	for (int i = 0; i < targetStringLength; i++) {
	    int randomLimitedInt = leftLimit + random.nextInt((rightLimit - leftLimit + 1));
	    buffer.append((char) randomLimitedInt);
	}
	return buffer.toString();
    }
    
    private String generatePrice() {
	double price = new Random().nextDouble() * 100;
	price = Double.parseDouble(String.format("%.3f", price));
	return String.valueOf(price);
    }
    
    public void generateFile() {
	Path path = Paths.get(filename);
	Charset charset = Charset.forName("US-ASCII");
	try (BufferedWriter writer = Files.newBufferedWriter(path, charset)) {
	    for (int nbCusip=0; nbCusip< NB_OF_CUSIPS; nbCusip++) {
		String cusip = generateCusip();
		writer.write(cusip, 0, cusip.length());
		writer.newLine();
		
		for (int nbPrices=0; nbPrices<NB_OF_PRICES_PER_CUSIP; nbPrices++) {
		    String price = generatePrice();
		    writer.write(price, 0, price.length());
		    writer.newLine();
		}
	    }
	    
	    System.out.println("Large file generated: " + filename);
	    
	} catch (IOException x) {
	    System.err.format("IOException: %s%n", x);
	}
    }
    
    public static void main(String[] args) {
	if (args.length != 1) {
	    System.err.println("The command line needs one parameter: ./java LargeFileGenerator output_file.txt");
	    System.exit(0);
	}
	
	LargeFileGenerator generator = new LargeFileGenerator(args[0]);
	generator.generateFile();
    }

}
