package cc.pp.sina.tokens.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.dbcp.BasicDataSource;

class TokenJDBC {

	private static final String TABLENAME = "wb_member_bind_sina";
	private final Random random = new Random();
	private final BasicDataSource dataSource;

	TokenJDBC(String driver, String url, String user, String password) {
		dataSource = new BasicDataSource();
		dataSource.setDriverClassName(driver);
		dataSource.setUsername(user);
		dataSource.setPassword(password);
		dataSource.setUrl(url);
		dataSource.setTestOnBorrow(true);
		dataSource.setValidationQuery("select 1");
	}

	public void stop() {
		try {
			dataSource.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	List<String> getRandom(int count) {
		try (Connection conn = dataSource.getConnection()) {
			int maxId = 0;
			String maxIdSql = "SELECT max(bid) as max FROM " + TABLENAME;
			try (Statement statement = conn.createStatement(); ResultSet rs = statement.executeQuery(maxIdSql)) {
				while (rs.next()) {
					maxId = rs.getInt("max");
				}
			}

			List<String> result = new ArrayList<>();
			String randomTokensSql = "select bind_access_token from " + TABLENAME + " where bid > ? and `expires` > ? limit ?";
			try (PreparedStatement stmt = conn.prepareStatement(randomTokensSql)) {
				stmt.setInt(1, random.nextInt(maxId - count));
				stmt.setLong(2, System.currentTimeMillis() / 1000);
				stmt.setInt(3, count);
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						result.add(rs.getString("bind_access_token"));
					}
				}
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	List<Long> getSinaUids() {
		try (Connection conn = dataSource.getConnection()) {
			List<Long> result = new ArrayList<>();
			String sql = "SELECT `bind_username` FROM " + TABLENAME + " WHERE `expires` > "
					+ System.currentTimeMillis()
					/ 1000;
			try (Statement statement = conn.createStatement(); ResultSet rs = statement.executeQuery(sql)) {
				while (rs.next()) {
					result.add(rs.getLong("bind_username"));
				}
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取最新的num条token信息
	 */
	public List<TokenInfo> getSinaTokenInfos(int num) {
		try (Connection conn = dataSource.getConnection();) {
			List<TokenInfo> result = new ArrayList<>();
			String sql = "SELECT `bind_username`,`bind_access_token`,max(`expires`) AS `max_expires` FROM " + TABLENAME
					+ " GROUP BY `bind_username` LIMIT " + num;
			try (Statement statement = conn.createStatement(); ResultSet rs = statement.executeQuery(sql);) {
				while (rs.next()) {
					result.add(new TokenInfo(rs.getLong("bind_username"), rs.getString("bind_access_token"), rs
							.getLong("max_expires")));
				}
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 根据Uid获取对应的最新token
	 */
	public String getTokenByUid(String uid) {
		try (Connection conn = dataSource.getConnection();) {
			String sql = "SELECT MAX(`expires`),`bind_access_token` FROM " + TABLENAME + " WHERE `bind_username` = \'"
					+ uid + "\'";
			try (Statement statement = conn.createStatement(); ResultSet rs = statement.executeQuery(sql);) {
				if (rs.next()) {
					return rs.getString("bind_access_token");
				} else {
					return null;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
