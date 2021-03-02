package com.zzerp.good;

public class Good {
	private String num;
	private Category category;
	private double price;
	private String description;
	private String model;
	private double purchase_price;
	private String title;
	private int id;
	private int state;
	private String spec;
	private String factory_model;
	private int reserve;
	private int total_sold;
	private int ordered;
	
	public int getOrdered() {
		return ordered;
	}

	public void setOrdered(int ordered) {
		this.ordered = ordered;
	}

	public Good(){
		category  = new Category();
	}
	
	public int getReserve() {
		return reserve;
	}
	
	public void setReserve(int reserve) {
		this.reserve = reserve;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory_id(Category category) {
		this.category = category;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public double getPurchase_price() {
		return purchase_price;
	}

	public void setPurchase_price(double purchase_price) {
		this.purchase_price = purchase_price;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getFactory_model() {
		return factory_model;
	}

	public void setFactory_model(String factory_model) {
		this.factory_model = factory_model;
	}

	public int getTotal_sold() {
		return total_sold;
	}

	public void setTotal_sold(int total_sold) {
		this.total_sold = total_sold;
	}

}
