public class ReverseKGroup{
  public static ListNode reverseKGroup(ListNode head, int k){

    // avoid the last.next rasie a NullException
    ListNode protect  = new ListNode(0,head);
    ListNode last = protect;

    // group iterator
    while(head != null){
      // 1. make group: walk back k-1 step, find the head and end of the group 
      ListNode end = getEnd(head,k);
      // if the group size less than k, not change it
      if (end==null){
        break;
      }

      ListNode nextGroupHead = end.next;


      //2.reverse the group 
      reverseGroup(head, nextGroupHead);

      //3.connect the small LinkList bond
      // after group
      head.next = nextGroupHead;
      //before group
      last.next = end;


      // move last
      last = head;
      head = nextGroupHead;
    }
    return protect.next;

  }

  public static void main(String[] args){
    ListNode one = LinkListUtils.createLinkList();
    System.out.print(LinkListUtils.printLinkedList(one)+" ===> ");
    System.out.print(LinkListUtils.printLinkedList(reverseKGroup(one,2)));
    //System.out.print(LinkListUtils.printLinkedList(reverseKGroup(one,3)));
    
  }

   static void reverseGroup(ListNode head,ListNode stop){

    // because the first is not null, so deal first step
    ListNode last = head;
    head = head.next;
    while(head!=stop){
      ListNode nextHead = head.next;
      head.next = last; 
      last = head;
      head = nextHead;
    }
  }

  static ListNode getEnd(ListNode head,int k){
    while(head!=null){
      k--;
      if (k==0){
        return head;
      }
      head = head.next;
    }
    // head = null
    return null;
  }

}
