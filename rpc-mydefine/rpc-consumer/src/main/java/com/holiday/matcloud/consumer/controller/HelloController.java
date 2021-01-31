package com.holiday.matcloud.consumer.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.holiday.matcloud.consumer.annotation.RpcReference;
import com.holiday.matcloud.service.RpcHelloService;

@RestController
@RequestMapping("/rpc")
public class HelloController {
 
	@RpcReference(registryAddress = "192.168.0.111:2181", registryType = "zookeeper", 
			serviceVersion = "1.0.0", timeout = 5000)
	private RpcHelloService rpcHelloService; 
	
	@RequestMapping("/consumer")
	public String consumerRpc() {
		return rpcHelloService.helloRpc("test");
	}
	
}
