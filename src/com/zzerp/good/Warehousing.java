package com.zzerp.good;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.zzerp.account.Account;

public class Warehousing {
	int id;
	Timestamp warehouse_date;
	Timestamp create_time;
	Account warehouser;
	String memo;
	String title;

	List<Warehousing_Good> wgList;
	
	public Warehousing(){
		warehouser = new Account();
		wgList = new ArrayList<Warehousing_Good>();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getWarehouse_date() {
		return warehouse_date;
	}

	public void setWarehouse_date(Timestamp warehouse_date) {
		this.warehouse_date = warehouse_date;
	}

	public Account getWarehouser() {
		return warehouser;
	}

	public void setWarehouser(Account warehouser) {
		this.warehouser = warehouser;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Timestamp getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}

	public List<Warehousing_Good> getWgList() {
		return wgList;
	}

	public void setWgList(List<Warehousing_Good> wgList) {
		this.wgList = wgList;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
