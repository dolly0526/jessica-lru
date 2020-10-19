package com.dolly.nio.buffer;

import java.nio.IntBuffer;

/**
 * 举例说明 Buffer 的使用
 *
 * @author yusenyang
 * @create 2020/10/16 14:07
 */
public class BasicBuffer {
    public static void main(String[] args) {

        // 创建一个 Buffer，大小为5
        IntBuffer intBuffer = IntBuffer.allocate(5);

        // 向 buffer 中存放数据
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i * 2);
        }

        // 将 buffer 转换，读写切换！！
        intBuffer.flip();

        // 从 buffer 中读取数据
        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
    }
}
