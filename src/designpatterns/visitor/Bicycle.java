package designpatterns.visitor;

import java.awt.Color;

public class Bicycle implements Visitable {

    private Color bicycleColor;

    private int wheelsNumber = 2;

    @Override
    public void accept(Visitor visitor) {
	visitor.visitBicycle(this);
    }

    public Color getBicycleColor() {
	return bicycleColor;
    }

    public void setBicycleColor(Color bicycleColor) {
	this.bicycleColor = bicycleColor;
    }

    public int getWheelsNumber() {
	return wheelsNumber;
    }

}
