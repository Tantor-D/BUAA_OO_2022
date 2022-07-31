# OO第四单元

## 架构设计

应该考虑使用代理模式的，而不是自己写一个全新的类

### 第一次作业

#### 项目结构

我的项目结构如下：

```java
|- mycode
    |-MainClass
    |-EnumsType	// 枚举类，用于表明Attribute的真实类型(引用和基础)
    |-Tools		// 工具类，负责具体的功能实现
    |-MyAttribute
    |-MyClass
    |-MyImplementation // 调用Tools类中的方法建立静态图，同时实现UserApi的询问
    |-MyInterface
    |-MyOperation
    |-MyParameter
```

#### 需求分析

本次作业的UML图信息已经全部给定，需要我们对不同的询问进行回答。由于图不会在中途进行改变，因此可以建立一张静态图，将大部分询问的结果直接保存到图中，不必在每次询问时重新计算。

根据以上分析，我的作业总体实现分两步：1.分析元素； 2.根据请求建立静态图，预先求出部分询问的结果

#### 分析元素

由于输入信息无序，但是需要按顺序建立类图，所以一次只解析一个类，解析顺序如下：（本次作业忽略了`UmlAssociation`和`UmlAssociationEnd`）

```java
Class
Interface
Attribute
Operation
Parameter
Generalization
InterfaceRealization
```

#### 静态图建立

我在`MyImplement`的构造函数中实现了静态图的简历，总体流程如下：
```java
public MyImplementation(UmlElement... elements) { 
    tool = new Tools(name2ClassId, id2Class, id2Interface, id2Operation, id2Attribute);
    tool.analyseClass(elements);
    tool.analyseInterface(elements);
    tool.analyseAttribute(elements);
    tool.analyseOperation(elements);
    tool.analyseParameter(elements);
    tool.analyseGeneralization(elements);
    tool.analyseInterfaceRealization(elements);

    // 以下为静态图的建立
    tool.classGetAllAttribute();    // 找到类所有的attribute,同时计算属性耦合度
    tool.interfaceGetAllInterface(); // 处理接口间的继承关系
    tool.renewClassDepthOfInheritance(); // 处理类之间的继承关系，刷新类的继承深度
    tool.classGetAllInterface();    // 处理类对接口的实现，找到所有类实现的接口
    tool.renewOperationWrongType(); // 刷新所有的operation是否存在错误类型,后来直接得结果
    tool.renewOperationCoupling();  // 刷新所有类的操作耦合度
}
```

#### 设计细节

在此次作业中，由于不了解未来的作业增量开发要求，且UmlElement中有很多属性和方法在自己的类图中是用不到的，因此没有新建子类继承UmlElemnt。故选择在自己的类中重新定义id、name等字段，在构造时给他们赋值，从而舍弃所有UmlElement，只保留自己重建的类和信息。

这样的优点是程序轻量，冗余信息少，缺点是迭代开发时对于缺少的信息需要一个个添加，迭代开发难度大。在第2，3次作业中我采用了代理模式解决这个问题。

### 第二次作业

#### 概览

第二次作业相比第一次作业增加了顺序图和状态图，项目的总体结构与第一次作业相差不大，类图的部分几乎不需要改动。

我的项目结构如下：

```java
|- mycode
    |-model		// 类图
    	|-ModelClass
    	|-ModelInterface
    	|-ModelOperation
    	|-ModelParameter
    	|-ModelAttribute
    |-sequence	// 顺序图
    	|-SequenceInteraction
    	|-SequenceLifeline
    |-statemachine	// 状态图
    	|-SmEvent
    	|-SmRegion
    	|-SmState
    	|-SmStateMachine
    	|-SmTransition
    |-MainClass
    |-EnumsType	 	 // 枚举类，表明Attribute的真实类型(引用和基础)
    |-EnumsStateType // 枚举类，表明State的类型:initial、normal、final
    |-Tools			 // 工具类
    |-MyImplementation
```

解析顺序如下：

```java
Class
Interface
Attribute
Operation
Parameter
Generalization
InterfaceRealization
// 顺序图的分析
Interaction
Lifeline
Endpoint
Message
// 状态图的分析
StateMachine
3kindsState
Transition
Event
```

#### 设计细节

本次作业中我采用了**代理模式**，解决了第一次作业中遇到的问题，以SequenceLifeline为例，采用代理模式既最大限度地保存了信息，又保持了自己写的类的独立性。

```java
public class SequenceLifeline {
    private final UmlLifeline umlLifeline; // 保留一个UmlLifeline的引用，用于随时访问UmlLifeline中的信息
    public SequenceLifeline(UmlLifeline umlLifeline) {
        this.umlLifeline = umlLifeline;
    }
    public String getId() {
        return umlLifeline.getId();
    }
    public String getName() {
        return umlLifeline.getName();
    }
    public UmlLifeline getUmlLifeline() {
        return umlLifeline;
    }
}
```

### 第三次作业

#### 概览

本次作业相比第二次作业新增了有效性判断，几乎没有改变第二次作业的框架结构，只在静态类图的建立部分进行了小修改。前两次作业的类图能够成功建立，前提是继承关系正确，即不存在循环继承。因此本次作业中，只有在R003和R004都正确的情况下才进行静态类图的建立。

本次作业的框架结构与第二次作业完全一致，几个有效性判断都是通过在第二次作业的基础上打补丁实现的。除了R003和R004其它的几个Rule都很好写，因为它们并不要求找出具体出错的对象，只需判断存不存在错误。

值得一说的是R003和R004：关于循环继承，通过计算可知，暴力dfs的复杂度是满足要求的，且只需要跑一次，所以我用的是暴力dfs。一开始写的拓扑，写完才发现拓扑只能判断有没有环orz。

进行重复继承判断的前提是不存在循环继承，此时只需要从顶层父类开始向下遍历，当某个类可以被多次访问到时，说明存在重复继承。根据这个性质可知，没有多继承的类不存在重复继承，因此只用考虑接口的情况。

## 架构设计思维 和 OO理解的演进

这个学期的OO课锻炼的主要就是面向对象编程能力 和 架构设计能力

### 第一单元

第一单元学习了层次化建模设计，个人认为是最痛苦的一个单元，OO课在开学第一周就给了我当头一棒，这一单元的收获主要有如下几点：

1. 学习到了层次化设计的要点
2. 能够熟练使用Java中的各种容器，对于一些面向对象的特性有了更深层次的理解
3. 首次接触checkStyle，代码风格更规范

### 第二单元

第二单元主要学习了多线程相关的架构设计，这是我第一次实际上手多线程，在动手编码之前恶补了理论知识，学习了java的底层的线程管理和类的存储方式，收获很大，同时因为在OO课中接触过了多线程，在OS课的学习中感觉轻松不少，两门课互补让我对线程、锁有了更深入的了解。

关于这一单元的架构设计，我还是坚持“非必要不加线程”，前两次作业中主要是输入线程和电梯线程交互，第三次作业中新增了调度器线程，需要注意的点突然多了好多，但最后设计成功之后也非常有成就感。遗憾的是我没有真正实现调度器算法，只有在第三次作业的实现了横向楼层选择的调度，纵向的调度用的是自由竞争，~~yysy，自由竞争比好几个同学写的调度算法要快不少~~

### 第三单元

第三单元主要学习了JML规格，在我看来这个单元的学习很有意义，契约式编程的思想在我未来的学习中一定会有很大作用，本单元的收获主要有如下几点：

1. 契约式编程思想
2. JML规格的阅读和书写
3. 对Java异常的学习

### 第四单元

第四单元主要学习了UML图，此单元作业在理解了作业要求和UML图中各字段的作用后就很简单了。在此单元作业中我使用了代理模式，既好写又便于迭代开发。

此单元的架构设计不难，感觉和第三单元类似，就是建图，比较简单。~~难点集中在阅读指导书和讨论区~~

## 测试理解 和 实践演进

说来惭愧，实际上我只在第三单元实现了自己的评测机。

- 回想起来我也真是头铁，第一单元根本就没怎么进行课下测试，中测过了就万岁，而且我居然没被hack。

- 第二单元时我自己手搓了几组极端数据然后就抱dalao腿用dalao的评测机了。

- 由于第二单元时我的程序被dalao测出了一些bug，在第三单元时我痛定思痛写了个评测机并着手做随机数据的强化，相关实现可见[OO_第三单元总结](https://www.cnblogs.com/tantor/p/16339105.html)和[C++实现一个简单java对拍器](https://www.cnblogs.com/tantor/p/16323189.html)。通过使用强化的随机数据和边界数据来对拍测试。针对不同询问设计了不同的随机数据生成逻辑，互测时多次hack成功。
- 第四单元由于临近期末and随机数据生成难度大，我主要依靠手搓mdj来测试，~~还有抱大腿~~。

其实从大二上开始就想实现一个评测机，但是很多人都是用python写的，我又不会，因此一直拖着。现在想起来其实就是在偷懒，虽然我只在一个单元实现了评测机，但也很高兴，相信未来我不会再偷懒了。

## 课程收获

在OO课程中学到了面向对象的思想，同时收获了一种“设计能力”，就跟老师在颁奖课上说的一样，以后面对复杂的项目工程，不会感到无从下手不敢开始了，面对离谱的客户需求，也可以泰然自若地去设计开发了。

其它每个单元的收获在上面已经有了，就不赘述了。

总之，个人认为OO课是大二下一门完成度很高的课程，虽然周常被虐，但也学到了很多东西。指导书很详细~~（除了Unit4）~~，老师、助教都很nice，体验很好！！！为OO课程组打5星好评！

## 改进建议

1. 个人认为第一、二单元的难度远超三、四单元，可以考虑将第三单元(JML)改成第一个单元，不至于让大家在第一单元就被重击~~(开学时的事情真的很多)~~。同时，JML那一节能考的实在不多，可以考虑将coding次数减少成2次，早一周结课，不至于到期末还要写代码。
2. 多增加一些设计模式的内容，在本学期的学习中，工厂模式、代理模式、单例模式等设计模式给了我很大的启发。同时还可以借此了解一些工业编码的习惯和约定
3. 多增加一些线下研讨的机会，尤其是在层次化设计和多线程这两个单元，这两个单元难度大，同学们的架构设计多种多样，在线下研讨时分享bug和优秀设计让我学到了很多，同时也让我的编码过程更加顺畅、bug更少。
4. 早一点公布每个单元的第一次作业，每个单元中最难的就是第一次作业，因为需要设计一个总体框架。个人认为可以在博客周的周六就把题目放出来，让大家先自行来了解和学习。同时在第一次作业的那一周将理论课的一个小课时拿出来让大家交流。
