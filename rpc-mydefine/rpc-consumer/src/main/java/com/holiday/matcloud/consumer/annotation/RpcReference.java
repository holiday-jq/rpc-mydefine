package com.holiday.matcloud.consumer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.annotation.Autowired;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) //作用目标字段
@Autowired
public @interface RpcReference {
    //调用的版本
	String serviceVersion() default "1.0.0";
	//注册中心的类型   可以是zookeeper  eureka
	String registryType() default "zookeeper";
	//注册中心地址
	String registryAddress() default "127.0.0.1:2181";
	//超时时间
	long timeout() default 5000;
	//扩展 --》重试次数	
}
