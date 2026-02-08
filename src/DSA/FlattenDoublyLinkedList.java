package DSA;

/**
 * This is my implementation that passes all tests.
 */
public class FlattenDoublyLinkedList {
    class Node {
        public int val;
        public Node prev;
        public Node next;
        public Node child;
    };

    public Node flatten(Node head) {
        if (head == null) {
            return null;
        }

        Node current = head;
        Node prev=null;
        Node root = new Node();
        Node node = root;

        while (current != null) {
            node.val = current.val;
            node.child = null;
            if (current.child != null) {
                node.next = flatten(current.child);
                node.next.prev = node;
                while (node.next != null) {
                    node = node.next;
                }
            }

            node.next = new Node();
            prev = node;
            node = node.next;
            node.prev = prev;
            current = current.next;
        }

        if (prev != null) {
            prev.next = null;
        }

        return root;
    }
}
