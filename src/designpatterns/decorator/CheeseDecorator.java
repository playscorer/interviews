package designpatterns.decorator;

public class CheeseDecorator extends PizzaDecorator {

    public CheeseDecorator(Pizza pizza) {
	super(pizza);
    }

    private double price = 0.5;

    @Override
    public double getPrice() {
	return super.getPrice() + price;
    }

}
