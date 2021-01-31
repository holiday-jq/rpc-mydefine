package com.holiday.matcloud.protocol.coder;

import com.holiday.matcloud.protocol.MsgHeader;
import com.holiday.matcloud.protocol.RpcProtocolPacket;
import com.holiday.matcloud.serialization.Serializer;
import com.holiday.matcloud.serialization.impl.SerializationFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
/**
 * 对象转字节流 
 * 属于 Netty的 ChannelOutboundHandler  
 * 在channel出站的时候调用    例如调用了channel.writeandflush()方法
 * 
 */
public class ProtocolPacketEncoder extends MessageToByteEncoder<RpcProtocolPacket<Object>>{

    /*
    +---------------------------------------------------------------+
    | 魔数 4byte | 协议版本号 1byte | 序列化算法 1byte | 报文类型 1byte  |
    +---------------------------------------------------------------+
    | 状态 1byte |        消息 ID 8byte     |      数据长度 4byte     |
    +---------------------------------------------------------------+
    |                   数据内容 （长度不定）                          |
    +---------------------------------------------------------------+
    */
	@Override
	protected void encode(ChannelHandlerContext ctx, RpcProtocolPacket<Object> msg, ByteBuf out) throws Exception {
		// TODO Auto-generated method stub
		MsgHeader header = msg.getHeader();
		out.writeInt(header.getMagic()); //魔数
		out.writeByte(header.getVersion()); //自定义协议版本号
		out.writeByte(header.getSerialization());//序列化算法
		out.writeByte(header.getMsgType()); //报文类型
		out.writeByte(header.getStatus()); // 状态
		out.writeLong(header.getRequestId()); //消息ID
		Serializer serializer = SerializationFactory.getRpcSerialization(header.getSerialization());
		byte []data = serializer.serialize(msg.getBody());
		out.writeInt(data.length); //数据长度
		out.writeBytes(data);//序列化后数据字节流
	}

}
