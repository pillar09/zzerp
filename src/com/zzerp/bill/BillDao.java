package com.zzerp.bill;

import java.util.List;
import java.util.Map;

import com.zzerp.good.Good;

public interface BillDao {

	int countBill(Bill bill, Map<String, Object> conditionMap);

	int countBillPreparing(Bill bill, Map<String, Object> conditionMap);

	int countBillReady(Bill bill, Map<String, Object> conditionMap);

	int countBillDeleted(Bill bill, Map<String, Object> conditionMap);

	List<Bill> listBill(Bill bill, int pageSize, int page, Map<String, Object> conditionMap);

	List<Bill> listBillDeleted(Bill bill, int pageSize, int page, Map<String, Object> conditionMap);

	List<Bill> listBillReady(Bill bill, int pageSize, int page, Map<String, Object> conditionMap);

	List<Bill> listBillPreparing(Bill bill, int pageSize, int page, Map<String, Object> conditionMap);

	int addBill(Bill bill);

	int deleteBill(int id);

	int updateBill(Bill bill);

	int deliver(Bill bill);

	int changeState(int id, int state);

	int acceptBill(Bill bill);

	Bill detailBill(int id);

	int recycleBill(int id);

	List<Good> listGood(int id);

	int fillWaybill(Bill bill);

	List<String[]> list(String[] ids);

	List<Bill> listByIds(String[] ids);

	int noteBill(int id, String salesman_note);

	int submitBill(int id);

	boolean checkBillNum(String num);

	int acceptBill(int id);

	boolean checkPack(String barcode);

	int deliverPacks(String[] barcodes);

	int reverseState(int id);

	int issueBill(int id, int issue);

	List<Integer> getBillByNum(String num);

	int declare(Bill bill);

	List<Declaration> getDeclarationList(int id);

	Integer addPack(Pack pack);

	Pack getPackByBarcode(String barcode);

	Integer updatePack(Pack pack);

}
