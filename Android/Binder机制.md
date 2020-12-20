### Binder机制
##### 概念：
* 机制：BInder是一种Android中实现跨进程通信（IPC）的方式；
* 组成：是一种物理设备驱动，文件目录：/dev/binder
* Android中的体现：继承自IBinder接口；
##### 概为什么选用Binder：
* 性能：Bidner数据拷贝只需要一次；
* 安全性：传统的IPC自身没有安全措施，只能依赖上层协议控制，而Binder是根据UID（安装时分配），故进程的UID是鉴别进程身份的重要标志。
##### Linux中其他通信方式：
* Socket：是一个通用接口；导致传输效率低，开销大，基于文件和端口，适用于网络传输；
* 管道（Pie）/消息队列：消息队列和管道采用存储-转发方式，即数据先从发送方缓存区拷贝到内核开辟的缓存区中，然后再从内核缓存区拷贝到接收方缓存区；需要2次拷贝；
* 共享内存：实现机制比较复杂；
##### Binder机制整体流程：
* --> bindService()
* --> onServiceConnected() （返回IBinder对象）
* --> Stub.asInterface()
* --> queryLocalInterface()
* --> 客户端发起请求：
* --> transact() 远程调用；本地挂起；
* --> 服务端调用onTransact(); 并返回数据；