package com.holiday.matcloud.protocol;

import java.io.Serializable;
   /*
    +---------------------------------------------------------------+
    | 魔数 2byte | 协议版本号 1byte | 序列化算法 1byte | 报文类型 1byte  |
    +---------------------------------------------------------------+
    | 状态 1byte |        消息 ID 8byte     |      数据长度 4byte     |
    +---------------------------------------------------------------+
    */
public class MsgHeader implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int magic; //魔数
	private byte version;//协议版本号
	private byte serialization; //序列化算法
	private byte msgType; // 报文类型
    private byte status; // 状态
    private long requestId; // 消息 ID
    private int msgLen; // 数据长度
    
	public int getMagic() {
		return magic;
	}
	public void setMagic(int magic) {
		this.magic = magic;
	}
	public byte getVersion() {
		return version;
	}
	public void setVersion(byte version) {
		this.version = version;
	}
	public byte getSerialization() {
		return serialization;
	}
	public void setSerialization(byte serialization) {
		this.serialization = serialization;
	}
	public byte getMsgType() {
		return msgType;
	}
	public void setMsgType(byte msgType) {
		this.msgType = msgType;
	}
	public byte getStatus() {
		return status;
	}
	public void setStatus(byte status) {
		this.status = status;
	}
	public long getRequestId() {
		return requestId;
	}
	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}
	public int getMsgLen() {
		return msgLen;
	}
	public void setMsgLen(int msgLen) {
		this.msgLen = msgLen;
	}    
}
