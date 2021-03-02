package com.zzerp.bill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.zzerp.good.Good;

@Repository
public class BillDaoImpl implements BillDao {

	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public int countBill(Bill bill, Map<String, Object> conditionMap) {
		List<Object> argList = new LinkedList<Object>();
		String billCondition = getCondition(bill, argList);
		String queryCondition = getCondition(conditionMap);
		String sql = "SELECT count(b.id) FROM bill b WHERE " + billCondition + queryCondition + " b.state > 0";
		Object[] args = argList.toArray();
		int count = jdbcTemplate.queryForInt(sql, args);
		return count;
	}

	@Override
	public int countBillDeleted(Bill bill, Map<String, Object> conditionMap) {
		List<Object> argList = new LinkedList<Object>();
		String billCondition = getCondition(bill, argList);
		String queryCondition = getCondition(conditionMap);
		String sql = "SELECT count(b.id) FROM bill b WHERE " + billCondition + queryCondition + " b.state < 0";
		Object[] args = argList.toArray();
		int count = jdbcTemplate.queryForInt(sql, args);
		return count;
	}

	private String getCondition(Bill bill) {
		if (bill == null) {
			return "";
		}
		StringBuffer buff = new StringBuffer("");
		if (bill.getNum() != null && !bill.getNum().isEmpty()) {
			buff.append(" b.num ILIKE '%" + bill.getNum().trim() + "%' AND");
		}
		if (bill.getConsignee() != null && !bill.getConsignee().isEmpty()) {
			buff.append(" b.consignee ILIKE '%" + bill.getConsignee().trim() + "%' AND");
		}
		if (bill.getSalesman() != null && bill.getSalesman().getId() != null && bill.getSalesman().getId() > 0) {
			buff.append(" b.salesman = " + bill.getSalesman().getId() + " AND");
		}

		if (bill.getBuyer() != null && !bill.getBuyer().isEmpty()) {
			buff.append(" b.buyer = '" + bill.getBuyer().trim() + "' AND");
		}

		if (bill.getMail() != null && !bill.getMail().isEmpty()) {
			buff.append(" b.mail ILIKE '%" + bill.getMail().trim() + "%' AND");
		}

		if (bill.getBuyer_tel_num() != null && !bill.getBuyer_tel_num().isEmpty()) {
			buff.append(" b.buyer_tel_num ILIKE '%" + bill.getBuyer_tel_num().trim() + "%' AND");
		}

		if (bill.getWaybill_num() != null && !bill.getWaybill_num().isEmpty()) {
			buff.append(" b.waybill_num ILIKE '%" + bill.getWaybill_num().trim() + "%' AND");
		}
		if (bill.getState() != 0) {
			buff.append(" b.state =" + bill.getState() + " AND");
		}
		if (bill.getIssue() != null && bill.getIssue() != 0) {
			buff.append(" b.issue =" + bill.getIssue() + " AND");
		}

		return buff.toString();
	}

	private String getCondition(Bill bill, List<Object> args) {
		if (bill == null) {
			return "";
		}
		StringBuffer buff = new StringBuffer("");
		if (bill.getNum() != null && !bill.getNum().isEmpty()) {
			buff.append(" b.num ILIKE ? AND");
			args.add("%" + bill.getNum().trim() + "%");
		}
		if (bill.getConsignee() != null && !bill.getConsignee().isEmpty()) {
			buff.append(" b.consignee ILIKE ? AND");
			args.add("%" + bill.getConsignee().trim() + "%");
		}
		if (bill.getSalesman() != null && bill.getSalesman().getId() != null && bill.getSalesman().getId() > 0) {
			buff.append(" b.salesman = ? AND");
			args.add(bill.getSalesman().getId());
		}

		if (bill.getBuyer() != null && !bill.getBuyer().isEmpty()) {
			buff.append(" b.buyer = ? AND");
			args.add(bill.getBuyer().trim());
		}

		if (bill.getMail() != null && !bill.getMail().isEmpty()) {
			buff.append(" b.mail ILIKE ? AND");
			args.add("%" + bill.getMail().trim() + "%");
		}

		if (bill.getBuyer_tel_num() != null && !bill.getBuyer_tel_num().isEmpty()) {
			buff.append(" b.buyer_tel_num ILIKE ? AND");
			args.add("%" + bill.getBuyer_tel_num().trim() + "%");
		}

		if (bill.getWaybill_num() != null && !bill.getWaybill_num().isEmpty()) {
			buff.append(" b.waybill_num ILIKE ? AND");
			args.add("%" + bill.getWaybill_num().trim() + "%");
		}
		if (bill.getState() != 0) {
			buff.append(" b.state =? AND");
			args.add(bill.getState());
		}
		if (bill.getIssue() != null && bill.getIssue() != 0) {
			buff.append(" b.issue =? AND");
			args.add(bill.getIssue());
		}

		return buff.toString();
	}

	private String getDateCondition(Object start, Object end, String fieldName) {
		if ((start != null && start != "") && (end == null || end == "")) {
			return " b." + fieldName + " between to_date('" + start + "', 'YYYY-MM-DD') and now() AND";
		} else if ((end != null && end != "") && (start == null || start == "")) {
			return " b." + fieldName + " < to_date('" + end + "', 'YYYY-MM-DD') AND";
		} else if ((end != null && end != "") && (start != null && start != "")) {
			return " b." + fieldName + " between to_date('" + start + "', 'YYYY-MM-DD') and to_date('" + end + "', 'YYYY-MM-DD') AND";
		}
		return "";
	}

	private String getCondition(Map<String, Object> conditionMap) {
		if (conditionMap == null) {
			return "";
		}
		StringBuffer buff = new StringBuffer("");

		buff.append(getDateCondition(conditionMap.get("order_time_start"), conditionMap.get("order_time_end"), "order_time"));
		buff.append(getDateCondition(conditionMap.get("pay_time_start"), conditionMap.get("pay_time_end"), "pay_time"));
		buff.append(getDateCondition(conditionMap.get("submit_time_start"), conditionMap.get("submit_time_end"), "submit_time"));
		buff.append(getDateCondition(conditionMap.get("deliver_time_start"), conditionMap.get("deliver_time_end"), "deliver_time"));

		return buff.toString();
	}

	@Override
	public List<Bill> listBill(Bill bill, int pageSize, int page, Map<String, Object> conditionMap) {
		List<Object> argList = new LinkedList<Object>();
		String billCondition = getCondition(bill, argList);
		String queryCondition = getCondition(conditionMap);
		String sql = "SELECT (b.submit_time is not null and b.deliver_time is not null and b.submit_time > b.deliver_time) waybill_changed,"
				+ "b.id,b.num,b.consignee,b.order_time,b.state,b.amount,b.pay_with,b.deliver_with,b.salesman,b.expect_deliver_with,"//
				+ "b.submit_time,b.deliver_time,b.pay_time,b.accept_time,b.link,b.create_user,b.logistics,b.country,b.freight,"//
				+ "a1.full_name create_user_full_name, a1.username create_user_username, a2.full_name salesman_full_name, a2.username salesman_username "//
				+ "FROM bill b left join account a1 on b.create_user=a1.id left join account a2 on b.salesman=a2.id WHERE " + billCondition + queryCondition//
				+ " b.state > 0 ORDER BY b.edit_time DESC NULLS LAST ,b.id DESC LIMIT " + pageSize + " OFFSET " + (page - 1) * pageSize;
		Object[] args = argList.toArray();
		List<Bill> list = jdbcTemplate.query(sql, args, new RowMapper<Bill>() {
			public Bill mapRow(ResultSet rs, int rowNum) throws SQLException {
				Bill actor = new Bill();
				actor.setWaybill_changed(rs.getBoolean("waybill_changed"));
				actor.setId(rs.getInt("id"));
				actor.setNum(rs.getString("num"));
				actor.setState(rs.getInt("state"));
				actor.setConsignee(rs.getString("consignee"));
				actor.setAmount(rs.getDouble("amount"));
				actor.setOrder_time(rs.getTimestamp("order_time"));
				actor.getCreate_user().setFull_name(rs.getString("create_user_full_name"));
				actor.getCreate_user().setUsername(rs.getString("create_user_username"));
				actor.setPay_with(rs.getString("pay_with"));
				actor.setDeliver_with(rs.getString("deliver_with"));
				actor.setExpect_deliver_with(rs.getString("expect_deliver_with"));
				actor.setDeliver_time(rs.getTimestamp("deliver_time"));
				actor.setSubmit_time(rs.getTimestamp("submit_time"));
				actor.setPay_time(rs.getTimestamp("pay_time"));
				actor.setAccept_time(rs.getTimestamp("accept_time"));
				actor.setLink(rs.getString("link"));
				actor.getCreate_user().setId(rs.getInt("create_user"));
				actor.setLogistics(rs.getString("logistics"));
				actor.setCountry(rs.getString("country"));
				actor.setFreight(rs.getDouble("freight"));
				actor.getSalesman().setId(rs.getInt("salesman"));
				actor.getSalesman().setFull_name(rs.getString("salesman_full_name"));
				actor.getSalesman().setUsername(rs.getString("salesman_username"));
				return actor;
			}
		});
		return list;
	}

	@Override
	public List<Bill> listBillDeleted(Bill filter, int pageSize, int page, Map<String, Object> conditionMap) {
		List<Object> argList = new LinkedList<Object>();
		String billCondition = getCondition(filter, argList);
		String queryCondition = getCondition(conditionMap);
		String sql = "SELECT b.id,b.num,b.consignee,b.order_time,b.state,b.amount,b.pay_with,b.deliver_with,b.salesman,"//
				+ "b.pay_time,b.deliver_time,b.pay_time,b.accept_time,b.link,b.create_user,b.logistics,b.country,b.freight,"//
				+ "a1.full_name create_user_full_name, a1.username create_user_username, a2.full_name salesman_full_name, a2.username salesman_username "//
				+ "FROM bill b left join account a1 on b.create_user=a1.id left join account a2 on b.salesman=a2.id WHERE " + billCondition + queryCondition//
				+ " b.state < 0 ORDER BY b.edit_time DESC NULLS LAST LIMIT " + pageSize + " OFFSET " + (page - 1) * pageSize;
		Object[] args = argList.toArray();
		List<Bill> list = jdbcTemplate.query(sql, args, new RowMapper<Bill>() {
			public Bill mapRow(ResultSet rs, int rowNum) throws SQLException {
				Bill actor = new Bill();
				actor.setId(rs.getInt("id"));
				actor.setNum(rs.getString("num"));
				actor.setState(rs.getInt("state"));
				actor.setConsignee(rs.getString("consignee"));
				actor.setAmount(rs.getDouble("amount"));
				actor.setOrder_time(rs.getTimestamp("order_time"));
				actor.getCreate_user().setFull_name(rs.getString("create_user_full_name"));
				actor.getCreate_user().setUsername(rs.getString("create_user_username"));
				actor.setPay_with(rs.getString("pay_with"));
				actor.setDeliver_with(rs.getString("deliver_with"));
				actor.setDeliver_time(rs.getTimestamp("deliver_time"));
				actor.setPay_time(rs.getTimestamp("pay_time"));
				actor.setAccept_time(rs.getTimestamp("accept_time"));
				actor.setLink(rs.getString("link"));
				actor.getCreate_user().setId(rs.getInt("create_user"));
				actor.setLogistics(rs.getString("logistics"));
				actor.setCountry(rs.getString("country"));
				actor.setFreight(rs.getDouble("freight"));
				actor.getSalesman().setId(rs.getInt("salesman"));
				actor.getSalesman().setFull_name(rs.getString("salesman_full_name"));
				actor.getSalesman().setUsername(rs.getString("salesman_username"));
				return actor;
			}
		});
		return list;
	}

	@Override
	public int addBill(final Bill bill) {
		final String sql = "INSERT INTO bill (create_time,title,num,state,create_user,amount,salesman,order_time,buyer,"//
				+ "limit_time,store_num,consignee,phone_num,country,address,zip_code,buyer_note,salesman_note,pay_with,expect_deliver_with,"//
				+ "address2,province,tel_num,fax_num,city,link,mail,buyer_phone_num,pay_time,last_edit_user,buyer_wangwang,"//
				+ "expected_arrival,amount_declared,declaration,buyer_tel_num,"
				+ "waybill_num,deliver_with,deliver_time)"//
//				+ " VALUES(now(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
				+ " VALUES(now(),?,?,?,(SELECT id FROM account WHERE username=?),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,(SELECT id FROM account WHERE username=?),?,?,?,?,?,?,?,?);";
		// int result = jdbcTemplate.update(sql,, ,,,);

		KeyHolder keyHolder = new GeneratedKeyHolder();
		int result = jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
				int index = 1;
				ps.setString(index++, bill.getTitle());
				ps.setString(index++, bill.getNum().trim());
				ps.setInt(index++, bill.getState());
				ps.setString(index++, bill.getCreate_user().getUsername());
//				ps.setInt(index++, bill.getCreate_user().getId());
				//ps.setDouble(index++, bill.getAmount());
				if (bill.getAmount() == null) {
					ps.setNull(index++, java.sql.Types.DOUBLE);
				} else {
					ps.setDouble(index++, bill.getAmount());
				}
				ps.setInt(index++, bill.getSalesman().getId());
				ps.setTimestamp(index++, bill.getOrder_time());
				ps.setString(index++, bill.getBuyer());
				ps.setTimestamp(index++, bill.getLimit_time());
				ps.setString(index++, bill.getStore_num());
				ps.setString(index++, bill.getConsignee());
				ps.setString(index++, bill.getPhone_num());
				ps.setString(index++, bill.getCountry());
				ps.setString(index++, bill.getAddress());
				ps.setString(index++, bill.getZip_code());
				ps.setString(index++, bill.getBuyer_note());
				ps.setString(index++, bill.getSalesman_note());
				ps.setString(index++, bill.getPay_with());
				ps.setString(index++, bill.getExpect_deliver_with());
				ps.setString(index++, bill.getAddress2());
				ps.setString(index++, bill.getProvince());
				ps.setString(index++, bill.getTel_num());
				ps.setString(index++, bill.getFax_num());
				ps.setString(index++, bill.getCity());
				ps.setString(index++, bill.getLink());
				ps.setString(index++, bill.getMail());
				ps.setString(index++, bill.getBuyer_phone_num());
				ps.setTimestamp(index++, bill.getPay_time());
				ps.setString(index++, bill.getLast_edit_user().getUsername());
				ps.setString(index++, bill.getBuyer_wangwang());
				ps.setTimestamp(index++, bill.getExpected_arrival());
				//ps.setDouble(index++, bill.getAmount_declared());
				if (bill.getAmount_declared() == null) {
					ps.setNull(index++, java.sql.Types.DOUBLE);
				} else {
					ps.setDouble(index++, bill.getAmount_declared());
				}
				ps.setString(index++, bill.getDeclaration());
				ps.setString(index++, bill.getBuyer_tel_num());
				ps.setString(index++, bill.getWaybill_num());
				ps.setString(index++, bill.getDeliver_with());
				ps.setTimestamp(index++, bill.getDeliver_time());
				return ps;
			}
		}, keyHolder);

		final int billid = keyHolder.getKey().intValue(); // now contains the
															// generated key
		final List<Bill_Good> actors = bill.getBgList();
		int[] updateCounts = jdbcTemplate.batchUpdate("INSERT INTO bill_good (bill_id,good_id,quantity,price) VALUES(?,?,?,?)", new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Bill_Good bg = actors.get(i);
				if (null != bg && bg.getGood().getId() != 0) {
					ps.setInt(1, billid);
					ps.setInt(2, bg.getGood().getId());
					ps.setInt(3, bg.getQuantity());
					ps.setDouble(4, bg.getPrice());
				} else {
					ps.cancel();
				}
			}

			public int getBatchSize() {
				return actors.size();
			}
		});
		bill.setId(billid);
		return billid;
	}

	@Override
	public int deleteBill(int id) {

		String releaseSql = "DELETE FROM bill_good WHERE bill_id = " + id;
		jdbcTemplate.update(releaseSql);
		String releasePackSql = "DELETE FROM pack WHERE bill_id = " + id;
		jdbcTemplate.update(releasePackSql);
		String deleteSql = "DELETE FROM bill WHERE id =" + id;
		return jdbcTemplate.update(deleteSql);

	}

	@Override
	public int updateBill(final Bill bill) {
		final String sql = "UPDATE bill SET title=?,num=?,amount=?,edit_time=now(),last_edit_user=(SELECT id FROM account WHERE username=?),order_time=?,buyer=?,"//
				+ "limit_time=?,store_num=?,consignee=?,phone_num=?,country=?,address=?,zip_code=?,pay_with=?,expect_deliver_with=?,"//
				+ "address2=?,province=?,tel_num=?,fax_num=?,city=?,link=?,mail=?,buyer_phone_num=?,pay_time=?,"//
				+ "buyer_wangwang=?,expected_arrival=?,amount_declared=?,declaration=?,salesman=?,buyer_tel_num=?" + " WHERE id=" + bill.getId();

		int result = jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				int index = 1;
				ps.setString(index++, bill.getTitle());
				ps.setString(index++, bill.getNum());
				ps.setDouble(index++, bill.getAmount());
				ps.setString(index++, bill.getLast_edit_user().getUsername());
				ps.setTimestamp(index++, bill.getOrder_time());
				ps.setString(index++, bill.getBuyer());
				ps.setTimestamp(index++, bill.getLimit_time());
				ps.setString(index++, bill.getStore_num());
				ps.setString(index++, bill.getConsignee());
				ps.setString(index++, bill.getPhone_num());
				ps.setString(index++, bill.getCountry());
				ps.setString(index++, bill.getAddress());
				ps.setString(index++, bill.getZip_code());

				ps.setString(index++, bill.getPay_with());

				ps.setString(index++, bill.getExpect_deliver_with());

				ps.setString(index++, bill.getAddress2());
				ps.setString(index++, bill.getProvince());
				ps.setString(index++, bill.getTel_num());
				ps.setString(index++, bill.getFax_num());
				ps.setString(index++, bill.getCity());
				ps.setString(index++, bill.getLink());
				ps.setString(index++, bill.getMail());
				ps.setString(index++, bill.getBuyer_phone_num());
				ps.setTimestamp(index++, bill.getPay_time());

				ps.setString(index++, bill.getBuyer_wangwang());
				ps.setTimestamp(index++, bill.getExpected_arrival());
				//ps.setDouble(index++, bill.getAmount_declared());
				if (bill.getAmount_declared() == null) {
					ps.setNull(index++, java.sql.Types.DOUBLE);
				} else {
					ps.setDouble(index++, bill.getAmount_declared());
				}
				ps.setString(index++, bill.getDeclaration());
				ps.setInt(index++, bill.getSalesman().getId());
				ps.setString(index, bill.getBuyer_tel_num());
			}

		});

		List<Bill_Good> bgs = bill.getBgList();

		final List<Bill_Good> actors = new LinkedList<Bill_Good>();
		final List<Bill_Good> actorsUpdate = new LinkedList<Bill_Good>();
		for (int i = 0; i < bgs.size(); i++) {
			Bill_Good bg = bgs.get(i);
			if (0 == bg.getId()) {
				actors.add(bg);
			} else {
				actorsUpdate.add(bg);
			}
		}

		final StringBuffer bgid = new StringBuffer("");
		//有ID的表示旧记录，需要更新
		jdbcTemplate.batchUpdate("UPDATE bill_good SET good_id=?,quantity=?,price=? WHERE id=?", new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Bill_Good bg = actorsUpdate.get(i);
				if (bg == null || bg.getGood().getId() == 0) {
					ps.cancel();
				}
				ps.setInt(1, bg.getGood().getId());
				ps.setInt(2, bg.getQuantity());
				ps.setDouble(3, bg.getPrice());
				ps.setInt(4, bg.getId());
				bg.getId();
				bgid.append(bg.getId());
				if ((i + 1) < actorsUpdate.size()) {
					bgid.append(",");
				}
			}

			public int getBatchSize() {
				return actorsUpdate.size();
			}
		});

		//删除不在列表中的商品，有库存的表明已发货，不能删除
		if (bgid.length() > 0) {
			String deleteSql = "DELETE FROM bill_good WHERE bill_id =" + bill.getId() + " AND id NOT IN (" + bgid.toString() + ") AND reserve IS NULL";
			jdbcTemplate.update(deleteSql);
		}

		//没有ID的说明是新增记录，新增必须放后面，避免前面操作删除
		jdbcTemplate.batchUpdate("INSERT INTO bill_good (bill_id,good_id,quantity,price) VALUES(?,?,?,?)", new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				if (actors.get(i) == null || actors.get(i).getGood().getId() == 0) {
					ps.cancel();
				}
				ps.setInt(1, bill.getId());
				ps.setInt(2, actors.get(i).getGood().getId());
				ps.setInt(3, actors.get(i).getQuantity());
				ps.setDouble(4, actors.get(i).getPrice());
			}

			public int getBatchSize() {
				return actors.size();
			}
		});

		return result;
	}

	@Override
	public Bill detailBill(int id) {
		String sql = "SELECT (b.submit_time is not null and b.deliver_time is not null and b.submit_time > b.deliver_time) waybill_changed,b.*,"
				+ " a1.full_name create_user_full_name, a2.full_name last_edit_user_full_name, a3.full_name consignor_full_name, a4.full_name salesman_full_name,"//
				+ " a1.username create_user_username, a2.username last_edit_username, a3.username consignor_username, a4.username salesman_username"//
				+ " FROM bill b left join account a1 on b.create_user=a1.id left join account a2 on b.last_edit_user=a2.id left join account a3 on b.consignor=a3.id left join account a4 on b.salesman=a4.id WHERE b.id=" + id;
		Bill b = jdbcTemplate.queryForObject(sql, new RowMapper<Bill>() {
			public Bill mapRow(ResultSet rs, int rowNum) throws SQLException {
				Bill actor = new Bill();
				actor.setId(rs.getInt("id"));
				actor.setWeight_declared(rs.getInt("weight_declared"));
				actor.setCreate_time(rs.getTimestamp("create_time"));
				actor.setTitle(rs.getString("title"));
				actor.setNum(rs.getString("num"));
				actor.setState(rs.getInt("state"));
				actor.getCreate_user().setId(rs.getInt("create_user"));
				actor.getSalesman().setId(rs.getInt("salesman"));
				actor.setAmount(rs.getDouble("amount"));
				actor.getLast_edit_user().setId(rs.getInt("last_edit_user"));
				actor.setOrder_time(rs.getTimestamp("order_time"));
				actor.setBuyer(rs.getString("buyer"));
				actor.setLimit_time(rs.getTimestamp("limit_time"));
				actor.setStore_num(rs.getString("store_num"));
				if (rs.getObject("freight") != null && !rs.wasNull()) {
					actor.setFreight(rs.getDouble("freight"));
				}
				actor.setConsignee(rs.getString("consignee"));
				actor.setPhone_num(rs.getString("phone_num"));
				actor.setCountry(rs.getString("country"));
				actor.setAddress(rs.getString("address"));
				actor.setZip_code(rs.getString("zip_code"));
				actor.setBuyer_note(rs.getString("buyer_note"));
				actor.setSalesman_note(rs.getString("salesman_note"));
				actor.setLogistics(rs.getString("logistics"));
				actor.setPay_with(rs.getString("pay_with"));
				actor.setExpect_deliver_with(rs.getString("expect_deliver_with"));
				actor.setDeliver_with(rs.getString("deliver_with"));
				actor.setWaybill_num(rs.getString("waybill_num"));
				actor.setBuyer_wangwang(rs.getString("buyer_wangwang"));
				actor.setExpected_arrival(rs.getTimestamp("expected_arrival"));
				//actor.setAmount_declared(rs.getDouble("amount_declared"));
				if (rs.getObject("amount_declared") != null && !rs.wasNull()) {
					actor.setAmount_declared(rs.getDouble("amount_declared"));
				}
				actor.setDeclaration(rs.getString("declaration"));
				actor.setAddress2(rs.getString("address2"));
				actor.setProvince(rs.getString("province"));
				actor.setTel_num(rs.getString("tel_num"));
				actor.setFax_num(rs.getString("fax_num"));
				actor.setCity(rs.getString("city"));
				actor.setLink(rs.getString("link"));
				actor.setMail(rs.getString("mail"));
				actor.setBuyer_phone_num(rs.getString("buyer_phone_num"));
				actor.setPay_time(rs.getTimestamp("pay_time"));
				actor.setSubmit_time(rs.getTimestamp("submit_time"));
				actor.setDeliver_time(rs.getTimestamp("deliver_time"));
				actor.setAccept_time(rs.getTimestamp("accept_time"));
				actor.setBuyer_tel_num(rs.getString("buyer_tel_num"));
				actor.setWaybill_changed(rs.getBoolean("waybill_changed"));
				actor.setIssue(rs.getInt("issue"));
				actor.getCreate_user().setFull_name(rs.getString("create_user_full_name"));
				actor.getLast_edit_user().setFull_name(rs.getString("last_edit_user_full_name"));
				actor.getConsignor().setFull_name(rs.getString("consignor_full_name"));
				actor.getSalesman().setFull_name(rs.getString("salesman_full_name"));
				actor.getCreate_user().setUsername(rs.getString("create_user_username"));
				actor.getLast_edit_user().setUsername(rs.getString("last_edit_username"));
				actor.getConsignor().setUsername(rs.getString("consignor_username"));
				actor.getSalesman().setUsername(rs.getString("salesman_username"));
				return actor;
			}
		});

		String gSql = "SELECT bg.*,g.reserve reserve_current,bgs.sum ordered_current FROM bill_good bg, good g LEFT OUTER JOIN (select good_id, sum(quantity) from bill_good,bill where bill_good.bill_id=bill.id AND bill.state=2 group by good_id) bgs on g.id= bgs.good_id WHERE bill_id = " + id + " AND bg.good_id=g.id ORDER BY id";
		List<Bill_Good> bgList = jdbcTemplate.query(gSql, new RowMapper<Bill_Good>() {
			public Bill_Good mapRow(ResultSet rs, int rowNum) throws SQLException {
				Bill_Good actor = new Bill_Good();
				actor.setId(rs.getInt("id"));
				actor.getGood().setId(rs.getInt("good_id"));
				actor.setBill_id(rs.getInt("bill_id"));
				actor.setPrice(rs.getDouble("price"));
				actor.setQuantity(rs.getInt("quantity"));
				if (rs.getObject("reserve") != null) {
					actor.setReserve(rs.getInt("reserve"));
				}
				if (rs.getObject("ordered") != null) {
					actor.setOrdered(rs.getInt("ordered"));
				}
				actor.getGood().setReserve(rs.getInt("reserve_current"));
				actor.getGood().setOrdered(rs.getInt("ordered_current"));
				return actor;
			}
		});

		b.setBgList(bgList);

		String pkSql = "SELECT p.* FROM pack p WHERE p.bill_id = " + id;
		List<Pack> pks = jdbcTemplate.query(pkSql, new RowMapper<Pack>() {

			public Pack mapRow(ResultSet rs, int rowNum) throws SQLException {
				Pack actor = new Pack();
				actor.setId(rs.getInt("id"));
				if (rs.getObject("length") != null && !rs.wasNull()) {
					actor.setLength(rs.getFloat("length"));
				}
				if (rs.getObject("length") != null && !rs.wasNull()) {
					actor.setLength(rs.getFloat("length"));
				}
				if (rs.getObject("state") != null && !rs.wasNull()) {
					actor.setState(rs.getInt("state"));
				}
				if (rs.getObject("width") != null && !rs.wasNull()) {
					actor.setWidth(rs.getFloat("width"));
				}
				if (rs.getObject("height") != null && !rs.wasNull()) {
					actor.setHeight(rs.getFloat("height"));
				}
				if (rs.getObject("weight") != null && !rs.wasNull()) {
					actor.setWeight(rs.getFloat("weight"));
				}
				if (rs.getObject("volume_rate") != null && !rs.wasNull()) {
					actor.setVolume_rate(rs.getInt("volume_rate"));
					if (actor.getLength() != null && actor.getWidth() != null && actor.getHeight() != null && actor.getVolume_rate() != 0) {
						actor.setVolume_weight(new Double(actor.getLength() * actor.getWidth() * actor.getHeight() / new Double(actor.getVolume_rate())));
					}
				}
				if (rs.getObject("barcode") != null && !rs.wasNull()) {
					actor.setBarcode(rs.getString("barcode"));
				}
				if (rs.getObject("deliver_with") != null && !rs.wasNull()) {
					actor.setDeliver_with(rs.getString("deliver_with"));
				}
				actor.setLogistics(rs.getString("logistics"));
				if (rs.getObject("waybill_num") != null && !rs.wasNull()) {
					actor.setWaybill_num(rs.getString("waybill_num"));
				}
				if (rs.getObject("freight") != null && !rs.wasNull()) {
					actor.setFreight(rs.getDouble("freight"));
				}
				return actor;
			}

		});
		Map<Integer, Pack> pkList = new LinkedHashMap<Integer, Pack>();
		for (int i = 0; i < pks.size(); i++) {
			pkList.put(i, pks.get(i));
		}
		b.setPkList(pkList);

		//下面获取申报内容
		List<Declaration> declares = getDeclarationList(id);
		Map<Integer, Declaration> declareMap = new LinkedHashMap<Integer, Declaration>();
		for (int i = 0; i < declares.size(); i++) {
			declareMap.put(i, declares.get(i));
		}
		b.setDeclareList(declareMap);

		return b;

	}

	@Override
	public List<Declaration> getDeclarationList(int id) {
		String declareSql = "SELECT p.* FROM declaration p WHERE p.bill_id = " + id;
		List<Declaration> declares = jdbcTemplate.query(declareSql, new RowMapper<Declaration>() {

			public Declaration mapRow(ResultSet rs, int rowNum) throws SQLException {
				Declaration actor = new Declaration();
				actor.setDeclaration_id(rs.getInt("declaration_id"));
				actor.setDeclaration(rs.getString("declaration"));
				actor.setDeclaration_en(rs.getString("declaration_en"));
				actor.setCurrency(rs.getString("currency"));
				actor.setCode(rs.getString("code"));
				if (rs.getObject("weight") != null && !rs.wasNull()) {
					actor.setWeight(rs.getInt("weight"));
				}
				if (rs.getObject("price") != null && !rs.wasNull()) {
					actor.setPrice(rs.getDouble("price"));
				}
				if (rs.getObject("quantity") != null && !rs.wasNull()) {
					actor.setQuantity(rs.getInt("quantity"));
				}
				if (actor.getQuantity() != null && actor.getPrice() != null) {
					actor.setTotal(actor.getQuantity() * actor.getPrice());
				}
				return actor;
			}

		});
		return declares;
	}

	@Transactional
	@Override
	public int deliver(final Bill bill) {
		final String reserveSql = "select g.id,(g.reserve - bg.quantity) reserve, (bgs.sum - bg.quantity) ordered from bill b, bill_good bg, good g LEFT OUTER JOIN (select good_id, sum(quantity) from bill_good,bill where bill_good.bill_id=bill.id AND bill.state=2 group by good_id) bgs on g.id= bgs.good_id where b.state=2 and b.id=bg.bill_id and bg.good_id = g.id and b.id = " + bill.getId();
		final List<Good> gList = jdbcTemplate.query(reserveSql, new RowMapper<Good>() {
			public Good mapRow(ResultSet rs, int rowNum) throws SQLException {
				Good actor = new Good();
				actor.setId(rs.getInt("id"));
				actor.setReserve(rs.getInt("reserve"));
				actor.setOrdered(rs.getInt("ordered"));
				return actor;
			}
		});

		int[] updateCounts = jdbcTemplate.batchUpdate("UPDATE good SET reserve=? WHERE id=?", new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, gList.get(i).getReserve());
				ps.setInt(2, gList.get(i).getId());
			}

			public int getBatchSize() {
				return gList.size();
			}
		});

		jdbcTemplate.batchUpdate("UPDATE bill_good SET reserve=?,ordered=?,reserve_time=now() WHERE bill_id=? AND good_id=?", new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, gList.get(i).getReserve());
				ps.setInt(2, gList.get(i).getOrdered());
				ps.setInt(3, bill.getId());
				ps.setInt(4, gList.get(i).getId());
			}

			public int getBatchSize() {
				return gList.size();
			}
		});

		final String sql = "UPDATE bill SET freight=?,logistics=?,deliver_with=?,deliver_time=?,waybill_num=?,consignor=(SELECT a.id FROM account a WHERE username=?),state=?" //
				+ " WHERE state=2 and id=" + bill.getId();

		int result = jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {

				if (bill.getFreight() == null) {
					ps.setNull(1, java.sql.Types.DOUBLE);
				} else {
					ps.setDouble(1, bill.getFreight());
				}

				ps.setString(2, bill.getLogistics());
				ps.setString(3, bill.getDeliver_with());
				ps.setTimestamp(4, bill.getDeliver_time());
				ps.setString(5, bill.getWaybill_num());
				ps.setString(6, bill.getConsignor().getUsername());
				ps.setInt(7, Bill.STATE_SENT);
			}

		});

		List<Pack> packAdded = new LinkedList<Pack>();
		List<Pack> packUpdated = new LinkedList<Pack>();

		for (Pack p : bill.getPkList().values()) {
			if (null == p.getId()) {
				packAdded.add(p);
			} else {
				packUpdated.add(p);
			}
		}

		final StringBuffer packIds = new StringBuffer("");

		String pkUpdateSql = "UPDATE pack SET length=?,width=?,height=?,weight=?,volume_rate=?,barcode=?,deliver_with=?,logistics=?,waybill_num=?,freight=? WHERE bill_id=? AND id=?";
		final Pack[] actors = packUpdated.toArray(new Pack[0]);
		int[] updatePkCounts = jdbcTemplate.batchUpdate(pkUpdateSql, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				int ind = 1;
				if (actors[i].getLength() == null) {
					ps.setNull(ind++, java.sql.Types.FLOAT);
				} else {
					ps.setFloat(ind++, actors[i].getLength());
				}
				if (actors[i].getWidth() == null) {
					ps.setNull(ind++, java.sql.Types.FLOAT);
				} else {
					ps.setFloat(ind++, actors[i].getWidth());
				}
				if (actors[i].getHeight() == null) {
					ps.setNull(ind++, java.sql.Types.INTEGER);
				} else {
					ps.setFloat(ind++, actors[i].getHeight());
				}
				if (actors[i].getWeight() == null) {
					ps.setNull(ind++, java.sql.Types.FLOAT);
				} else {
					ps.setFloat(ind++, actors[i].getWeight());
				}
				if (actors[i].getVolume_rate() == null) {
					ps.setNull(ind++, java.sql.Types.INTEGER);
				} else {
					ps.setInt(ind++, actors[i].getVolume_rate());
				}

				ps.setString(ind++, actors[i].getBarcode());
				ps.setString(ind++, actors[i].getDeliver_with());
				ps.setString(ind++, actors[i].getLogistics());
				ps.setString(ind++, actors[i].getWaybill_num());

				if (actors[i].getFreight() == null) {
					ps.setNull(ind++, java.sql.Types.DOUBLE);
				} else {
					ps.setDouble(ind++, actors[i].getFreight());
				}
				ps.setInt(ind++, bill.getId());
				ps.setInt(ind++, actors[i].getId());

				//把ID提出来，用来做删除判断
				packIds.append(actors[i].getId());
				if ((i + 1) < actors.length) {
					packIds.append(",");
				}
			}

			public int getBatchSize() {
				return actors.length;
			}
		});

		//包裹数据如果状态不是null，删除不了，但是后面又会重新插入，所有必须判断是否不在更新列表中
		if (packIds.length() > 0) {
			String deleteSql = "DELETE FROM pack WHERE state IS NULL AND bill_id =" + bill.getId() + " AND id NOT IN  (" + packIds.toString() + ")";
			jdbcTemplate.update(deleteSql);
		}
		//新增的部分放在后面
		String pkSql = "INSERT INTO pack (bill_id,length,width,height,weight,volume_rate,barcode,deliver_with,logistics,waybill_num,freight) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
		final Pack[] actorAddeds = packAdded.toArray(new Pack[0]);
		jdbcTemplate.batchUpdate(pkSql, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				int ind = 1;
				ps.setInt(ind++, bill.getId());
				if (actorAddeds[i].getLength() == null) {
					ps.setNull(ind++, java.sql.Types.FLOAT);
				} else {
					ps.setFloat(ind++, actorAddeds[i].getLength());
				}
				if (actorAddeds[i].getWidth() == null) {
					ps.setNull(ind++, java.sql.Types.FLOAT);
				} else {
					ps.setFloat(ind++, actorAddeds[i].getWidth());
				}
				if (actorAddeds[i].getHeight() == null) {
					ps.setNull(ind++, java.sql.Types.INTEGER);
				} else {
					ps.setFloat(ind++, actorAddeds[i].getHeight());
				}
				if (actorAddeds[i].getWeight() == null) {
					ps.setNull(ind++, java.sql.Types.FLOAT);
				} else {
					ps.setFloat(ind++, actorAddeds[i].getWeight());
				}
				if (actorAddeds[i].getVolume_rate() == null) {
					ps.setNull(ind++, java.sql.Types.INTEGER);
				} else {
					ps.setInt(ind++, actorAddeds[i].getVolume_rate());
				}

				ps.setString(ind++, actorAddeds[i].getBarcode());
				ps.setString(ind++, actorAddeds[i].getDeliver_with());
				ps.setString(ind++, actorAddeds[i].getLogistics());
				ps.setString(ind++, actorAddeds[i].getWaybill_num());

				if (actorAddeds[i].getFreight() == null) {
					ps.setNull(ind++, java.sql.Types.DOUBLE);
				} else {
					ps.setDouble(ind++, actorAddeds[i].getFreight());
				}
			}

			public int getBatchSize() {
				return actorAddeds.length;
			}
		});
		return result;
	}

	@Override
	public int acceptBill(final Bill bill) {
		String sql = "UPDATE bill SET state=" + Bill.STATE_SOLD + ", accept_time=? WHERE id=" + bill.getId();

		int result = jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {

				ps.setTimestamp(1, bill.getAccept_time());

			}

		});
		return result;
	}

	@Override
	public int changeState(int id, int state) {
		String sql = "UPDATE bill SET state=" + state + " WHERE id=" + id;
		int result = jdbcTemplate.update(sql);
		return result;
	}

	@Override
	public int recycleBill(int id) {

		String sql = "SELECT state FROM bill WHERE id=" + id;
		int state = jdbcTemplate.queryForInt(sql);
		int newState = (state > 0) ? -1 : 1;
		sql = "UPDATE bill SET state=" + newState + ",edit_time=now() WHERE id=" + id;
		int result = jdbcTemplate.update(sql);
		return result;
	}

	@Override
	public List<Good> listGood(int id) {
		String gSql = "SELECT g.*,c.title category_title FROM bill_good bg, good g, category c WHERE bg.good_id=g.id AND g.category_id=c.id AND bill_id = " + id;
		List<Good> list = jdbcTemplate.query(gSql, new RowMapper<Good>() {
			public Good mapRow(ResultSet rs, int rowNum) throws SQLException {
				Good actor = new Good();
				actor.setId(rs.getInt("id"));
				actor.setNum(rs.getString("num"));
				actor.getCategory().setTitle(rs.getString("category_title"));
				actor.setModel(rs.getString("model"));
				actor.setSpec(rs.getString("spec"));
				actor.setDescription(rs.getString("description"));
				actor.setPrice(rs.getDouble("price"));
				actor.setReserve(rs.getInt("reserve"));
				return actor;
			}
		});
		return list;
	}

	@Override
	public int fillWaybill(final Bill bill) {
		StringBuffer sql = new StringBuffer("UPDATE bill SET freight=?,logistics=?,deliver_with=?,deliver_time=?,waybill_num=?");
		if (null != bill.getConsignor()) {
			String username = bill.getConsignor().getUsername();
			if (null != username && !username.isEmpty()) {
				sql.append(",consignor=(SELECT id FROM account WHERE username='" + username + "')");
			}
		}
		sql.append(" WHERE id=" + bill.getId());

		int result = jdbcTemplate.update(sql.toString(), new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {

				if (bill.getFreight() == null) {

					ps.setNull(1, java.sql.Types.DOUBLE);
				} else {
					ps.setDouble(1, bill.getFreight());
				}
				ps.setString(2, bill.getLogistics());
				ps.setString(3, bill.getDeliver_with());
				ps.setTimestamp(4, bill.getDeliver_time());
				ps.setString(5, bill.getWaybill_num());

			}

		});

		List<Pack> packAdded = new LinkedList<Pack>();
		List<Pack> packUpdated = new LinkedList<Pack>();

		for (Pack p : bill.getPkList().values()) {
			if (null == p.getId()) {
				packAdded.add(p);
			} else {
				packUpdated.add(p);
			}
		}

		final StringBuffer packIds = new StringBuffer("");

		String pkUpdateSql = "UPDATE pack SET length=?,width=?,height=?,weight=?,volume_rate=?,barcode=?,deliver_with=?,logistics=?,waybill_num=?,freight=? WHERE bill_id=? AND id=?";
		final Pack[] actors = packUpdated.toArray(new Pack[0]);
		int[] updatePkCounts = jdbcTemplate.batchUpdate(pkUpdateSql, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				int ind = 1;
				if (actors[i].getLength() == null) {
					ps.setNull(ind++, java.sql.Types.FLOAT);
				} else {
					ps.setFloat(ind++, actors[i].getLength());
				}
				if (actors[i].getWidth() == null) {
					ps.setNull(ind++, java.sql.Types.FLOAT);
				} else {
					ps.setFloat(ind++, actors[i].getWidth());
				}
				if (actors[i].getHeight() == null) {
					ps.setNull(ind++, java.sql.Types.INTEGER);
				} else {
					ps.setFloat(ind++, actors[i].getHeight());
				}
				if (actors[i].getWeight() == null) {
					ps.setNull(ind++, java.sql.Types.FLOAT);
				} else {
					ps.setFloat(ind++, actors[i].getWeight());
				}
				if (actors[i].getVolume_rate() == null) {
					ps.setNull(ind++, java.sql.Types.INTEGER);
				} else {
					ps.setInt(ind++, actors[i].getVolume_rate());
				}

				ps.setString(ind++, actors[i].getBarcode());
				ps.setString(ind++, actors[i].getDeliver_with());
				ps.setString(ind++, actors[i].getLogistics());
				ps.setString(ind++, actors[i].getWaybill_num());

				if (actors[i].getFreight() == null) {
					ps.setNull(ind++, java.sql.Types.DOUBLE);
				} else {
					ps.setDouble(ind++, actors[i].getFreight());
				}
				ps.setInt(ind++, bill.getId());
				ps.setInt(ind++, actors[i].getId());

				//把ID提出来，用来做删除判断
				packIds.append(actors[i].getId());
				if ((i + 1) < actors.length) {
					packIds.append(",");
				}
			}

			public int getBatchSize() {
				return actors.length;
			}
		});

		//包裹数据如果状态不是null，删除不了，但是后面又会重新插入
		if (packIds.length() > 0) {
			String deleteSql = "DELETE FROM pack WHERE state IS NULL AND bill_id =" + bill.getId() + " AND id NOT IN  (" + packIds.toString() + ")";
			jdbcTemplate.update(deleteSql);
		}
		//新增的部分放在后面
		String pkSql = "INSERT INTO pack (bill_id,length,width,height,weight,volume_rate,barcode,deliver_with,logistics,waybill_num,freight) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
		final Pack[] actorAddeds = packAdded.toArray(new Pack[0]);
		jdbcTemplate.batchUpdate(pkSql, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				int ind = 1;
				ps.setInt(ind++, bill.getId());
				if (actorAddeds[i].getLength() == null) {
					ps.setNull(ind++, java.sql.Types.FLOAT);
				} else {
					ps.setFloat(ind++, actorAddeds[i].getLength());
				}
				if (actorAddeds[i].getWidth() == null) {
					ps.setNull(ind++, java.sql.Types.FLOAT);
				} else {
					ps.setFloat(ind++, actorAddeds[i].getWidth());
				}
				if (actorAddeds[i].getHeight() == null) {
					ps.setNull(ind++, java.sql.Types.INTEGER);
				} else {
					ps.setFloat(ind++, actorAddeds[i].getHeight());
				}
				if (actorAddeds[i].getWeight() == null) {
					ps.setNull(ind++, java.sql.Types.FLOAT);
				} else {
					ps.setFloat(ind++, actorAddeds[i].getWeight());
				}
				if (actorAddeds[i].getVolume_rate() == null) {
					ps.setNull(ind++, java.sql.Types.INTEGER);
				} else {
					ps.setInt(ind++, actorAddeds[i].getVolume_rate());
				}

				ps.setString(ind++, actorAddeds[i].getBarcode());
				ps.setString(ind++, actorAddeds[i].getDeliver_with());
				ps.setString(ind++, actorAddeds[i].getLogistics());
				ps.setString(ind++, actorAddeds[i].getWaybill_num());

				if (actorAddeds[i].getFreight() == null) {
					ps.setNull(ind++, java.sql.Types.DOUBLE);
				} else {
					ps.setDouble(ind++, actorAddeds[i].getFreight());
				}
			}

			public int getBatchSize() {
				return actorAddeds.length;
			}
		});
		return result;
	}

	@Override
	public int countBillPreparing(Bill bill, Map<String, Object> conditionMap) {
		List<Object> argList = new LinkedList<Object>();
		String billCondition = getCondition(bill, argList);
		String queryCondition = getCondition(conditionMap);
		String sql = "SELECT count(distinct b.id) FROM bill b, bill_good bg, good g WHERE " + billCondition + queryCondition //
				+ " g.id = bg.good_id and g.reserve< bg.quantity AND bg.bill_id= b.id ";
		Object[] args = argList.toArray();
		int count = jdbcTemplate.queryForInt(sql, args);
		return count;
	}

	@Override
	public int countBillReady(Bill bill, Map<String, Object> conditionMap) {
		List<Object> argList = new LinkedList<Object>();
		String billCondition = getCondition(bill, argList);
		String queryCondition = getCondition(conditionMap);

		String sql = "select count(b.id) from bill b where " + billCondition + queryCondition //
				+ " b.id not in (select bg.bill_id from bill_good bg, good g where g.id = bg.good_id and g.reserve< bg.quantity)";
		Object[] args = argList.toArray();
		int count = jdbcTemplate.queryForInt(sql, args);
		return count;
	}

	@Override
	public List<Bill> listBillReady(Bill bill, int pageSize, int page, Map<String, Object> conditionMap) {
		List<Object> argList = new LinkedList<Object>();
		String billCondition = getCondition(bill, argList);
		String queryCondition = getCondition(conditionMap);
		String sql = "SELECT (b.submit_time is not null and b.deliver_time is not null and b.submit_time > b.deliver_time) waybill_changed,"
				+ "b.id,b.num,b.consignee,b.order_time,b.state,b.amount,b.pay_with,b.deliver_with,b.salesman,b.expect_deliver_with,"//
				+ "b.pay_time,b.deliver_time,b.pay_time,b.accept_time,b.link,b.create_user,b.logistics,b.country,b.freight,b.waybill_num,"//
				+ "a1.full_name create_user_full_name, a1.username create_user_username, a2.full_name salesman_full_name, a2.username salesman_username "//
				+ "FROM bill b left join account a1 on b.create_user=a1.id left join account a2 on b.salesman=a2.id WHERE " + billCondition + queryCondition//
				+ " b.id not in (select bg.bill_id from bill_good bg, good g where g.id = bg.good_id and g.reserve< bg.quantity) "//
				+ " ORDER BY b.submit_time DESC NULLS LAST LIMIT " + pageSize + " OFFSET " + (page - 1) * pageSize;
		Object[] args = argList.toArray();
		List<Bill> list = jdbcTemplate.query(sql, args, new RowMapper<Bill>() {
			public Bill mapRow(ResultSet rs, int rowNum) throws SQLException {
				Bill actor = new Bill();
				actor.setWaybill_changed(rs.getBoolean("waybill_changed"));
				actor.setId(rs.getInt("id"));
				actor.setNum(rs.getString("num"));
				actor.setState(rs.getInt("state"));
				actor.setConsignee(rs.getString("consignee"));
				actor.setAmount(rs.getDouble("amount"));
				actor.setOrder_time(rs.getTimestamp("order_time"));
				actor.getCreate_user().setFull_name(rs.getString("create_user_full_name"));
				actor.getCreate_user().setUsername(rs.getString("create_user_username"));
				actor.setPay_with(rs.getString("pay_with"));
				actor.setDeliver_with(rs.getString("deliver_with"));
				actor.setExpect_deliver_with(rs.getString("expect_deliver_with"));
				actor.setDeliver_time(rs.getTimestamp("deliver_time"));
				actor.setPay_time(rs.getTimestamp("pay_time"));
				actor.setAccept_time(rs.getTimestamp("accept_time"));
				actor.setLink(rs.getString("link"));
				actor.getCreate_user().setId(rs.getInt("create_user"));
				actor.setLogistics(rs.getString("logistics"));
				actor.setCountry(rs.getString("country"));
				actor.setFreight(rs.getDouble("freight"));
				actor.setWaybill_num(rs.getString("waybill_num"));
				actor.getSalesman().setId(rs.getInt("salesman"));
				actor.getSalesman().setFull_name(rs.getString("salesman_full_name"));
				actor.getSalesman().setUsername(rs.getString("salesman_username"));
				return actor;
			}
		});
		return list;
	}

	@Override
	public List<Bill> listBillPreparing(Bill bill, int pageSize, int page, Map<String, Object> conditionMap) {
		List<Object> argList = new LinkedList<Object>();
		String billCondition = getCondition(bill, argList);
		String queryCondition = getCondition(conditionMap);
		String sql = "SELECT DISTINCT b.id,b.submit_time,(b.submit_time is not null and b.deliver_time is not null and b.submit_time > b.deliver_time) waybill_changed,"
				+ "b.num,b.consignee,b.order_time,b.state,b.amount,b.pay_with,b.deliver_with,b.salesman,b.expect_deliver_with,"//
				+ "b.pay_time,b.deliver_time,b.pay_time,b.accept_time,b.link,b.create_user,b.logistics,b.country,b.freight,b.waybill_num,"//
				+ "a1.full_name create_user_full_name, a1.username create_user_username, a2.full_name salesman_full_name, a2.username salesman_username "//
				+ "FROM bill b left join account a1 on b.create_user=a1.id left join account a2 on b.salesman=a2.id, bill_good bg, good g WHERE " + billCondition + queryCondition//
				+ " g.id = bg.good_id and g.reserve< bg.quantity AND bg.bill_id= b.id" //
				+ " ORDER BY b.submit_time DESC NULLS LAST LIMIT " + pageSize + " OFFSET " + (page - 1) * pageSize;
		Object args[] = argList.toArray();
		List<Bill> list = jdbcTemplate.query(sql, args, new RowMapper<Bill>() {
			public Bill mapRow(ResultSet rs, int rowNum) throws SQLException {
				Bill actor = new Bill();
				actor.setWaybill_changed(rs.getBoolean("waybill_changed"));
				actor.setId(rs.getInt("id"));
				actor.setNum(rs.getString("num"));
				actor.setState(rs.getInt("state"));
				actor.setConsignee(rs.getString("consignee"));
				actor.setAmount(rs.getDouble("amount"));
				actor.setOrder_time(rs.getTimestamp("order_time"));
				actor.getCreate_user().setFull_name(rs.getString("create_user_full_name"));
				actor.getCreate_user().setUsername(rs.getString("create_user_username"));
				actor.setPay_with(rs.getString("pay_with"));
				actor.setDeliver_with(rs.getString("deliver_with"));
				actor.setExpect_deliver_with(rs.getString("expect_deliver_with"));
				actor.setDeliver_time(rs.getTimestamp("deliver_time"));
				actor.setPay_time(rs.getTimestamp("pay_time"));
				actor.setAccept_time(rs.getTimestamp("accept_time"));
				actor.setLink(rs.getString("link"));
				actor.getCreate_user().setId(rs.getInt("create_user"));
				actor.setLogistics(rs.getString("logistics"));
				actor.setCountry(rs.getString("country"));
				actor.setFreight(rs.getDouble("freight"));
				actor.setWaybill_num(rs.getString("waybill_num"));
				actor.getSalesman().setId(rs.getInt("salesman"));
				actor.getSalesman().setFull_name(rs.getString("salesman_full_name"));
				actor.getSalesman().setUsername(rs.getString("salesman_username"));
				return actor;
			}
		});
		return list;
	}

	@Override
	public List<Bill> listByIds(String[] ids) {
		List<Bill> list = new ArrayList<Bill>();

		try {

			for (int i = 0; i < ids.length; i++) {
				int id = Integer.parseInt(ids[i]);
				Bill b = this.detailBill(id);
				list.add(b);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return list;
	}

	@Override
	public List<String[]> list(String[] ids) {
		List<String[]> list = new ArrayList<String[]>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String[] titles = { "ID",
				"编号",
				"金额",
				"创建时间",
				"下单时间",
				"付款时间",
				"提交时间",
				"预计到货时间",
				"最迟发货期限",
				"店铺号",
				"买家姓名",
				"邮箱地址",
				"买家手机",
				"买家电话",
				"买家在线ID",
				"付款方式",
				//
				"收货人",
				"国家/地区",
				"省/州",
				"城市",
				"邮编",
				"地址1",
				"地址2",
				"手机",
				"电话",
				"传真",
				//
				"运费",
				"快递代理",
				"发货方式",
				"物流单号",
				"发货时间",
				//
				"申报金额",
				"申报内容",

		};
		list.add(0, titles);
		try {

			for (int i = 0; i < ids.length; i++) {
				int id = Integer.parseInt(ids[i]);
				Bill b = this.detailBill(id);
				String[] bArray = {
						b.getId() + "",
						b.getNum() + "",
						"$" + b.getAmount(),
						getDateString(format, b.getCreate_time()),
						getDateString(format, b.getOrder_time()),
						getDateString(format, b.getPay_time()),
						getDateString(format, b.getSubmit_time()),
						getDateString(format, b.getExpected_arrival()),
						getDateString(format, b.getLimit_time()),
						b.getStore_num(),
						b.getBuyer(),
						b.getMail(),
						b.getBuyer_phone_num(),
						b.getBuyer_tel_num(),
						b.getBuyer_wangwang(),
						b.getPay_with(),
						//
						b.getConsignee(),
						b.getCountry(),
						b.getProvince(),
						b.getCity(),
						b.getZip_code(),
						b.getAddress(),
						b.getAddress2(),
						// b.getDelay_limit()+"",
						b.getPhone_num(),
						b.getTel_num(),
						b.getFax_num(),
						//
						b.getFreight() + "",
						b.getLogistics(),
						b.getDeliver_with(),
						b.getWaybill_num(),
						getDateString(format, b.getDeliver_time()),
						//
						// b.getCommission_rate()+"",
						b.getAmount_declared() + "",
						b.getDeclaration()
				};
				list.add(i + 1, bArray);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return list;
	}

	private String getDateString(DateFormat formatter, Date date) {
		if (null != date && null != formatter) {
			return formatter.format(date);
		}
		return "";
	}

	@Override
	public int noteBill(int id, final String salesman_note) {
		String sql = "update bill set salesman_note=salesman_note||? where id= " + id;
		return jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {

				ps.setString(1, salesman_note);

			}

		});
	}

	@Override
	public int submitBill(int id) {
		String sql = "update bill set state=2,submit_time=now() where id=" + id;
		return jdbcTemplate.update(sql);
	}

	@Override
	public boolean checkBillNum(String num) {
		String sql = "select count(id) from bill where num like '%" + num + "%'";
		int result = jdbcTemplate.queryForInt(sql);
		if (result > 0) {
			return true;
		}
		return false;

	}

	@Override
	public List<Integer> getBillByNum(String num) {
		String sql = "select id from bill where num like '%" + num + "%'";
		List<Integer> result = jdbcTemplate.query(sql, new RowMapper<Integer>() {

			@Override
			public Integer mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getInt("id");
			}

		});
		return result;

	}

	@Override
	public int acceptBill(int id) {
		String sql = "UPDATE bill SET state=" + Bill.STATE_SOLD + ", accept_time=now() WHERE id=" + id;

		int result = jdbcTemplate.update(sql);
		return result;
	}

	@Override
	public boolean checkPack(String barcode) {
		String sql = "select count(id) from pack where barcode like '" + barcode + "'";
		int result = jdbcTemplate.queryForInt(sql);
		if (result != 0) {
			return true;
//			String sqlCheck = "UPDATE pack SET state=" + Bill.STATE_SENT + " WHERE barcode like '" + barcode + "'";
//			result = jdbcTemplate.update(sqlCheck);
//			if (result > 0) {
//				return true;
//			}
		}

		return false;
	}

	@Override
	public int deliverPacks(String[] barcodes) {
		StringBuffer barcodeString = new StringBuffer("'" + barcodes[0] + "'");
		for (int i = 1; i < barcodes.length; i++) {
			barcodeString.append(",");
			barcodeString.append("'" + barcodes[i] + "'");
		}

		String sqlCheck = "UPDATE pack SET state=" + Bill.STATE_SENT + " WHERE barcode in (" + barcodeString.toString() + ")";
		int result = jdbcTemplate.update(sqlCheck);

		return result;
	}

	@Override
	public int reverseState(int id) {
		String sql = "SELECT state FROM bill WHERE id=" + id;
		int state = jdbcTemplate.queryForInt(sql);
		int newState = -state;
		sql = "UPDATE bill SET state=" + newState + ",edit_time=now() WHERE id=" + id;
		int result = jdbcTemplate.update(sql);
		return newState;
	}

	@Override
	public int issueBill(int id, int issue) {
		String sql = "UPDATE bill SET issue=" + issue + ",edit_time=now() WHERE id=" + id;
		int result = jdbcTemplate.update(sql);
		return result;
	}

	@Override
	public int declare(final Bill bill) {
		String upAmountSql = "UPDATE bill SET amount_declared=?,weight_declared=? WHERE id=?";
		jdbcTemplate.update(upAmountSql, new Object[] { bill.getAmount_declared(), bill.getWeight_declared(), bill.getId() });
		final List<Declaration> packAdded = new LinkedList<Declaration>();
		final List<Declaration> packUpdated = new LinkedList<Declaration>();

		for (Declaration p : bill.getDeclareList().values()) {
			if (null == p.getDeclaration_id()) {
				packAdded.add(p);
			} else {
				packUpdated.add(p);
			}
		}

		final StringBuffer declarationIds = new StringBuffer("");
		String usql = "UPDATE declaration SET declaration=?,quantity=?,price=?,declaration_en=?,code=?,currency=?,weight=? WHERE declaration_id=?";
		jdbcTemplate.batchUpdate(usql, new BatchPreparedStatementSetter() {

			@Override
			public int getBatchSize() {
				return packUpdated.size();
			}

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				int ind = 1;
				Declaration d = packUpdated.get(i);
				ps.setString(ind++, d.getDeclaration());
				if (d.getQuantity() == null) {
					ps.setNull(ind++, java.sql.Types.INTEGER);
				} else {
					ps.setInt(ind++, d.getQuantity());
				}
				if (d.getPrice() == null) {
					ps.setNull(ind++, java.sql.Types.DOUBLE);
				} else {
					ps.setDouble(ind++, d.getPrice());
				}
				ps.setString(ind++, d.getDeclaration_en());
				ps.setString(ind++, d.getCode());
				ps.setString(ind++, d.getCurrency());
				ps.setInt(ind++, d.getWeight());
				ps.setInt(ind++, d.getDeclaration_id());
				declarationIds.append(d.getDeclaration_id());
				if ((i + 1) < packUpdated.size()) {
					declarationIds.append(",");
				}
			}

		});

		//包裹数据如果状态不是null，删除不了，但是后面又会重新插入，所有必须判断是否不在更新列表中
		if (declarationIds.length() > 0) {
			String deleteSql = "DELETE FROM declaration WHERE bill_id =" + bill.getId() + " AND declaration_id NOT IN  (" + declarationIds.toString() + ")";
			jdbcTemplate.update(deleteSql);
		}

		//新增的必须放在后面
		String sql = "INSERT INTO declaration(declaration,bill_id,quantity,price,declaration_en,code,currency,weight) VALUES(?,?,?,?,?,?,?,?)";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public int getBatchSize() {
				return packAdded.size();
			}

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				int ind = 1;
				Declaration d = packAdded.get(i);
				ps.setString(ind++, d.getDeclaration());
				ps.setInt(ind++, bill.getId());
				if (d.getQuantity() == null) {
					ps.setNull(ind++, java.sql.Types.INTEGER);
				} else {
					ps.setInt(ind++, d.getQuantity());
				}
				if (d.getPrice() == null) {
					ps.setNull(ind++, java.sql.Types.DOUBLE);
				} else {
					ps.setDouble(ind++, d.getPrice());
				}
				ps.setString(ind++, d.getDeclaration_en());
				ps.setString(ind++, d.getCode());
				ps.setString(ind++, d.getCurrency());
				ps.setInt(ind++, d.getWeight());
			}

		});
		return 0;
	}

	@Override
	public Integer addPack(final Pack p) {
		if (p.getBill_id() == null) {
			return null;
		}
		final String sql = "insert into pack(bill_id,length,width,height,weight,volume_rate,barcode," +
				"deliver_with,waybill_num,freight,logistics,deliver_time,state)" +
				" values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int result = jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
				int index = 1;
				ps.setDouble(index++, p.getBill_id());
				if (p.getLength() == null) {
					ps.setNull(index++, Types.FLOAT);
				} else {
					ps.setFloat(index++, p.getLength());
				}
				if (p.getWidth() == null) {
					ps.setNull(index++, Types.FLOAT);
				} else {
					ps.setFloat(index++, p.getWidth());
				}
				if (p.getHeight() == null) {
					ps.setNull(index++, Types.FLOAT);
				} else {
					ps.setFloat(index++, p.getHeight());
				}
				if (p.getWeight() == null) {
					ps.setNull(index++, Types.FLOAT);
				} else {
					ps.setFloat(index++, p.getWeight());
				}
				if (p.getVolume_rate() == null) {
					ps.setNull(index++, Types.INTEGER);
				} else {
					ps.setInt(index++, p.getVolume_rate());
				}
				ps.setString(index++, p.getBarcode());
				ps.setString(index++, p.getDeliver_with());
				ps.setString(index++, p.getWaybill_num());
				if (p.getFreight() == null) {
					ps.setNull(index++, Types.DOUBLE);
				} else {
					ps.setDouble(index++, p.getFreight());
				}
				ps.setString(index++, p.getLogistics());
				ps.setTimestamp(index++, p.getDeliver_time());
				ps.setInt(index++, p.getState());
				return ps;
			}
		}, keyHolder);

		final Integer id = keyHolder.getKey().intValue(); // now contains the
		return id;

	}

	@Override
	public Pack getPackByBarcode(String barcode) {
		String sql = "select * from pack where barcode=?";
		List<Pack> list = jdbcTemplate.query(sql, new String[] { barcode }, new RowMapper<Pack>() {
			public Pack mapRow(ResultSet rs, int rowNum) throws SQLException {
				Pack a = new Pack();
				a.setBarcode(rs.getString("barcode"));
				a.setBill_id(rs.getInt("bill_id"));
				a.setLength(rs.getFloat("length"));
				a.setWidth(rs.getFloat("width"));
				a.setHeight(rs.getFloat("height"));
				a.setWeight(rs.getFloat("weight"));
				a.setVolume_rate(rs.getInt("volume_rate"));
				a.setDeliver_with(rs.getString("deliver_with"));
				a.setWaybill_num(rs.getString("waybill_num"));
				a.setFreight(rs.getDouble("freight"));
				a.setLogistics(rs.getString("logistics"));
				a.setDeliver_time(rs.getTimestamp("deliver_time"));
				return a;
			}
		});
		if (list.isEmpty())
			return null;
		else
			return list.get(0);
	}

	@Override
	public Integer updatePack(final Pack p) {
		final String sql = "update pack set length=?,width=?,height=?,weight=?,volume_rate=?," +
				"deliver_with=?,waybill_num=?,freight=?,logistics=?,deliver_time=? where barcode=?";
		int result = jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql);
				int index = 1;
				if (p.getLength() == null) {
					ps.setNull(index++, Types.FLOAT);
				} else {
					ps.setFloat(index++, p.getLength());
				}
				if (p.getWidth() == null) {
					ps.setNull(index++, Types.FLOAT);
				} else {
					ps.setFloat(index++, p.getWidth());
				}
				if (p.getHeight() == null) {
					ps.setNull(index++, Types.FLOAT);
				} else {
					ps.setFloat(index++, p.getHeight());
				}
				if (p.getWeight() == null) {
					ps.setNull(index++, Types.FLOAT);
				} else {
					ps.setFloat(index++, p.getWeight());
				}
				if (p.getVolume_rate() == null) {
					ps.setNull(index++, Types.INTEGER);
				} else {
					ps.setInt(index++, p.getVolume_rate());
				}

				ps.setString(index++, p.getDeliver_with());
				ps.setString(index++, p.getWaybill_num());
				if (p.getFreight() == null) {
					ps.setNull(index++, Types.DOUBLE);
				} else {
					ps.setDouble(index++, p.getFreight());
				}
				ps.setString(index++, p.getLogistics());
				ps.setTimestamp(index++, p.getDeliver_time());
				ps.setString(index++, p.getBarcode());
				return ps;
			}
		});
		return result;

	}

}