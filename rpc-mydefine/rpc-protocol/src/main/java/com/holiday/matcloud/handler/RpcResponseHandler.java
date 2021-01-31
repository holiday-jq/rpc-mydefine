package com.holiday.matcloud.handler;

import com.holiday.matcloud.common.RpcFuture;
import com.holiday.matcloud.common.RpcRequestHolder;
import com.holiday.matcloud.common.RpcResponse;
import com.holiday.matcloud.protocol.MsgHeader;
import com.holiday.matcloud.protocol.RpcProtocolPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
public class RpcResponseHandler extends SimpleChannelInboundHandler<RpcProtocolPacket<RpcResponse>>{

	public static RpcResponseHandler INSTANCE = new RpcResponseHandler();
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcProtocolPacket<RpcResponse> rpcProtocolPacket) throws Exception {
		MsgHeader meHeader = rpcProtocolPacket.getHeader();
		Long requestId = meHeader.getRequestId();
		RpcFuture<RpcResponse> rpcFuture = RpcRequestHolder.REQUEST_MAP.remove(requestId);
		rpcFuture.getPromise().setSuccess(rpcProtocolPacket.getBody());
	}

}
