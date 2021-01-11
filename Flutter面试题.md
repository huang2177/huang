### MethodChannel、EventChannel原理？
* Dart层通过以上提到的12种类型包含的类型数据进行编码，然后通过dart的类似jni的本地接口方法传递给c++层；
* c++层通过持有java对象flutterJNI的方法调用将消息传递到java层；
* java层解码接收到的消息，根据消息内容做指定的逻辑处理，得到结果后进行编码通过jni方法将响应结果返回给c++层；
* c++层处理返回的响应结果，并将结果通过发送时保存的dart响应方法对象回调给Dart层；
* Dart层通过回调方法对结果数据进行处理，然后通过codec解码数据做后续的操作；

### Widget、Element、RenderObject区别？
##### Widget
> 仅用于存储渲染所需要的信息, Widget只是一个配置。

##### RenderObject
> 负责管理布局、绘制等操作。

##### Element
> 才是这颗巨大的控件树上的实体。Element会持有renderObject和widget的实例