### 表达式求值

前缀表达式

- 形如“op A B”，其中 op 是一个运算符，A，B 是另外两个前缀表达式
- 例如： \* 3 + 1 2
- 又称波兰式

后缀表达式

- 形如“A B op”，其中 op 是一个运算符，A，B 是另外两个前缀表达式
- 例如： 1 2 + 3 \*
- 又称逆波兰式

中缀表达式

- 3 \* (1+2)

### 实战

150. Evaluate Reverse Polish Notation

You are given an array of strings tokens that represents an arithmetic expression in a Reverse Polish Notation.

Evaluate the expression. Return an integer that represents the value of the expression.

Note that:

- The valid operators are '+', '-', '\*', and '/'.
- Each operand may be an integer or another expression.
- The division between two integers always truncates toward zero.
- There will not be any division by zero.
- The input represents a valid arithmetic expression in a reverse polish notation.
- The answer and all the intermediate calculations can be represented in a 32-bit integer.

Example 1:

```
Input: tokens = ["2","1","+","3","*"]
Output: 9
Explanation: ((2 + 1) * 3) = 9
```

Example 2:

```
Input: tokens = ["4","13","5","/","+"]
Output: 6
Explanation: (4 + (13 / 5)) = 6
```

Example 3:

```
Input: tokens = ["10","6","9","3","+","-11","*","/","*","17","+","5","+"]
Output: 22
Explanation: ((10 * (6 / ((9 + 3) * -11))) + 17) + 5
= ((10 * (6 / (12 * -11))) + 17) + 5
= ((10 * (6 / -132)) + 17) + 5
= ((10 * 0) + 17) + 5
= (0 + 17) + 5
= 17 + 5
= 22
```

Constraints:

- 1 <= tokens.length <= 104
- tokens[i] is either an operator: "+", "-", "\*", or "/", or an integer in the range [-200, 200].

解题思路：

建立一个用于存数的栈，逐一扫描后缀表达式中的元素。

- 如果遇到一个数，则把该数入栈。
- 如果遇到运算符，就取出栈顶的两个数进行计算，然后把结果入栈。

扫描完成后，栈中恰好剩下一个数，就是该后缀表达式的值。

时间复杂度为 O(n)

```java
class Solution {
    private Stack<Integer> datas = new Stack();

    public int evalRPN(String[] tokens) {
        for(String token: tokens){
            if ("+".equals(token)||"-".equals(token)||"*".equals(token)||"/".equals(token)){
               int num2 = datas.pop();
               int num1 = datas.pop();
               if("+".equals(token)) datas.push(num1+num2);
               if("-".equals(token)) datas.push(num1-num2);
               if("*".equals(token)) datas.push(num1*num2);
               if("/".equals(token)) datas.push(num1/num2);
            }else {
               datas.push(Integer.parseInt(token));
            }
        }
        return datas.pop();
    }
}
```

227. Basic Calculator II

Given a string s which represents an expression, evaluate this expression and return its value.

The integer division should truncate toward zero.

You may assume that the given expression is always valid. All intermediate results will be in the range of [-231, 231 - 1].

Note: You are not allowed to use any built-in function which evaluates strings as mathematical expressions, such as eval().

Example 1:

```
Input: s = "3+2*2"
Output: 7
```

Example 2:

```
Input: s = " 3/2 "
Output: 1
```

Example 3:

```
Input: s = " 3+5 / 2 "
Output: 5
```

Constraints:

- 1 <= s.length <= 3 \_ 105
- s consists of integers and operators ('+', '-', '\_', '/') separated by some number of spaces.
- s represents a valid expression.
- All the integers in the expression are non-negative integers in the range [0, 231 - 1].
- The answer is guaranteed to fit in a 32-bit integer.

```java
class Solution {
    private Stack<Character> op = new Stack();
    private Stack<Integer> datas = new Stack();

    public int calculate(String s) {
        List<String> tokens = new ArrayList();
        String number = "";
        for(int i=0;i<s.length();i++){
            char ch = s.charAt(i);
            // deal with the numbers
            if (Character.isDigit(ch)){
                number += ch;
                continue;
            }else {
                if(!"".equals(number)){
                    tokens.add(number);
                    number = "";
                }
            }

            // deal with the space char
            if(Character.isSpaceChar(ch)) continue;

            // deal with the operation
            int curRank = getRank(ch);
            while(!op.isEmpty() && getRank(op.peek())>= curRank){
                tokens.add(Character.toString(op.pop()));
            }
            op.push(ch);
        }
        // deal with the last number
        if(!"".equals(number)){
            tokens.add(number);
            number = "";
        }
        while(!op.isEmpty()){
            tokens.add(Character.toString(op.pop()));
        }
        return evalRPN(tokens);
    }

    // return the rank of operator
    private int getRank(char ch){
        if(ch == '*' || ch == '/') return 2;
        if(ch == '+' || ch == '-') return 1;
        return 0;
    }

    private int evalRPN(List<String> tokens) {
        for(String token: tokens){
            if ("+".equals(token)||"-".equals(token)||"*".equals(token)||"/".equals(token)){
               int num2 = datas.pop();
               int num1 = datas.pop();
               if("+".equals(token)) datas.push(num1+num2);
               if("-".equals(token)) datas.push(num1-num2);
               if("*".equals(token)) datas.push(num1*num2);
               if("/".equals(token)) datas.push(num1/num2);
            }else {
               datas.push(Integer.parseInt(token));
            }
        }
        return datas.pop();
    }
}
```

772. Basic Calculator III

Given a string s which represents an expression, evaluate this expression and return its value.

The integer division should truncate toward zero.

You may assume that the given expression is always valid. All intermediate results will be in the range of [-231, 231 - 1].

Note: You are not allowed to use any built-in function which evaluates strings as mathematical expressions, such as eval().

Example 1:

```
Input: s = "1 + 1"
Output: 2
```

Example 2:

```
Input: s = " 2-1 + 2 "
Output: 3
```

Example 3:

```
Input: s = "(1+(4+5+2)-3)+(6+8)"
Output: 23
```

Constraints:

- 1 <= s.length <= 3 \_ 105
- s consists of integers and operators '+', '-', '(', ')',' ' separated by some number of spaces.
- s represents a valid expression.
- All the integers in the expression are non-negative integers in the range [0, 231 - 1].
- The answer is guaranteed to fit in a 32-bit integer.

```java
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Solution {

    private Stack<Character> op = new Stack();
    private Stack<Integer> datas = new Stack();

    public int calculate(String s) {
        List<String> tokens = new ArrayList();
        String number = "";
        // deal with the negative number
        boolean needZero = true;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            // deal with the numbers
            if (Character.isDigit(ch)) {
                number += ch;
                // after number not need zero
                needZero = false;
                continue;
            } else {
                if (!"".equals(number)) {
                    tokens.add(number);
                    number = "";
                }
            }

            // deal with the space char
            if (Character.isSpaceChar(ch)) continue;
            // deal with the ( operator
            if (ch == '(') {
                op.push(ch);
                // after (  need zero
                needZero = true;
                continue;
            }
            // deal with the ) operator
            if (ch == ')') {
                while (op.peek() != '(') {
                    tokens.add(Character.toString(op.pop()));
                }
                // pop the ( operator
                op.pop();
                // after ) not need zero
                needZero = false;
                continue;
            }

            // before deal with operator need deal with zero
            if ((ch == '+' || ch == '-') && needZero) {
                tokens.add("0");
            }
            // deal with the operation
            int curRank = getRank(ch);
            while (!op.isEmpty() && getRank(op.peek()) >= curRank) {
                tokens.add(Character.toString(op.pop()));
            }
            op.push(ch);
            // after + - * / need zero
            needZero = true;
        }
        // deal with the last number
        if (!"".equals(number)) {
            tokens.add(number);
            number = "";
        }
        while (!op.isEmpty()) {
            tokens.add(Character.toString(op.pop()));
        }
        return evalRPN(tokens);
    }

    private int getRank(char ch) {
        if (ch == '*' || ch == '/') return 2;
        if (ch == '+' || ch == '-') return 1;
        return 0;
    }

    private int evalRPN(List<String> tokens)
```
