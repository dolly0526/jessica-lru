package com.dolly.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Scattering：将数据写入到 buffer 时，可以采用 buffer 数组，依次写入
 * Gathering：从 buffer 读取数据时，可以采用 buffer 数组，依次读出
 *
 * @author yusenyang
 * @create 2020/10/19 20:35
 */
public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws IOException {

        // 使用 ServerSocketChannel 和 SocketChannel 网络
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        // 绑定端口到 socket，并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        // 创建 buffer 数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        // 等客户端连接（telnet）
        SocketChannel socketChannel = serverSocketChannel.accept();
        // 假定从客户端接收 8 个字节
        int msgLength = 8;

        // 循环读取
        while (true) {
            int byteRead = 0;

            while (byteRead < msgLength) {

                byteRead += socketChannel.read(byteBuffers);
                System.out.println("byteRead = " + byteRead);

                // 使用流打印，看看当前 buffer 的 position 和 limit
                Arrays.asList(byteBuffers).stream()
                        .map(buffer -> "position = " + buffer.position()
                                    + ", limit = " + buffer.limit())
                        .forEach(System.out::println);
            }

            // 将所有的 buffer 进行 flip
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.flip());

            // 将数据读出显示到客户端
            long byteWrite = 0;

            while (byteWrite < msgLength) {

                byteWrite += socketChannel.write(byteBuffers);
            }

            // 将所有的 buffer 进行 clear
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.clear());

            System.out.println("byteRead = " + byteRead
                            + ", byteWrite = " + byteWrite
                            + ", msgLength = " + msgLength);
        }
    }
}
