package com.holiday.matcloud.protocol.coder;

import com.holiday.matcloud.protocol.ProtocolConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
/**
 * 基于长度域拆包器 LengthFieldBasedFrameDecoder
 * 基于 Netty 自带的拆包器，我们可以在拆包之前判断当前连上来的客户端是否是支持自定义协议的客户端，如果不支持，可尽早关闭，节省资源。
 *
 */
public class Spliter extends LengthFieldBasedFrameDecoder{
    /*
    +---------------------------------------------------------------+
    | 魔数 4byte | 协议版本号 1byte | 序列化算法 1byte | 报文类型 1byte  |
    +---------------------------------------------------------------+
    | 状态 1byte |        消息 ID 8byte     |      数据长度 4byte     |
    +---------------------------------------------------------------+
    |                   数据内容 （长度不定）                          |
    +---------------------------------------------------------------+
    */
	
	//专业术语来说就是长度域相对整个数据包的偏移量是多少   所以是 4 + 1 + 1 + 1 + 1 + 8 = 16   
	private static final int LENGTH_FIELD_OFFSET = 16;
	//长度域的长度是多少
	private static final int LENGTH_FIELD_LENGTH = 4;
    
	public Spliter() {
		//第一个参数指的是数据包的最大长度，第二个参数指的是长度域的偏移量，第三个参数指的是长度域的长度
		super(Integer.MAX_VALUE, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH);
	}
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		if (in.getInt(in.readerIndex()) != ProtocolConstants.MAGIC_NUMBER) {
			ctx.channel().close();
			System.err.println("有不符合自定义的数据包传递！");
			return null;
		}
		return super.decode(ctx, in);
	}

}

