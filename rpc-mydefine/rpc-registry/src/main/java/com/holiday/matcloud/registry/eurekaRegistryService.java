package com.holiday.matcloud.registry;

import com.holiday.matcloud.common.ServiceMeta;
/**
 * 待扩展
 *
 */
public class eurekaRegistryService implements RegistryService{

	@Override
	public void register(ServiceMeta meta) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unRegister(ServiceMeta meta) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ServiceMeta discovery(String serviceName, int invokerHashCode) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
