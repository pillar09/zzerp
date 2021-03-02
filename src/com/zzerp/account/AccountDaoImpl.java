package com.zzerp.account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class AccountDaoImpl implements AccountDao {

	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public int countAccount(Map<String, Object> conditionMap) {
		String sql = "SELECT count(id) FROM account WHERE id != 0";
		int count = jdbcTemplate.queryForInt(sql);
		return count;
	}

	@Override
	public List<Account> listAccount(int pageSize, int page, Map<String, Object> conditionMap) {
		String sql = "SELECT a.id,a.username,a.cipher,a.description,a.phone_num,a.full_name,a.mail,a.state,a.male,a.num," +
				" c.id capacity_id, c.privilege capacity_privilege, c.post capacity_post, c.description capacity_description" +
				" FROM account a,capacity c WHERE a.id != 0 AND c.id=a.capacity_id " + " ORDER BY a.id DESC" +
				" LIMIT " + pageSize + " OFFSET " + (page - 1) * pageSize;

		//String sql = "SELECT * FROM account LIMIT " + pageSize + " OFFSET " + (page - 1) * pageSize;
		List<Account> list = jdbcTemplate.query(sql, new RowMapper<Account>() {
			public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
				Account a = new Account();
				a.setId(rs.getInt("id"));
				a.setUsername(rs.getString("username"));
				a.getCapacity().setId(rs.getInt("capacity_id"));
				a.setFull_name(rs.getString("full_name"));
				a.setPhone_num(rs.getString("phone_num"));
				a.setState(rs.getInt("state"));
				a.setMail(rs.getString("mail"));
				a.setDescription(rs.getString("description"));
				a.setCipher(rs.getString("cipher"));
				a.setMale(rs.getBoolean("male"));
				a.setNum(rs.getString("num"));
				a.getCapacity().setPost(rs.getString("capacity_post"));
				a.getCapacity().setPrivilege(rs.getString("capacity_privilege"));
				a.getCapacity().setDescription(rs.getString("capacity_description"));
				return a;
			}
		});
		return list;
	}

	@Override
	public int addAccount(final Account account) {
		final String sql = "INSERT INTO account (username,cipher,capacity_id,description,phone_num,mail,state,full_name,male,num) " + "VALUES(?,?,?,?,?,?,?,?,?,?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		int result = jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
				ps.setString(1, account.getUsername());
				ps.setString(2, account.getCipher());
				ps.setInt(3, account.getCapacity().getId());
				ps.setString(4, account.getDescription());
				ps.setString(5, account.getPhone_num());
				ps.setString(6, account.getMail());
				ps.setInt(7, account.getState());
				ps.setString(8, account.getFull_name());
				ps.setBoolean(9, account.getMale());
				ps.setString(10, account.getNum());
				return ps;
			}
		}, keyHolder);

		int id = keyHolder.getKey().intValue(); //now contains the generated key
		return id;
	}

	@Override
	public List<Capacity> listCapacity(int i) {
		String sql = "SELECT * FROM capacity ";
		List<Capacity> list = jdbcTemplate.query(sql, new RowMapper<Capacity>() {
			public Capacity mapRow(ResultSet rs, int rowNum) throws SQLException {
				Capacity actor = new Capacity();
				actor.setId(rs.getInt("id"));
				actor.setPrivilege(rs.getString("privilege"));
				actor.setPost(rs.getString("post"));
				actor.setDescription(rs.getString("description"));
				return actor;
			}
		});
		return list;
	}

	@Override
	public Account detailAccount(int id) {
		String sql = "SELECT a.id,a.username,a.cipher,a.description,a.phone_num,a.full_name,a.mail,a.state,a.male,a.num," +
				" c.id capacity_id, c.privilege capacity_privilege, c.post capacity_post,c.description capacity_description" +
				" FROM account a,capacity c WHERE c.id=a.capacity_id AND a.id=" + id;
		Account acc = jdbcTemplate.queryForObject(sql, new RowMapper<Account>() {
			public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
				Account a = new Account();
				a.setId(rs.getInt("id"));
				a.setUsername(rs.getString("username"));
				a.getCapacity().setId(rs.getInt("capacity_id"));
				a.setFull_name(rs.getString("full_name"));
				a.setPhone_num(rs.getString("phone_num"));
				a.setState(rs.getInt("state"));
				a.setMail(rs.getString("mail"));
				a.setDescription(rs.getString("description"));
				a.setCipher(rs.getString("cipher"));
				a.setMale(rs.getBoolean("male"));
				a.setNum(rs.getString("num"));
				a.getCapacity().setPost(rs.getString("capacity_post"));
				a.getCapacity().setPrivilege(rs.getString("capacity_privilege"));
				a.getCapacity().setDescription(rs.getString("capacity_description"));
				return a;
			}
		});
		return acc;
	}

	@Override
	public int updateAccount(final Account account) {
		String sql = "UPDATE account SET description=?,phone_num=?,mail=?,state=?,full_name=?,male=?,num=?,capacity_id=? WHERE id=" + account.getId();
		if(account.getCapacity().getId()==null){
			sql= "UPDATE account SET description=?,phone_num=?,mail=?,state=?,full_name=?,male=?,num=? WHERE id=" + account.getId();
		}
		
		int result = jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				//ps.setString(1, account.getUsername());
				//ps.setString(2, account.getCipher());
				ps.setString(1, account.getDescription());
				ps.setString(2, account.getPhone_num());
				ps.setString(3, account.getMail());
				ps.setInt(4, account.getState());
				ps.setString(5, account.getFull_name());
				ps.setBoolean(6, account.getMale());
				ps.setString(7, account.getNum());
				if(account.getCapacity().getId()!=null){
					ps.setInt(8, account.getCapacity().getId());
				}
			}

		});
		return result;
	}

	@Override
	public int deleteAccount(int id) {
		String sql = "DELETE FROM account WHERE id = " + id;
		return jdbcTemplate.update(sql);
	}

	@Override
	public Account detailAccount(String username) {
		String sql = "SELECT a.id,a.username,a.cipher,a.description,a.phone_num,a.full_name,a.mail,a.state,a.male,a.num," +
				" c.id capacity_id, c.privilege capacity_privilege, c.post capacity_post, c.description capacity_description" +
				" FROM account a,capacity c WHERE c.id=a.capacity_id AND a.username='" + username + "'";
		//String sql = "SELECT * FROM account a WHERE a.username='" + username + "'";
		List<Account> list = jdbcTemplate.query(sql, new RowMapper<Account>() {
			public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
				Account a = new Account();
				a.setId(rs.getInt("id"));
				a.setUsername(rs.getString("username"));
				a.getCapacity().setId(rs.getInt("capacity_id"));
				a.setFull_name(rs.getString("full_name"));
				a.setPhone_num(rs.getString("phone_num"));
				a.setState(rs.getInt("state"));
				a.setMail(rs.getString("mail"));
				a.setDescription(rs.getString("description"));
				a.setCipher(rs.getString("cipher"));
				a.setMale(rs.getBoolean("male"));
				a.setNum(rs.getString("num"));
				a.getCapacity().setPost(rs.getString("capacity_post"));
				a.getCapacity().setPrivilege(rs.getString("capacity_privilege"));
				a.getCapacity().setDescription(rs.getString("capacity_description"));
				return a;
			}
		});
		if(list.size()>0){			
			return list.get(0);
		}
		return null;
	}

	@Override
	public int updatePassword(final Account account) {
		String sql = "UPDATE account SET cipher=? WHERE username='" + account.getUsername() + "'";

		int result = jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, account.getCipher());
			}

		});
		return result;
	}

	@Override
	public boolean checkUsername(String username) {
		int count = jdbcTemplate.queryForInt("SELECT count(id) FROM account WHERE username='" + username + "'");
		return count > 0;
	}

	@Override
	public List<Account> listAccount(String[] privilege) {
		StringBuffer sbuff = new StringBuffer("");
		for (int i = 0; i < privilege.length; i++) {
			sbuff.append('\'' + privilege[i] + '\'');
			if (i != (privilege.length - 1)) {
				sbuff.append(',');
			}
		}

		String sql = "SELECT a.id,a.username,a.cipher,a.description,a.phone_num,a.full_name,a.mail,a.state,a.male,a.num," +
				" c.id capacity_id, c.privilege capacity_privilege, c.post capacity_post, c.description capacity_description" +
				" FROM account a,capacity c  WHERE a.id != 0 AND c.id=a.capacity_id AND c.privilege in (" + sbuff.toString() + ")" +
				" ORDER BY a.id DESC";

		List<Account> list = jdbcTemplate.query(sql, new RowMapper<Account>() {
			public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
				Account a = new Account();
				a.setId(rs.getInt("id"));
				a.setUsername(rs.getString("username"));
				a.getCapacity().setId(rs.getInt("capacity_id"));
				a.setFull_name(rs.getString("full_name"));
				a.setPhone_num(rs.getString("phone_num"));
				a.setState(rs.getInt("state"));
				a.setMail(rs.getString("mail"));
				a.setDescription(rs.getString("description"));
				a.setCipher(rs.getString("cipher"));
				a.setMale(rs.getBoolean("male"));
				a.setNum(rs.getString("num"));
				a.getCapacity().setPost(rs.getString("capacity_post"));
				a.getCapacity().setPrivilege(rs.getString("capacity_privilege"));
				a.getCapacity().setDescription(rs.getString("capacity_description"));
				return a;
			}
		});
		return list;

	}

	@Override
	public List<Account> listAccount(String privilege) {

		String sql = "SELECT a.id,a.username,a.cipher,a.description,a.phone_num,a.full_name,a.mail,a.state,a.male,a.num," +
				" c.id capacity_id, c.privilege capacity_privilege, c.post capacity_post, c.description capacity_description" +
				" FROM account a,capacity c  WHERE a.id != 0 AND c.id=a.capacity_id AND c.privilege = '"+privilege+"'" +
				" ORDER BY a.id DESC";

		List<Account> list = jdbcTemplate.query(sql, new RowMapper<Account>() {
			public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
				Account a = new Account();
				a.setId(rs.getInt("id"));
				a.setUsername(rs.getString("username"));
				a.getCapacity().setId(rs.getInt("capacity_id"));
				a.setFull_name(rs.getString("full_name"));
				a.setPhone_num(rs.getString("phone_num"));
				a.setState(rs.getInt("state"));
				a.setMail(rs.getString("mail"));
				a.setDescription(rs.getString("description"));
				a.setCipher(rs.getString("cipher"));
				a.setMale(rs.getBoolean("male"));
				a.setNum(rs.getString("num"));
				a.getCapacity().setPost(rs.getString("capacity_post"));
				a.getCapacity().setPrivilege(rs.getString("capacity_privilege"));
				a.getCapacity().setDescription(rs.getString("capacity_description"));
				return a;
			}
		});
		return list;

	}
	
	@Override
	public List<Account> listAccount() {
		
		String sql = "SELECT a.id,a.username,a.cipher,a.description,a.phone_num,a.full_name,a.mail,a.state,a.male,a.num," +
		" c.id capacity_id, c.privilege capacity_privilege, c.post capacity_post, c.description capacity_description" +
		" FROM account a,capacity c  WHERE a.id != 0 AND c.id=a.capacity_id AND a.state>=0" +
		" ORDER BY a.id DESC";
		
		List<Account> list = jdbcTemplate.query(sql, new RowMapper<Account>() {
			public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
				Account a = new Account();
				a.setId(rs.getInt("id"));
				a.setUsername(rs.getString("username"));
				a.getCapacity().setId(rs.getInt("capacity_id"));
				a.setFull_name(rs.getString("full_name"));
				a.setPhone_num(rs.getString("phone_num"));
				a.setState(rs.getInt("state"));
				a.setMail(rs.getString("mail"));
				a.setDescription(rs.getString("description"));
				a.setCipher(rs.getString("cipher"));
				a.setMale(rs.getBoolean("male"));
				a.setNum(rs.getString("num"));
				a.getCapacity().setPost(rs.getString("capacity_post"));
				a.getCapacity().setPrivilege(rs.getString("capacity_privilege"));
				a.getCapacity().setDescription(rs.getString("capacity_description"));
				return a;
			}
		});
		return list;
		
	}

}
