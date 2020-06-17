package designpatterns.decorator;

public abstract class PizzaDecorator implements Pizza {

    private Pizza pizza;

    public PizzaDecorator(Pizza pizza) {
	super();
	this.pizza = pizza;
    }

    @Override
    public double getPrice() {
	return pizza.getPrice();
    }

}
