package com.dolly.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 说明：
 *  1、我们自定义一个 Handler 需要继承 Netty 规定好的某个 HandlerAdapter
 *  2、这时我们自定义的 Handler，才能称为一个 Handler
 *
 * @author yusenyang
 * @create 2020/10/23 00:43
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取数据 -> 客户端发送的数据
     *
     * @param ctx 上下文对象，含有 pipeline、channel、地址
     * @param msg 客户端发送的数据，默认 Object
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server ctx = " + ctx);

        // 将 msg 转成一个 ByteBuf -> Netty 提供，不是 NIO 的 ByteBuffer
        ByteBuf buf = (ByteBuf) msg;

        System.out.println("客户端发送消息是：" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址：" + ctx.channel().remoteAddress());
    }

    /**
     * 数据读取完毕
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        // write + flush -> 将数据写入到缓冲，并刷新
        // 一般我们对这个发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello Netty!", CharsetUtil.UTF_8));
    }

    /**
     * 处理异常，一般是需要关闭通道
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
