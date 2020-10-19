package com.dolly.nio;

import java.nio.ByteBuffer;

/**
 * @author yusenyang
 * @create 2020/10/19 20:11
 */
public class ReadOnlyBuffer {
    public static void main(String[] args) {

        ByteBuffer byteBuffer = ByteBuffer.allocate(64);

        for (int i = 0; i < 64; i++) {
            byteBuffer.put((byte) i);
        }

        byteBuffer.flip();

        // 得到一个只读的 Buffer
        ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();

        // 读取
        while (readOnlyBuffer.hasRemaining()) {
            System.out.println(readOnlyBuffer.get());
        }

        // java.nio.ReadOnlyBufferException
        readOnlyBuffer.put((byte) 100);
    }
}
