package ICE.merger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A class that merges two sorted files.
 * The command line takes 3 arguments:
 * java SortedFileMerger input_sorted_file1.txt input_sorted_file2.txt output_merged_file.txt
 * 
 * @author Filipe
 *
 */
public class SortedFileMerger {
    
    private String filename1;
    private String filename2;
    private String resultFile;
    
    public SortedFileMerger(String filename1, String filename2, String resultFile) {
	this.filename1 = filename1;
	this.filename2 = filename2;
	this.resultFile = resultFile;
    }

    public void merge() {
	Path path1 = Paths.get(filename1);
	Path path2 = Paths.get(filename2);
	Path result = Paths.get(resultFile);
	Charset charset = Charset.forName("US-ASCII");
	
	try (BufferedReader reader1 = Files.newBufferedReader(path1, charset);
		BufferedReader reader2 = Files.newBufferedReader(path2, charset);
		BufferedWriter writer = Files.newBufferedWriter(result, charset)) {
	    
	    String line1 = reader1.readLine();
	    String line2 = reader2.readLine();
	    
	    while (line1 != null && line2 != null) {
		if (line1.compareTo(line2) < 0) {
		    writer.write(line1);
		    writer.newLine();
		    line1 = reader1.readLine();
		} else {
		    writer.write(line2);
		    writer.newLine();
		    line2 = reader2.readLine();
		}
	    }
	    
	    while (line1 != null) {
		writer.write(line1);
		writer.newLine();
		line1 = reader1.readLine();
	    }
	    
	    while (line2 != null) {
		writer.write(line2);
		writer.newLine();
		line2 = reader2.readLine();
	    }
	    
	    System.out.println("File merged: " + resultFile);
	    
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public static void main(String[] args) {
	if (args.length != 3) {
	    System.err.println(
		    "The command line needs 3 parameters: java SortedFileMerger input_sorted_file1.txt input_sorted_file2.txt output_merged_file.txt");
	    System.exit(0);
	}
	SortedFileMerger merger = new SortedFileMerger(args[0], args[1], args[2]);
	merger.merge();
    }

}
