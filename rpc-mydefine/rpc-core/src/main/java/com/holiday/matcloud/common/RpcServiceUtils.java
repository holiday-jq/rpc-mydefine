package com.holiday.matcloud.common;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RpcServiceUtils {
	
	public static Map<String, Object> rpcServiceMap = new ConcurrentHashMap<>();

	public static void storeService(String key, Object value) {
		rpcServiceMap.put(key, value);
	}

	public static Object getServiceInfo(String key) {
		return rpcServiceMap.get(key);
	}

	/**
	 * 用#号连接
	 * @param serviceName
	 * @param serviceVersion
	 * @return
	 */
	public static String createServiceKey(String serviceName, String serviceVersion) {
		return String.join("#", serviceName, serviceVersion);
	}
	
	/**
	 * 获取服务器Ip地址
	 * @return
	 */
	public static String getLocalAddress() {
		String address = "";
		try {
			 address = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return address;
	}
	
}
