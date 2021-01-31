package com.holiday.matcloud.handler;

import java.lang.reflect.InvocationTargetException;
import org.springframework.cglib.reflect.FastClass;
import com.holiday.matcloud.common.MsgType;
import com.holiday.matcloud.common.RpcRequest;
import com.holiday.matcloud.common.RpcResponse;
import com.holiday.matcloud.common.RpcServiceUtils;
import com.holiday.matcloud.protocol.MsgHeader;
import com.holiday.matcloud.protocol.MsgStatus;
import com.holiday.matcloud.protocol.RpcProtocolPacket;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable
public class RpcRequestHandler extends SimpleChannelInboundHandler<RpcProtocolPacket<RpcRequest>>{

	public static RpcRequestHandler INSTANCE = new RpcRequestHandler();
		
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcProtocolPacket<RpcRequest> protocolPacket) {
		//反射调用 消费者传来的参数的调用结果
		Object result = null;
		try {
			result = handleRequest(protocolPacket.getBody());
			RpcProtocolPacket<RpcResponse> responseProtocol = new RpcProtocolPacket<RpcResponse>();
			RpcResponse rpcResponse = new RpcResponse();
			
			MsgHeader msgHeader = protocolPacket.getHeader();
			msgHeader.setMsgType(MsgType.RESPONSE);//报文类型
			msgHeader.setStatus((byte)MsgStatus.SUCCESS.getCode()); //报文状态
			
			rpcResponse.setData(result);
			rpcResponse.setMessage("调用成功！");
			
			responseProtocol.setHeader(msgHeader);
			responseProtocol.setBody(rpcResponse);
			ctx.channel().writeAndFlush(responseProtocol);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	private Object handleRequest(RpcRequest rpcRequest) throws InvocationTargetException {
		String serviceKey = RpcServiceUtils.createServiceKey
				(rpcRequest.getClassName(), rpcRequest.getServiceVersion());
		// 拿到接口实现类的bean  即服务提供者
		Object serviceBean = RpcServiceUtils.getServiceInfo(serviceKey);
		if (serviceBean == null) {
	        throw new RuntimeException(String.format("service not exist: %s:%s", rpcRequest.getClassName(), rpcRequest.getMethodName()));
	    }
		Class<?> serviceClass = serviceBean.getClass();
		String methodName = rpcRequest.getMethodName();
		Object []params = rpcRequest.getParams();
	    Class<?> []parameterTypes = rpcRequest.getParameterTypes();
	  
	    FastClass fastClass = FastClass.create(serviceClass);
        int methodIndex = fastClass.getIndex(methodName, parameterTypes);
		return fastClass.invoke(methodIndex, serviceBean, params);
	
	}

}
