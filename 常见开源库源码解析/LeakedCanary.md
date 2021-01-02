### 初始化相关
* 2.0之前，需要手动调用install(Context),相对而言较麻烦；
* 2.0之后，只需配置依赖即可，实际内部使用ContentProvider来完成；

### Install方法分析：
* ActivityDestroyWatcher.install();
> 调用 registerActivityLifecycleCallbacks 方法注册 Activity 生命周期的监听，当 Activity 销毁的时候就会回调 onActivityDestroyed 方法。

* FragmentDestroyWatcher.install();
  * 8.0以上：AndroidOFragmentDestroyWatcher.
  * 是否使用AndroidX：AndroidXFragmentDestroyWatcher
  * 是否引入Support库：AndroidSupportFragmentDestroyWatcher

### ObjectWatcher的watch()方法
> 主要是检测发生内存泄漏的对象，核心原理是基于 WeakReference 和 ReferenceQueue 实现的。  
> 也就是每当发生 GC 时，弱引用所持有的对象就会被回收，并且 JVM 会把该对象放入关联的引用队列中。

##### 具体步骤为：
* 创建两个集合 **KeyedWeakReference**(弱引用) 、ReferenceQueue（引用队列）,并将需要观察的对象封装到 KeyedWeakReference 中;
* 每当发生 GC 时，KeyedWeakReference 所持有的对象就会被回收，并加入到ReferenceQueue。
* 然后遍历ReferenceQueue中保存的观察对象，从KeyedWeakReference中删除这些对象。剩下的就是发生内存泄漏的对象；
* 最后如果未回收的对象大于等于 5 个，就进行 dump head 操作生成 hprof 文件。

### hprof文件分析
* runAnalysis 方法中开启一个 **IntentService** 服务来分析 hprof 文件，分析库使用的是 Shark。
* 具体步骤：
  * 首选生成 heap graph（对象关系图）；
  * 接着根据 heap graph 找到泄漏对象到 **GC roots** 的最短路径；
  * 最后将 hprof 分析结果返回并显示通知栏消息，当点击通知栏消息后就会跳转到展示内存泄漏的界面。
