### Android屏幕（View）刷新机制
##### ViewRootImpl
* 界面上任何一个 View 的刷新请求最终都会走到 ViewRootImpl 中的 **scheduleTraversals()** 里来安排一次遍历绘制 View 树的任务；
* 该方法内会通过mTraversalScheduled变量过滤掉同一帧同重复的请求；

##### scheduleTraversals() 发送同步屏障
* scheduleTraversals() 会往主线程的消息队列中发送一个同步屏障，拦截这个时刻之后所有的同步消息的执行；
* 但不会拦截异步消息，以此来尽可能的保证当接收到屏幕刷新信号时可以尽可能第一时间处理遍历绘制 View 树的工作；

##### 调用Choreographer的postCallback()
* 开始安排一个遍历绘制 View 树的操作，作法是把 performTraversals() 封装到 Runnable 里面；
* 然后调用 Choreographer 的 postCallback() 方法；

##### 将Runnable加入待执行的队列里
* postCallback() 方法会先将这个 Runnable 任务以当前时间戳放进一个待执行的队列里；
* 然后如果当前是在主线程就会直接调用一个native 层方法；
* 如果不是在主线程，会发一个最高优先级的 message 到主线程，让主线程第一时间调用这个 native 层的方法。

##### 注册监听下一个屏幕刷新信号
* Choreographer向底层注册监听下一个屏幕刷新信号
* 当下一个屏幕刷新信号发出时，底层就会回调 Choreographer 的onVsync() 方法来通知上层 app；

##### doFrame()
* onVsync() 方法被回调时，会往主线程的消息队列中发送一个执行 doFrame() 方法的消息；
* 这个消息是异步消息，所以不会被同步屏障拦截住；

##### doTraversal()
* doFrame() 方法会去取出之前放进待执行队列里的任务来执行，其实就是postCallback中的runnable；
* 取出来的这个任务实际上是 ViewRootImpl 的 doTraversal() 操作；
* 先移除主线程的同步屏障；

##### performTraversals()
* 根据当前状态判断是否需要执行performMeasure() 测量、perfromLayout() 布局、performDraw() 绘制流程，
* 在这几个流程中都会去遍历 View 树来刷新需要更新的View；

### invalidate 和 requestLayout
* requestLayout会直接递归调用父窗口的requestLayout，直到ViewRootImpl,然后触发peformTraversals，由于mLayoutRequested为true，会导致onMeasure和onLayout被调用。不一定会触发OnDraw
   * requestLayout触发onDraw可能是因为在在layout过程中发现l,t,r,b和以前不一样，那就会触发一次invalidate，所以触发了onDraw，也可能是因为别的原因导致mDirty非空（比如在跑动画）

* view的invalidate会导致当前view被重绘,由于mLayoutRequested为false；
* 不会导致onMeasure和onLayout被调用，而OnDraw会被调用。
