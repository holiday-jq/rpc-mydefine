package com.holiday.matcloud.registry;

/**
 * 单例 双重检验 
 *
 */
public class RegistryFactory {

	public static volatile RegistryService registryService;
	
	public static  RegistryService getInstance (String addressPort, String type) throws Exception {
		 if (registryService == null) {
			  synchronized (RegistryFactory.class) {
				   if (registryService == null) {
					    switch (type) {
						case "zookeeper":
							registryService = new ZookeeperRegistryService(addressPort);
							break;
						case "eureka":
							//todo扩展
							break;
						default:
							break;
						}
				   } 
			  }
		 }
		 return registryService;
	}
}
