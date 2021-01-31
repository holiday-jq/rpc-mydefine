package com.holiday.matcloud.serialization.impl;

import com.holiday.matcloud.serialization.Serializer;

public class SerializationFactory {

	public static Serializer getRpcSerialization(byte serializationType) {
		SerializationTypeEnum typeEnum = SerializationTypeEnum.findByType(serializationType);

		switch (typeEnum) {
		case HESSIAN:
			return new HessianSerializer();
		case JSON:
			return new JSONSerializer();
		default:
			throw new IllegalArgumentException("serialization type is illegal, " + serializationType);
		}
	}
}
