package com.zzerp.core;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ConfigDao {

	@Resource
	private JdbcTemplate jdbcTemplate;

	public String getDefine(String term) {
		String sql = "SELECT define FROM config WHERE term=?";
		Map<String, Object> map = jdbcTemplate.queryForMap(sql, term);
		return map.get("define").toString();
	}

}
