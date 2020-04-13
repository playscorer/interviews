package algorithm;

public class Stack {
    
    public int CAPACITY = 10;
    
    private char[] array = new char[CAPACITY];
    
    private int head=0;
    
    private int tail=0;
    
    private int nbInserted=0;
    
    public void push(char c) {
	System.out.println("push(" + c + ")");
	if (nbInserted < CAPACITY) {
	    array[tail] = c;
	    tail++;
	    nbInserted++;
	    if (tail == CAPACITY) {
		tail = 0;
	    }
	} else {
	    System.out.println("Stack full");
	}
    }
    
    public char pop() {
	System.out.println("pop");
	char c = 0;
	if (nbInserted > 0) {
	    c = array[head];
	    head++;
	    nbInserted--;
	    if (head == CAPACITY) {
		head = 0;
	    }
	} else {
	    System.out.print("Stack empty");
	}
	return c;
    }
    
    public void display() {
	System.out.println("head:=" + head);
	System.out.println("tail:=" + tail);
	System.out.println("nbInserted:=" + nbInserted);
	System.out.println("array:=" + String.valueOf(array));
    }

    public static void main(String[] args) {
	Stack s = new Stack();
	s.push('A');
	s.push('B');
	s.push('C');
	s.push('D');
	s.push('E');
	s.push('F');
	s.push('G');
	s.push('H');
	s.push('I');
	s.push('J');
	s.display();
	s.push('K');
	s.display();
	System.out.println(s.pop());
	s.display();
	System.out.println(s.pop());
	s.display();
	s.pop();
	s.pop();
	s.pop();
	s.display();
	s.push('K');
	s.display();
	s.push('L');
	s.display();
	System.out.println(s.pop());
	s.display();
	System.out.println(s.pop());
	s.display();
	s.push('M');
	s.display();
	s.push('N');
	s.display();
	s.push('O');
	s.display();
	s.push('P');
	s.display();
	s.push('Q');
	s.display();
	s.push('R');
	s.display();
	System.out.println(s.pop());
	s.display();
	s.push('R');
	s.display();
	s.push('S');
	s.display();
	System.out.println(s.pop());
	s.display();
	System.out.println(s.pop());
	s.display();
	System.out.println(s.pop());
	s.display();
	System.out.println(s.pop());
	s.display();
	System.out.println(s.pop());
	s.display();
	System.out.println(s.pop());
	s.display();
	System.out.println(s.pop());
	s.display();
	System.out.println(s.pop());
	s.display();
	System.out.println(s.pop());
	s.display();
	System.out.println(s.pop());
	s.display();
	System.out.println(s.pop());
	s.display();
	s.push('S');
	s.display();
	s.push('T');
	s.display();
	s.push('U');
	s.display();
    }

}
