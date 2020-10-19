package com.dolly.nio.filechannel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author yusenyang
 * @create 2020/10/19 19:06
 */
public class FileChannel03 {
    public static void main(String[] args) throws IOException {

        // 输入流及管道
        FileInputStream fileInputStream = new FileInputStream("/Users/sgcx017/github/spring-boot-helloworld/NettyPro/src/main/resources/file01.txt");
        FileChannel fileChannel01 = fileInputStream.getChannel();

        // 输出流及管道
        FileOutputStream fileOutputStream = new FileOutputStream("/Users/sgcx017/github/spring-boot-helloworld/NettyPro/src/main/resources/file02.txt");
        FileChannel fileChannel02 = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(5);

        // 循环读取
        while (true) {

            // 这里有一个重要的方法，一定不要忘了 -> 清空 byteBuffer
            byteBuffer.clear();
            int read = fileChannel01.read(byteBuffer);

            if (read == -1) {
                break;
            }

            byteBuffer.flip();
            // 将 byteBuffer 中的数据写入到 fileChannel02
            fileChannel02.write(byteBuffer);
        }

        // 关闭资源
        fileInputStream.close();
        fileOutputStream.close();
    }
}
