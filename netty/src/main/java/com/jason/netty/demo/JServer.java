package com.jason.netty.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * Created by jasoncc on 25/04/17.
 */
public class JServer {

    public void  run(){
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
       try{
           ServerBootstrap strap = new ServerBootstrap();
           strap.group(boss,worker)
                   .channel(NioServerSocketChannel.class)
                   .localAddress(new InetSocketAddress(8087))
                   .option(ChannelOption.SO_BACKLOG,1024)
                   .childHandler(new ChannelInitializer<SocketChannel>() {
                       @Override
                       protected void initChannel(SocketChannel socketChannel) throws Exception {
                           socketChannel.pipeline().addLast("decoder", new StringDecoder());
                           socketChannel.pipeline().addLast("encoder", new StringEncoder());
                           socketChannel.pipeline().addLast(new JServerHandler());
                       }
                   }).childOption(ChannelOption.SO_KEEPALIVE,true);
           ChannelFuture future = strap.bind(8087).sync();
           future.channel().closeFuture().sync();
        }catch (Exception e){

       } finally {
           boss.shutdownGracefully();
           worker.shutdownGracefully();
       }
    }

    public static void main(String[] args) {
        new JServer().run();
    }
}
