package com.zzerp.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzerp.core.OperationLogger;
import com.zzerp.core.PageInfo;

@Controller
@RequestMapping("/account")
public class AccountController {
	int pageSize = 10;

	@Resource
	private AccountService accountService;

	@RequestMapping(value = "/manageAccounts", method = RequestMethod.GET)
	public String manageAccounts(ModelMap model, @RequestParam(value = "page", required = false) String pageParam) {
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		int rowCount = accountService.countAccount(conditionMap);
		int page = 1;
		if (pageParam != null) {
			page = Integer.parseInt(pageParam);
		}
		List<Account> listg = accountService.listAccount(pageSize, page, conditionMap);
		PageInfo<Account> pageInfo = new PageInfo<Account>(new Account(), rowCount, pageSize, page, conditionMap);
		model.put("pageInfo", pageInfo);
		model.put("accounts",listg);
		return "manageAccounts";
	}

	@RequestMapping("/addAccount")
	public String addAccount(Account account, ModelMap model) {
		int result = accountService.addAccount(account);
		if (result == 0) {
			List<Capacity> capacitys = accountService.listCapacity(0);
			model.put("capacitys", capacitys);
			model.put("result", result);
			model.put("errMsg", "账户名已存在，或者密码不一致！");
			model.put("account", account);
			return "addAccount";
		}
		return "redirect:manageAccounts.go";
	}

	@RequestMapping("/toAddAccount")
	public String toAddAccount(Account account, ModelMap model) {
		List<Capacity> capacitys = accountService.listCapacity(0);
		model.put("capacitys", capacitys);
		model.put("accountAdding", account);
		return "addAccount";
	}

	@RequestMapping("/toEditAccount")
	public String toEditAccount(@RequestParam(value = "id") int id, ModelMap model) {
		List<Capacity> capacitys = accountService.listCapacity(0);
		Account account = accountService.detailAccount(id);
		model.put("capacitys", capacitys);
		model.put("account", account);
		return "editAccount";
	}

	@RequestMapping("/updateAccount")
	public String updateAccount(Account account, ModelMap model) {
		int result = accountService.updateAccount(account);
		model.put("result", result);
		return "redirect:manageAccounts.go";
	}

	@RequestMapping("/checkUsername")
	public @ResponseBody
	boolean checkUsername(@RequestParam(value = "username") String username, ModelMap model) {
		return accountService.checkUsername(username);
	}

	@RequestMapping("/updateUserCenter")
	public String updateUserCenter(Account account, ModelMap model) {
		int result = accountService.updateAccount(account);
		model.put("result", result);
		return "userCenter";
	}

	@RequestMapping("/userCenter")
	public String userCenter(ModelMap model) {
		Account account = accountService.detailAccount(OperationLogger.getUsername());
		model.put("account", account);
		return "userCenter";
	}

	@RequestMapping("/updatePassword")
	public String updatePassword(Account account, ModelMap model) {
		account.setUsername(OperationLogger.getUsername());
		int result = accountService.updatePassword(account);
		if (result == -1) {
			model.put("errMsg", "两次密码不一致！");
		}

		if (result == -2) {
			model.put("errMsg", "原密码错误！");
		}

		model.put("result", result);
		return "editPassword";
	}

	@RequestMapping("/editPassword")
	public String editPassword(Account account, ModelMap model) {
		return "editPassword";
	}

	@RequestMapping("/resetPassword")
	public String resetPassword(@RequestParam(value = "id") int id, ModelMap model) {
		Account account = accountService.detailAccount(id);
		int result = accountService.resetPassword(account);
		model.put("result", result);
		return "redirect:manageAccounts.go";
	}

	@RequestMapping("/frozeAccount")
	public String frozeAccount(@RequestParam(value = "id") int id) {
		Account account = accountService.detailAccount(id);
		account.setState(-1);
		int result = accountService.updateAccount(account);
		return "redirect:manageAccounts.go";
	}

	@RequestMapping("/unfrozeAccount")
	public String unfrozeAccount(@RequestParam(value = "id") int id) {
		Account account = accountService.detailAccount(id);
		account.setState(0);
		int result = accountService.updateAccount(account);
		return "redirect:manageAccounts.go";
	}

	@RequestMapping("/deleteAccount")
	public String deleteAccount(@RequestParam(value = "id") int id) {
		int result = accountService.deleteAccount(id);
		return "redirect:manageAccounts.go";
	}

}
