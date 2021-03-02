package com.zzerp.bill;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.zzerp.account.Account;

public class Bill implements Cloneable{

	public static final int STATE_DRAFT = 1;
	public static final int STATE_READY = 2;
	public static final int STATE_SENT = 3;
	public static final int STATE_SOLD = 9;
	
	public static final int STATE_DELETED = -1;
	public static final int STATE_ISSUE = -9;

	private String title;
	private String num;
	private int state; //0:未赋值；-1:删除; 1:草稿; //2:待发货; 3:发货; 9:已签收(售后);
	private Account create_user;
	private Double amount;
	private Account salesman;
	private Timestamp order_time;
	private Timestamp pay_time;
	private Timestamp accept_time;
	private Timestamp deliver_time;
	private Timestamp create_time;
	private Integer id;
	private String address;
	private String address2;
	private String consignee;
	private String zip_code;
	private String buyer_note;
	private String salesman_note;
	private String country;
	private Integer delay_limit;
	private String phone_num;
	private Double freight;
	private String buyer;
	private List<Bill_Good> bgList;
	private Map<Integer,Pack> pkList;
	private Map<Integer,Declaration> declareList;
	private String logistics;//快递代理

	private String pay_with;
	private String deliver_with;
	private String waybill_num;
	private boolean waybill_changed;

	private String province;
	private String tel_num;
	private String fax_num;
	private String city;
	private String link;
	private String mail;
	private String buyer_phone_num;
	private Integer issue;
	private Integer weight_declared;
	private Account consignor;
	
	private Timestamp limit_time;
	private Double amount_declared;
	private String declaration;
	private String expect_deliver_with;
	private String store_num;
	private Account last_edit_user;
	private String buyer_wangwang;
	private Float commission_rate;
	private Timestamp expected_arrival;
	private Timestamp edit_time;
	private String buyer_tel_num;
	private Float total_weight;
	private Float total_volume_weight;
	
	private Timestamp submit_time;

	public Bill() {
		create_user = new Account();
		salesman = new Account();
		consignor = new Account();
		last_edit_user = new Account();
		bgList = new ArrayList<Bill_Good>();
		pkList = new LinkedHashMap<Integer,Pack>(){
			private static final long serialVersionUID = 1L;

			@Override
			public Pack get(Object key) {
				if(null == super.get(key)){
					super.put((Integer)key,new Pack());
				}
				return super.get(key);
				
			};
			
		};
		declareList = new LinkedHashMap<Integer,Declaration>(){
			private static final long serialVersionUID = 1L;

			@Override
			public Declaration get(Object key) {
				if(null == super.get(key)){
					super.put((Integer)key,new Declaration());
				}
				return super.get(key);
				
			};
			
		};
	}

	public String getLogistics() {
		return logistics;
	}

	public void setLogistics(String logistics) {
		this.logistics = logistics;
	}

	public List<Bill_Good> getBgList() {
		return bgList;
	}

	public void setBgList(List<Bill_Good> bgList) {
		this.bgList = bgList;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Timestamp getOrder_time() {
		return order_time;
	}

	public Timestamp getPay_time() {
		return pay_time;
	}

	public void setPay_time(Timestamp pay_time) {
		this.pay_time = pay_time;
	}

	public Timestamp getAccept_time() {
		return accept_time;
	}

	public void setAccept_time(Timestamp accept_time) {
		this.accept_time = accept_time;
	}

	public Timestamp getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZip_code() {
		return zip_code;
	}

	public void setZip_code(String zip_code) {
		this.zip_code = zip_code;
	}

	public String getBuyer_note() {
		return buyer_note;
	}

	public void setBuyer_note(String buyer_note) {
		this.buyer_note = buyer_note;
	}

	public String getSalesman_note() {
		return salesman_note;
	}

	public void setSalesman_note(String salesman_note) {
		this.salesman_note = salesman_note;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Account getCreate_user() {
		return create_user;
	}

	public void setCreate_user(Account create_user) {
		this.create_user = create_user;
	}

	public Account getSalesman() {
		return salesman;
	}

	public void setSalesman(Account salesman) {
		this.salesman = salesman;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Integer getDelay_limit() {
		return delay_limit;
	}

	public void setDelay_limit(Integer delay_limit) {
		this.delay_limit = delay_limit;
	}

	public String getPhone_num() {
		return phone_num;
	}

	public void setPhone_num(String phone_num) {
		this.phone_num = phone_num;
	}

	public Double getFreight() {
		return freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public void setOrder_time(Timestamp order_time) {
		this.order_time = order_time;
	}

	public Timestamp getDeliver_time() {
		return deliver_time;
	}

	public void setDeliver_time(Timestamp deliver_time) {
		this.deliver_time = deliver_time;
	}

	public String getPay_with() {
		return pay_with;
	}

	public void setPay_with(String pay_with) {
		this.pay_with = pay_with;
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

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getTel_num() {
		return tel_num;
	}

	public void setTel_num(String tel_num) {
		this.tel_num = tel_num;
	}

	public String getFax_num() {
		return fax_num;
	}

	public void setFax_num(String fax_num) {
		this.fax_num = fax_num;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getBuyer_phone_num() {
		return buyer_phone_num;
	}

	public void setBuyer_phone_num(String buyer_phone_num) {
		this.buyer_phone_num = buyer_phone_num;
	}

	public Account getConsignor() {
		return consignor;
	}

	public void setConsignor(Account consignor) {
		this.consignor = consignor;
	}

	public Timestamp getLimit_time() {
		return limit_time;
	}

	public void setLimit_time(Timestamp limit_time) {
		this.limit_time = limit_time;
	}

	public Double getAmount_declared() {
		return amount_declared;
	}

	public void setAmount_declared(Double amount_declared) {
		this.amount_declared = amount_declared;
	}

	public String getDeclaration() {
		return declaration;
	}

	public void setDeclaration(String declaration) {
		this.declaration = declaration;
	}

	public String getStore_num() {
		return store_num;
	}

	public void setStore_num(String store_num) {
		this.store_num = store_num;
	}

	public Account getLast_edit_user() {
		return last_edit_user;
	}

	public void setLast_edit_user(Account last_edit_user) {
		this.last_edit_user = last_edit_user;
	}

	public String getBuyer_wangwang() {
		return buyer_wangwang;
	}

	public void setBuyer_wangwang(String buyer_wangwang) {
		this.buyer_wangwang = buyer_wangwang;
	}

	public Float getCommission_rate() {
		return commission_rate;
	}

	public void setCommission_rate(Float commission_rate) {
		this.commission_rate = commission_rate;
	}

	public Timestamp getExpected_arrival() {
		return expected_arrival;
	}

	public void setExpected_arrival(Timestamp expected_arrival) {
		this.expected_arrival = expected_arrival;
	}

	public String getBuyer_tel_num() {
		return buyer_tel_num;
	}

	public void setBuyer_tel_num(String buyer_tel_num) {
		this.buyer_tel_num = buyer_tel_num;
	}

	public Timestamp getSubmit_time() {
		return submit_time;
	}

	public void setSubmit_time(Timestamp submit_time) {
		this.submit_time = submit_time;
	}

	public boolean isWaybill_changed() {
		return waybill_changed;
	}

	public void setWaybill_changed(boolean waybill_changed) {
		this.waybill_changed = waybill_changed;
	}

	public Map<Integer, Pack> getPkList() {
		return pkList;
	}

	public void setPkList(Map<Integer, Pack> pkList) {
		this.pkList = pkList;
	}

	public Float getTotal_weight() {
		return total_weight;
	}

	public void setTotal_weight(Float total_weight) {
		this.total_weight = total_weight;
	}

	public Float getTotal_volume_weight() {
		return total_volume_weight;
	}

	public void setTotal_volume_weight(Float total_volume_weight) {
		this.total_volume_weight = total_volume_weight;
	}

	public Timestamp getEdit_time() {
		return edit_time;
	}

	public void setEdit_time(Timestamp edit_time) {
		this.edit_time = edit_time;
	}

	public String getExpect_deliver_with() {
		return expect_deliver_with;
	}

	public void setExpect_deliver_with(String expect_deliver_with) {
		this.expect_deliver_with = expect_deliver_with;
	}

	public Integer getIssue() {
		return issue;
	}

	public void setIssue(Integer issue) {
		this.issue = issue;
	}

	@Override
	protected Bill clone(){
		Bill clone;
		try {
			clone = (Bill)super.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Map<Integer, Declaration> getDeclareList() {
		return declareList;
	}

	public void setDeclareList(Map<Integer, Declaration> declareList) {
		this.declareList = declareList;
	}

	public Integer getWeight_declared() {
		return weight_declared;
	}

	public void setWeight_declared(Integer weight_declared) {
		this.weight_declared = weight_declared;
	}
}
