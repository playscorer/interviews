package ICE.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class reads a large file containing a list of prices per cusip and shows the closing price for each cusip.
 * It takes the input file as a parameter in the command line: java PriceParser input_file.txt
 * 
 * @author Filipe
 *
 */
public class PriceParser {
    
    private String filename;
    
    public PriceParser(String filename) {
	this.filename = filename;
    }

    private boolean isNumeric(String strNum) {
	return strNum.matches("\\d+\\.\\d+");
    }
    
    public Map<String, Double> parse() {
	String currentCusip = null;
	Path path = Paths.get(filename);
	Charset charset = Charset.forName("US-ASCII");
	Map<String, Double> cusipMap = new HashMap<>();
	
	try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
	    String line = null;
	    
	    while ((line = reader.readLine()) != null) {
		// CUSIP
	        if (!isNumeric(line)) {
	            currentCusip = line;
		}
	        
		// price
	        else {
		    double price = Double.parseDouble(line);
		    
		    reader.mark(10);
		    String nextLine = reader.readLine();
		    reader.reset();
		    
		    // last price
		    if (isNumeric(line) && (nextLine == null || !isNumeric(nextLine))) {
			if (currentCusip != null) {
			    cusipMap.put(currentCusip, price);
			} else {
			    System.out.println("Unexpected price - cusip missing");
			    continue;
			}
		    }
		    
		}
	    }
	    
	} catch (IOException x) {
	    System.err.format("IOException: %s%n", x);
	}
	
	return cusipMap;
    }
    

    public static void main(String[] args) {
	if (args.length != 1) {
	    System.err.println("The command line needs one parameter: ./java PriceParser input_file.txt");
	    System.exit(0);
	}
	
	PriceParser priceParser = new PriceParser(args[0]);
	Map<String, Double> cusipMap = priceParser.parse();
	
	for (Entry<String, Double> entry : cusipMap.entrySet()) {
	    System.out.println("Cusip: " + entry.getKey() + " - closing price: " + entry.getValue());
	}
    }

}
