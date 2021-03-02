package com.zzerp.bill;

public class Declaration {
	private Integer declaration_id;
	private Integer bill_id;
	private Integer quantity;
	private Double price;
	private Double total;
	private String declaration;
	private String currency;
	private String declaration_en;
	private String code;
	private Integer weight;
	

	public Integer getDeclaration_id() {
		return declaration_id;
	}

	public void setDeclaration_id(Integer declaration_id) {
		this.declaration_id = declaration_id;
	}

	public Integer getBill_id() {
		return bill_id;
	}

	public void setBill_id(Integer bill_id) {
		this.bill_id = bill_id;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public String getDeclaration() {
		return declaration;
	}

	public void setDeclaration(String declaration) {
		this.declaration = declaration;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getDeclaration_en() {
		return declaration_en;
	}

	public void setDeclaration_en(String declaration_en) {
		this.declaration_en = declaration_en;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
