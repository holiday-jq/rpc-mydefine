package com.holiday.matcloud.registry;

import com.holiday.matcloud.common.ServiceMeta;

public interface RegistryService {
    /**
     * 注册服务
     * @param meta
     */
	void register(ServiceMeta meta) throws Exception;
	
	/**
	 * 注销服务
	 * @throws Exception
	 */
	void unRegister(ServiceMeta meta) throws Exception;
	
	/**
	 * 从注册中心获取服务
	 * @param serviceName
	 * @param invokerHashCode
	 * @return
	 * @throws Exception
	 */
	ServiceMeta discovery(String serviceName, int invokerHashCode) throws Exception;
	
	/**
	 * 销毁连接
	 * @throws Exception
	 */
	void destroy() throws Exception;
}
