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
        if (head == null) {
            return null;
        }
        if (head.next == null) {
            return head;
        }
        ListNode prev = head;
        ListNode cur = prev.next;
        prev.next = null;
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

    public static void main(String[] args) {
        ListNode head = new ListNode();
        ListNode cur = head;
        ListNode next = null;
        int listSize = 5;
        for (int i=1; i<=listSize; i++) {
            cur.val=i;
            cur.next = new ListNode();
            next = cur;
            cur = cur.next;
        }
        next.next = null;

        displayList(head);
        displayList(reverseList(head));
    }

}
