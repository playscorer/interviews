package amazon;

// IMPORT LIBRARY PACKAGES NEEDED BY YOUR PROGRAM
// SOME CLASSES WITHIN A PACKAGE MAY BE RESTRICTED
// DEFINE ANY CLASS AND METHOD NEEDED
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// CLASS BEGINS, THIS CLASS IS REQUIRED
class Question1 {
    // METHOD SIGNATURE BEGINS, THIS METHOD IS REQUIRED
    public ArrayList<String> popularNToys(int numToys, int topToys, List<String> toys, int numQuotes, List<String> quotes) {
	Map<String, Integer> mapToys = new HashMap<>();
	for (int iToy = 0; iToy < numToys; iToy++) {
	    String toy = toys.get(iToy);
	    for (int quote = 0; quote < numQuotes; quote++) {
		int occ = findOcc(toy, quotes.get(quote));
		Integer nbToys = 0;
		if ((nbToys = mapToys.get(toy)) == null)
		    mapToys.put(toy, occ);
		else
		    mapToys.put(toy, nbToys + occ);
	    }
	}

	return new ArrayList<>(byTop(topToys, mapToys));
    }
    // METHOD SIGNATURE ENDS

    public int findOcc(String toy, String quote) {
        int cpt=0;
        int idx=0;
        while ((idx=quote.indexOf(toy, idx)) > -1) {
            cpt++;
            idx += toy.length();
        }
        return cpt;
    }

    public List<String> byTop(int topToys, Map<String, Integer> mapToys) {
        return mapToys.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(topToys)
                .map(e->e.getKey())
                .collect(Collectors.toList());
    }
}