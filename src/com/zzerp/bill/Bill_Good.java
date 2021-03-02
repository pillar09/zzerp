package com.zzerp.bill;

import com.zzerp.good.Good;

public class Bill_Good {
	private int id;
	private int bill_id;
	private Good good;
	private int quantity;
	private double price;
	private Integer reserve;
	private Integer ordered;
	
	public Bill_Good(){
		good = new Good();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBill_id() {
		return bill_id;
	}

	public void setBill_id(int bill_id) {
		this.bill_id = bill_id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getSum() {
		return price * quantity;
	}

	public Good getGood() {
		return good;
	}

	public void setGood(Good good) {
		this.good = good;
	}

	public int getOrdered() {
		return ordered;
	}

	public void setOrdered(Integer ordered) {
		this.ordered = ordered;
	}

	public Integer getReserve() {
		return reserve;
	}

	public void setReserve(Integer reserve) {
		this.reserve = reserve;
	}

}
