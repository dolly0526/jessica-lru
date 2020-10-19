package com.dolly.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author yusenyang
 * @create 2020/10/19 18:16
 */
public class FileChannel02 {
    public static void main(String[] args) throws IOException {

        // 创建文件的输入流
        File file = new File("/Users/sgcx017/github/spring-boot-helloworld/NettyPro/src/main/resources/file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        // 通过 fileInputStream 获取对应的 FileChannel -> FileChannelImpl
        FileChannel fileChannel = fileInputStream.getChannel();

        // 创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        // 将通道的数据读入到 byteBuffer
        fileChannel.read(byteBuffer);

        // 将 byteBuffer 的字节数据转成 String
        System.out.println(new String(byteBuffer.array()));

        // 关闭资源
        fileInputStream.close();
    }
}
