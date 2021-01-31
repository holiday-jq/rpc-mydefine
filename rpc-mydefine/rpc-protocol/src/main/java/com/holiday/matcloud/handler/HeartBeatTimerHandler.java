package com.holiday.matcloud.handler;

import java.util.concurrent.TimeUnit;

import com.holiday.matcloud.protocol.MsgHeader;
import com.holiday.matcloud.protocol.ProtocolConstants;
import com.holiday.matcloud.protocol.RpcProtocolPacket;
import com.holiday.matcloud.common.MsgType;
import com.holiday.matcloud.common.RpcHeartBeatRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class HeartBeatTimerHandler extends ChannelInboundHandlerAdapter {
	
	 @Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		scheduleSendHeartBeat(ctx);
	}
	 
	public void scheduleSendHeartBeat(ChannelHandlerContext ctx) {
		ctx.executor().schedule(() -> {
			 if (ctx.channel().isActive()) {
				 RpcProtocolPacket<RpcHeartBeatRequest> heartBean = new RpcProtocolPacket<>();
				 MsgHeader msgHeader = new MsgHeader();
				 msgHeader.setMagic(ProtocolConstants.MAGIC_NUMBER);//魔数
				 msgHeader.setVersion((byte)1); //协议版本 目前没有校验
				 msgHeader.setSerialization((byte)1); //json序列化号为1
				 msgHeader.setMsgType(MsgType.HEART_BEANT_REQUEST); //请求报文类型  -->心跳类型
				 msgHeader.setStatus((byte) 0x1); //状态
				 msgHeader.setRequestId(1); //消息ID  
				 
				 heartBean.setHeader(msgHeader);
				 heartBean.setBody(new RpcHeartBeatRequest());
				 
				 ctx.channel().writeAndFlush(heartBean);
				 scheduleSendHeartBeat(ctx);
			 }
		 }, 30, TimeUnit.SECONDS);
	}
    
}
