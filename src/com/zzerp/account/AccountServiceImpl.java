package com.zzerp.account;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
	@Resource
	private PasswordEncoder zzPasswordEncoder;

	@Resource
	private AccountDao accountDao;

	@Override
	public int countAccount(Map<String, Object> conditionMap) {
		return accountDao.countAccount(conditionMap);
	}

	@Override
	public List<Account> listAccount(int pageSize, int page, Map<String, Object> conditionMap) {
		return accountDao.listAccount(pageSize, page, conditionMap);
	}

	@Override
	public int addAccount(Account account) {
		String psw = account.getCipher();
		if (psw != null && psw.equals(account.getRePassword())) {
			account.setCipher(zzPasswordEncoder.encodePassword(psw, null));
			return accountDao.addAccount(account);
		}
		return 0;
	}

	@Override
	public List<Capacity> listCapacity(int i) {
		return accountDao.listCapacity(i);
	}

	@Override
	public Account detailAccount(int id) {
		return accountDao.detailAccount(id);
	}

	@Override
	public int updateAccount(Account account) {
		return accountDao.updateAccount(account);
	}

	@Override
	public int deleteAccount(int id) {
		return accountDao.deleteAccount(id);
	}

	@Override
	public Account detailAccount(String username) {
		return accountDao.detailAccount(username);
	}

	@Override
	public int updatePassword(Account account) {
		String orgPsw = account.getCipher();
		String rePsw = account.getRePassword();
		String psw = account.getPassword();
		if (psw == null || !psw.equals(rePsw)) {
			return -1;
		}
		Account currentAccount = accountDao.detailAccount(account.getUsername());
		if (!currentAccount.getCipher().equals(zzPasswordEncoder.encodePassword(orgPsw, null))) {
			return -2;
		}
		account.setCipher(zzPasswordEncoder.encodePassword(psw, null));
		return accountDao.updatePassword(account);
	}

	@Override
	public int resetPassword(Account account) {
		account.setCipher(zzPasswordEncoder.encodePassword("666666", null));
		return accountDao.updatePassword(account);
	}

	@Override
	public boolean checkUsername(String username) {
		return accountDao.checkUsername(username);
	}

	@Override
	public List<Account> listAccount(String[] privilege) {
		return accountDao.listAccount(privilege);
	}

	@Override
	public List<Account> listAccount(String privilege) {
		return accountDao.listAccount(privilege);
	}

	@Override
	public List<Account> listAccount() {
		return accountDao.listAccount();
	}

}
