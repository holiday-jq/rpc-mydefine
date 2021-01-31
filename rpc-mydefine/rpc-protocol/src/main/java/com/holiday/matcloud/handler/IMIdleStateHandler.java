package com.holiday.matcloud.handler;

import java.util.concurrent.TimeUnit;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

public class IMIdleStateHandler extends IdleStateHandler{
		
	private static final int READER_IDLE_TIME = 60;
	
	/**
	 * 第一个参数表示读空闲时间
	 * 第二个参数表示写空闲时间
	 * 第三个参数表示读写空闲时间
	 * 第四个参数表示时间单位
	 */
	public IMIdleStateHandler() {
	     super(READER_IDLE_TIME, 0, 0, TimeUnit.SECONDS);
	}
	
	@Override
	protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
		// TODO Auto-generated method stub
        System.err.println(READER_IDLE_TIME + "秒内未读到数据，关闭连接");
		ctx.channel().close();
	}
}