import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;

public class Solution{

  // 出边数组
  private List<List<Integer>> to = new ArrayList<>();
  // 广度优先辅助队列
  private Queue<Integer> q = new LinkedList<>();




  public int treeDiameter(List<List<Integer>> edges){

    int n = 0;
    for(List<Integer> edge : edges){
      int x = edge.get(0);
      int y = edge.get(1);
      n = Math.max(n, Math.max(x,y));
    }
    // 从0开始，所以数组个数为n+1
    n++;
    // 初始化出边数组,一个结点，与他可以到达的所有点
    for(int i=0;i<n;i++) to.add(new ArrayList<>());
    for(List<Integer> edge: edges){
      int x = edge.get(0);
      int y = edge.get(1);
      // x可以到达y
      to.get(x).add(y);
      // y也可以到达x
      to.get(y).add(x);
    }

    // 从0出发找到最远的点P
    int p = findFarthest(0).getKey();
    // 从P出发找到最远的点，返回距离，
    // 因为要求的是距离
    return findFarthest(p).getValue();



  }

  // <点，距离>
  Pair<Integer,Integer> findFarthest(int start){
    q.add(start);

  }
}
