package com.dolly.nio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author yusenyang
 * @create 2020/10/19 14:09
 */
public class FileChannel01 {
    public static void main(String[] args) throws IOException {

        String str = "Hello World!";

        // 创建一个输出流 -> Channel
        FileOutputStream fileOutputStream = new FileOutputStream("/Users/sgcx017/github/spring-boot-helloworld/NettyPro/src/main/resources/file01.txt");

        // 通过 fileOutputStream 获取对应的 FileChannel -> FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();

        // 创建一个缓冲区 ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 将 str 放入 byteBuffer
        byteBuffer.put(str.getBytes());

        // 对 byteBuffer 进行 flip
        byteBuffer.flip();

        // 将 byteBuffer 的数据写入 fileChannel
        fileChannel.write(byteBuffer);

        // 关闭资源
        fileOutputStream.close();
    }
}
