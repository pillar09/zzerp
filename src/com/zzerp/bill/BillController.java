package com.zzerp.bill;

import java.beans.PropertyEditorSupport;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.Principal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MultipartFile;

import au.com.bytecode.opencsv.CSVWriter;

import com.zzerp.account.Account;
import com.zzerp.account.AccountService;
import com.zzerp.core.ConfigDao;
import com.zzerp.core.OperationLogger;
import com.zzerp.core.PageInfo;
import com.zzerp.core.QRCodeUtil;
import com.zzerp.core.RegX;
import com.zzerp.good.Category;
import com.zzerp.good.Good;
import com.zzerp.good.GoodService;

import edu.emory.mathcs.backport.java.util.Arrays;

@Controller
@RequestMapping("/bill")
@SessionAttributes({ "pageInfo_recycleBills", "pageInfo", "currentAccount", "allAccounts" })
public class BillController {
	static int PAGE_SIZE = 10;
	static String[] titles = { "ID", "编号", "金额", "创建时间", "下单时间", "付款时间", "提交时间", "预计到货时间", "最迟发货期限", "店铺号", "买家姓名",
			"邮箱地址", "买家手机", "买家电话", "买家在线ID", "付款方式",
			//
			"收货人", "国家/地区", "省/州", "城市", "邮编", "地址1", "地址2", "手机", "电话", "传真",
			//
			"运费", "快递代理", "发货方式", "物流单号", "发货时间",
			//
			"申报金额", "申报总重",

	};
	@Resource
	private BillService billService;

	@Resource
	private AccountService accountService;

	@Resource
	private GoodService goodService;

	@Resource
	private ConfigDao configDao;

	// @ModelAttribute("pageInfo_issueBills")
	// public PageInfo<Bill> pageInfo_issueBills() {
	// Bill filter = new Bill();
	// filter.setState(Bill.STATE_ISSUE);
	// int pageSize = PAGE_SIZE;
	// int page = 1;
	// LinkedHashMap<String, Object> conditionMap = new LinkedHashMap<String,
	// Object>();
	// int rowCount = billService.countBillByState(Bill.STATE_ISSUE,
	// conditionMap);
	// return new PageInfo<Bill>(filter, rowCount, pageSize, page,
	// conditionMap);
	// }

	@ModelAttribute("pageInfo_recycleBills")
	public PageInfo<Bill> pageInfo_recycleBills() {
		Bill filter = new Bill();
		int pageSize = PAGE_SIZE;
		int page = 1;
		LinkedHashMap<String, Object> conditionMap = new LinkedHashMap<String, Object>();
		int rowCount = billService.countBillDeleted(filter, conditionMap);
		return new PageInfo<Bill>(filter, rowCount, pageSize, page, conditionMap);
	}

	@ModelAttribute("allAccounts")
	public ArrayList<Account> allAccounts() {
		ArrayList<Account> allAccount = new ArrayList<Account>();
		List<Account> accounts = accountService.listAccount();
		allAccount.addAll(accounts);
		return allAccount;
	}

	@ModelAttribute("pageInfo")
	public PageInfo<Bill> pageInfo() {
		Bill filter = new Bill();
		filter.setState(0);
		int pageSize = PAGE_SIZE;
		int page = 1;
		LinkedHashMap<String, Object> conditionMap = new LinkedHashMap<String, Object>();
		int rowCount = billService.countBill(filter, conditionMap);
		return new PageInfo<Bill>(filter, rowCount, pageSize, page, conditionMap);
	}

	@ModelAttribute("currentAccount")
	public Account currentAccount() {
		return accountService.detailAccount(OperationLogger.getUsername());
	}

	@RequestMapping(value = "/manageBills", method = RequestMethod.GET)
	public String manageBills(@RequestParam(value = "page", required = false) String pageParam, //
			@RequestParam(value = "state", required = false) Integer st, // 如果为传入参数为null
			@RequestParam(value = "issue", required = false) Integer issue, //
			@RequestParam(value = "pageSize", required = false) Integer pageSizeParam, //
			@ModelAttribute(value = "pageInfo") PageInfo<Bill> pageInfo, //
			ModelMap model) {
		Map<String, Object> conditionMap = pageInfo.getConditionMap();

		// 查询数字
		Bill filterClone = pageInfo.getFilter().clone();
		filterClone.setIssue(null);
		filterClone.setState(0);
		int allCount = billService.countBill(filterClone, conditionMap);
		filterClone.setIssue(1);
		int issueCount = billService.countBill(filterClone, conditionMap);
		filterClone.setIssue(0);

		filterClone.setState(2);
		int preparingCount = billService.countBillPreparing(filterClone, conditionMap);
		int readyCount = billService.countBillReady(filterClone, conditionMap);
		filterClone.setState(1);
		int draftCount = billService.countBill(filterClone, conditionMap);
		filterClone.setState(3);
		int sendCount = billService.countBill(filterClone, conditionMap);
		filterClone.setState(9);
		int acceptCount = billService.countBill(filterClone, conditionMap);

		// 查询列表
		Bill filter = pageInfo.getFilter();

		if (null != st) {
			filter.setState(st);
			filter.setIssue(null);
		}
		int pageSize = pageInfo.getPageSize();
		int page = pageInfo.getPage();
		int rowCount = 0;

		List<Bill> list;
		if (null != pageParam && !pageParam.isEmpty()) {
			page = Integer.parseInt(pageParam);
		}

		if (null != pageSizeParam && pageSizeParam != 0) {
			pageSize = pageSizeParam;
		}
		if (issue != null) {
			filter.setIssue(issue);
		}
		int oriState = filter.getState();
		if (0 == oriState) {
			rowCount = allCount;
			if (null != filter.getIssue() && filter.getIssue() == 1) {
				rowCount = issueCount;
			}
			list = billService.listBill(filter, pageSize, page, conditionMap);

		} else if (102 == oriState) {
			filter.setState(2);
			rowCount = preparingCount;
			list = billService.listBillPreparing(filter, pageSize, page, conditionMap);
			filter.setState(102);
		} else if (2 == oriState) {
			rowCount = readyCount;
			list = billService.listBillReady(filter, pageSize, page, conditionMap);
		} else {
			rowCount = billService.countBill(filter, conditionMap);
			;
			list = billService.listBill(filter, pageSize, page, conditionMap);
		}

		PageInfo<Bill> pageInfo2 = new PageInfo<Bill>(filter, rowCount, pageSize, page, conditionMap);
		model.addAttribute("pageInfo", pageInfo2);
		model.put("bills", list);
		model.put("state", filter.getState() + "");
		model.put("allCount", allCount);
		model.put("preparingCount", preparingCount);
		model.put("draftCount", draftCount);
		model.put("readyCount", readyCount);
		model.put("sendCount", sendCount);
		model.put("acceptCount", acceptCount);
		model.put("issueCount", issueCount);
		model.put("issue", filter.getIssue());
		return "manageBills";
	}

	@RequestMapping(value = "/searchBills", method = RequestMethod.POST)
	public String searchBills(@ModelAttribute(value = "pageInfo") PageInfo<Bill> pageInfo, //
			ModelMap model) {
		Bill filter = pageInfo.getFilter();
		Map<String, Object> conditionMap = pageInfo.getConditionMap();

		filter.setState(0);
		int allCount = billService.countBill(filter, conditionMap);
		filter.setIssue(1);
		int issueCount = billService.countBill(filter, conditionMap);
		filter.setIssue(0);

		filter.setState(2);
		int preparingCount = billService.countBillPreparing(filter, conditionMap);
		int readyCount = billService.countBillReady(filter, conditionMap);
		filter.setState(1);
		int draftCount = billService.countBill(filter, conditionMap);
		filter.setState(3);
		int sendCount = billService.countBill(filter, conditionMap);
		filter.setState(9);
		int acceptCount = billService.countBill(filter, conditionMap);

		filter.setState(0);

		int rowCount = billService.countBill(filter, conditionMap);
		List<Bill> list = billService.listBill(filter, pageInfo.getPageSize(), 1, conditionMap);
		PageInfo<Bill> pageInfo2 = new PageInfo<Bill>(filter, rowCount, pageInfo.getPageSize(), 1, conditionMap);
		model.addAttribute("pageInfo", pageInfo2);
		model.put("bills", list);
		model.put("state", "0");
		model.put("allCount", allCount);
		model.put("preparingCount", preparingCount);
		model.put("draftCount", draftCount);
		model.put("readyCount", readyCount);
		model.put("sendCount", sendCount);
		model.put("acceptCount", acceptCount);
		model.put("issueCount", issueCount);
		return "manageBills";
	}

	@RequestMapping(value = "/recycleBills")
	public String recycleBillsSearch(@ModelAttribute("pageInfo_recycleBills") PageInfo<Bill> pageInfo,
			@RequestParam(value = "page", required = false) String pageParam, ModelMap model) {
		Map<String, Object> conditionMap = pageInfo.getConditionMap();
		int page = 1;
		if (pageParam != null) {
			page = Integer.parseInt(pageParam);
		}
		int pageSize = pageInfo.getPageSize();
		int rowCount = billService.countBillDeleted(pageInfo.getFilter(), conditionMap);
		List<Bill> list = billService.listBillDeleted(pageInfo.getFilter(), pageSize, page, conditionMap);
		PageInfo<Bill> pageInfo2 = new PageInfo<Bill>(pageInfo.getFilter(), rowCount, pageSize, page, conditionMap);
		model.addAttribute("pageInfo_recycleBills", pageInfo2);
		model.put("pageInfo_recycleBills", pageInfo2);
		model.put("bills", list);
		return "recycleBills";
	}

	// @RequestMapping(value = "/recycleBills", method = RequestMethod.GET)
	// public String recycleBills(@ModelAttribute("pageInfo_recycleBills")
	// PageInfo<Bill> pageInfo,
	// @RequestParam(value = "page", required = false) String pageParam,
	// ModelMap model) {
	// Map<String, Object> conditionMap = new LinkedHashMap<String, Object>();
	// int page = 1;
	// if (pageParam != null) {
	// page = Integer.parseInt(pageParam);
	// }
	// int pageSize = pageInfo.getPageSize();
	// int rowCount = billService.countBillDeleted(null, conditionMap);
	// List<Bill> list = billService.listBillDeleted(null, pageSize, page,
	// conditionMap);
	// PageInfo<Bill> pageInfo2 = new PageInfo<Bill>(pageInfo.getFilter(),
	// rowCount, pageSize, page, conditionMap);
	// model.addAttribute("pageInfo_recycleBills", pageInfo2);
	// model.put("pageInfo_recycleBills", pageInfo2);
	// model.put("bills", list);
	// return "recycleBills";
	// }

	@RequestMapping("/addBill")
	public String addBill(Bill bill, @ModelAttribute("pageInfo") PageInfo<Bill> pageInfo, ModelMap model) {
		Account currentAccount = accountService.detailAccount(OperationLogger.getUsername());
		bill.setCreate_user(currentAccount);
		bill.setLast_edit_user(currentAccount);
		bill.setState(Bill.STATE_DRAFT);
		billService.addBill(bill);
		return "redirect:manageBills.go";
	}

	@RequestMapping("/duplicateBill")
	public String duplicateBill(@RequestParam(value = "id") Integer id, //
			@ModelAttribute(value = "currentAccount") Account currentAccount, //
			ModelMap model) {
		List<Category> categorys = goodService.listCategory(0);
		model.put("categorys", categorys);
		Bill bill = billService.detailBill(id);
		bill.setCreate_user(currentAccount);
		bill.setNum(bill.getNum() + "-1");
		model.put("bill", bill);
		return "addBill";
	}

	@RequestMapping("/toAddBill")
	public String toAddBill(@ModelAttribute(value = "currentAccount") Account currentAccount, //
			ModelMap model) {
		List<Category> categorys = goodService.listCategory(0);
		model.put("categorys", categorys);
		Bill bill = new Bill();
		bill.setCreate_user(currentAccount);
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd01");
		bill.setNum(format.format(new Date()));
		model.put("bill", bill);
		return "addBill";
	}

	@RequestMapping("/toEditBill")
	public String toEditBill(@RequestParam(value = "id") int id, ModelMap model) {
		List<Category> categorys = goodService.listCategory(0);
		Bill bill = billService.detailBill(id);
		model.put("categorys", categorys);
		model.put("bill", bill);
		Map<String, Good> lhm = new LinkedHashMap<String, Good>();
		Map<String, List<String[]>> goodListMap = new LinkedHashMap<String, List<String[]>>();
		for (Bill_Good bg : bill.getBgList()) {
			Good g = goodService.detailGood(bg.getGood().getId());
			lhm.put(bg.getGood().getId() + "", g);
			int category_id = g.getCategory().getId();
			List<String[]> list = goodService.list(category_id);
			goodListMap.put(bg.getGood().getId() + "", list);
		}
		model.put("goodMap", lhm);
		model.put("goodListMap", goodListMap);
		return "editBill";
	}

	@RequestMapping("/toReviseBill")
	public String toReviseBill(@RequestParam(value = "id") Integer id, ModelMap model) {
		List<Category> categorys = goodService.listCategory(0);
		Bill bill = billService.detailBill(id);
		model.put("categorys", categorys);
		model.put("bill", bill);
		Map<String, Good> lhm = new LinkedHashMap<String, Good>();
		Map<String, List<String[]>> goodListMap = new LinkedHashMap<String, List<String[]>>();
		for (Bill_Good bg : bill.getBgList()) {
			Good g = goodService.detailGood(bg.getGood().getId());
			lhm.put(bg.getGood().getId() + "", g);
			int category_id = g.getCategory().getId();
			List<String[]> list = goodService.list(category_id);
			goodListMap.put(bg.getGood().getId() + "", list);
		}
		model.put("goodMap", lhm);
		model.put("goodListMap", goodListMap);
		return "reviseBill";
	}

	@RequestMapping("/detailBill")
	public String detailBill(@RequestParam(value = "id") int id, //
			@RequestParam(value = "state", required = false) String state, //
			ModelMap model) {

		Bill bill = billService.detailBill(id);

		model.put("bill", bill);
		Map<String, Good> lhm = new LinkedHashMap<String, Good>();
		for (Bill_Good bg : bill.getBgList()) {
			Good g = goodService.detailGood(bg.getGood().getId());
			lhm.put(bg.getGood().getId() + "", g);
		}
		String server_host = configDao.getDefine("server_host");
		QRCodeUtil.encode(server_host + "zzerp/file/shot.go?billNo=" + bill.getNum());
		model.put("goodMap", lhm);
		return "detailBill";
	}

	@RequestMapping("/detailDeletedBill")
	public String detailDeletedBill(@RequestParam(value = "id") int id, //
			@RequestParam(value = "state", required = false) String state, //
			ModelMap model) {

		Bill bill = billService.detailBill(id);

		model.put("bill", bill);
		Map<String, Good> lhm = new LinkedHashMap<String, Good>();
		for (Bill_Good bg : bill.getBgList()) {
			Good g = goodService.detailGood(bg.getGood().getId());
			lhm.put(bg.getGood().getId() + "", g);
		}

		model.put("goodMap", lhm);
		return "detailDeletedBill";
	}

	@RequestMapping("/toDeliverGoods")
	public String toDeliverGoods(@RequestParam(value = "id") int id, ModelMap model) {
		List<Category> categorys = goodService.listCategory(0);
		Bill bill = billService.detailBill(id);
		bill.setDeliver_time(new Timestamp(System.currentTimeMillis()));
		model.put("categorys", categorys);
		model.put("bill", bill);
		Map<String, Good> lhm = new LinkedHashMap<String, Good>();
		for (Bill_Good bg : bill.getBgList()) {
			Good g = goodService.detailGood(bg.getGood().getId());
			lhm.put(bg.getGood().getId() + "", g);
		}
		if (bill.getPkList().isEmpty()) {
			Pack firstPack = new Pack();
			firstPack.setVolume_rate(5000);
			firstPack.setDeliver_with(bill.getExpect_deliver_with());
			bill.getPkList().put(0, firstPack);
		}
		if (bill.getDeliver_with() == null || bill.getDeliver_with().trim().isEmpty()) {
			bill.setDeliver_with(bill.getExpect_deliver_with());
		}
		model.put("goodMap", lhm);
		return "deliverGoods";
	}

	@RequestMapping("/updateBill")
	public String updateBill(Bill bill, @ModelAttribute(value = "currentAccount") Account currentAccount, //
			ModelMap model) {
		bill.setLast_edit_user(currentAccount);
		billService.changeState(bill.getId(), Bill.STATE_DRAFT);
		billService.updateBill(bill);
		if (null != bill.getSalesman_note() && !bill.getSalesman_note().isEmpty()) {
			billService.noteBill(currentAccount.getUsername(), bill.getId(), bill.getSalesman_note());
		}
		return "redirect:manageBills.go";
	}

	@RequestMapping("/reviseBill")
	public String reviseBill(Bill bill, //
			@ModelAttribute(value = "currentAccount") Account currentAccount, //
			ModelMap model) {
		bill.setLast_edit_user(currentAccount);
		billService.updateBill(bill);
		billService.fillWaybill(bill);
		if (null != bill.getSalesman_note() && !bill.getSalesman_note().isEmpty()) {
			billService.noteBill(currentAccount.getUsername(), bill.getId(), bill.getSalesman_note());
		}
		return "redirect:manageBills.go";
	}

	@RequestMapping("/fillWaybill")
	public String fillWaybill(Bill bill, @ModelAttribute(value = "currentAccount") Account currentAccount, //
			ModelMap model) {

		bill.setDeliver_time(new Timestamp(System.currentTimeMillis()));
		billService.fillWaybill(bill);
		if (null != bill.getSalesman_note() && !bill.getSalesman_note().isEmpty()) {
			billService.noteBill(currentAccount.getUsername(), bill.getId(), bill.getSalesman_note());
		}
		Bill billDetails = billService.detailBill(bill.getId());

		Map<String, Good> lhm = new LinkedHashMap<String, Good>();
		for (Bill_Good bg : billDetails.getBgList()) {
			Good g = goodService.detailGood(bg.getGood().getId());
			lhm.put(bg.getGood().getId() + "", g);
		}

		model.put("bill", billDetails);
		model.put("goodMap", lhm);
		return "detailBill";
	}

	@RequestMapping(value = "/submitBill", method = RequestMethod.GET)
	public String submitBill(@RequestParam(value = "id") int id, //
			@ModelAttribute("pageInfo") PageInfo<Bill> pageInfo, ModelMap model) {
		billService.submitBill(id);
		return "redirect:manageBills.go";
	}

	@RequestMapping(value = "/submitBill", method = RequestMethod.POST)
	public String submitBill(Bill bill, @ModelAttribute(value = "currentAccount") Account currentAccount, //
			ModelMap model) {
		Integer billid = bill.getId();
		if (null == billid || billid == 0) {
			bill.setCreate_user(currentAccount);
			bill.setLast_edit_user(currentAccount);
			bill.setState(Bill.STATE_DRAFT);// 后面确保提交时再改变状态，避免出错后状态已变为ready
			billid = billService.addBill(bill);

		} else {
			bill.setLast_edit_user(currentAccount);
			billService.updateBill(bill);
			if (null != bill.getSalesman_note() && !bill.getSalesman_note().isEmpty()) {
				billService.noteBill(currentAccount.getUsername(), bill.getId(), bill.getSalesman_note());
			}
		}
		billService.submitBill(billid);// 改变状态为ready
		return "redirect:manageBills.go";
	}

	@RequestMapping("/deliver")
	public String deliver(Bill bill, @ModelAttribute(value = "currentAccount") Account currentAccount, //
			ModelMap model) {
		bill.setConsignor(currentAccount);
		bill.setDeliver_time(new Timestamp(System.currentTimeMillis()));
		billService.deliver(bill);
		if (null != bill.getSalesman_note() && !bill.getSalesman_note().isEmpty()) {
			billService.noteBill(currentAccount.getUsername(), bill.getId(), bill.getSalesman_note());
		}
		return "redirect:manageBills.go";
	}

	@RequestMapping("/deleteBill")
	public String deleteBill(@RequestParam(value = "id") int id, //
			ModelMap model) {
		int result = billService.deleteBill(id);
		return "redirect:recycleBills.go";
	}

	@RequestMapping("/acceptBill")
	public String acceptBill(@RequestParam(value = "id") int id, //
			ModelMap model) {
		billService.acceptBill(id);
		return "redirect:manageBills.go";
	}

	@RequestMapping("/recycleBill")
	public String recycleBill(@RequestParam(value = "id") int id, ModelMap model) {
		billService.recycleBill(id);
		return "redirect:manageBills.go";
	}

	@RequestMapping("/issueBill")
	public String issueBill(@RequestParam(value = "id") int id, ModelMap model) {
		billService.issueBill(id, 1);
		return "redirect:manageBills.go";
	}

	@RequestMapping("/solveIssue")
	public String solveIssue(@RequestParam(value = "id") int id, ModelMap model) {
		billService.issueBill(id, 0);
		return "redirect:manageBills.go";
	}

	// @RequestMapping(value = "/issueBills", method = RequestMethod.GET)
	// public String issueBills(@ModelAttribute("pageInfo_issueBills")
	// PageInfo<Bill> pageInfo,
	// @RequestParam(value = "page", required = false) String pageParam,
	// ModelMap model) {
	// Map<String, Object> conditionMap = new LinkedHashMap<String, Object>();
	// int page = 1;
	// if (pageParam != null) {
	// page = Integer.parseInt(pageParam);
	// }
	// int pageSize = pageInfo.getPageSize();
	// int rowCount =
	// billService.countBillByState(pageInfo.getFilter().getState(),
	// conditionMap);
	// List<Bill> list =
	// billService.listBillByState(pageInfo.getFilter().getState(), pageSize,
	// page, conditionMap);
	// PageInfo<Bill> pageInfo2 = new PageInfo<Bill>(pageInfo.getFilter(),
	// rowCount, pageSize, page, conditionMap);
	// model.addAttribute("pageInfo_issueBills", pageInfo2);
	// model.put("pageInfo_issueBills", pageInfo2);
	// model.put("bills", list);
	// return "issueBills";
	// }

	@RequestMapping(value = "/noteBill", method = RequestMethod.POST)
	public String noteBill(Bill bill, @ModelAttribute(value = "currentAccount") Account currentAccount, //
			ModelMap model) {
		if (null != bill.getSalesman_note() && !bill.getSalesman_note().isEmpty()) {
			billService.noteBill(currentAccount.getUsername(), bill.getId(), bill.getSalesman_note());
		}
		Bill billDetails = billService.detailBill(bill.getId());

		Map<String, Good> lhm = new LinkedHashMap<String, Good>();
		for (Bill_Good bg : billDetails.getBgList()) {
			Good g = goodService.detailGood(bg.getGood().getId());
			lhm.put(bg.getGood().getId() + "", g);
		}

		model.put("bill", billDetails);
		model.put("goodMap", lhm);
		return "detailBill";
	}

	@RequestMapping("/exportToCsv")
	public String exportToCsv(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "id") String ids, ModelMap model) {
		String[] idArray = ids.split(",");
		List<String[]> rs = billService.list(idArray);
		String fileName = "order.csv";
		String contentType = "application/octet-stream";
		// String realName = "Java设计模式.txt";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		CSVWriter out = null;
		BufferedOutputStream bos = null;
		try {
			out = new CSVWriter(new OutputStreamWriter(outputStream, "GBK"));
			out.writeAll(rs);
			out.flush();
			byte[] csvBytes = outputStream.toByteArray();
			outputStream.flush();
			outputStream.close();
			response.setContentType(contentType);
			response.setHeader("Content-disposition",
					"attachment; filename=" + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
			response.setHeader("Content-Length", csvBytes.length + "");

			bos = new BufferedOutputStream(response.getOutputStream());
			bos.write(csvBytes);
			bos.close();
		} catch (Exception e) {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e1) {
				}
			}
			return null;
		}

		return null;
	}

	private String getDateString(DateFormat formatter, Date date) {
		if (null != date && null != formatter) {
			return formatter.format(date);
		}
		return "";
	}

	@RequestMapping("importBills")
	public String importBills() {
		return "importBills";
	}

	@RequestMapping(value = "/importFromXlsNew", method = RequestMethod.POST)
	public String importFromXlsNew(@RequestParam("file") MultipartFile multipartFile, Principal principal, //
			@ModelAttribute(value = "currentAccount") Account currentAccount, // 当前账户
			ModelMap model) {
		if (null == multipartFile || multipartFile.getSize() <= 0) {
			return "importBills";
		}
		Sheet sheet = null;
		try {
			Workbook work = Workbook.getWorkbook(multipartFile.getInputStream());
			sheet = work.getSheet(0);
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm");
		int rows = sheet.getRows();
		int c = sheet.getColumns();
		if (c < 29) {
			return "importBills";
		}
		List<Bill> bills = new ArrayList<Bill>(rows);
		for (int i = 1; i < rows; i++) {
			Bill b = new Bill();
			b.setNum(sheet.getCell(0, i).getContents());// 0.A.订单号
			String salesmanNote = sheet.getCell(1, i).getContents(); // 1.B.订单状态
			b.setSalesman(currentAccount);// 2.C.负责人(业务员)
			b.setBuyer(sheet.getCell(3, i).getContents());// 3.D.买家名称
			b.setBuyer_wangwang((sheet.getCell(3, i).getContents()));// 3D.买家名称

//			b.setMail(sheet.getCell(4, i).getContents());// 4.买家邮箱
			try {
				b.setOrder_time(new Timestamp(formatter.parse(sheet.getCell(4, i).getContents()).getTime()));// 4.E.下单时间
				b.setPay_time(new Timestamp(formatter.parse(sheet.getCell(5, i).getContents()).getTime()));// 5.F.付款时间
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//String salesmanNote = "产品总金额："+sheet.getCell(7,i).getContents();// 6.G.产品总金额
			// 7.H.物流费用
			try {
				NumberFormat numFormatter = NumberFormat.getCurrencyInstance(Locale.US);
				Number number = numFormatter.parse(sheet.getCell(8, i).getContents());
				b.setAmount(number.doubleValue());// 8.I.订单金额
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			String separateline = "<hr/><div class='salesman_note'>" + dateString + " <b>导入</b></div>";
			salesmanNote += (separateline + "订单金额：" + sheet.getCell(8, i).getContents());
			// 9.J.满立减
			b.setSalesman_note(salesmanNote + separateline + sheet.getCell(10, i).getContents());// 10.K.产品信息
			// 11.L.订单备注
			// b.setAddress(sheet.getCell(12, i).getContents());//12.M.收货地址(包含国家和城市)
			b.setConsignee(sheet.getCell(13, i).getContents());// 13.N.收货人名称
			b.setCountry(sheet.getCell(14, i).getContents());// 14.O.收货国家
			b.setProvince(sheet.getCell(15, i).getContents());// 15.P.州/省
			b.setCity(sheet.getCell(16, i).getContents());// 16.Q.城市
			b.setAddress(sheet.getCell(17, i).getContents());// 17.R.地址(去掉国家和城市)
			b.setZip_code(sheet.getCell(18, i).getContents());// 18.邮编
			b.setTel_num(sheet.getCell(19, i).getContents());// 19.联系电话
			b.setPhone_num(sheet.getCell(20, i).getContents());// 20.手机
			b.setExpect_deliver_with(sheet.getCell(21, i).getContents());// 21.买家选择物流
			try {
				SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd");
				b.setLimit_time(new Timestamp(dateformatter.parse(sheet.getCell(22, i).getContents()).getTime()));// 22.W.发货期限
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String waybill = sheet.getCell(23, i).getContents();// 23.X.实际发货物流:运单号
			if (waybill != null && !waybill.isEmpty()) {
				String wbn[] = waybill.split(":");
				b.setDeliver_with(wbn[0]);
				b.setWaybill_num(wbn[1]);
			}

			try {
				SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd");
				b.setDeliver_time(new Timestamp(dateformatter.parse(sheet.getCell(24, i).getContents()).getTime()));// 24.Y.发货时间
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 25.Z.确认收货时间
			b.setStore_num(sheet.getCell(26, i).getContents());// 26.AA.店铺号
			Account salesman = accountService.detailAccount(sheet.getCell(27, i).getContents().trim());
			if (salesman != null) {
				b.setSalesman(salesman);// AB.销售
			}
			b.setPay_with(sheet.getCell(28, i).getContents());// AC.付款方式
			b.setCreate_user(currentAccount);
			b.setLast_edit_user(currentAccount);
			b.setState(Bill.STATE_DRAFT);// 后面确保提交时再改变状态，避免出错后状态已变为ready
			bills.add(b);
		}
		billService.addBills(bills);

		model.put("bills", bills);
		return "importBills";
	}

	@RequestMapping(value = "/importPackFromXls", method = RequestMethod.POST)
	public String importPackFromXls(@RequestParam("file") MultipartFile multipartFile, Principal principal, //
			@ModelAttribute(value = "currentAccount") Account currentAccount, // 当前账户
			ModelMap model) {
		if (null == multipartFile || multipartFile.getSize() <= 0) {
//			throw new ResourceAccessException("文件数据空");
			return "importPacks";
		}
		Sheet sheet = null;
		try {
			Workbook work = Workbook.getWorkbook(multipartFile.getInputStream());
			sheet = work.getSheet(0);
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (sheet == null) {
			return "importPacks";
		}
		
		int rows = sheet.getRows();
		int c = sheet.getColumns();
		String[][] contents = getSheetContents(sheet);
		if (c < 1) {
			return "importPacks";
		}
		List<Pack> packs = new ArrayList<Pack>(rows);
		for (int i = 1; i < rows; i++) {
			Pack p = new Pack();
			int column = 0;
			String bill_num = contents[i][column++];// 0.A.订单号
			if (bill_num.isEmpty()) {

			} else {
				p.setBill_num(bill_num);
				List<Integer> bids = billService.getBillByNum(bill_num);
				if (null == bids || bids.isEmpty()) {
//				billDao.addPack(p);
					p.setStatus("找不到订单");
				} else {
					p.setBill_id(bids.get(0));
				}
			}
			String stateString = contents[i][column++]; // 1.B.货物状态
			String waybill = contents[i][column++];// 2.C.实际发货物流:运单号
			if (waybill != null && !waybill.isEmpty() && waybill.contains(":")) {
				String wbn[] = waybill.split(":");
				p.setDeliver_with(wbn[0]);
				p.setWaybill_num(wbn[1]);
			}

			String sDeliver_time = contents[i][column++];
			if (sDeliver_time!=null && !sDeliver_time.isEmpty()) {
				try {
//					SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm");
					SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy HH:mm");
					p.setDeliver_time(new Timestamp(formatter.parse(sDeliver_time).getTime()));// 3.D.发货时间
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			column++;//4.E.确认收货时间
			column++;//5.F.物流备注
			String sLength = contents[i][column++].trim();
			if (RegX.PATTERN_NUM.matcher(sLength).matches()) {
				p.setLength(new Float(Integer.parseInt(sLength)));//6.G.包裹长（cm）
			}

			String sWidth = contents[i][column++].trim();
			if (RegX.PATTERN_NUM.matcher(sWidth).matches()) {
				p.setWidth(new Float(Integer.parseInt(sWidth)));//7.H.包裹宽（cm）
			}
			String sHeight = contents[i][column++].trim();
			if (RegX.PATTERN_NUM.matcher(sHeight).matches()) {
				p.setHeight(new Float(Integer.parseInt(sHeight)));//8.I.包裹高（cm）
			}
			String sVolumeRate = contents[i][column++].trim();
			if (RegX.PATTERN_NUM.matcher(sVolumeRate).matches()) {
				p.setVolume_rate(Integer.parseInt(sVolumeRate));//9.J.体积系数
			}
			String sWeight = contents[i][column++].trim();
			if (RegX.PATTERN_FLOAT.matcher(sWeight).matches()) {
				p.setWeight(Float.parseFloat(sWeight));//10.K.包裹重量（kg）
			}
			String sFreight = contents[i][column++].trim();
			if (RegX.PATTERN_FLOAT.matcher(sFreight).matches()) {
				p.setFreight(Double.parseDouble(sFreight));//11.L.实际运费（RMB）
			}
			column++;//12.M.物流代理	
			String barcode = contents[i][column++].trim();
			if (barcode == null || barcode.isEmpty()) {
				p.setBarcode(p.getWaybill_num());
			} else {
				p.setBarcode(barcode);//13.N.运单号（条码）	
			}

			//14.O.备注1	
			//15.P.备注2	
			//16.Q.备注3	
			//17.R.备注4	
			//18.S.备注5	
			//19.T.备注6	
			//20.U.备注7

			p.setState(0);
			if (p.getBarcode() != null) {
				packs.add(p);
			}
		}
		billService.addPacks(packs);

		model.put("packs", packs);
		return "importPacks";
	}

	private String[][] getSheetContents(Sheet sheet) {
		int row = sheet.getRows();
		int column = sheet.getColumns();
		String[][] contents = new String[row][column];
		for (int j = 0; j < column; j++) {
			for (int i = 0; i < row; i++) {
				contents[i][j] = sheet.getCell(j, i).getContents();
			}
		}

		return contents;
	}

	@RequestMapping("/exportToXls")
	public String exportToXls(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "id", required = false) String ids, ModelMap model)
			throws IOException, WriteException {
		if (null == ids) {
			return null;
		}
		String[] idArray = ids.split(",");
		if (null == idArray || idArray.length == 0) {
			return null;
		}
		List<Bill> bills = billService.listByIds(idArray);
		if (null == bills || bills.isEmpty()) {
			return null;
		}
		String fileName = "order.xls";
		String contentType = "application/octet-stream";
		WritableWorkbook wbook = null;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		BufferedOutputStream bos = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			wbook = Workbook.createWorkbook(outputStream);
			WritableSheet wsheet = wbook.createSheet(fileName, 0);
			WritableFont wfont = new jxl.write.WritableFont(WritableFont.createFont("宋体"), 10, WritableFont.NO_BOLD,
					false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
			WritableCellFormat wcfFC = new WritableCellFormat(wfont);
			List<String> titl = new ArrayList<String>(Arrays.asList(titles));
			for (int i = 0; i < 20; i++) {
				titl.add("备注" + i);
			}

			for (int i = 0; i < 10; i++) {
				titl.add("中文品名" + i);
				titl.add("英文品名" + i);
				titl.add("编码" + i);
				titl.add("重量(g)" + i);
				titl.add("单价" + i);
				titl.add("数量" + i);
				titl.add("小计" + i);
				titl.add("币种" + i);
			}

			for (int i = 0; i < 20; i++) {
				titl.add("备注" + i);
			}

			/*
			 * 发件人姓名：Pretty Wang 发件人省份：Guangdong 发件人城市：Shenzhen 发件人地址1：508,
			 * Block 2, Zhuguang Chuangxin Technology Park, Zhuguang Rd, Xili,
			 * Nanshan 发件人地址2：2-508, Zhuguang Chuangxin Kejiyuan, Xili, Nanshan
			 * 发件人手机：+8618926061129 发件人电话：+86-26792606 发件人传真：+86-26792601
			 * 发件人邮编：518055 发件人邮件：support@sino-smart.com
			 */

			titl.add("发件人姓名");
			titl.add("发件人省份");
			titl.add("发件人城市");
			titl.add("发件人地址1");
			titl.add("发件人地址2");
			titl.add("发件人手机");
			titl.add("发件人电话");
			titl.add("发件人传真");
			titl.add("发件人邮编");
			titl.add("发件人邮件");

			for (int i = 0; i < titl.size(); i++) {
				wsheet.addCell(new Label(i, 0, titl.get(i), wcfFC));
			}
			for (int i = 0; i < bills.size(); i++) {
				Bill b = bills.get(i);

				String[] bArray = { b.getId() + "", b.getNum() + "", "$" + b.getAmount(),
						getDateString(format, b.getCreate_time()), getDateString(format, b.getOrder_time()),
						getDateString(format, b.getPay_time()), getDateString(format, b.getSubmit_time()),
						getDateString(format, b.getExpected_arrival()), getDateString(format, b.getLimit_time()),
						b.getStore_num(), b.getBuyer(), b.getMail(), b.getBuyer_phone_num(), b.getBuyer_tel_num(),
						b.getBuyer_wangwang(), b.getPay_with(),
						//
						b.getConsignee(), b.getCountry(), b.getProvince(), b.getCity(), b.getZip_code(), b.getAddress(),
						b.getAddress2(),
						// b.getDelay_limit()+"",
						b.getPhone_num(), b.getTel_num(), b.getFax_num(),
						//
						b.getFreight() + "", b.getLogistics(), b.getDeliver_with(), b.getWaybill_num(),
						getDateString(format, b.getDeliver_time()),
						//
						// b.getCommission_rate()+"",
						b.getAmount_declared() + "", b.getWeight_declared() + "" };
				int j = 0;
				for (j = 0; j < bArray.length; j++) {
					wsheet.addCell(new Label(j, i + 1, bArray[j], wcfFC));
				}
				List<Declaration> des = billService.getDeclarationList(b.getId());
				j += 20;// 中间20个备注
				for (Declaration d : des) {
					wsheet.addCell(new Label(j++, i + 1, d.getDeclaration(), wcfFC));
					wsheet.addCell(new Label(j++, i + 1, d.getDeclaration_en(), wcfFC));
					wsheet.addCell(new Label(j++, i + 1, d.getCode(), wcfFC));
					wsheet.addCell(new Label(j++, i + 1, "" + d.getWeight(), wcfFC));
					wsheet.addCell(new Label(j++, i + 1, "" + d.getPrice(), wcfFC));
					wsheet.addCell(new Label(j++, i + 1, "" + d.getQuantity(), wcfFC));
					wsheet.addCell(new Label(j++, i + 1, "" + d.getTotal(), wcfFC));
					wsheet.addCell(new Label(j++, i + 1, "" + d.getCurrency(), wcfFC));
				}
				j = j + ((10 - des.size()) * 8);// 8表示一个申报内容有8个字段
				j += 20;// 中间20个备注

				/*
				 * 发件人姓名：Pretty Wang 发件人省份：Guangdong 发件人城市：Shenzhen 发件人地址1：508,
				 * Block 2, Zhuguang Chuangxin Technology Park, Zhuguang Rd,
				 * Xili, Nanshan 发件人地址2：2-508, Zhuguang Chuangxin Kejiyuan,
				 * Xili, Nanshan 发件人手机：+8618926061129 发件人电话：+86-26792606
				 * 发件人传真：+86-26792601 发件人邮编：518055 发件人邮件：support@sino-smart.com
				 */

				wsheet.addCell(new Label(j++, i + 1, "Pretty Wang", wcfFC));
				wsheet.addCell(new Label(j++, i + 1, "Guangdong", wcfFC));
				wsheet.addCell(new Label(j++, i + 1, "Shenzhen", wcfFC));
				wsheet.addCell(new Label(j++, i + 1,
						"508, Block 2, Zhuguang Chuangxin Technology Park, Zhuguang Rd, Xili, Nanshan", wcfFC));
				wsheet.addCell(new Label(j++, i + 1, "2-508, Zhuguang Chuangxin Kejiyuan, Xili, Nanshan", wcfFC));
				wsheet.addCell(new Label(j++, i + 1, "+8618926061129", wcfFC));
				wsheet.addCell(new Label(j++, i + 1, "+86-755-26792606", wcfFC));
				wsheet.addCell(new Label(j++, i + 1, "+86-755-26792601", wcfFC));
				wsheet.addCell(new Label(j++, i + 1, "518055", wcfFC));
				wsheet.addCell(new Label(j++, i + 1, "support@sino-smart.com", wcfFC));
			}
		} catch (Exception e) {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e1) {
				}
			}
			e.printStackTrace();
			return null;
		} finally {
			if (wbook != null) {
				wbook.write(); // 写入文件
				wbook.close();
			}
		}

		try {
			byte[] xlsBytes = outputStream.toByteArray();
			outputStream.flush();
			outputStream.close();
			response.setContentType(contentType);
			response.setHeader("Content-disposition",
					"attachment; filename=" + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
			response.setHeader("Content-Length", xlsBytes.length + "");

			bos = new BufferedOutputStream(response.getOutputStream());
			bos.write(xlsBytes);
			bos.close();
		} catch (Exception e) {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e1) {
				}
			}
			return null;
		}

		return null;
	}

	@RequestMapping("/checkBillNum")
	public @ResponseBody
	boolean checkBillNum(@RequestParam(value = "num") String num, ModelMap model) {
		return billService.checkBillNum(num);
	}

	@RequestMapping(value = "/toScanPacks", method = RequestMethod.GET)
	public String toScanPacks(Bill bill, @ModelAttribute(value = "currentAccount") Account currentAccount, //
			ModelMap model) {

		return "scanPacks";
	}

	@RequestMapping(value = "/checkPack", method = RequestMethod.POST)
	public @ResponseBody
	boolean checkPack(@RequestParam(value = "barcode") String barcode, //
			ModelMap model) {
		if (barcode == null || barcode.isEmpty()) {
			return false;
		}
		boolean check = billService.checkPack(barcode);

		return check;
	}

	@RequestMapping(value = "/deliverPacks", method = RequestMethod.POST)
	public String deliverPacks(@RequestParam(value = "barcode") String barcode, //
			ModelMap model) {
		int result = 0;
		if (barcode == null || barcode.isEmpty()) {
			return "scanPacks";
		}
		String[] barcodes = barcode.split(",");
		if (barcodes.length == 0) {
			return "scanPacks";
		}
		result = billService.deliverPacks(barcodes);
		model.put("result", result);
		return "scanPacks";
	}

	@RequestMapping(value = "/declare", method = RequestMethod.POST)
	public String declare(Bill bill, @ModelAttribute(value = "currentAccount") Account currentAccount, //
			ModelMap model) {
		billService.declare(bill);
		if (null != bill.getSalesman_note() && !bill.getSalesman_note().isEmpty()) {
			billService.noteBill(currentAccount.getUsername(), bill.getId(), bill.getSalesman_note());
		}
		Bill billDetails = billService.detailBill(bill.getId());
		Map<String, Good> lhm = new LinkedHashMap<String, Good>();
		for (Bill_Good bg : billDetails.getBgList()) {
			Good g = goodService.detailGood(bg.getGood().getId());
			lhm.put(bg.getGood().getId() + "", g);
		}
		model.put("bill", billDetails);
		model.put("goodMap", lhm);
		return "detailBill";
	}

	@RequestMapping(value = "/toDeclare", method = RequestMethod.GET)
	public String toDeclare(@RequestParam(value = "id") int id, ModelMap model) {
		Bill bill = billService.detailBill(id);
		List<Bill_Good> bgList = bill.getBgList();
		Map<String, Good> lhm = new LinkedHashMap<String, Good>();
		for (Bill_Good bg : bgList) {
			Good g = goodService.detailGood(bg.getGood().getId());
			lhm.put(bg.getGood().getId() + "", g);
		}
		if (bill.getDeclareList().isEmpty()) {
			for (int i = 0; i < bgList.size(); i++) {
				Declaration d = new Declaration();
				Bill_Good bg = bgList.get(i);
				Good g = goodService.detailGood(bg.getGood().getId());
				d.setDeclaration(g.getCategory().getTitle() + " " + g.getModel());
				d.setPrice(g.getPrice());
				d.setQuantity(1);
				d.setTotal(g.getPrice());
				d.setBill_id(bg.getBill_id());
				bill.getDeclareList().put(i, d);
			}

		}
		model.put("bill", bill);
		model.put("goodMap", lhm);
		return "declareGoods";
	}

	@InitBinder
	public void binder(WebDataBinder binder) {
		binder.registerCustomEditor(Timestamp.class, new PropertyEditorSupport() {
			public void setAsText(String value) {
				if (null == value || value.isEmpty()) {
					setValue(null);
					return;
				}
				try {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					format.setLenient(true);
					Date parsedDate = format.parse(value);
					setValue(new Timestamp(parsedDate.getTime()));
				} catch (java.text.ParseException e) {
					try {
						Date parsedDate = new SimpleDateFormat("yyyy-MM-dd").parse(value);
						setValue(new Timestamp(parsedDate.getTime()));
					} catch (ParseException e1) {
						try {
							Date parsedDate = new SimpleDateFormat("yyyy.MM.dd").parse(value);
							setValue(new Timestamp(parsedDate.getTime()));
						} catch (ParseException e2) {
							setValue(null);
							e2.printStackTrace();
						}
					}
				}
			}
		});
	}

}
