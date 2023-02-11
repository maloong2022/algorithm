public class ReverseLinkedList {

  public static ListNode reverseList(ListNode head){
    ListNode last = null;
   while(head != null){
      ListNode nextHead = head.next;

      head.next = last;

      last = head;
      head = nextHead;
    }
    return last;
  }
  
  public static void main(String[] args){
    ListNode one = LinkListUtils.createLinkList();
    System.out.print(LinkListUtils.printLinkedList(one)+" ===> ");
    System.out.print(LinkListUtils.printLinkedList(reverseList(one)));
    
  }
}
