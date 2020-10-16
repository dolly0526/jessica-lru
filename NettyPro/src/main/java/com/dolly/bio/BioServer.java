package com.dolly.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 思路：
 *  1、创建一个线程池
 *  2、如果有客户端连接，就创建一个线程，与之通讯（单独写一个方法）
 *
 * @author yusenyang
 * @create 2020/10/16 13:00
 */
public class BioServer {
    public static void main(String[] args) throws IOException {

        // 线程池机制
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

        // 创建 ServerSocket
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动了...");

        while (true) {
            System.out.println("线程id：" + Thread.currentThread().getId() + "，线程名称：" + Thread.currentThread().getName());

            // 监听，等待客户端连接
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端...");

            // 创建一个线程，与之通讯
            newCachedThreadPool.execute(new Runnable() {
                public void run() {

                    // 可以和客户端通讯
                    handler(socket);
                }
            });
        }
    }

    // 编写一个 handler 方法，和客户端通讯
    public static void handler(Socket socket) {
        byte[] bytes = new byte[1024];

        try {
            InputStream inputStream = socket.getInputStream();

            // 循环读取客户端发送的数据
            while (true) {

                System.out.println("线程id：" + Thread.currentThread().getId() + "，线程名称：" + Thread.currentThread().getName());
                int read = inputStream.read(bytes);

                if (read != -1) {
                    // 输出客户端发送的数据
                    System.out.println(new String(bytes, 0, read));

                } else {
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            System.out.println("关闭和client的连接...");

            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
