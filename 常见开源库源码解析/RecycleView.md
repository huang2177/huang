### RecycleView缓存：
##### mAttachedScrap
* 缓存屏幕中可见的元素；
* 为了再下一次执行addView 的过程中从mAttachedScrap获取到数据,这样可以将bindViewHolder方法所执行的时间省去了,可以加快view的绘制；

##### mCacheViews
* 刚刚移出屏幕的缓存数据，默认大小是**2**个
* 当其容量被充满同时又有新的数据添加的时候，会根据FIFO原则，把优先进入的缓存数据移出并放到下一级缓存中，然后再把新的数据添加进来。
* Cache里面的数据是干净的，也就是携带了原来的ViewHolder的所有数据信息，数据可以直接来拿来复用。
* Cache是**根据position来寻找数据**的，这个postion是根据第一个或者最后一个可见的item的position以及用户操作行为（上拉还是下拉）。

##### ViewCacheExtension
* 自定义缓存，通常适用于不会随position改变的场景，例如：某个固定位置需要放置广告；

##### RecyclerViewPool
* 根据ViewType来缓存ViewHolder，每个ViewType的数组大小为5，可以动态的改变。（SparseArray+List）
* RecyclerViewPool会把ViewHolder的所有数据清空，所以在使用的时候，需要重新调用onBindViewHolder();

### RecycleView第一次layout时，会发生预布局pre-layout吗？
* 第一次布局时，并不会触发pre-layout；
* pre-layout只会在每次notify change时才会被触发；
* 目的是通过saveOldPosition方法将屏幕中各位置上的ViewHolder的坐标记录下来，并在重新布局之后，通过对比**实现Item的动画效果**。
* [pre-layout](https://www.jianshu.com/p/b0c391bd38b3)

### ViewHolder何时被缓存到RecycledViewPool中？
* 当ItemView被滑动出屏幕时，并且CachedView已满，则ViewHolder会被缓存到RecycledViewPool中；
* 当数据发生变动时，执行完disAppearrance的ViewHolder会被缓存到RecycledViewPool中；

### CachedView和RecycledViewPool两者区别
* CachedView中的ViewHolder并不会清理相关信息(比如position、state等)，因此刚移出屏幕的ViewHolder，再次被移回屏幕时，只要从CachedView中查找并显示即可，不需要重新绑定(bindViewHolder)。
* 而缓存到RecycledViewPool中的ViewHolder会被清理状态和位置信息，因此从RecycledViewPool查找到ViewHolder，需要重新调用bindViewHolder绑定数据。

### 你是从哪些方面优化RecyclerView的？
* 尽量将复杂的数据处理操作放到异步中完成，将最优质的数据格式返回给UI线程。
* 优化RecyclerView的布局，避免过于复杂的布局；
* 针对快速滑动事件，当用户快速滑动时，停止加载数据操作。
* 如果ItemView的高度固定，可以使用setHasFixSize(true)。这样RecyclerView在onMeasure阶段可以直接计算出高度，不需要多次计算子ItemView的高度。
* 考虑使用RecycledViewPool来实现多个RecyclerView的缓存共享。