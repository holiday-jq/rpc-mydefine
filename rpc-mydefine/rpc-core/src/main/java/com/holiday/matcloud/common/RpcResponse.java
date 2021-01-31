package com.holiday.matcloud.common;

import java.io.Serializable;

/**
 * rpc调用请求返回对象
 *
 */
public class RpcResponse extends RpcPacket implements Serializable{

	private static final long serialVersionUID = 1L;

	private Object data;
	
	private String message;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public Byte msgType() {
		// TODO Auto-generated method stub
		return MsgType.RESPONSE;
	}	
}
