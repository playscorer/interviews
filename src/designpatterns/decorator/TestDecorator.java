package designpatterns.decorator;

public class TestDecorator {

    public static void main(String[] args) {
	Pizza parma = new Parma();
	System.out.println("Parma : " + parma.getPrice());
	parma = new CheeseDecorator(parma);
	System.out.println("Parma with cheese : " + parma.getPrice());
	parma = new EggDecorator(parma);
	System.out.println("Parma with cheese and egg : " + parma.getPrice());
    }

}
