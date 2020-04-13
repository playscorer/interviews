package ICE;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class reads a large file containing a list of prices per cusip and shows the closing price for each cusip.
 * It puts all prices in a map unnecessarly.
 * It takes the input file as a parameter in the command line: java Reader input_file.txt
 * @author Filipe
 *
 */
public class Reader {
    
    private Integer MAX_BUFFER_SIZE = 16;

    private String filename; 

    private Map<String, LinkedList<Double>> cusipMap = new HashMap<>();
    
    
    public Reader(String filename) {
	this.filename = filename;
    }


    private boolean isNumeric(String strNum) {
	return strNum.matches("\\d+\\.\\d+");
    }
    

    public void read() {
	int currentBuffer = 0;
	int cusipBuffer = 0;
	Path path = Paths.get(filename);
	Charset charset = Charset.forName("US-ASCII");
	
	try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
	    String line = null;
	    String currentCusip = null;
	    LinkedList<Double> pricesForCusip = null;
	    
	    while ((line = reader.readLine()) != null) {
	        System.out.println(line);
		
		// price
		if (isNumeric(line)) {
		    if (pricesForCusip == null) {
			System.out.println("Unexpected price - cusip missing");
			continue;
		    }
		    double price = Double.parseDouble(line);
		    pricesForCusip.add(price);
		}
		// cusip
		else {
		    currentCusip = line;
		    pricesForCusip = new LinkedList<>();
		    cusipMap.put(line, pricesForCusip);
		}

		currentBuffer += line.length() + 1;
		reader.mark(currentBuffer);
		String nextLine = reader.readLine();
		reader.reset();
		
		// current line is a price and next line is a cusip or null
		if (isNumeric(line) && (nextLine == null || !isNumeric(nextLine))) {
		    cusipBuffer = currentBuffer;
		    
		    // the split of the buffer can be done
		    if (currentBuffer >= MAX_BUFFER_SIZE) {
			for (Entry<String, LinkedList<Double>> entry : cusipMap.entrySet()) {
			    System.out.println("Cusip: " + entry.getKey() + " - closing price: " + entry.getValue().getLast());
			}
			cusipMap.clear();
			currentBuffer=0;
		    }
		    
		    reader.mark(cusipBuffer);
		    
		} else {
		    // the split of the buffer cannot be done here
		    if (currentBuffer >= MAX_BUFFER_SIZE) {
			// we only keep the current cusip with its partial list of prices in the map
			for (Iterator<String> itCusip = cusipMap.keySet().iterator(); itCusip.hasNext(); ) {
			    String cusipKey = itCusip.next();
			    if (!cusipKey.equals(currentCusip)) {
				System.out.println("Cusip: " + cusipKey + " - closing price: " + cusipMap.get(cusipKey).getLast());
				itCusip.remove();
			    } else {
				cusipMap.get(cusipKey).clear();
			    }
			}
			
			currentBuffer = cusipBuffer;
			reader.reset();
		    }
		}
		
	    }

/*	    for (Entry<String, LinkedList<Double>> entry : cusipMap.entrySet()) {
		System.out.println("Cusip: " + entry.getKey() + " - closing price: " + entry.getValue().getLast());
	    }*/
	    
	} catch (IOException x) {
	    System.err.format("IOException: %s%n", x);
	}
    }
    
    public void debug() {
	System.out.println(cusipMap);
    }


    public static void main(String[] args) {
	if (args.length != 1) {
	    System.err.println("The command line needs one parameter: ./java Reader input_file.txt");
	    System.exit(0);
	}
	
	Reader reader = new Reader(args[0]);
	reader.read();
	//reader.debug();
    }

}
