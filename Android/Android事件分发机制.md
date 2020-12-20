### 事件分发的核心方法
#### dispatchTouchEvent()
> Activity、ViewGroup、View都有此方法；
* 是Android事件分发的核心，主要负责事件的分发工作；
* 如果此方法返回True，则代表消费此次事件，则不会继续传递；
* 如果返回False，则表示不消费此事件，事件将继续传递下去；

#### onTouchEvent()
> Activity、ViewGroup、View都有此方法；
* 返回true，则表示消费次事件

#### onInterceptTouchEvent()
> ViewGroup独有；
* 如果是Down事件，会直接返回为true；