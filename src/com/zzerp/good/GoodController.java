package com.zzerp.good;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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

import com.zzerp.account.Account;
import com.zzerp.account.AccountService;
import com.zzerp.bill.Bill;
import com.zzerp.bill.Bill_Good;
import com.zzerp.core.OperationLogger;
import com.zzerp.core.PageInfo;

@Controller
@RequestMapping("/good")
@SessionAttributes({"currentAccount","pageInfo_warehousing","pageInfo_reserveRecord","pageInfo_good"})
public class GoodController {
	static int PAGE_SIZE = 10;

	@Resource
	private GoodService goodService;
	@Resource
	private AccountService accountService;
	
	@ModelAttribute("currentAccount")
	public Account currentAccount() {
		return accountService.detailAccount(OperationLogger.getUsername());
	}
	
	@ModelAttribute("pageInfo_reserveRecord")
	public PageInfo<ReserveRecord> pageInfo_reserveRecord() {
		ReserveRecord filter = new ReserveRecord();
		int pageSize = PAGE_SIZE;
		int page = 1;
		LinkedHashMap<String, Object> conditionMap = new LinkedHashMap<String, Object>();
		conditionMap.put("order", "reserve_time");
		int rowCount = 0;
		return new PageInfo<ReserveRecord>(filter, rowCount, pageSize, page, conditionMap);
	}
	
	@ModelAttribute("pageInfo_warehousing")
	public PageInfo<Warehousing> pageInfo_warehousing() {
		Warehousing filter = new Warehousing();
		int pageSize = PAGE_SIZE;
		int page = 1;
		LinkedHashMap<String, Object> conditionMap = new LinkedHashMap<String, Object>();
		conditionMap.put("order", "num");
		int rowCount = goodService.countWarehousing(filter, conditionMap);
		return new PageInfo<Warehousing>(filter, rowCount, pageSize, page, conditionMap);
	}
	
	@ModelAttribute("pageInfo_good")
	public PageInfo<Good> pageInfo_good() {
		Good filter = new Good();
		int pageSize = PAGE_SIZE;
		int page = 1;
		LinkedHashMap<String, Object> conditionMap = new LinkedHashMap<String, Object>();
		conditionMap.put("order", "num");
		int rowCount = goodService.count(filter, conditionMap);
		return new PageInfo<Good>(filter, rowCount, pageSize, page, conditionMap);
	}
	
	@RequestMapping(value = "/manageGoods", method = RequestMethod.GET)
	public String manageGoods(@ModelAttribute(value = "pageInfo_good") PageInfo<Good> pageInfo,//
			@RequestParam(value = "page", required = false) String pageParam, //
			@RequestParam(value = "pageSize", required = false) Integer pageSizeParam, //
			ModelMap model
	) {
		Map<String, Object> conditionMap = pageInfo.getConditionMap();
		int page = pageInfo.getPage();
		int pageSize = pageInfo.getPageSize();
		if (pageParam != null) {
			page = Integer.parseInt(pageParam);
		}
		if (pageSizeParam != null && pageSizeParam!=0) {
			pageSize = pageSizeParam;
		}
		List<Category> categorys = goodService.listCategory(0);
		
		Good filter = pageInfo.getFilter();
		
		int rowCount = goodService.count(filter, conditionMap);
		List<Good> listg = goodService.listGood(filter, pageSize, page, conditionMap);
		PageInfo<Good> pageInfo2 = new PageInfo<Good>(filter, rowCount, pageSize, page, conditionMap);
		model.addAttribute("pageInfo_good", pageInfo2);
		model.put("categorys",categorys);
		model.put("goods",listg);
		return "manageGoods";
	}
	

	@RequestMapping(value = "/searchGoods", method = RequestMethod.POST)
	public String searchGoods(@ModelAttribute(value = "pageInfo_good") PageInfo<Good> pageInfo,//
			Good filter, ModelMap model) {
		Map<String, Object> conditionMap = pageInfo.getConditionMap();
		int rowCount = goodService.count(filter, conditionMap);
		List<Good> list = goodService.listGood(filter, pageInfo.getPageSize(), 1, conditionMap);
		PageInfo<Good> pageInfo2 = new PageInfo<Good>(filter, rowCount, pageInfo.getPageSize(), 1, conditionMap);
		List<Category> categorys = goodService.listCategory(0);
		model.addAttribute("pageInfo_good", pageInfo2);
		model.put("categorys",categorys);
		model.put("goods", list);
		return "manageGoods";
	}

	@RequestMapping(value = "/addGood", method = RequestMethod.POST)
	public String addGood(Good good, ModelMap model) {
		int result = 0;
		String errMsg = "";
		try {
			result = goodService.addGood(good);
		} catch (Exception e) {
			e.printStackTrace();
			errMsg = e.getMessage();
			model.put("result", result);
			model.put("errMsg", errMsg);
			return "addGood";
		}
		int parentCateId = -1;
		List<Category> categorys = goodService.listCategory(parentCateId);
		model.put("result", result);
		model.put("categorys", categorys);
		return "addGood";
	}

	@RequestMapping("/deleteGood")
	public String deleteGood(@ModelAttribute(value = "pageInfo_good") PageInfo<Good> pageInfo,//
			@RequestParam(value = "id") int id, ModelMap model) {

		int result = goodService.deleteGood(id);
		if (result == -1) {
			model.put("result", result);
			model.put("errMsg", "商品已经在订单中使用，不可删除！");
		}
		Map<String, Object> conditionMap = pageInfo.getConditionMap();
		Good filter =  pageInfo.getFilter();
		int rowCount = goodService.count(filter, conditionMap);
		int page = pageInfo.getPage();
		int pageSize = pageInfo.getPageSize();
		List<Good> listg = goodService.listGood(filter, pageSize, page, conditionMap);
		PageInfo<Good> pageInfo2 = new PageInfo<Good>(filter, rowCount, pageSize, page, conditionMap);
		model.addAttribute("pageInfo_good", pageInfo2);
		model.put("goods",listg);
		return "manageGoods";
	}

	@RequestMapping("/toEditGood")
	public String toEditGood(@RequestParam(value = "id") int id, ModelMap model) {
		int parentCateId = -1;
		List<Category> categorys = goodService.listCategory(parentCateId);
		model.put("categorys", categorys);
		Good good = goodService.detailGood(id);
		model.put("good", good);
		return "editGood";
	}

	@RequestMapping("/updateGood")
	public String updateGood(Good good, ModelMap model) {
		goodService.updateGood(good);
		return "redirect:manageGoods.go";
	}
	
	@RequestMapping("/viewReserveRecords")
	public String viewReserveRecords(@ModelAttribute(value = "pageInfo_reserveRecord") PageInfo<ReserveRecord> pageInfo,//
			@RequestParam(value = "id", required = false) Integer good_id, //
			@RequestParam(value = "page", required = false) String pageParam, //
			@RequestParam(value = "pageSize", required = false) Integer pageSizeParam, //
			ReserveRecord r, ModelMap model) {
		ReserveRecord filter = pageInfo.getFilter();
		if (null != good_id) {
			filter.setId(good_id);
		}
		int page = pageInfo.getPage();
		int pageSize = pageInfo.getPageSize();
		if (pageParam != null) {
			page = Integer.parseInt(pageParam);
		}
		if (pageSizeParam != null && pageSizeParam!=0) {
			pageSize = pageSizeParam;
		}
		Good goodInfo = goodService.detailGood(filter.getId());
		Map conditionMap = pageInfo.getConditionMap();
		List<ReserveRecord> reserveRecords = goodService.listReserveRecord(filter, pageSize, page, conditionMap);
		Collections.reverse(reserveRecords);
		int rowCount = goodService.countReserveRecord(filter, conditionMap);
		PageInfo<ReserveRecord> pageInfo2 = new PageInfo<ReserveRecord>(filter, rowCount, pageSize, page, conditionMap);
		model.addAttribute("pageInfo_reserveRecord", pageInfo2);
		model.put("reserveRecords", reserveRecords);
		model.put("goodInfo",goodInfo);
		return "viewReserveRecords";
	}
	
	@RequestMapping("/publish")
	public String publish(ModelMap model) {
		int parentCateId = -1;
		List<Category> categorys = goodService.listCategory(parentCateId);
		model.put("categorys", categorys);
		return "addGood";
	}

	@RequestMapping(value = "test", method = RequestMethod.GET)
	public @ResponseBody
	List<String[]> getGoodsInJSON(@RequestParam("page") int p) {
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		int pageSize = PAGE_SIZE;
		List<String[]> list = goodService.list(pageSize, p, conditionMap);
		return list;
	}

	@RequestMapping(value = "listGoods", method = RequestMethod.POST)
	public @ResponseBody
	List<String[]> listGoodsInJSON(@RequestParam("category_id") int category_id) {
		List<String[]> list = goodService.list(category_id);

		return list;
	}

	@RequestMapping(value = "detailGood", method = RequestMethod.POST)
	public @ResponseBody
	Good detailGood(@RequestParam("id") int id) {
		Good good = goodService.detailGood(id);
		return good;
	}

	@RequestMapping("/warehousing")
	public String warehousing(ModelMap model) {
		Warehousing w = new Warehousing();
		w.setWarehouse_date(new Timestamp(System.currentTimeMillis()));
		List<Category> categorys = goodService.listCategory(0);
		model.put("categorys", categorys);
		model.put("w", w);
		return "warehousing";
	}
	
	@RequestMapping("/warehouse")
	public String warehouse(Warehousing w,
			@ModelAttribute(value = "currentAccount") Account currentAccount, ModelMap model) {
		w.setWarehouser(currentAccount);
		int result = goodService.warehouse(w);
		if(result==-2){
			List<Category> categorys = goodService.listCategory(0);
			model.put("categorys", categorys);
			model.put("w", w);
			model.put("result", result);
			model.put("errMsg", "商品重复，为避免错误，不同批次请分开为两个入库单！");
			
			return "warehousing";
		}
		return "redirect:warehousingRecord.go";
	}
	
	@RequestMapping("/detailWarehousing")
	public String detailWarehousing(@RequestParam(value = "id") int id,//
	@RequestParam(value = "state", required = false) String state,//
	ModelMap model) {

		Warehousing w = goodService.detailWarehousing(id);

		model.put("w", w);
		Map<String, Good> lhm = new LinkedHashMap<String, Good>();
		for (Warehousing_Good wg : w.getWgList()) {
			Good g = goodService.detailGood(wg.getGood().getId());
			lhm.put(wg.getGood().getId() + "", g);
		}

		model.put("goodMap", lhm);
		return "detailWarehousing";
	}

	@RequestMapping("/warehousingRecord")
	public String warehousingRecord(@ModelAttribute(value = "pageInfo_warehousing") PageInfo<Warehousing> pageInfo,//
			@RequestParam(value = "page", required = false) String pageParam, //
			@RequestParam(value = "pageSize", required = false) Integer pageSizeParam, //
			Warehousing w, ModelMap model) {
		int page = pageInfo.getPage();
		int pageSize = pageInfo.getPageSize();
		if (pageParam != null) {
			page = Integer.parseInt(pageParam);
		}
		if (pageSizeParam != null && pageSizeParam!=0) {
			pageSize = pageSizeParam;
		}
		Warehousing filter = pageInfo.getFilter();
		Map conditionMap = pageInfo.getConditionMap();
		List<Warehousing> warehousings = goodService.listWarehousing(filter, pageSize, page, conditionMap);
		int rowCount = goodService.countWarehousing(filter, conditionMap);
		PageInfo<Warehousing> pageInfo2 = new PageInfo<Warehousing>(filter, rowCount, pageSize, page, conditionMap);
		model.addAttribute("pageInfo_warehousing", pageInfo2);
		model.put("warehousings", warehousings);
		return "warehousingRecord";
	}
	
	@RequestMapping("/warehousingSearch")
	public String warehousingSearch(@ModelAttribute(value = "pageInfo_warehousing") PageInfo<Warehousing> pageInfo,//
			Warehousing w, ModelMap model) {
//		List<Warehousing> warehousings = goodService.listWarehousing(w, pageInfo.getConditionMap());
//		model.put("warehousings", warehousings);
		return "warehousingRecord";
	}

	@InitBinder
	public void binder(WebDataBinder binder) {
		binder.registerCustomEditor(Timestamp.class, new PropertyEditorSupport() {
			public void setAsText(String value) {
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
