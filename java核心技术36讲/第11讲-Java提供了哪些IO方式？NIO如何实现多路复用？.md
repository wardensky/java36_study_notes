# Java提供了哪些IO方式？NIO如何实现多路复用？

本文主要讲BIO、NIO和NIO2，也就是AIO

- BIO，同步阻塞式IO，简单理解：一个连接一个线程
- NIO，同步非阻塞IO，简单理解：一个请求一个线程
- AIO，异步非阻塞IO，简单理解：一个有效请求一个线程

- java.io包同步阻塞
- java.nio  多路复用，同步非阻塞
- nio2 异步非阻塞IO  AIO asynchronous IO 基于事件和回调机制，java 7
