package com.zzerp.good;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.zzerp.bill.Bill;
import com.zzerp.bill.Bill_Good;

@Repository
public class WarehousingDaoImpl implements WarehousingDao {
	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Warehousing> listWarehousing(Warehousing w, int pageSize, int page, Map<String, Object> conditionMap) {
		String sql="SELECT w.*, a.username warehouser_username FROM warehousing w LEFT JOIN account a ON w.warehouser=a.id ORDER BY create_time DESC LIMIT " + pageSize + " OFFSET " + (page - 1) * pageSize;
		
		List<Warehousing> list = jdbcTemplate.query(sql, new RowMapper<Warehousing>() {
			public Warehousing mapRow(ResultSet rs, int rowNum) throws SQLException {
				Warehousing actor = new Warehousing();
				actor.setId(rs.getInt("id"));
				actor.setWarehouse_date(rs.getTimestamp("warehouse_date"));
				actor.getWarehouser().setId(rs.getInt("warehouser"));
				actor.setCreate_time(rs.getTimestamp("create_time"));
				actor.getWarehouser().setUsername(rs.getString("warehouser_username"));
				return actor;
			}
		});
		return list;
	}
	
	@Override
	public List<Good> listGood(int id) {
		String gSql = "SELECT g.*,c.title category_title FROM warehousing_good wg LEFT JOIN good g LEFT JOIN category c ON g.category_id=c.id ON wg.good_id=g.id WHERE warehousing_id = " + id;
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
	public int countWarehousing(Warehousing filter, Map<String, Object> conditionMap) {
		String sql="SELECT count(id) FROM warehousing";
		int count = jdbcTemplate.queryForInt(sql);
		return count;
	}
	
	@Override
	public Warehousing detailWarehousing(int id){
		String sql = "SELECT w.*, a.username warehouser_username"//
			+ " FROM warehousing w LEFT JOIN account a ON w.warehouser=a.id WHERE w.id=" + id;
		Warehousing w = jdbcTemplate.queryForObject(sql, new RowMapper<Warehousing>() {
			public Warehousing mapRow(ResultSet rs, int rowNum) throws SQLException {
				Warehousing actor = new Warehousing();
				actor.setId(rs.getInt("id"));
				actor.setWarehouse_date(rs.getTimestamp("warehouse_date"));
				actor.setCreate_time(rs.getTimestamp("create_time"));
				actor.getWarehouser().setId(rs.getInt("warehouser"));
				actor.setMemo(rs.getString("memo"));
				actor.getWarehouser().setUsername(rs.getString("warehouser_username"));
				return actor;
			}
		});
	
		String gSql = "SELECT wg.*,g.reserve current_reserve FROM warehousing_good wg, good g WHERE warehousing_id = " + id + " AND wg.good_id=g.id ORDER BY wg.id";
		List<Warehousing_Good> wgList = jdbcTemplate.query(gSql, new RowMapper<Warehousing_Good>() {
			public Warehousing_Good mapRow(ResultSet rs, int rowNum) throws SQLException {
				Warehousing_Good actor = new Warehousing_Good();
				actor.setId(rs.getInt("id"));
				actor.getGood().setId(rs.getInt("good_id"));
				actor.setWarehousing_id(rs.getInt("warehousing_id"));
				actor.setQuantity(rs.getInt("quantity"));
				if (rs.getObject("ordered") != null) {
					actor.setOrdered(rs.getInt("ordered"));
				}
				if (rs.getObject("reserve") != null) {
					actor.setReserve(rs.getInt("reserve"));
				}
				actor.getGood().setReserve(rs.getInt("current_reserve"));
				return actor;
			}
		});
	
		w.setWgList(wgList);
		return w;
	}

	@Override
	public int warehouse(final Warehousing w) {
		final String sql = "INSERT INTO warehousing (warehouser,warehouse_date,create_time,memo) VALUES((SELECT id FROM account WHERE username=?),?,now(),?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		int result = jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
				ps.setString(1, w.getWarehouser().getUsername());
				ps.setTimestamp(2, w.getWarehouse_date());
				ps.setString(3, w.getMemo());
				return ps;
			}
		}, keyHolder);

		final int warehousing_id = keyHolder.getKey().intValue();

		final List<Warehousing_Good> actors = w.getWgList();
		int[] updateCounts = jdbcTemplate.batchUpdate(
				"INSERT INTO warehousing_good ( warehousing_id,good_id,quantity,reserve,ordered,reserve_time) VALUES(?,?,?,?,?,now())",
				new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						Warehousing_Good bg = actors.get(i);
						if (null != bg && bg.getGood().getId() != 0) {
							ps.setInt(1, warehousing_id);
							ps.setInt(2, bg.getGood().getId());
							ps.setInt(3, bg.getQuantity());
							ps.setInt(4, bg.getGood().getReserve() + bg.getQuantity());
							ps.setInt(5, bg.getGood().getOrdered());
						}
					}

					public int getBatchSize() {
						return actors.size();
					}
				});
		
//		for(Warehousing_Good actor:actors){
//			String goodSql = "SELECT g.*,c.id category_id,c.title category_title FROM good g, category c WHERE g.category_id = c.id AND g.id="+actor.getGood().getId();
//			Good good = jdbcTemplate.queryForObject(goodSql, new RowMapper<Good>() {
//				public Good mapRow(ResultSet rs, int rowNum) throws SQLException {
//					Good good = new Good();
//					good.setId(rs.getInt("id"));
//					good.setNum(rs.getString("num"));
//					good.setTitle(rs.getString("title"));
//					good.setModel(rs.getString("model"));
//					good.setPrice(rs.getDouble("price"));
//					good.setState(rs.getInt("state"));
//					good.setSpec(rs.getString("spec"));
//					good.setPurchase_price(rs.getDouble("purchase_price"));
//					good.setFactory_model(rs.getString("factory_model"));
//					good.setDescription(rs.getString("description"));
//					good.getCategory().setId(rs.getInt("category_id"));
//					good.getCategory().setTitle(rs.getString("category_title"));
//					good.setReserve(rs.getInt("reserve"));
//					return good;
//				}
//			});
//			actor.setGood(good);
//		}

		//Add to good reserve
		int[] updateGoodCounts = jdbcTemplate.batchUpdate(
				"UPDATE good SET reserve=? WHERE id=?",
				new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						Warehousing_Good wg = actors.get(i);
						ps.setInt(1, wg.getGood().getReserve() + wg.getQuantity());
						ps.setInt(2, wg.getGood().getId());
					}

					public int getBatchSize() {
						return actors.size();
					}
				});
		return warehousing_id;
	}
}
