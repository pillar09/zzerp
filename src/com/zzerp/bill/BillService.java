package com.zzerp.bill;

import java.util.List;
import java.util.Map;

public interface BillService {

	int countBill(Bill bill, Map<String, Object> conditionMap);

	int countBillPreparing(Bill bill, Map<String, Object> conditionMap);

	int countBillReady(Bill bill, Map<String, Object> conditionMap);

	int countBillDeleted(Bill filter, Map<String, Object> conditionMap);

	List<Bill> listBill(Bill bill, int pageSize, int page, Map<String, Object> conditionMap);

	List<Bill> listBillDeleted(Bill filter, int pageSize, int page, Map<String, Object> conditionMap);

	List<Bill> listBillPreparing(Bill bill, int pageSize, int page, Map<String, Object> conditionMap);

	List<Bill> listBillReady(Bill bill, int pageSize, int page, Map<String, Object> conditionMap);

	int addBill(Bill bill);

	int deleteBill(int id);
	
	int updateBill(Bill bill);

	Bill detailBill(int id);

	int deliver(Bill bill);

	int changeState(int id, int state);

	int recycleBill(int id);

	Bill detailBill(int id, int state);

	int fillWaybill(Bill bill);

	List<String[]> list(String[] ids);

	int noteBill(String username, Integer id, String salesman_note);

	int submitBill(int id);

	boolean checkBillNum(String num);

	int acceptBill(int id);

	List<Bill> listByIds(String[] ids);

	boolean checkPack(String barcode);

	int deliverPacks(String[] barcodes);

	int reverseState(int id);

	int issueBill(int id, int i);

	void addBills(List<Bill> bills);

	int declare(Bill bill);
	
	List<Declaration> getDeclarationList(int id);

	void addPacks(List<Pack> packs);

	List<Integer> getBillByNum(String bill_num);
}
