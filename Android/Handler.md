### 简述Handler机制
> Handler是Android通信的桥梁，Handler通过sendMessage发送消息，将消息放入MessageQueue中，在MessageQueue中通过时间的维度来进行排序，Looper通过调用loop方法不断的从MessageQueue中获取消息，执行Handler的dispatchMessage，最后调用handleMessage方法。

##### Handler
##### Looper
##### Message
* 属性target --> Handler;
* Message中使用到了享元设计模式。
* 内部维护了一个链表，并且最大长度是50，
* 当消息处理完之后会将消息内的属性设置为空，并且插入到链表的头部，使用obtain创建的Message会从头部获取空的Message

##### MessageQueue
> MessageQueue中组织Message的结构就是一个简单的单向链表，只保存了链表头部的引用.

### Handler中post、postDelayed是怎么实现精确计算时间的？
* Handler并没有自己处理delayed事件，而是把这个交给了messageQueue(其中会调用next())来处理的。
* 在next()里，循环开始的时候判断如果这个Message有延迟，**就调用nativePollOnce**(ptr, nextPollTimeoutMillis);进行阻塞。
* nativePollOnce()的作用类似与object.wait()，只不过是使用了Native的方法对这个线程精确时间的唤醒。

### 如果先postDelay10秒一个Runnable A，然后我再post一个Runnable B，是怎样执行的？
* postDelay()一个10秒钟的Runnable A、消息进队，MessageQueue调用nativePollOnce()阻塞，Looper阻塞；
* 紧接着post()一个Runnable B、消息进队，判断现在A时间还没到、正在阻塞，把B插入消息队列的头部（A的前面），然后调用nativeWake()方法唤醒线程；
* MessageQueue.next()方法被唤醒后，重新开始读取消息链表，第一个消息B无延时，直接返回给Looper；
* Looper处理完这个消息再次调用next()方法，MessageQueue继续读取消息链表，第二个消息A还没到时间，计算一下剩余时间（假如还剩9秒）继续调用nativePollOnce()阻塞；
* 直到**阻塞时间到**或者**下一次有Message进队**；

### 一个Thread可以有几个Looper？几个Handler？
* 一个线程只能有一个Looper，可以有多个Handler;
* 在线程中我们需要调用Looper.perpare,会创建一个Looper并且将Looper保存在ThreadLocal中;
> 主线程中会在ActivityThread中的main函数中创建；
* 每个线程都有一个LocalThreadMap，会将Looper保存在对应线程中的LocalThreadMap，key为ThreadLocal，value为Looper
* 在子线程中，如果手动为其创建了Looper，那么在所有的事情完成以后应该调用quit方法来终止消息循环，否则这个子线程就会一直处于等待（阻塞）状态。

### Handler异步消息处理（HandlerThread）
* 内部使用了Handler+Thread，并且处理了getLooper的并发问题。
* 如果获取Looper的时候发现Looper还没创建，则wait，等待looper创建了之后在notify；

### Handler中有Loop死循环，为什么没有阻塞主线程，原理是什么？
* 真正会卡死主线程的操作是在回调方法onCreate/onStart/onResume等操作时间过长，会导致掉帧，甚至发生ANR，looper.loop本身不会导致应用卡死。
* 在主线程的MessageQueue没有消息时，便阻塞在loop的queue.next()中的nativePollOnce()方法里，此时主线程会释放CPU资源进入休眠状态；


### ActivityThread实际上并非线程，那么ActivityThread中Looper绑定的是哪个Thread，也可以说它的动力是什么？
* 每个app运行时前首先创建一个进程，该进程是由Zygote fork出来的，用于承载App上运行的各种Activity/Service等组件。
* app所有线程与App所在进程之间资源共享，从Linux角度来说进程与线程除了是否共享资源外，并没有本质的区别，都是一个task_struct结构体。
* 其实承载**ActivityThread的主线程就是由Zygote fork而创建的进程**。