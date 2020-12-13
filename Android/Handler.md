### 简述Handler机制
> Handler是Android通信的桥梁，Handler通过sendMessage发送消息，将消息放入MessageQueue中，在MessageQueue中通过时间的维度来进行排序，Looper通过调用loop方法不断的从MessageQueue中获取消息，执行Handler的dispatchMessage，最后调用handleMessage方法。

##### Handler
##### Looper
##### Message
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
* 一个线程只能有一个Looper，可以有多个Handler，在线程中我们需要调用Looper.perpare,他会创建一个Looper并且将Looper保存在ThreadLocal中，每个线程都有一个LocalThreadMap，会将Looper保存在对应线程中的LocalThreadMap，key为ThreadLocal，value为Looper