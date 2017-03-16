package com.lonfe.processor;import io.netty.bootstrap.ServerBootstrap;import io.netty.channel.*;import io.netty.channel.nio.NioEventLoopGroup;import io.netty.channel.socket.SocketChannel;import io.netty.channel.socket.nio.NioServerSocketChannel;import io.netty.handler.codec.LengthFieldBasedFrameDecoder;import io.netty.handler.codec.LengthFieldPrepender;import io.netty.handler.codec.serialization.ClassResolvers;import io.netty.handler.codec.serialization.ObjectDecoder;import io.netty.handler.codec.serialization.ObjectEncoder;import org.springframework.context.support.ClassPathXmlApplicationContext;public class Server {	public static void start() throws Exception {		EventLoopGroup bossGroup = new NioEventLoopGroup();		EventLoopGroup workerGroup = new NioEventLoopGroup();		new ClassPathXmlApplicationContext("application.xml");		ServiceExportor.export();		try {			ServerBootstrap serverBootstrap = new ServerBootstrap().group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)					.localAddress(8088).childHandler(new ChannelInitializer<SocketChannel>() {						@Override						protected void initChannel(SocketChannel ch) throws Exception {							ChannelPipeline pipeline = ch.pipeline();							pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));							pipeline.addLast(new LengthFieldPrepender(4));							pipeline.addLast("encoder", new ObjectEncoder());							pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));							pipeline.addLast(new InvokerHandler());						}					}).option(ChannelOption.SO_BACKLOG, 128)					.childOption(ChannelOption.SO_KEEPALIVE, true);			ChannelFuture future = serverBootstrap.bind(8088).sync();			System.out.println("Server start listen at " + 8088);			future.channel().closeFuture().sync();		} catch (Exception e) {			bossGroup.shutdownGracefully();			workerGroup.shutdownGracefully();		}	}	public static void main(String[] args) throws Exception {		start();	}}