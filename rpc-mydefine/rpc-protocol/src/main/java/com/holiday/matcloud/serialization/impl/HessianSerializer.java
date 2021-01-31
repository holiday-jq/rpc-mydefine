package com.holiday.matcloud.serialization.impl;

import com.holiday.matcloud.serialization.Serializer;
import com.holiday.matcloud.serialization.SerializerAlogrithm;

/**
 * 
 *todo
 *
 */
public class HessianSerializer implements Serializer{

	@Override
	public byte getSerializerAlogrithm() {
		// TODO Auto-generated method stub
		return SerializerAlogrithm.HESSIAN;
	}

	@Override
	public byte[] serialize(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T deserialize(Class<T> clazz, byte[] bytes) {
		// TODO Auto-generated method stub
		return null;
	}

}
