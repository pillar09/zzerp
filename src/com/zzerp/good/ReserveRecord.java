package com.zzerp.good;

import java.sql.Timestamp;

public class ReserveRecord {

	private int id;
	private Timestamp reserve_time;
	private int from_id;
	private int quantity;
	private Integer reserve;
	private Integer ordered;
	private Integer reserve_type;//1:发货，2:入库

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getReserve_time() {
		return reserve_time;
	}

	public void setReserve_time(Timestamp reserve_time) {
		this.reserve_time = reserve_time;
	}

	public int getFrom_id() {
		return from_id;
	}

	public void setFrom_id(int from_id) {
		this.from_id = from_id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Integer getReserve() {
		return reserve;
	}

	public void setReserve(Integer reserve) {
		this.reserve = reserve;
	}

	public Integer getOrdered() {
		return ordered;
	}

	public void setOrdered(Integer ordered) {
		this.ordered = ordered;
	}

	public Integer getReserve_type() {
		return reserve_type;
	}

	public void setReserve_type(Integer reserve_type) {
		this.reserve_type = reserve_type;
	}
}
