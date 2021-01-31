package com.holiday.matcloud.common;

public class RpcHeartBeatRequest extends RpcPacket{

	@Override
	public Byte msgType() {
		// TODO Auto-generated method stub
		return MsgType.HEART_BEANT_REQUEST;
	}

}
