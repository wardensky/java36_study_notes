# Exception和Error有什么区别？

![](https://static001.geekbang.org/resource/image/ac/00/accba531a365e6ae39614ebfa3273900.png)

上面这张图比较有用，起码图里面的error和exception要熟悉。

简言之，error就是不能处理的，exception就是能处理的。


- 尽量不要捕获类似 Exception 这样的通用异常，而是应该捕获特定异常
- 不要生吞（swallow）异常


本章很基础。
