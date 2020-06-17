package designpatterns.decorator;

public class EggDecorator extends PizzaDecorator {

    public EggDecorator(Pizza pizza) {
	super(pizza);
    }

    private double price = 1.0;

    @Override
    public double getPrice() {
	return super.getPrice() + price;
    }

}
