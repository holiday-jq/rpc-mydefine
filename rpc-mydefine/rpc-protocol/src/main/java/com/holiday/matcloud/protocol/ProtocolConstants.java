package com.holiday.matcloud.protocol;

import java.util.HashMap;
import java.util.Map;

import com.holiday.matcloud.common.MsgType;
import com.holiday.matcloud.common.RpcHeartBeanResponse;
import com.holiday.matcloud.common.RpcHeartBeatRequest;
import com.holiday.matcloud.common.RpcPacket;
import com.holiday.matcloud.common.RpcRequest;
import com.holiday.matcloud.common.RpcResponse;

public class ProtocolConstants {
	// 固定魔数
	public static final int MAGIC_NUMBER = 0x12345678;

	public static Map<Byte, Class<? extends RpcPacket>> packetTypeMap;

	static {
		packetTypeMap = new HashMap<>();
		packetTypeMap.put(MsgType.REQUEST, RpcRequest.class);
		packetTypeMap.put(MsgType.RESPONSE, RpcResponse.class);
		packetTypeMap.put(MsgType.HEART_BEANT_REQUEST, RpcHeartBeatRequest.class);
		packetTypeMap.put(MsgType.HEART_BEANT_RESPONSE, RpcHeartBeanResponse.class);
	}

	public static Class<? extends RpcPacket> getPacketTypeMap(byte type) {
		return packetTypeMap.get(type);
	}
}
