package com.holiday.matcloud.common;

/**
 * 服务元数据信息  发送到注册中心上的
 */
public class ServiceMeta {
    /**
     * 服务名称
     */
	private String serviceName;
	/**
	 * 服务版本
	 */
	private String serviceVersion;
	
	/**
	 * 服务地址
	 */
	private String serviceAddress;
	
	/**
	 * 服务端口
	 */
	private int servicePort;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceVersion() {
		return serviceVersion;
	}

	public void setServiceVersion(String serviceVersion) {
		this.serviceVersion = serviceVersion;
	}

	public String getServiceAddress() {
		return serviceAddress;
	}

	public void setServiceAddress(String serviceAddress) {
		this.serviceAddress = serviceAddress;
	}

	public int getServicePort() {
		return servicePort;
	}

	public void setServicePort(int servicePort) {
		this.servicePort = servicePort;
	}

	@Override
	public String toString() {
		return "ServiceMeta [serviceName=" + serviceName + ", serviceVersion=" + serviceVersion + ", serviceAddress="
				+ serviceAddress + ", servicePort=" + servicePort + "]";
	}
				
}
