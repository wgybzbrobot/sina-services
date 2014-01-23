package cc.pp.sina.tokens.service;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class TokenJDBC {

	private static final String tablename = "wb_member_bind_sina";
	private final Random random = new Random();
	private BasicDataSource dataSource;

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
			String maxIdSql = "SELECT max(bid) as max FROM " + tablename;
			try (Statement statement = conn.createStatement();
				 ResultSet rs = statement.executeQuery(maxIdSql)) {
				while (rs.next()) {
					maxId = rs.getInt("max");
				}
			}

			List<String> result = new ArrayList<>();
			String randomTokensSql = "select bind_access_token from " + tablename + " where bid > ? and `expires` > ? limit ?";
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
			String sql = "SELECT `bind_username` FROM " + tablename + " WHERE `expires` > " + System.currentTimeMillis()
					/ 1000;
			try (Statement statement = conn.createStatement();
				 ResultSet rs = statement.executeQuery(sql)) {
				while (rs.next()) {
					result.add(rs.getLong("bind_username"));
				}
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
