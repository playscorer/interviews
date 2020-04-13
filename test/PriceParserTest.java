

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import ICE.parser.PriceParser;

/**
 * Unit test for the PriceParser class 
 * @author Filipe
 */
public class PriceParserTest {
    

    @Test
    public void test() {
	// ARRANGE
	String filename = "src/ICE/ticks.txt";
	PriceParser priceParser = new PriceParser(filename);
	
	// ACT
	Map<String, Double> cusipMap = priceParser.parse();
	
	// ASSERT
	Assert.assertEquals(114.567d, cusipMap.get("RE34RT21"), 0);
	Assert.assertEquals(9.082d, cusipMap.get("CV32GH58"), 0);
	Assert.assertEquals(47.565d, cusipMap.get("AR45YU67"), 0);
    }

}
