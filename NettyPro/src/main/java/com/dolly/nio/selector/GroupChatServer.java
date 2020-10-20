package com.dolly.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @author yusenyang
 * @create 2020/10/20 14:08
 */
public class GroupChatServer {
    private static final int PORT = 6667;
    private Selector selector;
    private ServerSocketChannel listenChannel;

    public GroupChatServer() {
        try {
            // 得到 selector
            selector = Selector.open();

            // 得到 listenChannel
            listenChannel = ServerSocketChannel.open();

            // 绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));

            // 设置非阻塞模式
            listenChannel.configureBlocking(false);

            // 将该 listenChannel 注册到 selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 监听，循环等待
    public void listen() {
        while (true) {

            try {
                if (selector.select() == 0) {
                    System.out.println("等待...");
                    continue;
                }

                // 如果有事件要处理
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                while (iterator.hasNext()) {

                    // 取出 selectionKey
                    SelectionKey key = iterator.next();

                    // 监听到 accept
                    if (key.isAcceptable()) {

                        SocketChannel socketChannel = listenChannel.accept();

                        // 设置非阻塞
                        socketChannel.configureBlocking(false);

                        // 将 socketChannel 注册到 selector
                        socketChannel.register(selector, SelectionKey.OP_READ);

                        // 提示
                        System.out.println(socketChannel.getRemoteAddress() + " 上线了...");
                    }

                    // 监听到 可读，专门写一个方法
                    if (key.isReadable()) {
                        readData(key);
                    }

                    // 手动从集合中移动当前的 selectionKey，防止重复操作
                    iterator.remove();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 读取客户端消息
    private void readData(SelectionKey key) {

        // 定义一个 SocketChannel
        SocketChannel channel = null;
        SocketAddress remoteAddress = null;

        try {
            // 得到 channel
            channel = (SocketChannel) key.channel();
            remoteAddress = channel.getRemoteAddress();

            // 创建 buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            if (channel.read(buffer) > 0) {

                // 把缓冲区的数据转成字符串
                String msg = new String(buffer.array());

                System.out.println("from 客户端：" + msg);

                // 向其他客户端转发消息（去掉自己），专门写一个方法来处理
                sendInfoToOtherClients(msg, channel);
            }
        } catch (IOException e) {
            try {
                System.out.println(remoteAddress + " 离线了...");

                // 取消注册
                key.cancel();

                // 关闭通道
                channel.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    // 转发消息给其他客户端（通道）
    private void sendInfoToOtherClients(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中...");

        // 遍历所有注册到 selector 上的 SocketChannel，并排除 self
        for (SelectionKey key : selector.keys()) {

            // 通过 key 取出对应的 SocketChannel
            Channel targetChannel = key.channel();

            // 排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != self) {

                // 转型
                SocketChannel dest = (SocketChannel) targetChannel;

                // 将 msg 存储到 buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());

                // 将 buffer 的数据写入通道
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        GroupChatServer server = new GroupChatServer();

        server.listen();
    }
}
