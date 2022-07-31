# BUAA_OO_第三单元总结

## 一、测试数据

采用了随机数据+特殊数据的方法准备测试数据

### 随机数据

采用的生成策略为：纯随机数据+优化。

**纯随机策略：**设置询问条数上限，随后每个询问都以相同概率随机出现。

分析纯随机生成策略，可知其中$personNum : groupNum: relationNum : MessageNum = 1 : 1:1 : 1$，显然生成的是一个稀疏图，其测试强度不够，因此在纯随机的基础上进行了如下四个方向的优化

1. person、group数不能太多，也不能太少。太多则生成稀疏图，太少测试强度不足
   - 以person为例
     在生成的一开始就生成一些addPerson，使其数量不太少
     限制addPerson和addGroup的条数上限，使其数量不太多
2. addRelation、sendMessage这种“加边”操作要多
   - 以addRelation为例，当要生成addRelation时，一次生成多条
3. 尽量避免“被孤立者”的出现
   - 以addPerson为例，在addPerson后紧跟个addToGroup
4. 询问覆盖率尽可能高
   - 在数据中间and最后把所有询问指令都过一遍

### 特殊数据

主要考虑三类数据，尤其关注JML中限定的边界以及异常

- 超时数据
  - 如第十次作业的`qgvs`，在互测时生成一个仅一个group，其中有1111人，剩下全部询问`qgvs`的数据。
- 针对异常：
  - 如：没有人的数据、group为空的数据等
- 针对某条指令：
  - 如`atg`限定一个group中至多1111人，而随机数据难以覆盖这一点，特别生成一个数据，将两千人加到一个group后进行所有可行的询问操作

### 对拍器

对拍核心代码如下所示：

```cpp
	for (int i = 1; i <= random_test_time; i++) {
        printf("===================================================\n");
        system("generator.exe");// 执行当前目录下的随机数据生成器generator.exe

        start_time1 = clock();
        system("java -jar MyCode.jar < ./in.txt > ./out1.txt");
        end_time1 = clock();
        printf("your code's run time : %.2lf s\n", (end_time1 - start_time1) / 1000);

        start_time2 = clock();
        system("java -jar Caster.jar < ./in.txt > ./out2.txt");
        end_time2 = clock();
        printf("test code's run time : %.2lf s\n", (end_time2 - start_time2) / 1000);

        if (system("fc out1.txt out2.txt")) {
            printf("发现Caster错误\n");
            return 0;
        } else {
            printf("Caster通过第%d组随机数据\n", i);
        }
        printf("===================================================\n\n\n\n");
        Sleep(1000); // 等待1s
    }
```





## 二、模型构建和维护策略

### 容器选择

由于本单元作业中涉及到了大量的查询操作，如果采用数组或链表存储，同时使用JML规格中的遍历方法，其复杂度可至$O(N)$或$O(N^2)$，如果考虑方法间的相互调用，其复杂度可升至$O(N^4)$，因此本次作业中我选择的容器为`HashMap`和`HashSet`，两者在查询时的复杂度为$O(1)$，能大大提高查询的效率。

### 图的模型构建

- 将Person抽象为图的点，将relation、message等抽象为图中的边，存入MyPerson类中
- 采用`HashMap`构建图的边
- 采用`PriorityQueue`来处理Dijkstra算法
- 使用并查集构建关系集合

### 图的维护策略

- **两个维护原则**：应更新即更新、应缓存尽缓存（除最短路径和最小生成树算法）保证在查询时可以用$O(1)$或$O(N)$的复杂度得出结果。

  - **应更新即更新：**当图发生改变时，及时改变各个节点的状态，保证状态的状态最新，在查询时可以直接获取结果。

  - **应缓存尽缓存：**此条原则与上一条相互呼应，考虑到作业中的查询类指令很多，放弃即问即查的维护方式，事先存好结果。

- **最短路径和最小生成树算法**：仅询问时搜索
  - 最短路径的存储代价太大，需要$N^2$的空间，且若询问中的起点变化或图发生改变，需更新结果，且更新的代价极大（需要以每个点为起点跑一遍最短路算法）。权衡来看保存其结果的意义不大，甚至会起到反效果，因此仅在询问时才求值
  - 最小生成树的存储代价和更新代价都小于最短路径，但是一样每当图发生变化时都需要更新结果，考虑到询问次数有限，且其维护代价还是较高，因此仅在询问时才求值。

## 三、性能问题和修复情况

- **性能问题：**
  - 在第一次作业中因为已实现了并查集优化，就偷懒没有优化`queryBlockSum()`导致性能较低被hack，除此之外没有被hack。

- **修复：**
  - 修改“即问即查”为“应更新及更新”，事先就存好结果，在询问时直接$O(1)$出结果。
  - 维护方法：当加人时，`blockNum++`，当合并两个并查集时，`blockNum--`。

## 四、模型拓展

### 拓展假设

假设出现了几种不同的Person

- Advertiser：持续向外发送产品广告
- Producer：产品生产商，通过Advertiser来销售产品
- Customer：消费者，会关注广告并选择和自己偏好匹配的产品来购买 -- 所谓购买，就是直接通过Advertiser给相应Producer发一个购买消息
- Person：吃瓜群众，不发广告，不买东西，不卖东西

如此Network可以支持市场营销，并能查询某种商品的销售额和销售路径等 请讨论如何对Network扩展，给出相关接口方法，并选择3个核心业务功能的接口方法撰写JML规格（借鉴所总结的JML规格模式）

### 总体设计

Advertiser、Producer、Customer可以继承Person类。

Advertiser发送广告：

```java
    /*@ public normal_behavior
      @ requires containsMessage(id) && (getMessage(id) instanceof Advertisement);
      @ assignable messages;
      @ assignable people[*].messages;
      @ ensures (\forall int i; 0 <= i && i < people.length && getMessage(id).getPerson1().isLinked(people[i]);
      @           (\forall int j; 0 <= j && j < \old(people[i].getMessages().size());
      @             people[i].getMessages().get(j+1) == \old(people[i].getMessages().get(j))) &&
      @           people[i].getMessages().get(0).equals(\old(getMessage(id))) &&
      @           people[i].getMessages().size() == \old(people[i].getMessages().size()) + 1);
      @ ensures !containsMessage(id) && messages.length == \old(messages.length) - 1 &&
      @         (\forall int i; 0 <= i && i < \old(messages.length) && \old(messages[i].getId()) != id;
      @         (\exists int j; 0 <= j && j < messages.length; messages[j].equals(\old(messages[i]))));
      @ ensures (\forall int i; 0 <= i && i < people.length && !getMessage(id).getPerson1().isLinked(people[i]);
      @           people[i].getMessages().equals(\old(people[i].getMessages()));
      @ also
      @ public exceptional_behavior
      @ signals (MessageIdNotFoundException e) !containsMessage(id);
      @ signals (ClassTypeException e) !(getMessage(id) instanceof Advertisement);
      @*/
    public void sendAdvertisement(int id) throws
            MessageIdNotFoundException, ClassTypeException;
```

Producer生产产品：

```java
   /*@ public normal_behavior
      @ requires contains(producerId) && (getPerson(producerId) instanceof Producer);
      @ assignable getProducer(producerId).productCount;
      @ ensures getProducer(producerId).getProductCount(productId) ==
      @           \old(getProducer(producerId).getProductCount(productId)) + 1;
      @ also
      @ public exceptional_behavior
      @ signals (PersonIdNotFoundException e) !contains(producerId);
      @ signals (ClassTypeException e) !(getPerson(producerId) instanceof Producer);
      @*/
    public void produceProduct(int producerId, int productId) throws
            PersonIdNotFoundException, ClassTypeException;
```

Customer发送购买消息：

```Java
    /*@ public normal_behavior
      @ requires containsMessage(id) && (getMessage(id) instanceof BuyMessage);
      @ requires (getMessage(id).getPerson1() instanceof Customer) && (getMessage(id).getPerson2() instanceof Advertiser);
      @ assignable messages;
      @ assignable getMessage(id).getPerson1().money;
      @ assignable getMessage(id).getPerson2().messages, getMessage(id).getPerson2().money;
      @ ensures !containsMessage(id) && messages.length == \old(messages.length) - 1 &&
      @         (\forall int i; 0 <= i && i < \old(messages.length) && \old(messages[i].getId()) != id;
      @         (\exists int j; 0 <= j && j < messages.length; messages[j].equals(\old(messages[i]))));
      @ ensures (\forall int i; 0 <= i && i < \old(getMessage(id).getPerson2().getMessages().size());
      @          \old(getMessage(id)).getPerson2().getMessages().get(i+1) == \old(getMessage(id).getPerson2().getMessages().get(i)));
      @ ensures \old(getMessage(id)).getPerson2().getMessages().get(0).equals(\old(getMessage(id)));
      @ ensures \old(getMessage(id)).getPerson2().getMessages().size() == \old(getMessage(id).getPerson2().getMessages().size()) + 1;
      @ ensures (\old(getMessage(id)).getPerson1().getMoney() ==
      @         \old(getMessage(id).getPerson1().getMoney()) - ((BuyMessage)\old(getMessage(id))).getMoney() &&
      @         \old(getMessage(id)).getPerson2().getMoney() ==
      @         \old(getMessage(id).getPerson2().getMoney()) + ((BuyMessage)\old(getMessage(id))).getMoney());
      @ also
      @ public exceptional_behavior
      @ signals (MessageIdNotFoundException e) !containsMessage(id);
      @ signals (NotBuyMessageException e) !(getMessage(id) instanceof BuyMessage);
      @ signals (NotCustomerException e) !(getMessage(id).getPerson1() instanceof Customer);
      @ signals (NotAdvertiserException e) !(getMessage(id).getPerson2() instanceof Advertiser);
      @*/
    public void sendBuyMessage(int id) throws
            MessageIdNotFoundException, NotBuyMessageException, NotCustomerException, NotAdvertiserException;
```

## 五、学习体会

这一单元的作业相比前两个单元明显更加简单，而且直接不绕弯~~(说的就是Unit4)~~。

通过学习JML，我感受到了“契约式编程”的力量，也了解了巨型项目开发时所采用的的一些方法，收获很大。

在这一单元的训练中，我阅读JML的能力有很大提升，不会出现理解不了需求的情况，但是书写JML的能力有待提升。

由于本单元的作业较为简单，我将重心放到了评测上，着重优化了随机数据生成器，提升询问其覆盖率。



