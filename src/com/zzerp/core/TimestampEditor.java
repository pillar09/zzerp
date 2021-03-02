package com.zzerp.core;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;

public class TimestampEditor extends PropertyEditorSupport {
	private String patten;

	public TimestampEditor(String string) {
		patten=string;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		Timestamp time = Timestamp.valueOf(text+" 00:00:00.0");
		setValue(time);
	}

}