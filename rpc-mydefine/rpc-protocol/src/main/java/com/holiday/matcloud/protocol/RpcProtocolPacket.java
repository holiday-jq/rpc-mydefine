package com.holiday.matcloud.protocol;

import java.io.Serializable;

public class RpcProtocolPacket<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private MsgHeader header;
	
	private T body;

	public MsgHeader getHeader() {
		return header;
	}

	public void setHeader(MsgHeader header) {
		this.header = header;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}
}
