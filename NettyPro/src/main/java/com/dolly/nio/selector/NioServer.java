package com.dolly.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author yusenyang
 * @create 2020/10/20 00:25
 */
public class NioServer {
    public static void main(String[] args) throws IOException {

        // 创建 ServerSocketChannel -> ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 得到一个 Selector 对象
        Selector selector = Selector.open();

        // 绑定一个端口 6666，在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        // 把 serverSocketChannel 注册到 selector 关心事件为 OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环等待客户端连接
        while (true) {

            // 等待 1 秒后，没有事件发生，则重新进入循环
            if (selector.select(1000) == 0) {
                System.out.println("服务器等待了1秒，无连接...");
                continue;
            }

            /**
             * 如果返回大于 0，就获取到相关的 selectionKey 集合：
             *  1、如果返回大于 0，表示已经获取到关注的事件
             *  2、selector.selectionKeys() 返回关注的事件集合
             *  3、通过 selectionKeys 反向获取通道
             */
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            // 遍历 selectionKeys，使用迭代器遍历
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            // 根据 key 对应的通道发生的事件做相应的处理
            while (keyIterator.hasNext()) {

                // 获取到 selectionKey
                SelectionKey key = keyIterator.next();

                // 如果是 OP_ACCEPT，有新的客户端连接
                if (key.isAcceptable()) {

                    // 给该客户端生成一个 SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功，生成了一个 socketChannel " + socketChannel.hashCode());

                    // 将 socketChannel 设置为非阻塞
                    socketChannel.configureBlocking(false);

                    // 将 socketChannel 注册到 selector，关注事件为 OP_READ，同时给 socketChannel 关联一个 Buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                // 如果是 OP_READ，可读
                if (key.isReadable()) {

                    // 通过 key 反向获取到对应的 channel
                    SocketChannel channel = (SocketChannel) key.channel();

                    // 获取到该 channel 关联的 buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();

                    channel.read(buffer);
                    System.out.println("from 客户端：" + new String(buffer.array()));
                }

                // 手动从集合中移动当前的 selectionKey，防止重复操作
                keyIterator.remove();
            }
        }
    }
}
