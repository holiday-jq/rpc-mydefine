package com.holiday.matcloud.consumer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import com.holiday.matcloud.common.MsgType;
import com.holiday.matcloud.common.RpcFuture;
import com.holiday.matcloud.common.RpcRequest;
import com.holiday.matcloud.common.RpcRequestHolder;
import com.holiday.matcloud.common.RpcResponse;
import com.holiday.matcloud.protocol.MsgHeader;
import com.holiday.matcloud.protocol.ProtocolConstants;
import com.holiday.matcloud.protocol.RpcProtocolPacket;
import com.holiday.matcloud.registry.RegistryService;

import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;

public class RpcInvokerProxy implements InvocationHandler {

	private final String serviceVersion;
	private final long timeout;
	private final RegistryService registryService;
	
	public RpcInvokerProxy(String serviceVersion, long timeout, RegistryService registryService) {
		this.serviceVersion = serviceVersion;
		this.timeout = timeout;
		this.registryService = registryService;
	}
	
    //动态代理增强方法 屏蔽远程调用细节
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		RpcProtocolPacket<RpcRequest> protocol = new RpcProtocolPacket<>();
		Long requestId = RpcRequestHolder.REQUEST_ID_GEN.incrementAndGet();
		
		MsgHeader msgHeader = new MsgHeader();
		msgHeader.setMagic(ProtocolConstants.MAGIC_NUMBER);//魔数
		msgHeader.setVersion((byte)1); //协议版本 目前没有校验
		msgHeader.setSerialization((byte)1); //json序列化号为1
		msgHeader.setMsgType(MsgType.REQUEST); //请求报文类型
		msgHeader.setStatus((byte) 0x1); //状态
		msgHeader.setRequestId(requestId); //消息ID  
		//msgHeader.setMsgLen();长度会在encode()出站设置
			
		RpcRequest requestBody = new RpcRequest();
		requestBody.setServiceVersion(serviceVersion); //调用版本
		requestBody.setClassName(method.getDeclaringClass().getName()); //className
		requestBody.setMethodName(method.getName()); //方法名称
		requestBody.setParameterTypes(method.getParameterTypes()); //参数类型
		requestBody.setParams(args);
		
		protocol.setHeader(msgHeader);
		protocol.setBody(requestBody);
		ConsumerClient.sendRequest(protocol, registryService);
		RpcFuture<RpcResponse> future = new RpcFuture<>(new DefaultPromise<>(new DefaultEventLoop()), timeout);
		RpcRequestHolder.REQUEST_MAP.put(requestId, future);
		return future.getPromise().get(future.getTimeout(), TimeUnit.MILLISECONDS).getData();
	}

}
