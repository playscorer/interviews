package designpatterns.visitor;

import java.awt.Color;

public class Car implements Visitable {

    private Color carColor;

    private int wheelsNumber = 4;

    @Override
    public void accept(Visitor visitor) {
	visitor.visitCar(this);
    }

    void setCarColor(Color c) {
	this.carColor = c;
    }

    public Color getCarColor() {
	return carColor;
    }

    public int getWheelsNumber() {
	return wheelsNumber;
    }

}
