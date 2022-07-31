# OO 第一单元总结与反思

## 简介

​	本人由于对Java的不了解和对字符串处理的不熟悉，第一周到周六才开始写作业，直接就使用了预解析的方式，之后的几次作业也偷懒沿用了预解析的形式来完成作业。使用预解析的话，因为无需考虑对符号的处理，只用处理运算的过程，所以比起正常读入要简单非常多。三次作业的架构大体相同，区别只在于对因子的处理以及化简。

## 第一次作业

### 作业思路

​	对于因子（Factor）的表示，可以统一为$a*x^b$，因此，对于表达式类（Expr）可以使用一个`Arraylist<Factor> factors`来表示。 
​	在Expr类中实现对每个类的运算操作。

### UML图与类结构

![image-20220325144213080](https://s2.loli.net/2022/07/28/DaPI4UgmdZCwxoT.png)

其中，各个类的含义如下：

```java
|- MainClass：主类，含有main()，是程序入口
|- Expr：表达式类，实现了Expr间的各种运算操作
|- Factor：因子类
|- Parser：用于提取fx表达式的各个元素
|- Tools：在main函数实现了读入和对操作的分类后来具体执行
```



## 第二、三次作业

​	因为是使用与解析的形式完成的作业，全程只需关助运算无需考虑文法分析，所以三次作业的架构都是相似的，最大的区别在于Factor的组成以及优化策略。

### UML图和类结构

![homework3](https://s2.loli.net/2022/07/28/THazh7OK1qPd5ZL.jpg)

### 思路和各类的作用

​	使用一个hashmap来保存每一个表达式 `HashMap<String, Function>`，且使用深拷贝来保证hashmap中的value不会被更改，保证在未来调用时值不会变化。
​	对每次输出的命令进行分析，然后按要求进行表达式的计算操作，返回一个全新的实例化对象存入HashMap中。对每一次的运算结果都进行化简，保证每一条被存储的结果都是最简的。

- Mainclass
  - 有main函数，为程序的入口。
  - main函数需要判断当前读入的操作种类，然后调用mytool对相应的操作进行处理。

- MyTool
  - 有9各方法，分别对应预解析中的每个操作，由main()调用

- Parser
  - 供mytool使用，用于提取操作中的各个段，如：fx，sin，3（常数）。

- Function
  - 表达式类，内有一个`ArrayList<Factor>`
  - 关于方法，实现了 深拷贝、合并同类项，加法，乘法，sin cos操作，重写equals和Hashcode，

- Factor
  - 因子类，因统一表述为了 $a*x^b*\prod sin(expr)*\prod cos(expr)$  的形式，内部属性有a(BigInteger), b(BigInteger), sinmap(HashMap), cosmap(HashMap)。
  - 关于方法，实现了加法，乘法，优化，深拷贝，重写equals


### 度量分析

代码规模：

| Source File    | Total Lines | Source Code Lines |
| -------------- | ----------- | ----------------- |
| Factor.java    | 396         | 343               |
| Function.java  | 271         | 221               |
| MainClass.java | 68          | 38                |
| MyTool.java    | 97          | 74                |
| Parser.java    | 32          | 26                |
| Total          | 864         | 702               |

![image-20220325152319206](https://s2.loli.net/2022/07/28/p2J6BxjShf8Ubq1.png)

几个比较复杂的方法：

| method                                  | CogC             | ev(G)              | iv(G)              | v(G)               |
| :-------------------------------------- | ---------------- | ------------------ | ------------------ | ------------------ |
| Factor.Factor()                         | 0.0              | 1.0                | 1.0                | 1.0                |
| Factor.Factor(BigInteger, BigInteger)   | 0.0              | 1.0                | 1.0                | 1.0                |
| Factor.equalZero()                      | 0.0              | 1.0                | 1.0                | 1.0                |
| Factor.getAa()                          | 0.0              | 1.0                | 1.0                | 1.0                |
| Factor.getBb()                          | 0.0              | 1.0                | 1.0                | 1.0                |
| Factor.getCosMap()                      | 0.0              | 1.0                | 1.0                | 1.0                |
| Factor.getSinMap()                      | 0.0              | 1.0                | 1.0                | 1.0                |
| Factor.hashCode()                       | 0.0              | 1.0                | 1.0                | 1.0                |
| Factor.setAa(BigInteger)                | 0.0              | 1.0                | 1.0                | 1.0                |
| Factor.setBb(BigInteger)                | 0.0              | 1.0                | 1.0                | 1.0                |
| Factor.setCosMap(HashMap)               | 0.0              | 1.0                | 1.0                | 1.0                |
| Factor.setSinMap(HashMap)               | 0.0              | 1.0                | 1.0                | 1.0                |
| Function.Function()                     | 0.0              | 1.0                | 1.0                | 1.0                |
| Function.Function(String)               | 0.0              | 1.0                | 1.0                | 1.0                |
| Function.equalZero()                    | 0.0              | 1.0                | 1.0                | 1.0                |
| Function.getFactors()                   | 0.0              | 1.0                | 1.0                | 1.0                |
| Function.posFunc()                      | 0.0              | 1.0                | 1.0                | 1.0                |
| Function.refinefunc()                   | 0.0              | 1.0                | 1.0                | 1.0                |
| Function.setFactors(ArrayList)          | 0.0              | 1.0                | 1.0                | 1.0                |
| Function.subFunc(Function)              | 0.0              | 1.0                | 1.0                | 1.0                |
| MyTool.opAdd(HashMap, String)           | 0.0              | 1.0                | 1.0                | 1.0                |
| MyTool.opCos(HashMap, String)           | 0.0              | 1.0                | 1.0                | 1.0                |
| MyTool.opMul(HashMap, String)           | 0.0              | 1.0                | 1.0                | 1.0                |
| MyTool.opNeg(HashMap, String)           | 0.0              | 1.0                | 1.0                | 1.0                |
| MyTool.opNewNum(HashMap, String)        | 0.0              | 1.0                | 1.0                | 1.0                |
| MyTool.opPos(HashMap, String)           | 0.0              | 1.0                | 1.0                | 1.0                |
| MyTool.opPow(HashMap, String)           | 0.0              | 1.0                | 1.0                | 1.0                |
| MyTool.opSin(HashMap, String)           | 0.0              | 1.0                | 1.0                | 1.0                |
| MyTool.opSub(HashMap, String)           | 0.0              | 1.0                | 1.0                | 1.0                |
| Parser.getFx(String)                    | 0.0              | 1.0                | 1.0                | 1.0                |
| Parser.getOpNum1(String)                | 0.0              | 1.0                | 1.0                | 1.0                |
| Parser.getOpNum2(String)                | 0.0              | 1.0                | 1.0                | 1.0                |
| Parser.getSpecialNum(String)            | 0.0              | 1.0                | 1.0                | 1.0                |
| Factor.Factor(String)                   | 2.0              | 1.0                | 2.0                | 2.0                |
| Function.addFunc(Function)              | 1.0              | 1.0                | 2.0                | 2.0                |
| Function.copyTheFunc()                  | 1.0              | 1.0                | 2.0                | 2.0                |
| Function.cosFunc()                      | 2.0              | 1.0                | 2.0                | 2.0                |
| Function.hashCode()                     | 1.0              | 1.0                | 2.0                | 2.0                |
| Function.negFunc()                      | 1.0              | 1.0                | 2.0                | 2.0                |
| Function.sinFunc()                      | 2.0              | 1.0                | 2.0                | 2.0                |
| Parser.transToFunction(HashMap, String) | 2.0              | 2.0                | 2.0                | 2.0                |
| Factor.addFac(Factor)                   | 2.0              | 3.0                | 3.0                | 3.0                |
| Factor.copyTheFac()                     | 2.0              | 1.0                | 3.0                | 3.0                |
| Function.mulFunc(Function)              | 3.0              | 1.0                | 3.0                | 3.0                |
| Function.powFunc(BigInteger)            | 2.0              | 2.0                | 2.0                | 3.0                |
| Function.printFunc()                    | 2.0              | 2.0                | 3.0                | 3.0                |
| Factor.mulFac(Factor)                   | 8.0              | 1.0                | 5.0                | 5.0                |
| Factor.printSignal(boolean)             | 14.0             | 1.0                | 6.0                | 6.0                |
| Factor.refineFac()                      | 10.0             | 7.0                | 7.0                | 7.0                |
| Function.equals(Object)                 | 11.0             | 8.0                | 3.0                | 8.0                |
| Factor.compareTo(Factor)                | 8.0              | 9.0                | 5.0                | 9.0                |
| Factor.refineFac2()                     | 20.0             | 9.0                | 9.0                | 9.0                |
| Function.mergeFactors()                 | 16.0             | 5.0                | 9.0                | 9.0                |
| Function.judgeOnlyOne()                 | 5.0              | 4.0                | 7.0                | 10.0               |
| Function.specialPrint()                 | 7.0              | 2.0                | 10.0               | 10.0               |
| MainClass.main(String[])                | 11.0             | 1.0                | 10.0               | 10.0               |
| Factor.judgeCanMerge(Factor)            | 14.0             | 9.0                | 7.0                | 13.0               |
| Factor.printFac(boolean)                | 26.0             | 1.0                | 14.0               | 14.0               |
| Factor.equals(Object)                   | 18.0             | 12.0               | 6.0                | 15.0               |
| Total                                   | 191.0            | 120.0              | 161.0              | 189.0              |
| Average                                 | 3.23728813559322 | 2.0338983050847457 | 2.7288135593220337 | 3.2033898305084745 |

​		可以看到，复杂度比较高的方法集中于输出和表达式优化上，这些方法主要都是在使用面向过程的方式来进行处理，所以复杂度偏高。其它的方法复杂度都较低。

### 化简策略

#### 因子的化简

对于每一个因子，都统一表述为了：$a*x^b*\prod sin(expr)*\prod cos(expr)$ 的形式。比较方便执行化简操作。

- sin(0) = 0，将a->0，清空sinmap和cosmap。
- cos(0)= 1，删去cosmap中的这一项
- cos(-a) = cos(a)
- sin(-a)= -sin(a)
- 2sin(aa)cos(aa)==sin(2*aa)

#### 表达式化简

- $sin(a)^2 + cos(a)^2 = 1$
- 表达式合并

### 个人踩坑总结

- 第一次作业中，对引用的理解不够深，不知道深拷贝，导致出错。
  - 在java中，除了基本数据类型以外，其他的都是引用数据类型，可以将它们理解为指针。
  - 引用对象赋值给另一个引用对象时只是复制了一个引用（指针），实际上存储的数据没有被复制，所以通过某一个引用所做的修改在另一个引用中也看的见

- 在第二次作业时，因为是第一次使用hashmap，不知道当自定义的对象做key时，需要重写`equals()`和`Hashcode()`。
  - 所有的类都是Object类的子类，如果不重写`equals()`和`hashcode()`方法，就会调用Object类的方法，而这些方法并不会对实例化对象的具体值进行比较，只会比较引用的地址，不能满足作业要求。
- 关于ArrayList和HashMap中删除元素的操作
  - 个人通过查询资料，认为通过使用Iterator来删除元素是最稳妥的方法。
  - Java Iterator（迭代器）不是一个集合，它是一种**用于访问集合的方法**，可用于迭代 ArrayList 和 HashSet 等集合。

```java
'这是一个使用迭代器来删除ArrayList<Integer>中小于10的元素的方法'
Iterator<Integer> it = numbers.iterator();
while(it.hasNext()) {
    Integer i = it.next();
    if(i < 10) {  
        it.remove();  // 删除小于 10 的元素
    }
}
```



## 总结

​	假期没有预习pre，导致开学才匆忙做pre，开学第一周就毫无防备的来了一个大作业，整个学习过程十分痛苦。第一单元的三次作业都是采用预解析的方法完成的作业，第一周是时间不足导致的，第二周纯粹是因为想偷懒所以写的预解析。

​	在OO学习的第一个月，我偷懒犯了很多错，也少做了很多应该做的事。在第二个月一定好好学习。





