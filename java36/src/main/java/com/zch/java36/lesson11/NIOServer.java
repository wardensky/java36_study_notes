package com.zch.java36.lesson11;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class NIOServer extends Thread {

	public void run() {
		try (Selector selector = Selector.open(); ServerSocketChannel serverSocket = ServerSocketChannel.open();) {// 创建
																													// Selector
																													// 和
																													// Channel
			serverSocket.bind(new InetSocketAddress(InetAddress.getLocalHost(), 8888));
			serverSocket.configureBlocking(false);
			// 注册到 Selector，并说明关注点
			serverSocket.register(selector, SelectionKey.OP_ACCEPT);
			while (true) {
				selector.select();// 阻塞等待就绪的 Channel，这是关键点之一
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> iter = selectedKeys.iterator();
				while (iter.hasNext()) {
					SelectionKey key = iter.next();
					// 生产系统中一般会额外进行就绪状态检查
					sayHelloWorld((ServerSocketChannel) key.channel());
					iter.remove();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sayHelloWorld(ServerSocketChannel server) throws IOException {
		try (SocketChannel client = server.accept();) {
			client.write(Charset.defaultCharset().encode("Hello world!"));
		}
	}
	// 省略了与前面类似的 main

}
