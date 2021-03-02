package com.zzerp.bill;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zzerp.good.Good;

@Service
@Transactional
public class BillServiceImpl implements BillService {

	@Resource
	private BillDao billDao;

	@Override
	public int countBill(Bill bill, Map<String, Object> conditionMap) {
		return billDao.countBill(bill, conditionMap);
	}

	@Override
	public List<Bill> listBill(Bill bill, int pageSize, int page, Map<String, Object> conditionMap) {

		List<Bill> list = billDao.listBill(bill, pageSize, page, conditionMap);
		return process(list);

	}

	private List<Bill> process(List<Bill> list) {
		for (Bill b : list) {
			List<Good> gList = billDao.listGood(b.getId());
			StringBuffer buff = new StringBuffer("");
			if (gList.isEmpty()) {
				continue;
			}
			buff.append(gList.get(0).getCategory().getTitle());
			if (gList.size() >= 2) {
				buff.append(",");
				buff.append(gList.get(1).getCategory().getTitle());
				int i = gList.size() - 2;
				while (i > 0) {
					buff.append(".");
					i--;
				}
			}
			b.setTitle(buff.toString());
		}
		return list;
	}

	@Override
	public int addBill(Bill bill) {
		List<Bill_Good> bgList = bill.getBgList();
		List<Bill_Good> bgs = new ArrayList<Bill_Good>();
		if (bgList != null) {
			for (Bill_Good bg : bgList) {
				if (bg != null && bg.getGood().getId() > 0) {
					bgs.add(bg);
				}
			}
		}
		bill.setBgList(bgs);
		return billDao.addBill(bill);
	}

	@Override
	public int deleteBill(int id) {
		return billDao.deleteBill(id);
	}

	@Override
	@Transactional
	public int updateBill(Bill bill) {
		List<Bill_Good> bgList = bill.getBgList();
		List<Bill_Good> bgs = new ArrayList<Bill_Good>();

		for (Bill_Good bg : bgList) {
			if (bg != null && bg.getGood().getId() > 0) {
				bgs.add(bg);
			}
		}
		bill.setBgList(bgs);
		return billDao.updateBill(bill);
	}

	@Override
	public Bill detailBill(int id) {
		return billDao.detailBill(id);
	}

	@Override
	public Bill detailBill(int id, int state) {
		return billDao.detailBill(id);
	}

	@Transactional
	@Override
	public int deliver(Bill bill) {
		StringBuffer waybill_num = new StringBuffer("");
		StringBuffer deliver_with = new StringBuffer("");
		StringBuffer logistics = new StringBuffer("");
		Double freight = new Double(0);
		Iterator<Pack> it = bill.getPkList().values().iterator();
		while (it.hasNext()) {
			Pack p = it.next();
			waybill_num.append(p.getBarcode());
			deliver_with.append(p.getDeliver_with());
			logistics.append(p.getLogistics());
			if (it.hasNext()) {
				waybill_num.append(",");
				deliver_with.append(",");
				logistics.append(",");
			}
			if (null != p.getFreight()) {
				freight += p.getFreight();
			}
		}
		bill.setWaybill_num(waybill_num.toString());
		bill.setDeliver_with(deliver_with.toString());
		bill.setLogistics(logistics.toString());
		bill.setFreight(freight);
		return billDao.deliver(bill);
	}

	@Override
	public int changeState(int id, int state) {
		return billDao.changeState(id, state);
	}

	@Override
	public int recycleBill(int id) {
		return billDao.recycleBill(id);
	}

	@Transactional
	@Override
	public int fillWaybill(Bill bill) {
		StringBuffer waybill_num = new StringBuffer("");
		StringBuffer deliver_with = new StringBuffer("");
		StringBuffer logistics = new StringBuffer("");
		Double freight = new Double(0);

		Iterator<Pack> it = bill.getPkList().values().iterator();
		while (it.hasNext()) {
			Pack p = it.next();
			waybill_num.append(p.getBarcode());
			deliver_with.append(p.getDeliver_with());
			logistics.append(p.getLogistics());
			if (it.hasNext()) {
				waybill_num.append(",");
				deliver_with.append(",");
				logistics.append(",");
			}
			if (null != p.getFreight()) {
				freight += p.getFreight();
			}
		}

		bill.setWaybill_num(waybill_num.toString());
		bill.setDeliver_with(deliver_with.toString());
		bill.setLogistics(logistics.toString());
		bill.setFreight(freight);
		return billDao.fillWaybill(bill);
	}

	@Override
	public int countBillPreparing(Bill bill, Map<String, Object> conditionMap) {
		return billDao.countBillPreparing(bill, conditionMap);
	}

	@Override
	public int countBillReady(Bill bill, Map<String, Object> conditionMap) {
		return billDao.countBillReady(bill, conditionMap);
	}

	@Override
	public List<Bill> listBillPreparing(Bill bill, int pageSize, int page, Map<String, Object> conditionMap) {
		List<Bill> list = billDao.listBillPreparing(bill, pageSize, page, conditionMap);
		return process(list);
	}

	@Override
	public List<Bill> listBillReady(Bill bill, int pageSize, int page, Map<String, Object> conditionMap) {
		List<Bill> list = billDao.listBillReady(bill, pageSize, page, conditionMap);
		return process(list);
	}

	@Override
	public int countBillDeleted(Bill filter, Map<String, Object> conditionMap) {
		return billDao.countBillDeleted(filter, conditionMap);
	}

	@Override
	public List<Bill> listBillDeleted(Bill filter, int pageSize, int page, Map<String, Object> conditionMap) {
		List<Bill> list = billDao.listBillDeleted(filter, pageSize, page, conditionMap);
		return process(list);
	}

	@Override
	public List<String[]> list(String[] ids) {
		return billDao.list(ids);
	}

	@Override
	public List<Bill> listByIds(String[] ids) {
		return billDao.listByIds(ids);
	}

	@Override
	public int noteBill(String username, Integer id, String salesman_note) {
		String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String separateline = "<hr/><div class='salesman_note'>" + dateString + " <b>" + username + "</b></div>";
		return billDao.noteBill(id, separateline + salesman_note);
	}

	@Transactional
	@Override
	public int submitBill(int id) {
		return billDao.submitBill(id);
	}

	@Override
	public boolean checkBillNum(String num) {
		return billDao.checkBillNum(num);
	}

	@Override
	public int acceptBill(int id) {
		return billDao.acceptBill(id);
	}

	public BillDao getBillDao() {
		return billDao;
	}

	public void setBillDao(BillDao billDao) {
		this.billDao = billDao;
	}

	@Override
	public boolean checkPack(String barcode) {
		return this.billDao.checkPack(barcode);
	}

	@Transactional
	@Override
	public int deliverPacks(String[] barcodes) {
		return this.billDao.deliverPacks(barcodes);
	}

	@Override
	public int reverseState(int id) {
		return this.billDao.reverseState(id);
	}

	@Override
	public int issueBill(int id, int i) {
		return billDao.issueBill(id, i);
	}

	@Transactional
	@Override
	public void addBills(List<Bill> bills) {
		for (Bill bill : bills) {
			List<Integer> bids = billDao.getBillByNum(bill.getNum().trim());
			//如果该订单已经存在就不导入
			if (null == bids || bids.isEmpty()) {
				billDao.addBill(bill);
			} else {
				bill.setId(bids.get(0));
				bill.setState(0);
			}
		}
	}

	@Override
	public int declare(Bill bill) {
		return billDao.declare(bill);
	}

	@Override
	public List<Declaration> getDeclarationList(int id) {
		return billDao.getDeclarationList(id);
	}

	@Override
	public void addPacks(List<Pack> packs) {
		for (Pack p : packs) {
			Integer bill_id = p.getBill_id();
			String barcode = p.getBarcode();
			Pack pack = billDao.getPackByBarcode(barcode);
			if (pack == null) {
				if (null != bill_id) {
					Integer id = billDao.addPack(p);
					if (id != null) {
						p.setId(id);
						p.setStatus("新增");
					}
				}
			} else {
				if (bill_id != null && pack.getBill_id() - bill_id != 0) {
					p.setStatus("订单号冲突");
				} else {
					if (p.getLength() == null) {
						p.setLength(pack.getLength());
					}
					if (p.getWidth() == null) {
						p.setWidth(pack.getWidth());
					}
					if (p.getHeight() == null) {
						p.setHeight(pack.getHeight());
					}
					if (p.getWeight() == null) {
						p.setWeight(pack.getWeight());
					}
					if (p.getVolume_rate() == null) {
						p.setVolume_rate(pack.getVolume_rate());
					}
					if (p.getDeliver_with() == null) {
						p.setDeliver_with(pack.getDeliver_with());
					}
//					if (p.getWaybill_num() == null) {
//						p.setWaybill_num(pack.getWaybill_num());
//					}
					if (p.getFreight() == null) {
						p.setFreight(pack.getFreight());
					}
					if (p.getLogistics() == null) {
						p.setLogistics(pack.getLogistics());
					}
					if (p.getDeliver_time() == null) {
						p.setDeliver_time(pack.getDeliver_time());
					}
					Integer result = billDao.updatePack(p);
					if (result > 0) {
						p.setStatus("更新");
						String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
						String separateline = "<hr/><div class='salesman_note'>" + dateString + " <b>导入</b></div>";
						billDao.noteBill(pack.getBill_id(), separateline + "包裹信息由：（" + pack + "）修改为（" + p + "）");
						p.setBill_id(pack.getBill_id());
					}
				}
			}
		}
	}

	@Override
	public List<Integer> getBillByNum(String bill_num) {
		return billDao.getBillByNum(bill_num);
	}

}
