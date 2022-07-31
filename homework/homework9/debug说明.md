被刀了两刀都是因为算法复杂度的问题，与正确性无关。

**之前做法：**用并查集加速iscircle的判断，但是对于queryBlock，还是按照JML的思路在写，没有进行优化，挨了两刀。

**此次修改**：

1. 在MyNetWork中维护一个变量blockNum，表示关系中集合个数
2. 当新增人时，`blockNum++`
3. 当两个人新建关系时，若不在一个集合中，`blockNum--`
4. 对于方法queryBlockSum，直接`return blockNum`