package com.zzerp.core;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class OperationLogger implements IOperationLogger {

	private final Logger logger = Logger.getLogger(OperationLogger.class);
	private String moduleName;

	public static IOperationLogger getLogger(String moduleName) {
		IOperationLogger logger = loggersHolder.get(moduleName);
		if (logger == null) {
			logger = new OperationLogger(moduleName);
			loggersHolder.put(moduleName, logger);
		}
		return logger;
	}

	private OperationLogger(String moduleName) {
		this.moduleName = moduleName;
	}

	public static String getUsername() {
		String username = "";
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null && auth.getPrincipal() != null) {
				if (auth.getPrincipal() instanceof UserDetails) {
					username = ((UserDetails) auth.getPrincipal()).getUsername();
				} else {
					username = auth.getPrincipal().toString();
				}
			}
//			if (username == null || username.length() == 0) {
//				HttpServletRequest request = ServletActionContext.getRequest();
//				HttpSession session = request.getSession(false);
//				Assertion assertion = (Assertion) (session == null ? request.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION) : session.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION));
//				if (assertion != null) {
//					AttributePrincipal principal = assertion.getPrincipal();
//					username = principal.getName();
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return username;
	}

	private static Map<String, IOperationLogger> loggersHolder = new HashMap<String, IOperationLogger>();

	public void tellAdding(String category, String content) {
		tell(LOG_LEVEL_ADD, category, content);
	}

	public void tellAdding(String category, String content, String memo) {
		tell(LOG_LEVEL_ADD, category, content, memo);
	}

	@Override
	public void tellSearching(String category, String content) {
		tell(LOG_LEVEL_SEARCH, category, content);
	}

	@Override
	public void tellSearching(String category, String content, String memo) {
		tell(LOG_LEVEL_SEARCH, category, content, memo);
	}

	@Override
	public void tellUpdating(String category, String content) {
		tell(LOG_LEVEL_UPDATE, category, content);
	}

	@Override
	public void tellUpdating(String category, String content, String memo) {
		tell(LOG_LEVEL_UPDATE, category, content, memo);
	}

	@Override
	public void tellRemoving(String category, String content) {
		tell(LOG_LEVEL_REMOVE, category, content);
	}

	@Override
	public void tellRemoving(String category, String content, String memo) {
		tell(LOG_LEVEL_REMOVE, category, content, memo);
	}

	@Override
	public void tell(Integer level, String category, String content, String memo) {

		OperationLog ol = new OperationLog();
		ol.setCategory(category);
		ol.setContent(content);
		ol.setMemo(memo);
		ol.setLogLevel(level);
		ol.setOperatedTime(new Date());
		logger.debug("Category=" + ol.getCategory());
		logger.debug("Content=" + ol.getContent());
		logger.debug("OperatedTime=" + ol.getOperatedTime());
		logger.debug("LogLevel=" + ol.getLogLevel());
		logger.debug("ModuleName=" + ol.getModuleName());

//		try {
//			WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(ServletActionContext.getServletContext());
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public void tell(Integer level, String category, String content) {
		tell(level, category, content, "");

	}

	@Override
	public void tell(String category, String content) {
		tell(LOG_LEVEL_SEARCH, category, content);
	}

	@Override
	public void tell(String category, String content, String memo) {
		tell(LOG_LEVEL_SEARCH, category, content, memo);
	}

}
