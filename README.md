# Android笔试题（45分钟）
## 单项选择题
1. 对于两个Activity A、B ，A通过startActivity()方法打开B。当在B中点击Back返回到A时，以下哪个方法 **不会** 被调用 （____）  
A.  A.onCreate()　　　　B.  A.onResume()  
C.  B.onPause()　　　　D.  B.onStop()

2. 以下有关Java访问控制符描述 **错误** 的是 （____）  
A.  public可以被其他类所访问  
B.  protected可以在同一个包中的类访问  
C.  default可以被子类访问  
D.  private可以被当前类访问  

3. 以下关于Java容器的描述中， **错误** 的是（____）  
A. Hashtable不允许记录空值  
B. HashMap支持线程同步  
C. LinkedList的读取性能要比ArrayList差  
D. LinkedHashMap输出的顺序和输入的相同  

4. Which method must be defined by a class implementing the java.lang.Runnable interface?(____)  
A. void run()  
B. void run(int priority)  
C. public void start()  
D. public void run()  
E. public void run(int priority)  
F. public void start(int priority)  

5. 以下代码输出结果是(____)  
A. NULL  
B. Compilation fails.  
C. The code runs with no output.  
D. An exception is thrown at runtime. 
```
  public class Test {
      public static void main(String[] args) {
          String str = NULL;
          System.out.println(str);
      }
  }
```
 

6. Http缺省的请求方法是。(____)  
A. PU　　B. GET  
C. POST　　D. TRACE

7. TCP的协议数据单元被称为(____)   
A. 比特　　B. 帧　　C. 分段　　D. 字符

## 多项选择题
1. Which two CANNOT directly cause a thread to stop executing? (____)  
A. Existing from a synchronized block.  
B. Calling the wait method on an object.  
C. Calling notify method on an object.  
D. Calling read method on an InputStream object.  
E. Calling the SetPriority method on a Thread object.  
 
2. 以下哪个方法不能在子类中被重写(____)  
A. final void methoda() {}  
B. void final methoda() {}  
C. static final void methoda() {}  
D. static void methoda() {}  
E. final abstract void methoda() {}  

3. 在观察者模式中，表述正确的是(____)  
A. 观察者角色的更新是被动的。  
B. 被观察者可以通知观察者进行更新  
C. 观察者可以改变被观察者的状态，再由被观察者通知所有观察者依据被观察者的状态进行。  
D. 一个观察者可以观察多个被观察者的状态。  

4. 两个不同的计算机类型能通信，以下描述错误的是(____)  
A. 它们都是兼容的协议组  
B. 它们都使用TCP/IP  
C. 它们符合OSI 模型  
D. 它们一个是Macintosh，一个是Unix 工作站  

## 代码查错题
```
输入一个字符串，要求输出每一个字符出现的次数（不要求顺序）
样例：
- 输入
aababcabcdabcde
- 输出
a(5) b(4) c(3) d(2) e(1)
```
以下的解题代码片段中，有若干处错误，请用下划线标识出来，并在其后面更正
```
1    public class Demo {

2    	public static void main(String[] args) {

3    		Scanner scanner = new Scanner(System.in);

4    		System.out.println(请输入字符串);

5    		String str = scanner.next();

6    		TreeMap<Character, Integer> map = new TreeMap();

7    		for(int i = 0; i <= str.length(); i++) {

8    			Character key = str.charAt(i);

9    			if(map.containsKey(key)) {

10    				map.put(key, 1);

11    			}else {

12    				map.put(key, map.get(key) + 1);

13    			}			

14    		}

15    		StringBuffer sb = new StringBuffer();

16    		Set<Entry<Character, Integer>> entrySet = map.entrySet();

17    		for(Entry<Character, Integer> entry in entrySet) {

18    			sb.append(entry.getKey())

19              .append("(")

20              .append(entry.getValue())

21              .append(") ");

22    		}

23    		System.out.println(sb.getString());

24    }
```


## 手写编程题
#### 对于给定的二叉树，使用非递归的方式输出其中序遍历
```
/**
 * Definition for a binary tree node.
 * public class Node {
 *     int val;
 *     Node left;
 *     Node right;
 *     Node(int x) { val = x; }
 * }
 */
class Solution {
    public List<Integer> middleOrderTraversal(TreeNode root) {
        // white your code
    }
}
```
