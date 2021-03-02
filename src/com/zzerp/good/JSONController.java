package com.zzerp.good;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/json")
public class JSONController {

	@RequestMapping(value="{name}", method = RequestMethod.GET)
	public @ResponseBody List<Map>  getShopInJSON(@PathVariable String name) {

		List<Map> list = new ArrayList<Map>();

		LinkedHashMap<String,String> lhm = new LinkedHashMap<String,String>();
		lhm.put("model", "mkyong1");
		lhm.put("mkyong3", "mkyong1");
		lhm.put("s", "10.25");
		list.add(lhm);
		return list;

	}
	
}