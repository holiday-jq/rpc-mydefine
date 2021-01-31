package com.holiday.matcloud.serialization.impl;

import com.alibaba.fastjson.JSON;
import com.holiday.matcloud.serialization.Serializer;
import com.holiday.matcloud.serialization.SerializerAlogrithm;

public class JSONSerializer implements Serializer{

	@Override
	public byte getSerializerAlogrithm() {
		// TODO Auto-generated method stub
		return SerializerAlogrithm.JSON;
	}

	@Override
	public byte[] serialize(Object object) {
		// TODO Auto-generated method stub
		return JSON.toJSONBytes(object);
	}

	@Override
	public <T> T deserialize(Class<T> clazz, byte[] bytes) {
		// TODO Auto-generated method stub
		return JSON.parseObject(bytes, clazz);
	}

}
