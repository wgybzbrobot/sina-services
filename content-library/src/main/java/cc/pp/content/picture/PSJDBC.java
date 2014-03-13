package cc.pp.content.picture;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;

public class PSJDBC {

	private final BasicDataSource dataSource = new BasicDataSource();

	public PSJDBC(String username, String password, String dbname) {
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setUrl("jdbc:mysql://localhost:3306/" + dbname + 
				"?useUnicode=true&characterEncoding=utf-8");
	}

	public Connection getConnection() {
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void close() {
		try {
			dataSource.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取图片数据
	 */
	public HashMap<String,List<UsePicInfo>> getPictureInfo(String tablename) {
		
		HashMap<String,List<UsePicInfo>> result = new HashMap<>();
		String sql = "SELECT * FROM " + tablename;
		try (Connection conn = getConnection();Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(sql);) {
			while (rs.next()) {
				UsePicInfo temp = new UsePicInfo();
				temp.setUid(rs.getLong("uid"));
				temp.setWid(rs.getLong("wid"));
				temp.setBind_username(rs.getString("bind_username"));
				temp.setUrl(rs.getString("url"));
				temp.setMd5(rs.getString("md5"));
				temp.setType(rs.getString("type"));
				temp.setFrom(rs.getString("from"));
				if (result.get(rs.getString("from")) == null) {
					List<UsePicInfo> pics = new ArrayList<>();
					pics.add(temp);
					result.put(rs.getString("from"), pics);
				} else {
					result.get(rs.getString("from")).add(temp);
				}
			}
			return result;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}


}
