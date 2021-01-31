package com.holiday.matcloud.consumer;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;

import com.holiday.matcloud.registry.RegistryFactory;
import com.holiday.matcloud.registry.RegistryService;

public class RpcReferenceBean implements FactoryBean<Object>{

	private Class<?> interfaceClassType;
	
	private String serviceVersion;
	
	private String registryType;
	
	private String registryAddr;
	
	private long timeout;
		
	private Object object;
	
	@Override
	public Object getObject() throws Exception {
		// TODO Auto-generated method stub
		return object;
	}
	//生命周期 init方法
	public void init() throws Exception {
		RegistryService registryService = RegistryFactory.getInstance(this.registryAddr, this.registryType);
	    this.object = Proxy.newProxyInstance(
	    		interfaceClassType.getClassLoader(), 
	    		new Class[] {interfaceClassType},
	    		new RpcInvokerProxy(serviceVersion, timeout, registryService));
	}

	@Override
	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		return interfaceClassType;
	}
	
	public Class<?> getinterfaceClassType() {
		return interfaceClassType;
	}

	public void setinterfaceClassType(Class<?> interfaceClassType) {
		this.interfaceClassType = interfaceClassType;
	}

	public String getServiceVersion() {
		return serviceVersion;
	}

	public void setServiceVersion(String serviceVersion) {
		this.serviceVersion = serviceVersion;
	}

	public String getRegistryType() {
		return registryType;
	}

	public void setRegistryType(String registryType) {
		this.registryType = registryType;
	}

	public String getRegistryAddr() {
		return registryAddr;
	}

	public void setRegistryAddr(String registryAddr) {
		this.registryAddr = registryAddr;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
}
