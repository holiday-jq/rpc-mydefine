package com.holiday.matcloud.consumer;

import com.holiday.matcloud.protocol.RpcProtocolPacket;
import com.holiday.matcloud.protocol.coder.ProtocolPacketDecoder;
import com.holiday.matcloud.protocol.coder.ProtocolPacketEncoder;
import com.holiday.matcloud.protocol.coder.Spliter;
import com.holiday.matcloud.registry.RegistryService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import com.holiday.matcloud.common.RpcRequest;
import com.holiday.matcloud.common.RpcServiceUtils;
import com.holiday.matcloud.common.ServiceMeta;
import com.holiday.matcloud.handler.RpcResponseHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

@Component
public class ConsumerClient implements InitializingBean {

	private static Bootstrap bootstrap;

	private static EventLoopGroup group;

	@Override
	public void afterPropertiesSet() throws Exception {
		bootstrap = new Bootstrap();
		group = new NioEventLoopGroup();

		bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<Channel>() {
			@Override
			protected void initChannel(Channel ch) {
				ch.pipeline().addLast(new Spliter());
				ch.pipeline().addLast(new ProtocolPacketDecoder());
				ch.pipeline().addLast(RpcResponseHandler.INSTANCE);
				ch.pipeline().addLast(new ProtocolPacketEncoder());
			}
		});
	}

	public static void sendRequest(RpcProtocolPacket<RpcRequest> protocol, RegistryService registryService)
			throws Exception {
		RpcRequest rpcRequest = protocol.getBody();
		Object[] params = rpcRequest.getParams();
		String serviceKey = RpcServiceUtils.createServiceKey(rpcRequest.getClassName(), rpcRequest.getServiceVersion());
		int invokerHashCode = params.length > 0 ? params[0].hashCode() : serviceKey.hashCode();
		ServiceMeta serviceMetadata = registryService.discovery(serviceKey, invokerHashCode);
		if (serviceMetadata != null) {
			ChannelFuture channelFuture = bootstrap
					.connect(serviceMetadata.getServiceAddress(), serviceMetadata.getServicePort()).sync();
			channelFuture.addListener(future -> {
				if (future.isSuccess()) {
					System.err.println("connect rpc server " + serviceMetadata.getServiceAddress() + " on port "
							+ serviceMetadata.getServicePort() + " success.");
				} else {
					System.err.println("connect rpc server " + serviceMetadata.getServiceAddress() + " on port "
							+ serviceMetadata.getServicePort() + " success.");
					future.cause().printStackTrace();
					group.shutdownGracefully();
				}
			});
			Channel channel = channelFuture.channel();
			channel.writeAndFlush(protocol);
		}
	}

}
