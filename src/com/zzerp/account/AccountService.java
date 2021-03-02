package com.zzerp.account;

import java.util.List;
import java.util.Map;

public interface AccountService {

	int countAccount(Map<String, Object> conditionMap);

	List<Account> listAccount(int pageSize, int page, Map<String, Object> conditionMap);

	int addAccount(Account account);

	List<Capacity> listCapacity(int i);

	Account detailAccount(int id);

	int updateAccount(Account account);

	int deleteAccount(int id);

	Account detailAccount(String username);

	int updatePassword(Account account);

	int resetPassword(Account account);

	boolean checkUsername(String username);

	List<Account> listAccount(String[] privilege);

	List<Account> listAccount(String privilege);
	
	List<Account> listAccount();

}
