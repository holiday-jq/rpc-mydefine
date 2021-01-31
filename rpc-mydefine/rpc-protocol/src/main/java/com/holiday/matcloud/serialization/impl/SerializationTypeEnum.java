package com.holiday.matcloud.serialization.impl;

public enum SerializationTypeEnum {

	JSON(1), 
	HESSIAN(2);

	private int type;

	SerializationTypeEnum(int i) {
		this.type = i;
	}

	public static SerializationTypeEnum findByType(byte serializationType) {
		for (SerializationTypeEnum typeEnum : SerializationTypeEnum.values()) {
			if (typeEnum.getType() == serializationType) {
				return typeEnum;
			}
		}
		return JSON;
	}

	public int getType() {
		return type;
	}
}
