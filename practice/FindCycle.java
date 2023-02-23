public class FindCycle {
  public static boolean hasCycle(ListNode head) {
    ListNode fast = head;
    while (fast != null && fast.next != null) {
      fast = fast.next.next;
      head = head.next;
      if (fast == head) {
        return true;
      }
    }
    return false;
  }

  public static void main(String[] args) {

    ListNode one = LinkListUtils.createCycleLinkList();
    // LinkListUtils.printLinkedList(one);
    System.out.println(hasCycle(one));
  }
}
