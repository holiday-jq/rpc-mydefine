package com.holiday.matcloud.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.holiday.matcloud.common.ServiceMeta;
import com.holiday.matcloud.registry.RegistryFactory;
import com.holiday.matcloud.registry.RegistryService;

public class RegistryTest {

	private RegistryService registryService;

	@Before
	public void init() throws Exception {
		registryService = RegistryFactory.getInstance("192.168.0.111:2181", "zookeeper");
	}

	@After
	public void close() throws Exception {
		registryService.destroy();
	}

	@Test
	public void testAll() throws Exception {
//		ServiceMeta serviceMeta1 = new ServiceMeta();
//		serviceMeta1.setServiceAddress("127.0.0.1");
//		serviceMeta1.setServicePort(8080);
//		serviceMeta1.setServiceName("test1");
//		serviceMeta1.setServiceVersion("1.0.0");
//
//		ServiceMeta serviceMeta2 = new ServiceMeta();
//		serviceMeta2.setServiceAddress("127.0.0.2");
//		serviceMeta2.setServicePort(8080);
//		serviceMeta2.setServiceName("test2");
//		serviceMeta2.setServiceVersion("1.0.0");
//
//		ServiceMeta serviceMeta3 = new ServiceMeta();
//		serviceMeta3.setServiceAddress("127.0.0.3");
//		serviceMeta3.setServicePort(8080);
//		serviceMeta3.setServiceName("test3");
//		serviceMeta3.setServiceVersion("1.0.0");
//
//		registryService.register(serviceMeta1);
//		registryService.register(serviceMeta2);
//		registryService.register(serviceMeta3);

//		ServiceMeta discovery1 = registryService.discovery("test1#1.0.0", "test1".hashCode());
//		ServiceMeta discovery2 = registryService.discovery("test2#1.0.0", "test2".hashCode());
//		ServiceMeta discovery3 = registryService.discovery("test3#1.0.0", "test3".hashCode());
//
//		System.err.println(discovery1.getServiceName());
//		System.err.println(discovery2.getServiceName());
//		System.err.println(discovery3.getServiceName());

		// 无论是否unRegister 连接关闭数据会清空
//	        registryService.unRegister(discovery1);
//	        registryService.unRegister(discovery2);
//	        registryService.unRegister(discovery3);
		ServiceMeta discovery1 = registryService.discovery("com.holiday.matcloud.service.RpcHelloService#1.0.0", "test1".hashCode());
		System.err.println(discovery1.getServiceName());
	}

}
