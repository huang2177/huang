#### LRUCache原理
Lru的全称是Least Recently Used ，近期最少使用的；把近期最少使用的数据从缓存中移除，保留使用最频繁的数据;
其中用到的数据对象是LinkedHashMap

#### ThreadLocal 原理
* 作用：ThreadLocal是解决线程安全问题一个很好的思路，它通过为每个线程提供一个独立的变量副本解决了变量并发访问的冲突问题。
* 原理：每个线程都会有一个类型为ThreadLocal.ThreadLocalMap的map,这个map就是用来存储与这个线程绑定的变量,map的key就是ThreadLocal对象,value就是线程正在执行的任务中的某个变量的包装类Entry.
* 使用注意：
  * 使用ThreadLocal对象,尽量使用static,不然会使线程的ThreadLocalMap产生太多Entry,从而造成内存泄露
  * 使用weakReference,能够在ThreadLocal失去强引用的时候,ThreadLocal对应的Entry能够在下次gc时被回收,回收后的空间能够得到复用,在一定程度下能够避免内存泄露.
 set(null)把当前的ThreadLocal为key的值设为了空,避免线程下次再执行其他任务时被使用,但此时这个key对应的Entry值还在,只是Entry.value=null
 remove方法会把这个key对应Entry的值设为空
从重用和效率的角度来说,set(null)的性能优于remove,在实际的项目中推荐使用set(null)来回收ThreadLocal设置的值.

#### HashMap，ConcurrentHashMap，HashSet，LinkedHashMap，SpareArray
##### HashMap：
* HashMap由数组+单链表组成的，数组是HashMap的主体，链表则是主要为了解决哈希冲突而存在的；
* 对于添加操作，其时间复杂度依然为O(1)，因为最新的Entry会插入链表头部，仅需简单改变引用链即可，而对于查找操作来讲，此时就需要遍历链表，然后通过key对象的equals方法逐一比对查找。
* 所以，性能考虑，HashMap中的链表出现越少，性能才会越好；
* 在jdk1.8中，当链表的长度大于8时，转换为红黑树的结构（一种自平衡二叉查找树）。
* 线程不安全，在高并发时，HashMap扩容时容易形成环状链表；（https://blog.csdn.net/hhx0626/article/details/54024222）

##### HashTable：
* HashTable和HashMap的实现原理几乎一样；
* HashTable不允许key和value为null。
* HashTable是线程安全的，但是代价比较大，get/put所有相关操作都是synchronized，所有的操作都是串行的；

##### HashSet：
* 基于HashMap实现的，在构造器中会new一个HashMap；
* HashSet允许存在重复的元素；
* 所有放入HashSet中的集合元素实际上由HashMap的key来保存，而HashMap的value则存储了一个PRESENT，它是一个静态的Object对象。
* ConcurrentHashMap：
* 采用了非常精妙的"分段锁"策略；
* ConcurrentHashMap的主干是个Segment数组，
* Segment继承了ReentrantLock，所以它就是一种可重入锁（ReentrantLock)。在ConcurrentHashMap，一个Segment就是一个子哈希表，Segment里维护了一个HashEntry数组，并发环境下，对于不同Segment的数据进行操作是不用考虑锁竞争的。（就按默认的ConcurrentLeve为16来讲，理论上就允许16个线程并发执行）

##### LinkedHashMap：
* LinkedHashMap由数组+双向链表组成的；
* 每个Entry有三个指针，next用于维护HashMap各个桶中的Entry链，before、after用于维护LinkedHashMap的双向链表；
* LinkedHashMap增加了标志位accessOrder属性 标志位accessOrder (值为true时，表示按照访问顺序迭代；值为false时，表示按照插入顺序迭代)。

##### TreeMap：
* TreeMap底层采用一颗”红黑数”来保存集合中的Entry。所以TreeMap添加元素，取出元素的性能都比HashMap低，需要通过循环找到新增的Entry的插入位置，因为比较耗性能。
* TreeMap中的所有Entry总是按key根据指定的排序规则保持有序状态。

##### SpareseArray ：
* SpareseArray 也是通过键值对存储数据，只是key为整形int , 类似于key = Interger 的HashMap，但是SpareseArray 的key 为 int 非 Interger ，更省空间。
* SpareArray 意为稀松数组，其结构类似于数组结构，依次排开；HashMap是散列列表，根据hash值来存储；因此SpareArray 会比 HashMap节省很多空间。
* 插入效率来看，如果是正序插入( 0 ->size插入)，SpareArray 的插入效率会高于 HashMap。如果是逆序插入(size -> 0)的顺序插入，则SpareArray 的插入效率表现是最差的，会低于HashMap（SpareArray 采用二分查找）。
* 从查找速度来来考虑，HashMap的查找速度 会 高于 SparseArray。
* 通过以上分析，SpareArray 相对于 HashMap的最大优势在内存空间。

#### Arraylist，linkedlist，Vector
##### LinkedList 　
> remove，insert方法在LinkedList的首部或尾部。这些操作使LinkedList可被用作堆栈（stack），队列（queue）或双向队列（deque）。注意LinkedList没有同步方法。如果多个线程同时访问一个List，则必须自己实现访问同步。一种解决方法是在创建List时构造一个同步的List： 　　　　List list = Collections.synchronizedList(new LinkedList(...));
##### ArrayList
> ArrayList实现了可变大小的数组。它允许所有元素，包括null。ArrayList没有同步。 size，isEmpty，get，set方法运行时间为常数。但是add方法开销为分摊的常数，添加n个元素需要O(n)的时间。每个ArrayList实例都有一个容量（Capacity），即用于存储元素的数组的大小。ArrayList也是非同步的（unsynchronized）。
##### Vector　　
>Vector非常类似ArrayList，但是Vector是同步的。由Vector创建的Iterator，虽然和ArrayList创建的Iterator是同一接口，但是，因为Vector是同步的，当一个Iterator被创建而且正在被使用，另一个线程改变了Vector的状态（例如，添加或删除了一些元素），这时调用Iterator的方法时将抛出ConcurrentModificationException，因此必须捕获该异常。
##### Stack 
> Stack继承自Vector，实现一个后进先出的堆栈。Stack提供5个额外的方法使得Vector得以被当作堆栈使用。基本的push和pop方法，还有peek方法得到栈顶的元素，empty方法测试堆栈是否为空，search方法检测一个元素在堆栈中的位置。Stack刚创建后是空栈。
##### 线程间 操作 List
List list = Collections.synchronizedList(new LinkedList(...));

#### 数据结构中堆的概念，堆排序
* 内存中也有一块叫做堆的存储区域，但是这与数据结构中的堆是完全不同的概念。
* 因为堆是一棵完全二叉树，所以我们可以用顺序表来实现，而且堆也只能用顺序表。可以用vector来实现。
* 实现：若在输出堆顶的最小值之后，使得剩余n-1个元素的序列重又建成一个堆，则得到n个元素的次小值。如此反复执行，便能得到一个有序序列，这个过程称之为堆排序。
* 应用：在一个n个数的序列中取其中最大的k个数（Top k问题）。

#### JVM内存模型（https://blog.csdn.net/u010425776/article/details/51170118）
##### 程序计数器 
* 也就是说，程序计数器里面记录的是当前线程正在执行的那一条字节码指令的地址；
* 线程私有，每个线程都有自己的计数器;
* 是唯一一个不会出现OOM的内存区域；
* 生命周期跟随线程；
##### Java虚拟机栈 
* Java虚拟机栈会为每一个即将运行的Java方法创建一块叫做“栈帧”的区域，这块区域用于存储该方法在运行过程中所需要的一些信息，这些信息包括：
   * 局部变量表 
   * 操作数栈
   * 动态链接
   * 方法出口信息
> 注意：
人们常说，Java的内存空间分为“栈”和“堆”，栈中存放局部变量，堆中存放对象。  但这里的“栈”只代表了Java虚拟机栈中的局部变量表部分。真正的Java虚拟机栈是由一个个栈帧组成。
Java虚拟机栈也是线程私有的，每个线程都有各自的Java虚拟机栈，而且随着线程的创建而创建，随着线程的死亡而死亡。
* StackOverFlowError（堆栈溢出）表示当前线程申请的栈超过了事先定好的栈的最大深度，但内存空间可能还有很多。 
* OutOfMemoryError（内存溢出）是指当线程申请栈时发现栈已经满了，而且内存也全都用光了。
* Memory leak（内存泄漏）：是指程序在申请内存后，无法释放已申请的内存空间。最终会导致内存溢出。

##### 本地方法栈  在很多虚拟机中已经将本地方法栈和虚拟机栈合二为一了；

##### 堆 
* 线程共享  整个Java虚拟机只有一个堆，所有的线程都访问同一个堆。而程序计数器、Java虚拟机栈、本地方法栈都是一个线程对应一个的
* 在虚拟机启动时创建
* 垃圾回收的主要场所。
* 可以进一步细分为：新生代、老年代。  	新生代又可被分为：Eden、From Survior、To Survior。 

##### 方法区
* 存放已经被虚拟机加载的类信息、常量（存放在运行时常量池中）、静态变量等。 
* 线程共享  方法区是堆的一个逻辑部分，因此和堆一样，都是线程共享的。整个虚拟机中只有一个方法区。
* 永久代  方法区中的信息一般需要长期存在，而且它又是堆的逻辑分区，因此用堆的划分方法，我们把方法区称为老年代。
* 内存回收效率低  方法区中的信息一般需要长期存在，回收一遍内存之后可能只有少量信息无效。  对方法区的内存回收的主要目标是：对常量池的回收 和 对类型的卸载。
* Java虚拟机规范对方法区的要求比较宽松。  和堆一样，允许固定大小，也允许可扩展的大小，还允许不实现垃圾回收。

#### 并发集合了解哪些 (https://blog.csdn.net/paincupid/article/details/52017292)
##### ConcurrentHashMap
##### Vector 

#### 开启线程的三种方式,run()和start()方法区别
> start()：表示线程已经准备就绪；
> run()：是真正执行线程的方法；

#### GC回收策略，能不能显式调用System.gc()
##### 新生代的GC：
新生代通常存活时间较短，因此基于Copying算法来进行回收；
所谓Copying算法就是扫描出存活的对象，并复制到一块新的完全未使用的空间中，对应于新生代，就是在Eden和FromSpace或ToSpace之间copy。
##### 旧生代的GC：
对象存活的时间比较长，比较稳定，因此采用标记(Mark)算法来进行回收（具体实现的是可达性算法）；
所谓标记就是扫描出存活的对象，然后再进行回收未被标记的对象，回收后对用空出的空间要么进行合并，要么标记出来便于下次进行分配，总之就是要减少内存碎片带来的效率损耗。
不推荐显式调用System.gc():
此函数建议JVM进行主GC,虽然只是建议而非一定,但很多情况下它会触发主GC,从而增加主GC的频率,也即增加了间歇性停顿的次数。
##### 可达性算法：
当一个对象到GC Roots没有任何引用链相连时，则证明此对象是不可用的
Java语言里，可作为GC Roots对象的包括如下几种：     
 a.虚拟机栈(栈桢中的本地变量表)中的引用的对象     
 b.方法区中的类静态属性引用的对象     
 c.方法区中的常量引用的对象     
 d.本地方法栈中JNI的引用的对象 
 
#### 排序，快速排序的实现


#### Java中对象的生命周期
##### 创建阶段(Created)
> 一旦对象被创建，并被分派给某些变量赋值，这个对象的状态就切换到了应用阶段;

##### 应用阶段(In Use)
> 对象至少被一个强引用持有着。

##### 不可见阶段(Invisible)
> 当一个对象处于不可见阶段时，说明程序本身不再持有该对象的任何强引用，虽然该这些引用仍然是存在着的。（该对象仍可能被静态变量或线程或JNI等强引用持有着）
简单说就是程序的执行已经超出了该对象的作用域了。

##### 不可达阶段(Unreachable)
> 对象处于不可达阶段是指该对象不再被任何强引用所持有。

##### 收集阶段(Collected)
> 当垃圾回收器发现该对象已经处于“不可达阶段”并且垃圾回收器已经对该对象的内存空间重新分配做好准备时，则对象进入了“收集阶段”。如果该对象已经重写了finalize()方法，则会去执行该方法的终端操作。（不推荐重写finalize()方法）

##### 终结阶段(Finalized)
> 当对象执行完finalize()方法后仍然处于不可达状态时，则该对象进入终结阶段。在该阶段是等待垃圾回收器对该对象空间进行回收。

##### 对象空间重分配阶段(De-allocated)
>垃圾回收器对该对象的所占用的内存空间进行回收或者再分配了，则该对象彻底消失了，称之为“对象空间重新分配阶段”。

#### 类加载机制
##### 双亲委派机制
> PathClassLoader，DexClassLoader，dexPathList，Element,

##### 双亲委派模型
* 双亲委托模型就是首先判断该Class是否已经加载，如果没有则不是自身去查找而是委托给父加载器进行查找，这样依次的进行递归，直到委托到最顶层的Bootstrap ClassLoader，如果Bootstrap ClassLoader找到了该Class，就会直接返回，如果还没找到则最后会交由自身（出发点）去查找（并不会向下查找），如果还没找到，则ClassNotFoundException。
* 启动类加载器（Bootstrap ClassLoader）：这是由C++语言实现的一个加载器，是虚拟机的一部分，随虚拟机启动运行。
* 扩展类加载器（Extension ClassLoader）：负责加载/lib/ext目录中的，或者被java.ext.dir系统变量指定路径中的所有类库。
* 应用程序类加载器（Application ClassLoader）：负责加载用户类路径（ClassPath）上所指定的类库，如果程序中没有自定义自己的类加载器，这个就是默认加载器。
##### 双亲委托模型的好处：
* 防止内存中出现多份同样的字节码，比如两个类A和类B都要加载System类：
* 如果不用委托而是自己加载自己的，那么类A就会加载一份System字节码，然后类B又会加载一份System字节码，这样内存中就出现了两份System字节码。


#### https相关，如何验证证书的合法性，https中哪里用了对称加密，哪里用了非对称加密，对加密算法（如RSA）等是否有了解
##### 问题域：A与B通信，有且只有A和B能够知道通信内容
> 对称加密（AES或者其他加密算法）：对通信内容进行加密；
##### 问题一：服务器怎么告诉客户端使用哪种加密算法？（通过协商，若协商没有加密，仍然有风险）
> 非对称加密：（RSA或者其他算法）：对对称加密的协商过程进行加密（采用随机数来生成对称加密算法）；
##### 问题二：怎么得到公钥？
> 将公钥放到服务器（有多了一次请求）,服务端将公钥发送给每一个客户端（可行）；
##### 问题三：公钥被掉包了怎么办？
> 使用第三方机构（CA）的公钥解决：“数字证书”中包含了服务器交给第三方的公钥，客户端拿到公钥，如果能解密，说明是服务器返回的，反之，则被掉包了；（第三方机构会使用自己的私钥对服务器的公钥再次加密）
##### 问题四：第三方机构也可以给中间人颁发这样的“数字证书”，这样还是有风险进行掉包？
> 数字签名（数字证书的编号）：在证书上面写着如何生成证书编号。客户端生成编号以后和证书中的编号进行对比；

##### 非对称加密的特点: