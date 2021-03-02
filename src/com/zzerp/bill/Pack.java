package com.zzerp.bill;

import java.sql.Timestamp;

public class Pack {
	Integer bill_id;
	String bill_num;
	Float length;
	Float width;
	Float height;
	Float weight;
	Integer volume_rate;
	Integer id;
	Double volume_weight;
	String barcode;
	
	String status;//用于导入时显示页面信息
	
	private String deliver_with;
	private String waybill_num;
	private Double freight;
	private Integer state; //-1:删除; 1:草稿; //2:待发货; 3:发货; 9:已签收(售后);
	private String logistics;
	private Timestamp deliver_time;

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getLogistics() {
		return logistics;
	}

	public void setLogistics(String logistics) {
		this.logistics = logistics;
	}

	public Integer getBill_id() {
		return bill_id;
	}

	public void setBill_id(Integer bill_id) {
		this.bill_id = bill_id;
	}

	public Float getLength() {
		return length;
	}

	public void setLength(Float length) {
		this.length = length;
	}

	public Float getWidth() {
		return width;
	}

	public void setWidth(Float width) {
		this.width = width;
	}

	public Float getHeight() {
		return height;
	}

	public void setHeight(Float height) {
		this.height = height;
	}

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public Integer getVolume_rate() {
		return volume_rate;
	}

	public void setVolume_rate(Integer volume_rate) {
		this.volume_rate = volume_rate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getVolume_weight() {
		return volume_weight;
	}

	public void setVolume_weight(Double volume_weight) {
		this.volume_weight = volume_weight;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getDeliver_with() {
		return deliver_with;
	}

	public void setDeliver_with(String deliver_with) {
		this.deliver_with = deliver_with;
	}

	public String getWaybill_num() {
		return waybill_num;
	}

	public void setWaybill_num(String waybill_num) {
		this.waybill_num = waybill_num;
	}

	public Double getFreight() {
		return freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}

	public Timestamp getDeliver_time() {
		return deliver_time;
	}

	public void setDeliver_time(Timestamp deliver_time) {
		this.deliver_time = deliver_time;
	}

	public String getBill_num() {
		return bill_num;
	}

	public void setBill_num(String bill_num) {
		this.bill_num = bill_num;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		StringBuffer buf =new StringBuffer("运单号:"+barcode);
		buf.append(" 重量:"+weight);
		buf.append(" 体积:"+length+"x"+width+"x"+height);
		buf.append(" 快递:"+logistics);
		buf.append(" 运费:"+freight);
		buf.append(" 体积系数:"+volume_rate);
		return buf.toString();
		
	}
}
