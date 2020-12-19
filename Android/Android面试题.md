### 模块化实现（好处，原因）
* 为什么模块间解耦，复用？   	
> 原因：各个都是独立的模块，每个模块负责的功能不同，业务逻辑不同，模块间业务解耦。模块功能比较单一，可在多个项目中使用
* 为什么可以多团队并行开发，测试？
> 原因：每个团队负责不同的模块，提升开发，测试效率
* 为什么可单独编译某个模块，提升开发效率？
> 原因：每个模块实际上也是一个完整的项目，可以进行单独编译，调试

### 统计启动时长,标准
adb shell am start -w packagename/activity
会返回3个值：
* WaitTime 返回从 startActivity 到应用第一帧完全显示这段时间. 就是总的耗时，包括前一个应用 Activitypause 的时间和新应用启动的时间；
* ThisTime 表示一连串启动 Activity 的最后一个 Activity 的启动耗时；
* TotalTime 表示新应用启动的耗时，包括新进程的启动和 Activity 的启动，但不包括前一个应用Activitypause的耗时。开发者一般只要关心 TotalTime 即可，这个时间才是自己应用真正启动的耗时。

### SP是进程同步的吗?有什么方法做到同步
* 不能，它基于单个文件的，默认是没有考虑同步互斥，而且，APP对SP对象做了缓存，不好互斥同步；
* ContentProvider基于Binder，不存在进程间互斥问题，对于同步，也做了很好的封装，不需要开发者额外实现。 

### 介绍下SurfaceView
* SurfaceView继承之View，但拥有独立的绘制表面，即它不与其宿主窗口共享同一个绘图表面，可以单独在一个线程进行绘制，并不会占用主线程的资源。
* SurfaceView有两个子类GLSurfaceView和VideoView
* SurfaceView和View的区别：
* View主要适用于主动更新的情况下，而SurfaceView主要适用于被动更新，例如频繁地刷新
* View在主线程中对画面进行刷新，而SurfaceView通常会通过一个子线程来进行页面的刷新
* View在绘图时没有使用双缓冲机制，而SufaceView在底层实现机制中就已经实现了双缓冲机制

### BroadcastReceiver与LocalBroadcastReceiver 区别
* LocalBroadcastReceiver只会存在于应用内部，不用担心机密数据泄露问题；
* 其他程序无法将广播发送到我们程序的内部，因此不需要担心会有安全漏洞的隐患；
* 发送本地广播要比全局广播更加高效；
* 基于主线程的 Looper 新建了一个 Handler，handleMessage中会调用接收器对广播的消息进行处理
* 注册时用一个ArrayList保存ReceiverRecord（继承自IntentFilter），发送的时候对其进行遍历，然后调用onReceive();

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


###  android事件传递机制

### 画出 Android 的大体架构图
* 应用层：应用是用java语言编写的运行在虚拟机上的程序，比如Email客户端，SMS短消息程序，日历等。
* 应用框架层：这一层是编写Google发布的核心应用时所使用的API框架。应用程序框架层包括活动管理器、窗口管理器、内容提供者、视图系统、包管理器、电话管理器、资源管理器等。
* 系统运行库（C/C++库以及Android运行库）层：当使用Android应用框架时，Android系统会通过一些C/C++库来支持我们使用的各个组件，使其更好的为我们服务，比如其中的SQLite（关系数据库），Webkit（Web浏览器引擎）。
* Linux内核层：Android的核心系统服务给予Linux2.6内核，如安全性、内存管理、进程管理、网络协议栈和驱动模型等都依赖于该内核，比如Binder IPC(Internet Process Connection进程间通信)驱动，android的一个特殊驱动程序，具有单独的设备节点，提供进程间通信的功能。

### 描述点击 Android Studio 的 build 按钮后发生了什么
* 通过AAPT工具进行资源文件（包括AdroidManifest.xml，布局文件，各种xml文件）的打包，生成R.java文件；
* 通过AIDL工具处理AIDL文件，生成相应的Java文件；
* 通过Javac工具对项目源码进行编译，生成class文件；
* 通过DX工具将所有的class文件转换成DEX文件；
* ApkBuilder	-------> 	.apk文件；
* keyStore	    ------->	签名；
* ZipAlign		------->	对齐（将apk中的资源文件的起时距离偏移4个字节）；

### Apk的安装流程？
* 安装Apk；
* 拷贝Apk；
* 资源管理器解析Apk中的资源文件；
* 解析AndroidManifest文件，创建 data/app 目录；
* 对dex文件进行优化，并缓存；
* 解析四大组件，并注册到PMS（PackageParse去解析）中（这里的Acitivity只是一个存档，并不是完整的组件，例如：ActivityInfo，ServiceInfo）；

### 点击桌面图标发生了什么？
* Launcher进程将启动MainActivity发送给AMS；（Binder的方式）
* AMS会交给ActivityStarter处理Intent/flag，再交给ActivityStackSupervisior（高版本）/ActivityStack处理进栈相关流程；同时以Socket的方式请求Zygote进程Fork新进程；
* 在新进程创建ActivityThread对象，新创建的进程就是该应用的主线程（UI线程），在主线程里面开启Looper消息循环，开始处理Activity；
* ActivityThread利用ClassLoader去加载Activity，创键Activity实例，并且回调一系列的生命周期的方法；


### 对Dalvik、ART 虚拟机有基本的了解；
##### Dalvik（4.4以前）
> Dalvik虚拟机在启动的时候会先将.dex文件转换成快速运行的机器码，又因为65535这个问题，导致我们在应用冷启动的时候有一个合包的过程，最后导致的一个结果就是我们的app启动慢，这就是Dalvik虚拟机的JIT特性（Just In Time）。
采用传统的垃圾回收算法，会出现内存碎片化；（标记-->回收-->恢复）
##### ART （5.0开始）
> ART有一个很好的特性AOT（ahead of time），这个特性就是我们在安装APK的时候就将dex直接处理成可直接供ART虚拟机使用的机器码，ART虚拟机将.dex文件转换成可直接运行的.oat文件，ART虚拟机天生支持多dex，所以也不会有一个合包的过程，所以ART虚拟机会很大的提升APP冷启动速度。
ART虚拟机允许标记和回收可以同时进行；另外引入了移动垃圾回收器技术，使得碎片化的内存能够对齐，能够节约一点内存。

### App是如何沙箱化，为什么要这么做；
> 沙箱，对使用者来说可以理解为一种安全环境，对恶意访问者来说是一种限制。
Android 沙箱实现了应用程序的相互隔离，用于对文件系统的实体访问进行控制，也可以对其他Android系统资源进行访问控制。
简单的说，每个应用程序无法访问其他应用程序的资源。沙箱系统的原理主要基于Linux系统的UID/GID机制。Android对传统的 Linux的UID/GID机制进行了修改。在 Linux 中，一个用户 ID 识别一个给定用户;在 Android 上，一个用户 ID 识别一个应用程序。
Android定义了从名称到独特标识符的Android ID(AID)映射表。初始的映射表中定义了一些特权用户和一些系统关键用户。（用来代替UID/GID）

### 系统启动流程
> Zygote进程 –> SystemServer进程 –> 各种系统服务 –> 应用进程


### Retrofit理解？
* OKHttpCall.execute（），该方法生成一个okhttp3.Call将任务抛给OKHttp。
* 完了调用parseResponse，用Converter将okhttp3.Response转换成我们在范型中指定的类型Response<ResponseBody> response = call.execute()，我指定了okhttp3.ResonseBody，然后返回结果。
* 可以在在构造Retrofit时提供GsonConverter，addConverterFactory(GsonConverterFactory.create())；

### RxJava：

### Glide源码？glide 使用什么缓存？
磁盘缓存：默认使用的是LRU（Least Recently Used）算法（时间）。如果你想使用其他的缓存算法，就只能通过实现DiskCache接口来完成了。
内存缓存：Glide中有一个叫做BitmapPool的类，可以复用其中的Bitmap对象，从而避免Bitmap对象的创建，减小内存开销。

### Bitmap如何处理大图，如一张30M的大图，如何预防OOM
> setImageBitmap或setImageResource或BitmapFactory.decodeResource直接使用图片路径来设置一张大图。（最终都是通过java层的createBitmap来完成的，需要消耗更多内存）
##### 解决方法：
通过BitmapFactory.decodeStream方法，（decodeStream直接调用JNI>>nativeDecodeAsset()，无需再使用java层的createBitmap，从而节省了java层的空间。）
Decode时使用BitmapFactory.Options参数；
Options.inSampleSize, 成比例放缩
Options.inJustDecodeBounds，只获取长宽，不获取图片
Options.inPreferredConfig，修改图片编码格式
D手动回收Bitmap；
##### 加载微信长图方案：
* BitmapRegionDecoder用于显示图片的某一块矩形区域；（bitmapRegionDecoder.decodeRegion(rect, options)）
* 自定义显示大图控件：
   * 提供一个设置图片的入口
   * 重写onTouchEvent，在里面根据用户移动的手势，去更新显示区域的参数
   * 每次更新区域参数后，调用invalidate，onDraw里面去regionDecoder.decodeRegion拿到bitmap，去draw；


### 广播（动态注册和静态注册区别，有序广播和标准广播），广播的使用场景

### 数据库数据迁移问题

### 是否熟悉Android jni开发，jni如何调用java层代码

### Android相关优化（如内存优化、网络优化、布局优化、电量优化）
##### 内存优化：
* 使用更加轻量的数据结构，比如SparseArray、ArrayMap等
* 避免在Android里面使用Enum
* 减小Bitmap对象的内存占用，
* 使用更小的图片
* 优化界面交互过程中频繁的内存使用；譬如在列表等操作中只加载可见区域的Bitmap、滑动时不加载、停止滑动后再开始加载。
* 尽量使用线程池替代多线程操作，这样可以节约内存及CPU占用率。
* 避免创建不必要的对象诸如一些临时对象, 特别是循环中的.

##### 网络优化：
* 压缩/减少数据传输量
* 利用缓存减少网络传输
* 针对弱网(移动网络), 不自动加载图片
* 界面先反馈, 请求延迟提交
* 例如, 用户点赞操作, 可以直接给出界面的点赞成功的反馈, 使用 HYPERLINK "https://link.jianshu.com/?t=https://developer.android.com/reference/android/app/job/JobScheduler.html" \t "https://www.jianshu.com/p/_blank" JobScheduler在网络情况较好的时候打包请求.

##### 布局优化：
* 重用（include）
* 合并：
* RelativeLayout会让子View调用2次onMeasure；
* LinearLayout 在有weight时，才会让子View调用2次onMeasure。Measure的耗时越长那么绘制效率就低。
* merge的使用；
* 懒加载（ViewStub）

内部类和静态内部类和匿名内部类，以及项目中的应用
编译器会默认为成员内部类添加了一个指向外部类对象的引用（也就是外部类的this变量）

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


下拉状态栏是不是影响activity的生命周期，如果在onStop的时候做了网络请求，onResume的时候怎么恢复
跨进程弹出对话框，实际证明也是没有影响的。
只有再启动另外一个Activity的时候才会进入onPause状态，而不是想象中的被覆盖或者不可见
同时通过AlertDialog源码或者Toast源码我们都可以发现它们实现的原理都是windowmanager.addView()来添加的， 它们都是一个个view ,因此不会对activity的生命周期有任何影响。 

### 模式MVP,MVC,MVVM介绍

### 逻辑地址与物理地址，为什么使用逻辑地址

### Android进程分类

### Activity的启动模式
##### standard：
> 每次启动的实例都不一样（都会重新创建实例，但是所属任务栈却是一样的）；
#####  singleTop：
> 如果改Activity位于栈顶，就不会被重建，直接调用onNewIntent();如果不是在栈顶，就和standard模式一样；
##### singleTask：
> 检查AndroidManifest.xml中是否有taskAffinity属性：
   * 没有taskAffinity：则会重新创建一个Task，将该Activity入栈；
   * 存在taskAffinity：
   * 若栈中存在该Activity实例，将上面所有Activity出栈；
   * 若没有，则新建，入栈；
### singleInstance：
> 具备singleTask模式的所有特性，另外，在这种模式下，该Activity会独占一个Task，具全局唯一性（整个系统都只有这一个实例）；


### ANR的原因

### 四大组件

### Activity与Service通信的方式I
##### ntent方式
##### 接口的方式


### startService();
> 开启Service，调用者退出后Service依然存在；启动流程：onCreate() -> onStartCommand() -> onDestory()
### bindService()；
> 调用者退出后Service依然存在；启动流程：onCreate() -> onBind() -> onUnBind() -> onDestory()

### service生命周期

### Activity之间的通信方式

### Activity与Fragment之间生命周期比较

### 设计模式

### 关于handler，在任何地方new handler 都是什么线程下

### sqlite升级，增加字段的语句

### AndroidManifest的作用与理解

### Application 和 Activity 的 context 对象的区别

### Serializable，为什么引入Parcelable，怎样简化Parcelable的使用；
##### Serializable：
> 是java层提供的方法，会频繁的触发IO操作，效率相对来说比较低，适合将对象存到磁盘上；

##### Parcelable：
> Android提供的Api，将序列化后的字节流写入到一个共性内存中，其他对象也可以从这里读取字节流，因此效率比较高，适合对象之间或者进程之间传递对象；

* 需要手动实现序列化的内容，需要指定每个变量的序列化和反序列化；
* 把复杂的对象拆分成可传输的基础数据类型，便于传输；
* 底层调用native实现；

### 项目：拉活怎么做的

##### 提升进程的优先级，降低进程被杀死的概率：
##### 一个像素的Activity；
##### 拉活被杀死的进程；
##### 双进程守护；

### AsyncTask机制，如何取消AsyncTask

### 进程状态

### webView与js的交互方式：
##### 对于Android调用JS代码的方法有2种：  
* 通过WebView的loadUrl（）（获取js的返回值困难） ; 
* 通过WebView的evaluateJavascript（），特点：
> 因为该方法(evaluateJavascript)的执行不会使页面刷新，而第一种方法（loadUrl ）的执行则会；
Android 4.4 后才可使用；

##### 对于JS调用Android代码的方法有3种：  
* addJavascriptInterface（）进行对象映射 ;缺点：当JS拿到Android这个对象后，就可以调用这个Android对象中所有的方法，包括系统类（java.lang.Runtime 类），从而进行任意代码执行； 
* 通过 WebViewClient 的shouldOverrideUrlLoading ()回调拦截 url ; 
* 通过 WebChromeClient 的onJsAlert()、onJsConfirm()、onJsPrompt（）方法回调拦截JS对话框alert()、confirm()、prompt（） 消息

##### webView内存泄漏相关问题优化：


### 进程间通信方式？