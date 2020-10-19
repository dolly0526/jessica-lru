package com.dolly.nio.buffer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * MappedByteBuffer 可让文件直接在内存（堆外内存）修改，操作系统不需要拷贝一次
 *
 * @author yusenyang
 * @create 2020/10/19 20:25
 */
public class MappedByteBufferTest {
    public static void main(String[] args) throws IOException {

        RandomAccessFile randomAccessFile = new RandomAccessFile("/Users/sgcx017/github/spring-boot-helloworld/NettyPro/src/main/resources/file03.txt", "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();

        /**
         * 参数1：FileChannel.MapMode.READ_WRITE -> 使用读写模式
         * 参数2：0 -> 可以直接修改的起始位置
         * 参数3：5 -> 是映射到内存的大小（不是索引位置）
         */
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        mappedByteBuffer.put(0, (byte) 'H');
        mappedByteBuffer.put(3, (byte) '9');

        randomAccessFile.close();
    }
}
