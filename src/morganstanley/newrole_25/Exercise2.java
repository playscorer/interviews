package morganstanley.newrole_25;

public class Exercise2 {

    /**
     * Q1: Define how a linked list internally works and write an implementation.
     * A1: Using the Node class alone is not the best practice. Rather wrap the root node into
     *     the LinkedList class.
     *
     * Q2: Write the add method.
     *
     * Q3: How would you instantiate a list in Java?
     * A3: List<Car> carList = new ArrayList<>();
     *
     * Q4: Can you instantiate MyLinkedList the same way?
     * A4: No because it does not implement the List interface and its methods
     */
    public static class Node<V> {
        private V val;
        private Node<V> next;

        public Node(V val) {
            this.val = val;
            this.next = null;
        }
    }

    public static class MyLinkedList<V> {
        private Node<V> root;

        void add(V val) {
            Node<V> newNode = new Node<>(val);

            if (root == null) {
                root = newNode;
            } else {
                Node<V> current = root;
                while(current.next != null) {
                    current = current.next;
                }
                current.next = newNode;
            }

        }
    }

    public static void main(String[] args) {
        MyLinkedList<Integer> myLinkedList = new MyLinkedList<>();
        myLinkedList.add(5);
        myLinkedList.add(2);
        myLinkedList.add(7);

        Node<Integer> current = myLinkedList.root;
        while (current.next != null) {
            System.out.println(current.val);
            current = current.next;
        }
        System.out.println(current.val);
    }
}
