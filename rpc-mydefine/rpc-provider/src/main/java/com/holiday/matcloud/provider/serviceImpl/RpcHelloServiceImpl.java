package com.holiday.matcloud.provider.serviceImpl;

import com.holiday.matcloud.provider.annotation.RpcService;
import com.holiday.matcloud.service.RpcHelloService;

@RpcService(serviceInterface = RpcHelloService.class, serviceVersion = "1.0.0")
public class RpcHelloServiceImpl implements RpcHelloService{

	@Override
	public String helloRpc(String result) {
		// TODO Auto-generated method stub
		return "rpc success !!!"+ result;
	}
	
		
}
