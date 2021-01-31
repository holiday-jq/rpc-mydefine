package com.holiday.matcloud.handler;

import com.holiday.matcloud.common.MsgType;
import com.holiday.matcloud.common.RpcHeartBeanResponse;
import com.holiday.matcloud.common.RpcHeartBeatRequest;
import com.holiday.matcloud.protocol.MsgHeader;
import com.holiday.matcloud.protocol.ProtocolConstants;
import com.holiday.matcloud.protocol.RpcProtocolPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
/**
 * 本来想加空闲检查机制和 心跳发送
 * 遇到了坑--》SimpleChannelInboundHandler<RpcProtocolPacket<RpcHeartBeatRequest>>
 * SimpleChannelInboundHandler上这个泛型 RpcProtocolPacket<RpcHeartBeatRequest>
 * 前置知识java的泛型 是类型擦除式泛型  经过javac编辑后转成 裸类型
 *  RpcProtocolPacket<RpcHeartBeatRequest>和 RpcProtocolPacket<RpcRequest> 这种泛型
 *  编译后 都是RpcProtocolPacket
 *  
 *  然后SimpleChannelInboundHandler 分辨不出来  我传RpcProtocolPacket<RpcRequest>这种类型的时候
 *  在 接收RpcProtocolPacket<RpcHeartBeatRequest>的handler也能接收到
 *  
 *  要解决这个问题  就不要传RpcProtocolPacket<T>这些格式     直接传不加泛型  定义另一种格式
 */
@Sharable
public class HeartBeanRequestPacketHandler extends SimpleChannelInboundHandler<RpcProtocolPacket<RpcHeartBeatRequest>> {

	public static HeartBeanRequestPacketHandler INSTANCE = new HeartBeanRequestPacketHandler();

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcProtocolPacket<RpcHeartBeatRequest> rpcProtocolPacket)
			throws Exception {
		System.err.println(rpcProtocolPacket);
		
		RpcProtocolPacket<RpcHeartBeanResponse> heartBeanResponse = new RpcProtocolPacket<>();
		MsgHeader msgHeader = new MsgHeader();
		msgHeader.setMagic(ProtocolConstants.MAGIC_NUMBER);// 魔数
		msgHeader.setVersion((byte) 1); // 协议版本 目前没有校验
		msgHeader.setSerialization((byte) 1); // json序列化号为1
		msgHeader.setMsgType(MsgType.HEART_BEANT_RESPONSE); // 请求报文类型 -->心跳类型
		msgHeader.setStatus((byte) 0x1); // 状态
		msgHeader.setRequestId(1); // 消息ID

		heartBeanResponse.setHeader(msgHeader);
		heartBeanResponse.setBody(new RpcHeartBeanResponse());

		ctx.channel().writeAndFlush(heartBeanResponse);
	}

}
