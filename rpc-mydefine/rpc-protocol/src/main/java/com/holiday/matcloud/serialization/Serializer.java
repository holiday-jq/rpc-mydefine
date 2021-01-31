package com.holiday.matcloud.serialization;

public interface Serializer {
	//具体序列化算法
	byte getSerializerAlogrithm();
	
	byte[] serialize(Object object);
	
	<T> T deserialize(Class<T> clazz, byte[] bytes);
}
