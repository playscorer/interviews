package DSA;

public class ReverseList {

    /**
     * Given the head of a singly linked list, reverse the list, and return the reversed list.
     */
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    public static ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        while (curr != null) {
            ListNode nextTemp = curr.next;
            curr.next = prev;
            prev = curr;
            curr = nextTemp;
        }
        return prev;
    }

    public static ListNode reverseListLessWorst(ListNode head) {
        if (head == null) {
            return null;
        }

        ListNode prev=head;
        ListNode cur=head.next;

        while (cur != null) {
            ListNode next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }

        head.next=null;
        return prev;
    }

    public static ListNode reverseListWorstCode(ListNode head) {
        if (head == null) {
            return null;
        }
        if (head.next == null) {
            return head;
        }
        ListNode prev = head;
        ListNode cur = head.next;
        head.next = null;
        while (cur.next != null) {
            head = cur.next;
            cur.next = prev;
            prev = cur;
            cur = head;
        }
        cur.next=prev;
        return cur;
    }

    public static void displayList(ListNode head) {
        while (head != null) {
            System.out.print(head.val + " ");
            head = head.next;
        }
        System.out.println();
    }

    public static ListNode createNewList(int length, int incr) {
        ListNode head = new ListNode();
        ListNode cur = head;
        ListNode prev = null;

        for (int i=1; i<=length; i+=incr) {
            cur.val=i;
            cur.next = new ListNode();
            prev = cur;
            cur = cur.next;
        }
        prev.next = null;

        return head;
    }

    public static void main(String[] args) {
        ListNode head = createNewList(5,1);
        displayList(head);
        displayList(reverseList(head));
    }

}
