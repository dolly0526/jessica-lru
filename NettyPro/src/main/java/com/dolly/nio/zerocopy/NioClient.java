package com.dolly.nio.zerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @author yusenyang
 * @create 2020/10/20 20:54
 */
public class NioClient {
    public static void main(String[] args) throws IOException {

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 7001));

        String fileName = "/Users/sgcx017/github/spring-boot-helloworld/NettyPro/src/main/resources/file01.txt";

        FileChannel fileChannel = new FileInputStream(fileName).getChannel();

        // 准备发送
        long startTime = System.currentTimeMillis();

        // 零拷贝
        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);

        System.out.println("发送的总字节数 = " + transferCount + "，耗时 = " + (System.currentTimeMillis() - startTime));

        // 关闭通道
        fileChannel.close();
    }
}
