package com.holiday.matcloud.protocol.coder;

import java.util.List;

import com.holiday.matcloud.common.RpcPacket;
import com.holiday.matcloud.protocol.MsgHeader;
import com.holiday.matcloud.protocol.ProtocolConstants;
import com.holiday.matcloud.protocol.RpcProtocolPacket;
import com.holiday.matcloud.serialization.Serializer;
import com.holiday.matcloud.serialization.impl.SerializationFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class ProtocolPacketDecoder extends ByteToMessageDecoder{

	public static final int HEADER_TOTAL_LEN = 20;
	
    /*
    +---------------------------------------------------------------+
    | 魔数 42byte | 协议版本号 1byte | 序列化算法 1byte | 报文类型 1byte  |
    +---------------------------------------------------------------+
    | 状态 1byte |        消息 ID 8byte     |      数据长度 4byte     |
    +---------------------------------------------------------------+
    |                   数据内容 （长度不定）                          |
    +---------------------------------------------------------------+
    */
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.markReaderIndex(); //标记 读指针
        //数据包 长度少于20 说明这个包不是 自定义协议的数据包
        if (in.readableBytes() < HEADER_TOTAL_LEN) {
            return;
        }
        int magic = in.readInt();
        byte version = in.readByte();
        byte SerializerAlogrithm = in.readByte(); //序列化算法
        byte msgType = in.readByte(); //报文类型
        byte msgStatus = in.readByte(); //状态
        long requestId = in.readLong(); //消息ID
        int msgLength = in.readInt(); //消息长度
        byte []data = new byte [msgLength];
        in.readBytes(data);
        Serializer serializer = SerializationFactory.getRpcSerialization(SerializerAlogrithm);
        MsgHeader msgHeader = new MsgHeader();
        msgHeader.setMagic(magic);;
        msgHeader.setVersion(version);
        msgHeader.setSerialization(SerializerAlogrithm);
        msgHeader.setMsgType(msgType);
        msgHeader.setStatus(msgStatus);
        msgHeader.setRequestId(requestId);
        msgHeader.setMsgLen(msgLength);
        
        Class<? extends RpcPacket> MsgClass = ProtocolConstants.getPacketTypeMap(msgType);
        RpcPacket packet = serializer.deserialize(MsgClass, data);
        
        RpcProtocolPacket<RpcPacket> message = new RpcProtocolPacket<>();
        message.setHeader(msgHeader);
        message.setBody(packet);
        out.add(message);
	}

}
