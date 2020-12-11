#### 模块化实现（好处，原因）
* 为什么模块间解耦，复用？   	
> 原因：各个都是独立的模块，每个模块负责的功能不同，业务逻辑不同，模块间业务解耦。模块功能比较单一，可在多个项目中使用
* 为什么可以多团队并行开发，测试？
> 原因：每个团队负责不同的模块，提升开发，测试效率
* 为什么可单独编译某个模块，提升开发效率？
> 原因：每个模块实际上也是一个完整的项目，可以进行单独编译，调试

#### 统计启动时长,标准
adb shell am start -w packagename/activity
会返回3个值：
* WaitTime 返回从 startActivity 到应用第一帧完全显示这段时间. 就是总的耗时，包括前一个应用 Activitypause 的时间和新应用启动的时间；
* ThisTime 表示一连串启动 Activity 的最后一个 Activity 的启动耗时；
* TotalTime 表示新应用启动的耗时，包括新进程的启动和 Activity 的启动，但不包括前一个应用Activitypause的耗时。开发者一般只要关心 TotalTime 即可，这个时间才是自己应用真正启动的耗时。

#### SP是进程同步的吗?有什么方法做到同步
* 不能，它基于单个文件的，默认是没有考虑同步互斥，而且，APP对SP对象做了缓存，不好互斥同步；
* ContentProvider基于Binder，不存在进程间互斥问题，对于同步，也做了很好的封装，不需要开发者额外实现。 

#### 介绍下SurfaceView
* SurfaceView继承之View，但拥有独立的绘制表面，即它不与其宿主窗口共享同一个绘图表面，可以单独在一个线程进行绘制，并不会占用主线程的资源。
* SurfaceView有两个子类GLSurfaceView和VideoView
* SurfaceView和View的区别：
* View主要适用于主动更新的情况下，而SurfaceView主要适用于被动更新，例如频繁地刷新
* View在主线程中对画面进行刷新，而SurfaceView通常会通过一个子线程来进行页面的刷新
* View在绘图时没有使用双缓冲机制，而SufaceView在底层实现机制中就已经实现了双缓冲机制

#### BroadcastReceiver与LocalBroadcastReceiver 区别
* LocalBroadcastReceiver只会存在于应用内部，不用担心机密数据泄露问题；
* 其他程序无法将广播发送到我们程序的内部，因此不需要担心会有安全漏洞的隐患；
* 发送本地广播要比全局广播更加高效；
* 基于主线程的 Looper 新建了一个 Handler，handleMessage中会调用接收器对广播的消息进行处理
* 注册时用一个ArrayList保存ReceiverRecord（继承自IntentFilter），发送的时候对其进行遍历，然后调用onReceive();

#### Binder机制
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


Handler 机制


android 事件传递机制


#### 画出 Android 的大体架构图
* 应用层：应用是用java语言编写的运行在虚拟机上的程序，比如Email客户端，SMS短消息程序，日历等。
* 应用框架层：这一层是编写Google发布的核心应用时所使用的API框架。应用程序框架层包括活动管理器、窗口管理器、内容提供者、视图系统、包管理器、电话管理器、资源管理器等。
* 系统运行库（C/C++库以及Android运行库）层：当使用Android应用框架时，Android系统会通过一些C/C++库来支持我们使用的各个组件，使其更好的为我们服务，比如其中的SQLite（关系数据库），Webkit（Web浏览器引擎）。
* Linux内核层：Android的核心系统服务给予Linux2.6内核，如安全性、内存管理、进程管理、网络协议栈和驱动模型等都依赖于该内核，比如Binder IPC(Internet Process Connection进程间通信)驱动，android的一个特殊驱动程序，具有单独的设备节点，提供进程间通信的功能。

#### 描述点击 Android Studio 的 build 按钮后发生了什么
* 通过AAPT工具进行资源文件（包括AdroidManifest.xml，布局文件，各种xml文件）的打包，生成R.java文件；
* 通过AIDL工具处理AIDL文件，生成相应的Java文件；
* 通过Javac工具对项目源码进行编译，生成class文件；
* 通过DX工具将所有的class文件转换成DEX文件；
* ApkBuilder	-------> 	.apk文件；
* keyStore	    ------->	签名；
* ZipAlign		------->	对齐（将apk中的资源文件的起时距离偏移4个字节）；

#### Apk的安装流程？
* 安装Apk；
* 拷贝Apk；
* 资源管理器解析Apk中的资源文件；
* 解析AndroidManifest文件，创建 data/app 目录；
* 对dex文件进行优化，并缓存；
* 解析四大组件，并注册到PMS（PackageParse去解析）中（这里的Acitivity只是一个存档，并不是完整的组件，例如：ActivityInfo，ServiceInfo）；

#### 点击桌面图标发生了什么？
* Launcher进程将启动MainActivity发送给AMS；（Binder的方式）
* AMS会交给ActivityStarter处理Intent/flag，再交给ActivityStackSupervisior（高版本）/ActivityStack处理进栈相关流程；同时以Socket的方式请求Zygote进程Fork新进程；
* 在新进程创建ActivityThread对象，新创建的进程就是该应用的主线程（UI线程），在主线程里面开启Looper消息循环，开始处理Activity；
* ActivityThread利用ClassLoader去加载Activity，创键Activity实例，并且回调一系列的生命周期的方法；


#### 对Dalvik、ART 虚拟机有基本的了解；
##### Dalvik（4.4以前）
> Dalvik虚拟机在启动的时候会先将.dex文件转换成快速运行的机器码，又因为65535这个问题，导致我们在应用冷启动的时候有一个合包的过程，最后导致的一个结果就是我们的app启动慢，这就是Dalvik虚拟机的JIT特性（Just In Time）。
采用传统的垃圾回收算法，会出现内存碎片化；（标记-->回收-->恢复）
##### ART （5.0开始）
> ART有一个很好的特性AOT（ahead of time），这个特性就是我们在安装APK的时候就将dex直接处理成可直接供ART虚拟机使用的机器码，ART虚拟机将.dex文件转换成可直接运行的.oat文件，ART虚拟机天生支持多dex，所以也不会有一个合包的过程，所以ART虚拟机会很大的提升APP冷启动速度。
ART虚拟机允许标记和回收可以同时进行；另外引入了移动垃圾回收器技术，使得碎片化的内存能够对齐，能够节约一点内存。

#### App是如何沙箱化，为什么要这么做；
> 沙箱，对使用者来说可以理解为一种安全环境，对恶意访问者来说是一种限制。
Android 沙箱实现了应用程序的相互隔离，用于对文件系统的实体访问进行控制，也可以对其他Android系统资源进行访问控制。
简单的说，每个应用程序无法访问其他应用程序的资源。沙箱系统的原理主要基于Linux系统的UID/GID机制。Android对传统的 Linux的UID/GID机制进行了修改。在 Linux 中，一个用户 ID 识别一个给定用户;在 Android 上，一个用户 ID 识别一个应用程序。
Android定义了从名称到独特标识符的Android ID(AID)映射表。初始的映射表中定义了一些特权用户和一些系统关键用户。（用来代替UID/GID）

#### 系统启动流程
> Zygote进程 –> SystemServer进程 –> 各种系统服务 –> 应用进程


#### Retrofit理解？
* OKHttpCall.execute（），该方法生成一个okhttp3.Call将任务抛给OKHttp。
* 完了调用parseResponse，用Converter将okhttp3.Response转换成我们在范型中指定的类型Response<ResponseBody> response = call.execute()，我指定了okhttp3.ResonseBody，然后返回结果。
* 可以在在构造Retrofit时提供GsonConverter，addConverterFactory(GsonConverterFactory.create())；

#### RxJava：


#### Glide源码？glide 使用什么缓存？
磁盘缓存：默认使用的是LRU（Least Recently Used）算法（时间）。如果你想使用其他的缓存算法，就只能通过实现DiskCache接口来完成了。
内存缓存：Glide中有一个叫做BitmapPool的类，可以复用其中的Bitmap对象，从而避免Bitmap对象的创建，减小内存开销。

#### Bitmap如何处理大图，如一张30M的大图，如何预防OOM
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


广播（动态注册和静态注册区别，有序广播和标准广播），广播的使用场景


数据库数据迁移问题


TCP与UDP区别与应用（三次握手和四次挥手）涉及到部分细节（如client如何确定自己发送的消息被server收到）


是否熟悉Android jni开发，jni如何调用java层代码


Android系统为什么会设计ContentProvider，进程共享和线程安全问题


Android相关优化（如内存优化、网络优化、布局优化、电量优化）
内存优化：
使用更加轻量的数据结构，比如SparseArray、ArrayMap等
避免在Android里面使用Enum
减小Bitmap对象的内存占用，
使用更小的图片
优化界面交互过程中频繁的内存使用；譬如在列表等操作中只加载可见区域的Bitmap、滑动时不加载、停止滑动后再开始加载。
尽量使用线程池替代多线程操作，这样可以节约内存及CPU占用率。
避免创建不必要的对象诸如一些临时对象, 特别是循环中的.

网络优化：
压缩/减少数据传输量
利用缓存减少网络传输
针对弱网(移动网络), 不自动加载图片
界面先反馈, 请求延迟提交
例如, 用户点赞操作, 可以直接给出界面的点赞成功的反馈, 使用 HYPERLINK "https://link.jianshu.com/?t=https://developer.android.com/reference/android/app/job/JobScheduler.html" \t "https://www.jianshu.com/p/_blank" JobScheduler在网络情况较好的时候打包请求.
布局优化：
重用（include）
合并：
RelativeLayout会让子View调用2次onMeasure；
LinearLayout 在有weight时，才会让子View调用2次onMeasure。Measure的耗时越长那么绘制效率就低。
merge的使用；

懒加载（ViewStub）



内部类和静态内部类和匿名内部类，以及项目中的应用
编译器会默认为成员内部类添加了一个指向外部类对象的引用（也就是外部类的this变量）
为什么局部内部类和匿名内部类只能访问局部final变量：
    public void test(final int b) {
        final int a = 10;
        new Thread() {
            public void run() {
                System.out.println(a);
                System.out.println(b);
            }
        }.start();
    }
当test方法执行完毕之后，变量a的生命周期就结束了，而此时Thread对象的生命周期很可能还没有结束。所以Java采用了 复制  的手段来解决这个问题（会把a复制到常量池中），使用final是不允许内部类改变a的值。



封装view的时候怎么知道view的大小


计算一个view的嵌套层级
int i = 0;
private void getParents(ViewParent view){
if (view.getParent() == null) {
Log.v("tag", "最终==="+i);
return;
}
i++;
ViewParent parent = view.getParent();
Log.v("tag", "i===="+i);
Log.v("tag", "parent===="+parent.toString());
getParents(parent);
}

下拉状态栏是不是影响activity的生命周期，如果在onStop的时候做了网络请求，onResume的时候怎么恢复
跨进程弹出对话框，实际证明也是没有影响的。
只有再启动另外一个Activity的时候才会进入onPause状态，而不是想象中的被覆盖或者不可见
同时通过AlertDialog源码或者Toast源码我们都可以发现它们实现的原理都是windowmanager.addView()来添加的， 它们都是一个个view ,因此不会对activity的生命周期有任何影响。 


二叉树，给出根节点和目标节点，找出从根节点到目标节点的路径


模式MVP，MVC介绍


断点续传的实现


逻辑地址与物理地址，为什么使用逻辑地址


Android进程分类


前台切换到后台，然后再回到前台，Activity生命周期回调方法。弹出Dialog，生命值周期回调方法。

activity栈


Activity的启动模式
standard：
每次启动的实例都不一样（都会重新创建实例，但是所属任务栈却是一样的）；
singleTop：
如果改Activity位于栈顶，就不会被重建，直接调用onNewIntent();
如果不是在栈顶，就和standard模式一样；
singleTask：
检查AndroidManifest.xml中是否有taskAffinity属性：
没有taskAffinity：则会重新创建一个Task，将该Activity入栈；
存在taskAffinity：
若栈中存在该Activity实例，将上面所有Activity出栈；
若没有，则新建，入栈；
singleInstance：
具备singleTask模式的所有特性，另外，在这种模式下，该Activity会独占一个Task，具全局唯一性（整个系统都只有这一个实例）；




ANR的原因


四大组件


Activity与Service通信的方式
Intent方式
接口的方式

startService(); 开启Service，调用者退出后Service依然存在；
启动流程：onCreate() -> onStartCommand() -> onDestory()
bindService()；调用者退出后Service依然存在；
启动流程：onCreate() -> onBind() -> onUnBind() -> onDestory()


Activity之间的通信方式


Activity与Fragment之间生命周期比较


service生命周期


观察者模式，适配器模式，装饰者模式，外观模式的异同？


String 为什么要设计成不可变的？
设计考虑：（字符串常量池的需要）
String s1 = “abcd”;
String s2 = “abcd”;
上面的代码只会在堆中生成一个变量；假如String是可变的，一个对象改变了，就会影响到另外的变量；
效率优化：（允许String缓存HashCode）
String的Hash被频繁使用（HashMap），如果String不可变，就保证了Hash的唯一性，不用每次都去缓存；
安全性：
String被许多的Java类(库)用来当做参数,例如 网络连接地址URL,文件路径path,还有反射机制所需要的String参数等, 假若String不是固定不变的,将会引起各种安全隐患。

String，Stringbuffer 与stringbuilder 的区别？


java四中引用


关于handler，在任何地方new handler 都是什么线程下


sqlite升级，增加字段的语句


AndroidManifest的作用与理解


BIO（Blocking I/O），NIO（Noblocking I/O），AIO（Asyn I/O）

同步: 应用进程触发IO操作后，等待或者轮询查看IO状态是否改变。
异步: 应用进程触发IO操作后，就去执行其它任务，当有内核进程通知应用进程IO状态发生改变，应用进程根据最新的状态进行IO操作。
阻塞: 当IO状态改变后，进行相关IO操作时，如果IO被占用，一直处于等待，知道IO可用为止。
非阻塞: 当IO状态改变后，进行相关IO操作时，如果IO被占用，马上返回操作结果值，当IO就绪时，IO函数会通知最新的IO状态，从而继续进行IO操作。

同步阻塞（BIO）:
小明去书店买书，然后一直在柜台前等着，并且一直询问：有书吗？有书吗？……
适用于方法简单，开发速度快。但是连接数较小，对服务器性能较高，并发性差。
同步非阻塞（NIO）:
小明去书店买书，跟柜台说完后就去网吧玩了，玩一会就跑回柜台问一下：书准备好了吗？
适用于连接数多且为短链接，轻数据量的服务器，比如通讯服务器，编程方式复杂，JDK1.4中引入。

异步阻塞（NIO）：
小明给书店打电话说要定新书，当在网吧玩时，书店打来电话，说有新书了，小明自己去拿书。
异步非阻塞（AIO）：
小明给书店打电话说定新书，当在网吧玩时，书店打电话说，有新书到了，并派人将新书送过来。
适用于连接数少且为长连接，重数据量的服务器，比如视频服务器，充满在JDK1.7中引入，所以Android系统中，只用5.0+版本才支持AIO。

NIO作为一种中高负载的I/O模型，相对于传统的BIO (Blocking I/O)来说有了很大的提高，处理并发不用太多的线程，省去了创建销毁的时间，如果线程过多调度是问题，同时很多线程可能处于空闲状态，大大浪费了CPU时间，同时过多的线程可能是性能大幅下降，一般的解决方案中可能使用线程池来管理调度但这种方法治标不治本。
使用NIO在Android中有哪些用处
Android NIO分为三大类，ByteBuffer、FileChannel和SocketChannel
在Android中还没具体的使用场景，但在官方给出的OpenGL教程中却用到了； HYPERLINK "http://www.cnblogs.com/spring87/p/4925628.html"

Activity生命周期


fragment 各种情况下的生命周期


fragment之间传递数据的方式？


横竖屏切换的时候，Activity 各种情况下的生命周期


Application 和 Activity 的 context 对象的区别


序列化的作用，以及 Android 两种序列化的区别。


如何实现Fragment的滑动


ViewPager使用细节，如何设置成每次只初始化当前的Fragment，其他的不初始化
根据setUserVisibleHint()实现懒加载


Serializable，为什么引入Parcelable，怎样简化Parcelable的使用；
Serializable：
是java层提供的方法，会频繁的触发IO操作，效率相对来说比较低，适合将对象存到磁盘上；

Parcelable：
Android提供的Api，将序列化后的字节流写入到一个共性内存中，其他对象也可以从这里读取字节流，因此效率比较高，适合对象之间或者进程之间传递对象；
步骤：
1）implements Parcelable
2）重写writeToParcel方法，将你的对象序列化为一个Parcel对象，即：将类的数据写入外部提供的Parcel中，打包需要传递的数据到Parcel容器保存，以便从 Parcel容器获取数据；
3）重写describeContents方法，内容接口描述，默认返回0就可以；
4）实例化静态内部对象CREATOR实现接口Parcelable.Creator；

项目：拉活怎么做的
提升进程的优先级，降低进程被杀死的概率：
一个像素的Activity；
提升应用的

拉活被杀死的进程；
双进程守护；


AsyncTask机制，如何取消AsyncTask


进程状态


Java线程池


线程和进程的区别？为什么要有线程，而不是仅仅用进程？


多线程：怎么用、有什么问题要注意；Android线程有没有上限，然后提到线程池的上限


如何保证多线程读写文件的安全？


如何实现线程同步？


为什么不能在子线程更新UI


算法判断单链表成环与否？


object类的equal和hashcode方法重写，为什么？
HashCode：
hashCode是jdk根据对象的地址或者字符串或者数字算出来的int类型的数值 ；

内存泄漏的可能原因？用IDE如何分析内存泄漏？


OOM的可能原因？Oom是否可以try catch？
1.资源对象没关闭造成的内存泄露，try catch finally中将资源回收放到finally语句可以有效避免OOM。资源性对象比如： 	（1）Cursor；
（2）调用registerReceiver后未调用unregisterReceiver()； 	（3）未关闭InputStream/OutputStream； 	（4）Bitmap使用后未调用recycle() ；
2.作用域不一样，导致对象不能被垃圾回收器回收，比如： 	（1）非静态内部类会隐式地持有外部类的引用； 	（2）Context泄露； 概括一下，避免Context相关的内存泄露，记住以下事情：    	1、 不要保留对Context-Activity长时间的引用（对Activity的引用的时候，必须确保拥有和Activity一样的生命周期）；    	2、尝试使用Context-Application来替代Context-Activity 3、如果你不想控制内部类的生命周期，应避免在Activity中使用非静态的内部类，而应该使用静态的内部类，并在其中创建一个对Activity的弱引用；       这种情况的解决办法是使用一个静态的内部类，其中拥有对外部类的WeakReference； （3）Thread 引用其他对象也容易出现对象泄露； （4）onReceive方法里执行了太多的操作；
内存压力过大
（1）图片资源加载过多，超过内存使用空间，例如Bitmap 的使用；   	（2）重复创建view，listview应该使用convertview和viewholder；
如何避免内存泄露 	1.使用缓存技术，比如LruCache、DiskLruCache、对象重复并且频繁调用可以考虑对象池； 	2.对于引用生命周期不一样的对象，可以用软引用或弱引用SoftReferner WeakReferner； 	3.对于资源对象 使用finally 强制关闭； 	4.内存压力过大就要统一的管理内存；

差值器&估值器


进程间通信方式？


Volatile,synchronize,lock的原理


wait/notify


死锁的概念，怎么避免死锁
在两个或多个并发进程中，如果每个进程持有某种资源而又都等待别的进程释放它或它们现在保持着的资源，在未改变这种状态之前都不能向前推进，称这一组进程产生了死锁 ，通俗地讲，就是两个或多个进程被无限期地阻塞、相互等待的一种状态
死锁产生的原因主要是：  　　 1.系统资源不足  　　 2.进程推进顺序非法
产生死锁的必要条件：  　　（1）互斥（mutualexclusion），一个资源每次只能被一个进程使用  　　（2）不可抢占（nopreemption），进程已获得的资源，在未使用完之前，不能强行剥夺  　　（3）占有并等待（hold andwait），一个进程因请求资源而阻塞时，对已获得的资源保持不放  　　（4）环形等待（circularwait），若干进程之间形成一种首尾相接的循环等待资源关系。
避免死锁：
（1）破坏“互斥”条件:一般“互斥”条件是无法破坏的。因此，在死锁预防里主要是破坏其他三个必要条件，而不去涉及破坏“互斥”条件。
（2）破坏“请求和保持”条件:在系统中不允许进程在已获得某种资源的情况下，申请其他资源。即要想出一个办法，阻止进程在持有资源的同时申请其他资源。
（3）破坏“不可抢占”条件：允许对资源实行抢夺。 		方法一：如果占有某些资源的一个进程进行进一步资源请求被拒绝，则该进程必须释放它最初占有的资源，如果有必要，可再次请求这些资源和另外的资源。 		方法二：如果一个进程请求当前被另一个进程占有的一个资源，则操作系统可以抢占另一个进程，要求它释放资源。只有在任意两个进程的优先级都不相同的条件下，该方法才能预防死锁。
（4）破坏“循环等待”条件：将系统中的所有资源统一编号，进程可在任何时刻提出资源申请，但所有申请必须按照资源的编号顺序（升序）提出。这样做就能保证系统不出现死锁。
解除死锁：
（1）抢占资源。从一个或多个进程中抢占足够数量的资源，分配给死锁进程，以解除死锁状态。
（2）终止（或撤销）进程。终止（或撤销）系统中的一个或多个死锁进程，直至打破循环环路，使系统从死锁状态解脱出来。
总结：
写程序时应该尽量避免同时获得多个锁,如果一定有必要这么做,则有一个原则:如果所有线程在需要多个锁时都按相同的先后顺序(常见的是按Mutex变量的地址顺序)获得锁,则不会出现死锁。比如一个程序中用到锁1、锁2、锁3,它们所对应的Mutex变量的地址是锁1<锁2<锁3,那么所有线程在需要同时获得2个或3个锁时都应该按锁1、锁2、锁3的顺序获得。
如果要为所有的锁确定一个先后顺序比较困难,则应pthread_mutex_trylock调用代替pthread_mutex_lock 调用,以免死锁。


两个不重复的数组集合中，求共同的元素。


webView与js的交互方式：

对于Android调用JS代码的方法有2种：  1. 通过WebView的loadUrl（）（获取js的返回值困难） ; 2. 通过WebView的evaluateJavascript（），特点：
a：因为该方法的执行不会使页面刷新，而第一种方法（loadUrl ）的执行则会；
b：Android 4.4 后才可使用；

对于JS调用Android代码的方法有3种：  1.addJavascriptInterface（）进行对象映射 ;
缺点：当JS拿到Android这个对象后，就可以调用这个Android对象中所有的方法，包括系统类（java.lang.Runtime 类），从而进行任意代码执行； 2.通过 WebViewClient 的shouldOverrideUrlLoading ()回调拦截 url ; 3.通过 WebChromeClient 的onJsAlert()、onJsConfirm()、onJsPrompt（）方法回调拦截JS对话框alert()、confirm()、prompt（） 消息


