package designpatterns.visitor;

import java.awt.Color;

public class VisitorPaint implements Visitor {

    @Override
    public void visitCar(Car car) {
	car.setCarColor(Color.blue);
    }

    @Override
    public void visitBicycle(Bicycle bicycle) {
	bicycle.setBicycleColor(Color.blue);
    }

}
