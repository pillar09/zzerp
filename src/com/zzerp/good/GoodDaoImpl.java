package com.zzerp.good;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class GoodDaoImpl implements GoodDao {

	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Good> listGood(Good good, int pageSize, int page,Map<String, Object> conditionMap) {
		String order = (String) conditionMap.get("order");
		if (order == null) {
			order = "id";
		}
		String condition = getCondition(good);
		String sql = "SELECT g.num,c.title,g.price,g.description,g.model,g.id,g.state,g.spec,g.reserve,bgs1.ordered,bgs2.total_sold " + //
				"FROM good g LEFT OUTER JOIN (select bg1.good_id, sum(quantity) ordered from bill_good bg1,bill where bg1.bill_id=bill.id AND bill.state=2 group by good_id) bgs1 on g.id=bgs1.good_id " +
				" LEFT OUTER JOIN  (select bg2.good_id, sum(quantity) total_sold from bill_good bg2,bill where bg2.bill_id=bill.id AND (bill.state=3 OR bill.state=9) group by good_id) bgs2 on g.id=bgs2.good_id,"+
				" category c" +
				" WHERE "+condition+" g.category_id = c.id ORDER BY g.category_id,g." + order + " DESC LIMIT " + pageSize + " OFFSET " + (page - 1) * pageSize;
		List<Good> list = jdbcTemplate.query(sql, new RowMapper<Good>() {
			public Good mapRow(ResultSet rs, int rowNum) throws SQLException {
				Good actor = new Good();
				actor.setId(rs.getInt("id"));
				actor.setNum(rs.getString("num"));
				actor.getCategory().setTitle(rs.getString("title"));
				actor.setModel(rs.getString("model"));
				actor.setSpec(rs.getString("spec"));
				actor.setDescription(rs.getString("description"));
				actor.setPrice(rs.getDouble("price"));
				actor.setReserve(rs.getInt("reserve"));
				actor.setOrdered(rs.getInt("ordered"));
				actor.setTotal_sold(rs.getInt("total_sold"));
				return actor;
			}
		});
		return list;
	}

	private String getCondition(Good good) {
		StringBuffer buff  = new StringBuffer("");
		if(good.getCategory()!=null && good.getCategory().getId()>0){
			buff.append(" category_id = "+ good.getCategory().getId()+" AND ");
		}
		if(good.getNum()!=null && !good.getNum().isEmpty()){
			buff.append(" num ILIKE '"+ good.getNum().trim()+"%' AND ");
		}
		if(good.getModel()!=null && !good.getModel().isEmpty()){
			buff.append(" model ILIKE '%"+ good.getModel().trim()+"%' AND ");
		}
		return buff.toString();
	}

	@Override
	public List<Category> listCategory(int parentCateId) {
		String sql = "SELECT * FROM category order by title";

		List<Category> list = jdbcTemplate.query(sql, new RowMapper<Category>() {
			public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
				Category actor = new Category();
				actor.setId(rs.getInt("id"));
				actor.setTitle(rs.getString("title"));
				actor.setDescription(rs.getString("description"));
				return actor;
			}
		});
		return list;
	}

	@Override
	public int addGood(final Good g) {
		String sql = "INSERT INTO good (num,category_id,price,description,model,title,state,purchase_price,spec,factory_model) " +
				"VALUES(?,?,?,?,?,?,?,?,?,?);";

		int result = jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, g.getNum());
				ps.setInt(2, g.getCategory().getId());
				ps.setDouble(3, g.getPrice());
				ps.setString(4, g.getDescription());
				ps.setString(5, g.getModel());
				ps.setString(6, g.getTitle());
				ps.setInt(7, g.getState());
				ps.setDouble(8, g.getPurchase_price());
				ps.setString(9, g.getSpec());
				ps.setString(10, g.getFactory_model());
			}

		});
		return result;
	}

	@Override
	public int count(Good good, Map<String, Object> conditionMap) {
		String condition = getCondition(good);
		String sql = "SELECT count(id) FROM good Where "+condition+" id>0";
		int count = jdbcTemplate.queryForInt(sql);
		return count;
	}

	@Override
	public List<String[]> list(int pageSize, int page, Map<String, Object> conditionMap) {
		String sql = "SELECT g.num,c.title,g.price,g.description,g.model,g.id,g.state,g.spec,g.reserve FROM good g, category c where g.category_id = c.id ORDER BY id DESC LIMIT " + pageSize + " OFFSET " + (page - 1) * pageSize;
		List<String[]> list = jdbcTemplate.query(sql, new RowMapper<String[]>() {
			public String[] mapRow(ResultSet rs, int rowNum) throws SQLException {
				String[] good = new String[8];
				good[0] = rs.getString("id");
				good[1] = rs.getString("num");
				good[2] = rs.getString("title");
				good[3] = rs.getString("model");
				good[4] = rs.getString("price");
				good[5] = rs.getString("state");
				good[6] = rs.getString("spec");
				good[7] = rs.getString("reserve");
				return good;
			}
		});
		return list;
	}

	@Override
	public int deleteGood(int id) {
		int count = jdbcTemplate.queryForInt("SELECT count(id) FROM bill_good WHERE good_id=" + id);
		if (count > 0) {
			return -1;//This Good is Used in bill.
		}
		String sql = "delete from good where id=" + id;
		int result = jdbcTemplate.update(sql);
		return result;
	}

	@Override
	public List<String[]> list(int category_id) {
		String sql = "SELECT g.num,c.title,g.price,g.description,g.model,g.spec,g.id,g.state,g.spec,c.title category_title FROM good g, category c where g.category_id = c.id and g.category_id=" + category_id + " ORDER BY g.category_id,g.num DESC ";
		List<String[]> list = jdbcTemplate.query(sql, new RowMapper<String[]>() {
			public String[] mapRow(ResultSet rs, int rowNum) throws SQLException {
				String[] good = new String[8];
				good[0] = rs.getString("id");
				good[1] = rs.getString("num");
				good[2] = rs.getString("title");
				good[3] = rs.getString("model");
				good[4] = rs.getString("price");
				good[5] = rs.getString("state");
				good[6] = rs.getString("category_title");
				good[7] = rs.getString("spec");
				return good;
			}
		});
		return list;
	}

	@Override
	public Good detailGood(int id) {
		String sql = "SELECT g.num,g.title,g.price,g.description,model,g.id,g.state,g.spec,g.purchase_price,g.factory_model,g.reserve,c.id AS category_id,c.title AS category_title,bgs.sum ordered " +
				"FROM good g  LEFT OUTER JOIN (select good_id, sum(quantity) from bill_good,bill where bill_good.bill_id=bill.id AND bill.state=2 group by good_id) bgs on g.id= bgs.good_id, category c WHERE g.category_id = c.id AND g.id=" + id;
		Good good = jdbcTemplate.queryForObject(sql, new RowMapper<Good>() {
			public Good mapRow(ResultSet rs, int rowNum) throws SQLException {
				Good good = new Good();
				good.setId(rs.getInt("id"));
				good.setNum(rs.getString("num"));
				good.setTitle(rs.getString("title"));
				good.setModel(rs.getString("model"));
				good.setPrice(rs.getDouble("price"));
				good.setState(rs.getInt("state"));
				good.setSpec(rs.getString("spec"));
				good.setPurchase_price(rs.getDouble("purchase_price"));
				good.setFactory_model(rs.getString("factory_model"));
				good.setDescription(rs.getString("description"));
				good.getCategory().setId(rs.getInt("category_id"));
				good.getCategory().setTitle(rs.getString("category_title"));
				good.setReserve(rs.getInt("reserve"));
				good.setOrdered(rs.getInt("ordered"));
				return good;
			}
		});
		return good;
	}

	@Override
	public int updateGood(final Good g) {
		String sql = "UPDATE good SET title=?,num=?,category_id=?,state=?,price=?,description=?,model=?,purchase_price=?,spec=?,factory_model=? WHERE id = ?";

		int result = jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, g.getTitle());
				ps.setString(2, g.getNum());
				ps.setInt(3, g.getCategory().getId());
				ps.setInt(4, g.getState());
				ps.setDouble(5, g.getPrice());
				ps.setString(6, g.getDescription());
				ps.setString(7, g.getModel());
				ps.setDouble(8, g.getPurchase_price());
				ps.setString(9, g.getSpec());
				ps.setString(10, g.getFactory_model());
				ps.setInt(11, g.getId());
			}

		});
		return result;
	}

	@Override
	public int countReserveRecord(ReserveRecord filter, Map<String, Object> conditionMap) {
		String sql = "select count(id) from bill_good where good_id=? and reserve_time is not null";
		Integer count = jdbcTemplate.queryForObject(sql, new Object[] { filter.getId() }, new RowMapper<Integer>() {
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("count");
			}
		});
		sql = "select count(id) from warehousing_good where good_id=? and reserve_time is not null";
		Integer countWarehousing = jdbcTemplate.queryForObject(sql, new Object[] { filter.getId() }, new RowMapper<Integer>() {
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("count");
			}
		});
		return count + countWarehousing;
	}

	@Override
	public List<ReserveRecord> listReserveRecord(ReserveRecord filter, int pageSize, int page, Map<String, Object> conditionMap) {
		
		String sql ="(select id,quantity,bill_id from_id,1 reserve_type,reserve,ordered,reserve_time from bill_good where good_id=? and reserve_time is not null) union " +//
				"(select id,quantity,warehousing_id,2 reserve_type,reserve,ordered,reserve_time from warehousing_good where good_id=? and reserve_time is not null) order by reserve_time DESC"+//
				" LIMIT " + pageSize + " OFFSET " + (page - 1) * pageSize;
		List<ReserveRecord> rrs = jdbcTemplate.query(sql, new Object[]{filter.getId(),filter.getId()}, new RowMapper<ReserveRecord>() {
			public ReserveRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
				ReserveRecord rr = new ReserveRecord();
				rr.setId(rs.getInt("id"));
				rr.setFrom_id(rs.getInt("from_id"));
				rr.setReserve_time(rs.getTimestamp("reserve_time"));
				rr.setQuantity(rs.getInt("quantity"));
				if(null != rs.getObject("reserve")){
					rr.setReserve(rs.getInt("reserve"));
				}
				if(null !=  rs.getObject("ordered")){					
					rr.setOrdered(rs.getInt("ordered"));
				}
				rr.setReserve_type(rs.getInt("reserve_type"));
				return rr;
			}
		});
		return rrs;
	}

}
