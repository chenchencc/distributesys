package com.jason.netty.demo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by jasoncc on 25/04/17.
 */
public class JServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("接受到消息了:"+msg);
        ctx.writeAndFlush("能不能好使啊");
    }
}
