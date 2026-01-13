package DSA;

public class MergeTwoLists {
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }
    /*
    This solution copies each node value.
     */
    public static ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode newHead = new ListNode();
        ListNode curNode = newHead;
        ListNode prev = null;

        if (list1 == null && list2 == null) return null;

        while (list1 != null && list2 != null) {
            if (list1.val <= list2.val) {
                curNode.val = list1.val;
                list1 = list1.next;
            } else {
                curNode.val = list2.val;
                list2 = list2.next;
            }
            prev = curNode;
            curNode.next = new ListNode();
            curNode = curNode.next;
        }

        while (list1 != null) {
            curNode.val = list1.val;
            list1 = list1.next;
            prev = curNode;
            curNode.next = new ListNode();
            curNode = curNode.next;
        }

        while (list2 != null) {
            curNode.val = list2.val;
            list2 = list2.next;
            prev = curNode;
            curNode.next = new ListNode();
            curNode = curNode.next;
        }

        if (prev != null)
            prev.next = null;

        return newHead;
    }

    /*
    This codes modifies the original lists.
     */
    public static ListNode mergeTwoListsSimplerCode(ListNode list1, ListNode list2) {
        ListNode newHead = new ListNode();
        ListNode curNode = newHead;

        while (list1 != null && list2 != null) {
            if (list1.val <= list2.val) {
                curNode.next = list1;
                list1 = list1.next;
            } else {
                curNode.next = list2;
                list2 = list2.next;
            }
            curNode = curNode.next;
        }

        if (list1 == null) {
            curNode.next = list2;
        }

        if (list2 == null) {
            curNode.next = list1;
        }

        return newHead.next;
    }

    public static void displayList(ListNode head) {
        while (head != null) {
            System.out.print(head.val + " ");
            head = head.next;
        }
        System.out.println();
    }

    public static ListNode createNewList(int start, int length, int incr) {
        ListNode head = new ListNode();
        ListNode cur = head;
        ListNode prev = null;
        int val=start;

        for (int i=1; i<=length; i++) {
            cur.val=val;
            cur.next = new ListNode();
            prev = cur;
            cur = cur.next;
            val += incr;
        }
        prev.next = null;

        return head;
    }

    public static void main(String[] args) {
        ListNode list1 = createNewList(1,3,1);
        ListNode list2 = createNewList(1, 3,2);
        displayList(list1);
        displayList(list2);
        displayList(mergeTwoLists(list1, list2));
        displayList(list1);
        displayList(list2);
        displayList(mergeTwoListsSimplerCode(list1, list2)); // original list has been modified
        // Java passes object references by value.
        // When you pass list1 and list2 to the method,
        // Java copies the references into the method's parameters.
        // Inside the method, when you do list1 = list1.next or list2 = list2.next,
        // you're only updating the local parameter variables,
        // not the original references in the calling code.
        // However, the internal structure of these lists has been modified
        // because the method changed the next pointers of the nodes themselves.
        displayList(list1); // list1 still has its original value
        displayList(list2); // list2 still has its original value
    }
}
