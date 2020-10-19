package com.dolly.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author yusenyang
 * @create 2020/10/19 19:34
 */
public class FileChannel04 {
    public static void main(String[] args) throws IOException {

        // 创建相关的流
        FileInputStream fileInputStream = new FileInputStream("/Users/sgcx017/github/spring-boot-helloworld/NettyPro/src/main/resources/file01.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("/Users/sgcx017/github/spring-boot-helloworld/NettyPro/src/main/resources/file03.txt");

        // 获取各个流对应的 fileChannel
        FileChannel fisChannel = fileInputStream.getChannel();
        FileChannel fosChannel = fileOutputStream.getChannel();

        // 使用 transferFrom 完成拷贝
        fosChannel.transferFrom(fisChannel, 0, fisChannel.size());

        // 关闭资源
        fileInputStream.close();
        fileOutputStream.close();
    }
}
