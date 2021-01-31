package com.holiday.matcloud.common;

import java.io.Serializable;

/**
 * rpc调用  网络通信的具体请求对象传送内容
 */
public class RpcRequest extends RpcPacket implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String serviceVersion; //服务版本
	private String className; //类的全限定名
	private String methodName; //方法名
	private Object[] params; //参数
	private Class<?>[] parameterTypes; //参数的类对象class
	
	public String getServiceVersion() {
		return serviceVersion;
	}
	public void setServiceVersion(String serviceVersion) {
		this.serviceVersion = serviceVersion;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public Object[] getParams() {
		return params;
	}
	public void setParams(Object[] params) {
		this.params = params;
	}
	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}
	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}
	@Override
	public Byte msgType() {
		// TODO Auto-generated method stub
		return MsgType.REQUEST;
	}	
}
