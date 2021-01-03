### Retrofit中的设计模式
* 建造者模式
> Retrofit对象的构建、ServiceMethod的构建，都使用了建造者模式；调用者不需要知道复杂的构建过程，通过build()方法即可使用；

* 代理模式
> interface的构建是通过动态代理来实现的；

