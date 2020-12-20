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

### 模式MVP,MVC,MVVM介绍

### 逻辑地址与物理地址，为什么使用逻辑地址

### ANR的原因

### sqlite升级，增加字段的语句

### AndroidManifest的作用与理解

### Serializable，为什么引入Parcelable，怎样简化Parcelable的使用；
##### Serializable：
> 是java层提供的方法，会频繁的触发IO操作，效率相对来说比较低，适合将对象存到磁盘上；

##### Parcelable：
> Android提供的Api，将序列化后的字节流写入到一个共性内存中，其他对象也可以从这里读取字节流，因此效率比较高，适合对象之间或者进程之间传递对象；

* 需要手动实现序列化的内容，需要指定每个变量的序列化和反序列化；
* 把复杂的对象拆分成可传输的基础数据类型，便于传输；
* 底层调用native实现；

### 项目：拉活怎么做的
* 提升进程的优先级，降低进程被杀死的概率：
* 一个像素的Activity；
* 拉活被杀死的进程；
* 双进程守护；

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