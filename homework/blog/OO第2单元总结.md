# OO第二单元总结与反思

## 第一次作业设计

### 代码架构

```c
|- InputHandler 	输入类，线程
|- Elevator			电梯类，线程
|- Request			单个请求
|- RequestQueue		等待队列，为Elevator与InputHandler的公有对象
|- OutputHandler	输出类
|- MainClass		主类
```

### 框架设计

线程有Elevator和InputHandler。

InputHandler在接收到输入后便将相应的请求放到电梯的RequestQueue中。

电梯处理其对应的RequestQueue的请求。没有请求时要wait()避免轮询。

**同步块设计和锁的选择：**RequestQueue为共享对象，可能出现线程不同步的问题，因此使用`synchronized`方法确保线程同步，锁的是实例化对象。

### 设计细节

- 避免轮询，电梯在没有请求且输入没有结束时，使其`wait()`。与其相对应的`notifyall()`**在且仅在**RequestQueue中的`setEnd()`和`addRequest()`中，保证当RequestQueue不再有新对象 和 新增请求时，能够将电梯唤醒，让电梯及时工作或杀电梯线程。
- 线程结束方法：输入线程只要输入结束后就会结束，在取到NULL请求后，将所有请求队列`setEnd(True)`，电梯发现请求队列为end且人已经全部送完了的话，就结束线程。

### 电梯运行策略

电梯使用Look策略，其含义如下：

- 电梯转向，需要满足2个条件：
  	1.电梯中没有人
  	2.当前电梯的运行方向上没有请求存在（无论请求的方向）
- 捎带策略：当前楼层中存在与电梯运行方向相同的请求且电梯中人数没满。

在写电梯的run方法时，修改了好几个版本，以下是最后最精简版本的伪代码

```java
@Override
public void run(){
    while(true) {
        boolean openFlag = false; // 是否开过门
        if (elevatorIsEmpty && RequestQueue.isEmpty && RequestQueue.isEnd) {
            return;
        }
        if (personNeedOut()) { // 要不要开门下人
        	openDoor;
            personGoOut;
            openFlag = true; // 记录，开过门了
        }
        if (isNeedToTurn()) { // 转向判断
			Turn();
        }
        if (openFlag) { // 上人
            personGoIn; //开过门了，无条件上人 
        	closeDoor;
        } else if (personCanIn) {
            openDoor;
            personGoIn;
  			closeDoor;
        }
        if (!elevator.isEmpty || request_exist_this_side) { 
            move(); // 电梯有人或者该方向上有请求时，就移动
        } else if (RequestQueue.isEnd) {
            return; 
        } else {
            RequestQueue.letItWait(); // 没人在电梯，没新请求，就wait()
        }
    }
}
```

### UML类图

![homework5](https://s2.loli.net/2022/04/29/FKAGYf6OWcRngbv.jpg)



## 第二次作业设计

### 代码结构

```c
|- InputHandler 		输入类，线程
|- OutputHandler		输出类
|- Elevator				抽象电梯类，
|- VerticalElevator		纵向电梯类
|- HorizontalElevator	横向电梯类
|- Ask					单个请求
|- AskQueue				等待队列，为Elevator与InputHandler的公有对象
|- Const				常数类
|- MainClass			主类
```

Elevator是抽象类，内部实现了一些横向纵向电梯共有的方法。

### 总体设计

此次作业新增了横向电梯，并且需要实时增加电梯。由于采用自由竞争的分配策略，整体设计框架与第一次作业相比变化不大。

- InputHandler对输入信息进行判断，如果是增加电梯的话就新增电梯，是请求的话就将其分配到对应的请求列表中。
- 由于采用的是自由竞争，所以无需调度器，只要将请求分配到相应的请求队列就可以了
- 同一层（或同一栋）的几个电梯共享一个请求队列，此队列记录了在该楼层（该楼座）上的移动请求。
- 视每个电梯独立，每个电梯都会运人，并尝试从AskQueue中取出请求。纵向电梯的行为与之前相比没有任何变化，横向电梯的移动策略是伪look。
- **同步块设计和锁的选择：**AskQueue为共享对象，可能出现线程不同步的问题，因此使用`synchronized`方法确保线程同步，锁的是实例化对象。

### bug分析

我第一版的作业存在轮询的bug，但是能过中策，~~中策数据也太水了~~，所以没有发现bug，最后是用了何tr大佬的评测机才发现有这个bug，这里表示一下蒟蒻对dalao的感谢和膜拜orz。

**原因分析**：究其原因，是在不该notifyAll()的地方notifyAll()了。我一开始仿照第一次作业中的写法，在所有AskQueue中的方法中都notifyAll()了。例如：isEnd()。由于电梯run方法中每一次循环都会调用isEnd()，所以其实根本无法成功wait()，例如：A楼座中有电梯1，2，电梯1已经wait()了，但是电梯2里面还有人，在其每次循环中，都会调用isEnd()方法，即会唤醒电梯1，电梯1被唤醒后循环一圈又wait()了，但是电梯2在下一个循环中又会唤醒电梯1。也就是说wait()失败了。

**解决办法**：什么时候需要唤醒电梯？（1）输入结束了，要杀线程，在此之前要唤醒电梯线程；（2）新加请求后，需要唤醒电梯接客。
因此，仅仅需要保留`setEnd()`和`addRequest()`两个方法中的`notifyAll()`。

### 横向电梯运行策略

纵向电梯运行策略与上一次作业完全一致。此处介绍横向电梯。

采用的是伪look策略：

- **转向判断：** 满足两个条件中的任意一个就转向
  1. 电梯没人、此楼座和前方两个楼座无法捎带、转向后此楼座和后方两座楼座有可捎带请求，转向
  2. 电梯没人、前方两个楼座无法请求（不考虑方向），后方两个楼座有请求，转向。 （此时本楼座一定没有请求）

- **捎带策略：** 可以稍带就捎带，仅捎带同方向的请求。

### UML类图

![homework6](https://s2.loli.net/2022/04/29/CZcmy5NYzHB47OA.png)



## 第三次作业设计

### 代码结构

```c
|- InputHandler 			输入类，线程
|- OutputHandler			输出类
|- Elevator					抽象电梯类
|- VerticalElevator			纵向电梯类
|- HorizontalElevator		横向电梯类
|- HorizontalElevatorMap	横向电梯管理类
|- Scheduler				调度器
|- Ask						单个请求
|- AskQueue					等待队列
|- Const					常数类
|- MainClass				主类
```

HorizontalElevatorMap为横向电梯管理类，其内部为hashMap，key为楼层，value为Arraylist<HorizontalElevator>。因为调度器需要每个楼层的电梯信息来选定换乘楼层，所以创建了这个类。

### 总体设计

​	本次作业与前次作业不同点在于需要换乘，一个请求需要分成多段来处理。为了保证不过多的修改Ask类，需要引入一个Scheduler类来处理分配请求。（若在输入时就决定未来路线，将大大增加InputHandler类的复杂度，同时大改Ask类，不利于代码的复用）

- 因此，本次作业主要的不同点为新加了一个调度器，其会根据请求者当前的位置和目的地位置决定此次的方向和目的地。
- 输入线程在得到请求后就将请求放到调度器的waitQueue中，当乘客出电梯时，判断是否到达了最终终点，没到的话将请求重新放入调度器中准备下一次的调度。
- 电梯的行为与上一次作业几乎一致，为自由竞争。电梯会主动去接能接上的乘客，先到先得。仅需在横向电梯那里增加能否开门的判断。
- 因为Scheduler需要读取横向电梯的属性，InputHandler可以增加横向电梯，所以在此引入了一个新的共享对象，需要增加一个横向电梯类HorizontalElevatorMap，新写方法来查询电梯的属性、新增电梯，且都要synchronized。

### 调度器设计及线程交互方式

#### 调度器设计

​	调度器的调度仅仅取决于乘客目前的位置和目的地。由于没有采用流水线的设计模式，又需要避免出现调度出现反复横跳的情况，因此优先采用换乘次数少的策略，在代码实现时先判断能不能不换乘，再看能不能只换乘一次，最后是需要换乘两次。

#### 调度策略

此处简介调度器分配移动请求的策略，即选择换座楼层的策略。

为了满足效率需要，定义拥堵程度 $congestionDegree = waitNum \ /\ elevatorNum$,

1. 能不换乘就不换乘，直接换楼or换座到达最终重点。
2. 当必须要换乘时，判断nowFloor和destinationFloor有无水平电梯可以完成换座。若可行则选择这两种方案。
   当两种方案都可行时，优选$congestionDegree$小的楼层。
3. 当必须换乘两次时：
   - 优先遍历nowFloor和destinationFloor之间的楼层，选出最不堵的且能够实换乘的那一层。
   - 然后一层一层向上下楼层扩展考虑，找到一个可行的就停。都行时优选$congestionDegree$小的楼层。

#### 线程结束方法

​	一开始直接采用前两次作业类似的方法，当调度器`waitQueue.isEnd() == true && waitQueue.isEmpty() == true`时（waitQueue为待分配的队列），就结束线程。但由于现在仍在电梯中的人出电梯后可能仍需要使用调度器决定之后的行程，因此这样写会导致调度器提前结束。

​	因此维护两个量：totalAskNum 和finishedAskNum，唯有满足以上条件且 `totalAskNum == finishedAskNum`时才结束线程。

​	除此之外，还需考虑最后如何唤醒调度器线程。在本次作业中，每当一个人抵达了最后的终点，都会判断 `totalAskNum == finishedAskNum && waitQueue.isEnd()`,若条件成立，则新增一个非法请求用于唤醒Scheduler线程，调度器会忽略这个请求，同时结束线程。

#### 线程交互

调度器与输入线程和电梯线程存在交互，在本次作业中，采用了生产者消费者模式，通过waitQueue()这一个“托盘”，实现了对请求的获取和分配，完成了线程间的交互。

### 同步块的设计和锁的选择

本次作业中有两个共享对象：WaitQueue和 HorizontalElevatorMap。

- WaitQueue：输入线程新增请求，电梯、调度器读取并取出请求。

  - 锁实例化对象，给所有访问和修改请求的方法加锁。

    ```java
    public synchronized void addAsk(Ask ask) {
    	// add an ask
        notifyAll();
    }
    ```

- HorizontalElevatorMap：输入线程新建电梯，调度器读取电梯开门情况。

  - 锁实例化对象，两个地方要加锁。

    1. 增加电梯和读取开门情况的方法加锁

    2. 调度器在调度时，需保证在某一个请求的分配过程中电梯状态不变，此时不允许新增电梯，需要给这一段分配请求的代码加锁。

       ```java
       synchronized(horizontalElevator) {
           // 具体分配实现
       }
       ```





### UML类图 和 协作图

![UML_homework7](https://s2.loli.net/2022/04/29/rvgs3NdfHWSY9Im.png)

![CommunicationDiagram1](https://s2.loli.net/2022/04/29/fK28EbmySDJt3oZ.png)



## 心得体会

**线程安全：**

​	第三单元的作业让我初步了解了多线程，虽然三次作业都没有出现因为多线程问题引发的bug，但是在码代码的过程中多次遇到了线程安全的问题，第二次作业中甚至根本没有意识到轮询的问题。在未来的设计过程中要时刻关注线程安全的问题。

**层次化设计：**

​	其实三次作业的层次化设计都有很多可以改进的地方。由于我没有采用有限状态机的写法，选择了将电梯运行逻辑写到run方法中，所以电梯类中包含了策略和电梯行为，导致两个电梯类很大，迭代开发难度较大。

​	在第二次作业中，我设计了一个Elevator抽象类，本来是想将两电梯的共有属性和共有方法写入其中，但后来因为各种方法的需要，又决定将相关的方法写到了子类中。最后又因为方法参数的问题，决定把所有的属性放到子类中，反复横跳十分痛苦。通过本次作业，个人认为如果不存在需要用父类引用调用子类方法的情况，没必要增加父类，若无以上需求，最后只能起到一个代码复用的效果。

**一些感想：**

​	本单元的三次作业我都采用了半重构的方法进行迭代开发。
​	对于代码变化较小的部分（如纵向电梯），我也是对着上一次作业的代码重打了一次，导致三次作业迭代开发所用的时间都挺长的。虽然这样可以更好的保证正确性，但其实没必要，完全可以利用IDEA来定位代码中需要修改的部分，减小开发的工作量。
