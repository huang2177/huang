#### 更简洁的字符串，字符串模板
#### Kotlin中大多数控制结构都是表达式
* if else
* when
* try catch

#### 更好调用的函数
* 显式参数名/默认参数值
* 扩展函数和属性

#### 懒初始化by lazy 和 延迟初始化lateinit
* by lazy 修饰val的变量
* lateinit 修饰var的变量，且变量是非空的类型

#### 用类委托来快速实现装饰器模式
```
class CountingSet2<T>(val innerSet: MutableCollection<T>) : MutableCollection<T> by innerSet {
    override fun add(element: T): Boolean {
        objectAdded++
        return innerSet.add(element)
    }
}
```
> 可以选择性实现父类的方法，不然的话需要全部实现；

#### 高阶函数简化代码


# Kotlin 子协程异常时取消顺序
* 抛出异常的子协程
* 父协程通知其他子协程停止
* 直到所有的子协程全部停止，父协程才会终止
