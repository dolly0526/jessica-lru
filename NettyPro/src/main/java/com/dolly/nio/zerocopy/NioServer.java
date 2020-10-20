package com.dolly.nio.zerocopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author yusenyang
 * @create 2020/10/20 20:51
 */
public class NioServer {
    public static void main(String[] args) throws IOException {

        InetSocketAddress address = new InetSocketAddress(7001);

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        ServerSocket serverSocket = serverSocketChannel.socket();

        serverSocket.bind(address);

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();
            int readCount = 0;

            while (readCount != -1) {

                readCount = socketChannel.read(byteBuffer);

                // 倒带 -> position = 0，mark 作废
                byteBuffer.rewind();
            }
        }
    }
}
