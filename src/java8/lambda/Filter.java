package java8.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Filter {
    
    private static enum Color {GREEN, RED, BLACK};
    
    static class Apple {
	private Color c;
	private int weight;
	
	public Apple(Color c, int weight) {
	    this.c = c;
	    this.weight = weight;
	}

	@Override
	public String toString() {
	    return "Apple [c=" + c + ", weight=" + weight + "]";
	}
	
    }
    
    public static List<Apple> filterGreenGt100(List<Apple> listOfApples) {
	return listOfApples.stream().filter(a -> a.c.equals(Filter.Color.GREEN))
		.filter(a -> a.weight >= 100).collect(Collectors.toList());
    }
    
    public static void main(String[] args) {
	List<Apple> listOfApples = new ArrayList<>();
	listOfApples.add(new Apple(Filter.Color.BLACK, 120));
	listOfApples.add(new Apple(Filter.Color.RED, 50));
	listOfApples.add(new Apple(Filter.Color.GREEN, 120));
	listOfApples.add(new Apple(Filter.Color.GREEN, 50));
	
	System.out.println(listOfApples);
	System.out.println(filterGreenGt100(listOfApples));
    }

}
