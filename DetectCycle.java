public class DetectCycle{
  public static ListNode detectCycle(ListNode head){
    ListNode fast = head;
    ListNode slow = head;
    while(fast!=null && fast.next!=null){
      fast = fast.next.next;
      slow = slow.next;
      if(slow==fast){
        while(head!=slow){
          head = head.next;
          slow = slow.next;
        }
        return head;
      }
    }
    return null;
  }

  public static void main(String[] args){
    ListNode one = LinkListUtils.createCycleLinkList();
    System.out.println(detectCycle(one).val);
  }
}
