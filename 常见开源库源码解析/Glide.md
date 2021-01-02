### Glide 缓存策略有关：
* ALL
> 既缓存原始图片，也缓存转换过后的图片；对于远程图片，缓存 DATA 和 RESOURCE；对于本地图片，只缓存 RESOURCE。

* AUTOMATIC (默认策略)
>尝试对本地和远程图片使用最佳的策略。当你加载远程数据（比如，从 URL 下载）时，AUTOMATIC 策略仅会存储未被你的加载过程修改过 (比如，变换、裁剪等) 的原始数据（DATA），因为下载远程数据相比调整磁盘上已经存在的数据要昂贵得多。对于本地数据，AUTOMATIC 策略则会仅存储变换过的缩略图（RESOURCE），因为即使你需要再次生成另一个尺寸或类型的图片，取回原始数据也很容易。

* DATA
> 只缓存未被处理的文件。我的理解就是我们获得的 stream。它是不会被展示出来的，需要经过装载 decode，对图片进行压缩和转换，等等操作，得到最终的图片才能被展示。

NONE
* 表示不缓存任何内容。

* RESOURCE
> 表示只缓存转换过后的图片（也就是经过decode，转化裁剪的图片）。

### Glide 缓存机制
* 内存缓存：通过loadFromMemory()
  * loadFromActiveResources()：内部使用弱引用包装，active.acquire()引用计数机制，被引用一次就+1，当为0的时候，就会退居二线内存容器中；
  * loadFromCache()：继承自 LruCache，当命中EngineKey。LruCache会删除这个EngineKey。同时会放进一级缓存中，同时计数+1；

* 磁盘缓存：磁盘缓存比较简单.
  * ResourceCacheKey：已经decode过的可以之间供Target给到View去渲染的；
  * DataCacheKey：还未decode过的，缓存的是源数据。

