package com.zzerp.good;

public class Warehousing_Good {
	int id;
	int warehousing_id;
	Good good;
	int quantity;
	private Integer reserve;
	private Integer ordered;
	
	public Warehousing_Good(){
		good = new Good();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWarehousing_id() {
		return warehousing_id;
	}

	public void setWarehousing_id(int warehousing_id) {
		this.warehousing_id = warehousing_id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Good getGood() {
		return good;
	}

	public void setGood(Good good) {
		this.good = good;
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

}
