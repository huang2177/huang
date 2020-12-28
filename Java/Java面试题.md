### LRUCache原理
Lru的全称是Least Recently Used ，近期最少使用的；把近期最少使用的数据从缓存中移除，保留使用最频繁的数据;
其中用到的数据对象是LinkedHashMap

### ThreadLocal 原理
* 作用：ThreadLocal是解决线程安全问题一个很好的思路，它通过为每个线程提供一个独立的变量副本解决了变量并发访问的冲突问题。
* 原理：每个线程都会有一个类型为ThreadLocal.ThreadLocalMap的map,这个map就是用来存储与这个线程绑定的变量,map的key就是ThreadLocal对象,value就是线程正在执行的任务中的某个变量的包装类Entry.
* 使用注意：
  * 使用ThreadLocal对象,尽量使用static,不然会使线程的ThreadLocalMap产生太多Entry,从而造成内存泄露
  * 使用weakReference,能够在ThreadLocal失去强引用的时候,ThreadLocal对应的Entry能够在下次gc时被回收,回收后的空间能够得到复用,在一定程度下能够避免内存泄露.
 set(null)把当前的ThreadLocal为key的值设为了空,避免线程下次再执行其他任务时被使用,但此时这个key对应的Entry值还在,只是Entry.value=null
 remove方法会把这个key对应Entry的值设为空
从重用和效率的角度来说,set(null)的性能优于remove,在实际的项目中推荐使用set(null)来回收ThreadLocal设置的值.

### 类加载机制
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

### String 为什么要设计成不可变的？
##### 设计考虑：（字符串常量池的需要）
`String s1 = “abcd”;` \
`String s2 = “abcd”;`
>上面的代码只会在堆中生成一个变量；假如String是可变的，一个对象改变了，就会影响到另外的变量；

##### 效率优化：（允许String缓存HashCode）
> String的Hash被频繁使用（HashMap），如果String不可变，就保证了Hash的唯一性，不用每次都去缓存；
##### 安全性：
> String被许多的Java类(库)用来当做参数,例如 网络连接地址URL,文件路径path,还有反射机制所需要的String参数等, 假若String不是固定不变的,将会引起各种安全隐患。

### StringBuffer与StringBuilder的区别？
* StringBuffer支持并发操作，线性安全的，适合多线程中使用。
* StringBuilder不支持并发操作，线性不安全的，不适合多线程中使用。但其在单线程中的性能较高。

### java四中引用
##### 强引用
> 这种方式就是强引用，强引用在任何时候都不会被jvm回收，即使抛出OutOfMemoryError。

##### 软引用
> 通过SoftReference的get方法来获取对象。软引用，在jvm内存不足的情况下会被回收。

##### 弱引用
> 通过WeakReference的get方法来获取对象。在gc的时候就会被回收，不管内存是否充足。

##### 虚引用
> 虚引用和没有引用是一样的，需要和队列(ReferenceQueue)联合使用。当jvm扫描到虚引用的对象时，会先将此对象放入关联的队列中，因此我们可以通过判断队列中是否存这个对象，来进行回收前的一些处理。

### OOM的可能原因？Oom是否可以try catch？
* 资源对象没关闭造成的内存泄露，try catch finally中将资源回收放到finally语句可以有效避免OOM。资源性对象比如： 	
  * Cursor；
  * 调用registerReceiver后未调用unregisterReceiver()；
  * 未关闭InputStream/OutputStream； 
  * Bitmap使用后未调用recycle() ；
* 作用域不一样，导致对象不能被垃圾回收器回收，比如： 	
  * 非静态内部类会隐式地持有外部类的引用； 
  * Context泄露； 概括一下，避免Context相关的内存泄露，记住以下事情：    
    * 不要保留对Context-Activity长时间的引用（对Activity的引用的时候，必须确保拥有和Activity一样的生命周期）；    	
    * 尝试使用Context-Application来替代Context-Activity
    * 如果你不想控制内部类的生命周期，应避免在Activity中使用非静态的内部类，而应该使用静态的内部类，并在其中创建一个对Activity的弱引用；       这种情况的解决办法是使用一个静态的内部类，其中拥有对外部类的WeakReference； 
    * Thread 引用其他对象也容易出现对象泄露； 
    * onReceive方法里执行了太多的操作；
* 内存压力过大
  * 图片资源加载过多，超过内存使用空间，例如Bitmap 的使用；   	
  * 重复创建view，listview应该使用convertview和viewholder；
  * 如何避免内存泄露 	
  * 使用缓存技术，比如LruCache、DiskLruCache、对象重复并且频繁调用可以考虑对象池； 	
  * 对于引用生命周期不一样的对象，可以用软引用或弱引用SoftReferner WeakReferner； 	
  * 对于资源对象 使用finally 强制关闭； 	
  * 内存压力过大就要统一的管理内存；

### 为什么局部内部类和匿名内部类只能访问局部final变量：
public void test(final int b) {
    final int a = 10;
    new Thread() {
        public void run() {
            System.out.println(a);
            System.out.println(b);
        }
    }.start();
}
> 当test方法执行完毕之后，变量a的生命周期就结束了，而此时Thread对象的生命周期很可能还没有结束。所以Java采用了 复制  的手段来解决这个问题（会把a复制到常量池中），使用final是不允许内部类改变a的值。

### 为什么要同时重写hashcode和equals?
* 并没有硬性要求一定要同时修改这两个方法，只是要求，当equals方法被重写，hashcode也必须要被重写。
* 目的：要维持对象的一个规则，当两个对象调equals返回true，则两个对象的hashcode是一致的
