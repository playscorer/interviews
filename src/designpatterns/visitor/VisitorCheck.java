package designpatterns.visitor;

public class VisitorCheck implements Visitor {

    @Override
    public void visitCar(Car car) {
	if (car.getWheelsNumber() == 4) {
	    System.out.println("Right!");
	} else {
	    System.out.println("Wrong!");
	}
    }

    @Override
    public void visitBicycle(Bicycle bicycle) {
	if (bicycle.getWheelsNumber() == 2) {
	    System.out.println("Right!");
	} else {
	    System.out.println("Wrong!");
	}
    }

}
