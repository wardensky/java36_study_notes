# 对比Vector、ArrayList、LinkedList有何区别？

![](https://static001.geekbang.org/resource/image/67/c7/675536edf1563b11ab7ead0def1215c7.png)


掌握上面的图。
<p>Vector 是 Java 早期提供的<strong>线程安全的动态数组</strong>，如果不需要线程安全，并不建议选择，毕竟同步是有额外开销的。Vector 内部是使用对象数组来保存数据，可以根据需要自动的增加容量，当数组已满时，会创建新的数组，并拷贝原有数组数据。</p>


<p>ArrayList 是应用更加广泛的<strong>动态数组</strong>实现，它本身不是线程安全的，所以性能要好很多。与 Vector 近似，ArrayList 也是可以根据需要调整容量，不过两者的调整逻辑有所区别，Vector 在扩容时会提高 1 倍，而 ArrayList 则是增加 50%。</p>


<p>LinkedList 顾名思义是 Java 提供的<strong>双向链表</strong>，所以它不需要像上面两种那样调整容量，它也不是线程安全的。</p>


归并排序、交换排序（冒泡、快排）、选择排序、插入排序.

这章比较简单，都是基础。
