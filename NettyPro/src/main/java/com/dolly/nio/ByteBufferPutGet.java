package com.dolly.nio;

import java.nio.ByteBuffer;

/**
 * @author yusenyang
 * @create 2020/10/19 20:08
 */
public class ByteBufferPutGet {
    public static void main(String[] args) {

        ByteBuffer byteBuffer = ByteBuffer.allocate(64);

        byteBuffer.putInt(100);
        byteBuffer.putLong(9);
        byteBuffer.putChar('余');
        byteBuffer.putShort((short) 4);

        byteBuffer.flip();

        // java.nio.BufferUnderflowException
        System.out.println(byteBuffer.getLong());
        System.out.println(byteBuffer.getLong());
        System.out.println(byteBuffer.getLong());
        System.out.println(byteBuffer.getLong());
    }
}
