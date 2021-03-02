package com.zzerp.core;


public class Changelog {
	String version;
	String[] items;
	String date;

	public Changelog(String version, String date,String[] items) {
		setVersion(version);
		setDate(date);
		setItems(items);
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String[] getItems() {
		return items;
	}

	public void setItems(String[] items) {
		this.items = items;
	}

}
