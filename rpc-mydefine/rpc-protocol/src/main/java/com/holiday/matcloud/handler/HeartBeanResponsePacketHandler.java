package com.holiday.matcloud.handler;

import com.holiday.matcloud.common.RpcHeartBeanResponse;
import com.holiday.matcloud.protocol.RpcProtocolPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
public class HeartBeanResponsePacketHandler extends SimpleChannelInboundHandler<RpcProtocolPacket<RpcHeartBeanResponse>>{

	public static HeartBeanResponsePacketHandler INSTANCE = new HeartBeanResponsePacketHandler();
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcProtocolPacket<RpcHeartBeanResponse> msg)
			throws Exception {
				
	}

}
