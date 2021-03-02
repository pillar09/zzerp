package com.zzerp.core;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.zzerp.account.Account;

/**
 * @author liangzhu 操作日志信息类
 */
public class OperationLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8770529366492641233L;
	private BigDecimal id; // 日志id
	private String category; // 日志类别
	private Integer logLevel; // 日志级别
	private Account account; // 操作者用户

	private String content; // 操作内容
	private Date operatedTime; // 操作日期时间
	private String memo; // 说明
	private String moduleName;

	public OperationLog() {

	}

	public OperationLog(BigDecimal id, String category, String moduleName,
			Integer logLevel, Account user, String content, Date operatedTime,
			String memo) {
		this.category = category;
		this.content = content;
		this.id = id;
		this.logLevel = logLevel;
		this.memo = memo;
		this.moduleName = moduleName;
		this.operatedTime = operatedTime;
		this.account = user;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(Integer logLevel) {
		this.logLevel = logLevel;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getOperatedTime() {
		return operatedTime;
	}

	public void setOperatedTime(Date operatedTime) {
		this.operatedTime = operatedTime;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

}
