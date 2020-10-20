package com.dolly.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author yusenyang
 * @create 2020/10/20 17:18
 */
public class GroupChatClient {
    private final String HOST = "127.0.0.1";
    private final int PORT = 6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    public GroupChatClient() {
        try {
            // 得到 selector
            selector = Selector.open();

            // 连接服务器
            socketChannel = socketChannel.open(new InetSocketAddress(HOST, PORT));

            // 设置非阻塞模式
            socketChannel.configureBlocking(false);

            // 将 socketChannel 注册到 selector
            socketChannel.register(selector, SelectionKey.OP_READ);

            // 得到 username
            username = socketChannel.getLocalAddress().toString().substring(1);

            System.out.println(username + " is ok...");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 向服务器发送消息
    public void sendInfo(String info) {
        info = username + " 说：" + info;

        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 读取从服务器端回复的消息
    public void readInfo() {

        try {
            if (selector.select() > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    if (key.isReadable()) {

                        // 得到相关的通道
                        SocketChannel socketChannel = (SocketChannel) key.channel();

                        // 得到一个 Buffer
                        ByteBuffer buffer = ByteBuffer.allocate(1024);

                        // 读取
                        socketChannel.read(buffer);

                        // 把读到的缓冲区的数据转成字符串
                        String msg = new String(buffer.array());

                        System.out.println(msg.trim());
                    }

                    // 删除当前 selectionKey，防止重复操作
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GroupChatClient client = new GroupChatClient();

        // 启动一个线程，每隔3秒，读取从服务器发送的数据
        new Thread() {
            @Override
            public void run() {
                while (true) {

                    client.readInfo();

                    try {
                        Thread.currentThread().sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        // 发送数据给服务器端
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            client.sendInfo(scanner.nextLine());
        }
    }
}
