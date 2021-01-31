package com.holiday.matcloud.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import com.holiday.matcloud.common.RpcServiceUtils;
import com.holiday.matcloud.common.ServiceMeta;
import com.holiday.matcloud.handler.HeartBeanRequestPacketHandler;
import com.holiday.matcloud.handler.IMIdleStateHandler;
import com.holiday.matcloud.handler.RpcRequestHandler;
import com.holiday.matcloud.protocol.coder.ProtocolPacketDecoder;
import com.holiday.matcloud.protocol.coder.ProtocolPacketEncoder;
import com.holiday.matcloud.protocol.coder.Spliter;
import com.holiday.matcloud.provider.annotation.RpcService;
import com.holiday.matcloud.registry.RegistryFactory;
import com.holiday.matcloud.registry.RegistryService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

@Component
public class ProviderBeanPostProcessor implements EnvironmentAware, InitializingBean, BeanPostProcessor{
	
    private final Logger Log = LoggerFactory.getLogger(getClass());

	private Environment environment;
	
	private RegistryService registryService;
	
	private final String address = RpcServiceUtils.getLocalAddress();
	
	private int servicePort;
	
	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
         String type = environment.getProperty("rpc.registryType", "zookeeper");
         String addressPort = environment.getProperty("rpc.registryAddr");
         servicePort = environment.getProperty("rpc.nettyServicePort", Integer.class);;
         registryService = RegistryFactory.getInstance(addressPort, type); 
	     new Thread( () -> {
	    	 startRpcServer();
	     }).start(); ;
	}
	
	public void startRpcServer() {
		  EventLoopGroup boss = new NioEventLoopGroup();
	      EventLoopGroup worker = new NioEventLoopGroup();
	      try {
	            ServerBootstrap bootstrap = new ServerBootstrap();
	            bootstrap.group(boss, worker)
	            .channel(NioServerSocketChannel.class)
	            .childOption(ChannelOption.SO_KEEPALIVE, true)
	            .childHandler(new ChannelInitializer<NioSocketChannel>() {

					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						// TODO Auto-generated method stub
						ch.pipeline().addLast(new Spliter());
						ch.pipeline().addLast(new ProtocolPacketDecoder());
						ch.pipeline().addLast(RpcRequestHandler.INSTANCE);
						ch.pipeline().addLast(new ProtocolPacketEncoder());
					}
				});
	            ChannelFuture channelFuture = bootstrap.bind(this.address, this.servicePort).sync();
	            Log.info("server addr {} started on port {}", this.address, this.servicePort);
	            channelFuture.channel().closeFuture().sync();
	      } catch (Exception e) {
			    Log.error(e.getMessage());
		  } finally {
			    boss.shutdownGracefully();
	            worker.shutdownGracefully();
		  }
	}
	
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
		// 判断bean上 有没有@rpcService
		if (rpcService != null) {
			String serviceInterfaceName = rpcService.serviceInterface().getName();
			String serviceVersion = rpcService.serviceVersion();
			/**
			 * 构建服务元数据
			 */
			ServiceMeta serviceMeta = new ServiceMeta();
			serviceMeta.setServiceName(serviceInterfaceName);
			serviceMeta.setServiceVersion(serviceVersion);
			//服务发布端口
			serviceMeta.setServicePort(servicePort);
			//机器地址
			serviceMeta.setServiceAddress(address);
			
			try {
				registryService.register(serviceMeta);
				RpcServiceUtils.storeService(RpcServiceUtils.createServiceKey(
						serviceMeta.getServiceName(), 
						serviceMeta.getServiceVersion()),
				        bean);
			} catch (Exception e) {
				Log.error("failed to register service"+e.getMessage());
			}
		}
		return bean;
	}
	
}
