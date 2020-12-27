### 内存优化：
> 常见问题：内存抖动（短时间创建大量对象，且马上销毁）、内存泄漏、内存溢出、内存碎片
* 使用更加轻量的数据结构，比如SparseArray、ArrayMap等
* 避免在Android里面使用Enum
* 减小Bitmap对象的内存占用，
* 使用更小的图片
* 优化界面交互过程中频繁的内存使用；譬如在列表等操作中只加载可见区域的Bitmap、滑动时不加载、停止滑动后再开始加载。
* 尽量使用线程池替代多线程操作，这样可以节约内存及CPU占用率。
* 避免创建不必要的对象诸如一些临时对象, 特别是循环中的.

### 网络优化：
* 压缩/减少数据传输量
* 利用缓存减少网络传输
* 针对弱网(移动网络), 不自动加载图片
* 界面先反馈, 请求延迟提交
* 例如, 用户点赞操作, 可以直接给出界面的点赞成功的反馈, 使用 HYPERLINK "https://link.jianshu.com/?t=https://developer.android.com/reference/android/app/job/JobScheduler.html" \t "https://www.jianshu.com/p/_blank" JobScheduler在网络情况较好的时候打包请求.

### 布局优化：
> 过度绘制、布局复杂、层级过深
* 重用（include）
* 合并：merge的使用
* 懒加载（ViewStub）

### 性能检测工具
<https://www.jianshu.com/p/7e9ca2c73c97>

* Android studio profile
* 通过SDK 提供的Debug类获取trace文件；
* 通过DDMS的traceview来分析；


### 电量优化
##### 核心工作电量优化
* 内存优化、布局优化、网络优化

##### 核心工作电量优化
* 优化定位的方式：GPS > 移动网络 > WIFI;
> 比如外卖系统，可能不需要那么精确的定位，可是利用WIFI、移动网络 + 用户的收货地址来确定
* 优化定位时间间隔：驾车、骑行、步行、起步时定位、交通路口；
* 终极方案：在充电+WI-FI时进行一些任务的进行；

### XML绘制到渲染屏幕的过程
* CPU负责计算程多边形、纹理
* OpenGl负责绘制图像（Display list）
* GPU栅格化需要显示内容并渲染到屏幕；（栅格化就是把矢量图转化为位图的过程）


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
