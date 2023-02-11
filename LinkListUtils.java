public class LinkListUtils {

  public static String printLinkedList(ListNode head){

    if (head == null) {
      return null;
    }
    StringBuilder str = new StringBuilder();
    str.append("[");
    while(head != null){
      str.append(head.val).append("->");
      head = head.next;
    }
    str.append("NULL]");
    return str.toString();
  }

  public static ListNode createLinkList(){
    ListNode one = new ListNode(1);
    ListNode two = new ListNode(2);
    ListNode three = new ListNode(3);
    ListNode four = new ListNode(4);
    ListNode five = new ListNode(5);
    one.next = two;
    two.next = three;
    three.next = four;
    four.next = five;
    five.next = null;
    return one;
  }

  public static ListNode createCycleLinkList(){
    ListNode one = new ListNode(3);
    ListNode two = new ListNode(2);
    ListNode three = new ListNode(0);
    ListNode four = new ListNode(4);
    one.next = two;
    two.next = three;
    three.next = four;
    four.next = two;
    return one;
  }

}
