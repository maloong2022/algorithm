### 堆 Heap

堆是一种高效的维护集合中最大或最小元素的数据结构

大根堆：根节点最大的堆，用于维护和查询 max
小根堆：根节点最小的堆，用于维护和查询 min

堆是一颗树，并且满足堆性质

- 大根堆任意节点的关键码>= 它所有子结点的关键码（父>= 子）
- 小根堆任意节点的关键码<= 它所有子结点的关键码（父<= 子）

二叉堆 Binary Heap

二叉堆是一种简易实现

- 本质上一颗满足堆性质的完全二叉树

常规操作

- 建堆（build）： O（N）
- 查询最值（get max/min）：O（1）
- 插入（insert）：O（log N）
- 取出最值（delete max/min）：O（logN）

斐波那契堆、配对堆等可以做到插入 O（1），左偏树、斜堆等可以支持合并

#### 二叉堆的实现

二叉堆一般使用一个一维数组来存储，利用完全二叉树的节点编号特性

假设第一个元素存储在索引（下标）为 1 的位置的话

- 索引为 p 的节点的左孩子的索引为 p\*2
- 索引为 p 的节点的右孩子的索引为 p\*2 + 1
- 索引为 p 的节点的父亲的索引为 p/2(下取整)

假设第一个元素存储在索引（下标）为 0 的位置的话

- 索引为 p 的节点的左孩子的索引为 p\*2 + 1
- 索引为 p 的节点的右孩子的索引为 p\*2 + 2
- 索引为 p 的节点的父亲的索引为 (p-1)/2(下取整)

#### 插入（insert）

新元素一律插入到数组 heap 的尾部

- 设插入到了索引 p 的位置

然后向上进行一次调整（Heapify Up）

- 若已到达根，停止
- 若满足堆性质(heap[p]<=heap[p/2]),停止
- 否则交换 heap[p]和 heap[p/2],令 p = p/2，继续调整

O（log N）

#### 取出堆顶（extract/delete max）

把堆顶（heap[1]）与堆尾（heap[n]）交换，删除堆尾（数组最后一个元素）

然后从根向下进行一次调整（Heapify Down）

- 每次与左右子节点中较大的一个比较，检查堆性质，不满足则交换
- 注意判断子结点是否存在

O（log N）

#### 优先队列（Priority Queue）

二叉堆是优先队列一种简单、常用的实现，但不是最优的实现

理论上二叉堆可以支持 O（log N）删除任意元素，只需要

- 定位该元素在堆中的节点 P（可以通过在数值与索引之间建立映射得到）
- 与堆尾交换，删除堆尾
- 从 p 向上、向下各进行一次调整

不过优先队列并没有提供这个方法，在各语言内置的库中，需要支持删除任意元素时，一般使用有序集合等基于平衡二叉搜索树的实现