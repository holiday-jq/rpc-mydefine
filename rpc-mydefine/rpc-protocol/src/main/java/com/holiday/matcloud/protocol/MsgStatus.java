package com.holiday.matcloud.protocol;

public enum MsgStatus {
	
    SUCCESS(0),
    FAIL(1);

    private final int code;

    MsgStatus(int code) {
        this.code = code;
    }

	public int getCode() {
		return code;
	}
}
