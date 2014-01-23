package cc.pp.sina.mapred.sql;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeiboJDBC {

	private static Logger logger = LoggerFactory.getLogger(WeiboJDBC.class);

	private Connection conn;
	private String driver;
	private final String url;
	private String user;
	private String password;

	/**
	 * 初始化数据库参数
	 */
	private void init() {

		Properties props = null;
		try {
			props = DbParamsRead.getDbParams();
			driver = props.getProperty("db.driver");
			user = props.getProperty("db.user");
			password = props.getProperty("db.password");
		} catch (FileNotFoundException e) {
			logger.info("FileNotFoundException is occured in " + getClass(), e);
		} catch (IOException e) {
			logger.info("IOException is occured in " + getClass(), e);
		}
	}

	/**
	 * 线上环境
	 */
	public WeiboJDBC(String ip) {

		init();
		url = "jdbc:mysql://" + ip + ":3306/pp_fenxi?useUnicode=true&characterEncoding=utf-8";
	}

	/**
	 * 本地测试
	 */
	public WeiboJDBC(String ip, String user, String password) {

		init();
		this.user = user;
		this.password = password;
		url = "jdbc:mysql://" + ip + ":3306/pp_fenxi?useUnicode=true&characterEncoding=utf-8";
	}

	/**
	 * 判断链接
	 */
	public boolean mysqlStatus() {

		boolean status = true;
		try {
			Class.forName(this.driver);
			this.conn = DriverManager.getConnection(this.url, this.user, this.password);
		} catch (ClassNotFoundException e) {
			logger.info("ClassNotFoundException is occured by mysqlStatus in " + getClass(), e);
			status = false;
		} catch (SQLException e) {
			logger.info("SQLException is occured by mysqlStatus in " + getClass(), e);
			status = false;
		} catch (Exception e) {
			logger.info("Exception is occured by mysqlStatus in " + getClass(), e);
			status = false;
		}

		return status;
	}

	/**
	 * 加载所有Token
	 */
	public List<String> getSinaAllTokens() throws SQLException {

		List<String> result = new ArrayList<String>();
		String sql = "SELECT `oauth_token` FROM `member_bind_sina`";
		Statement statement = this.conn.createStatement();
		ResultSet rs = statement.executeQuery(sql);
		while (rs.next()) {
			result.add(rs.getString("oauth_token"));
		}
		rs.close();
		statement.close();

		return result;
	}

	/**
	 * 获取用于uid和粉丝数
	 */
	public List<String> getSinaUidAndFanscount(String tablename, int low, int high, int threshold) throws SQLException {

		List<String> result = new ArrayList<String>();
		String sql = "SELECT `id`,`followers_count` FROM " + tablename + " WHERE `bid` >= " + low + " AND `bid` <= "
				+ high;
		Statement statement = this.conn.createStatement();
		ResultSet rs = statement.executeQuery(sql);
		while (rs.next()) {
			if (rs.getInt("followers_count") >= threshold) {
				result.add(rs.getString("id"));
			}
		}
		rs.close();
		statement.close();

		return result;
	}

	/**
	 * 获取数据表中最大Id
	 */
	public int getMaxId(String tablename) throws SQLException {

		int max = 0;
		String sql = new String("SELECT MAX(`bid`) `bid` FROM " + tablename);
		Statement statement = this.conn.createStatement();
		ResultSet rs = statement.executeQuery(sql);
		if (rs.next()) {
			max = rs.getInt("bid");
		}
		rs.close();
		statement.close();

		return max;
	}

	/**
	 * 查找用户的真伪属性：
	 *     0————不存在
	 *     1————水军
	 *     2————真实用户
	 */
	public int sinaUserQualityInfo(Long uid) throws SQLException {

		int result = 0;
		String tablename = "sinauserbaseinfo_" + uid % 32;
		String sql = "SELECT `followers_count`,`statuses_count` FROM " + tablename + " WHERE `id` = " + uid;

		try (Statement statement = this.conn.createStatement(); ResultSet rs = statement.executeQuery(sql);) {
			if (rs.next()) {
				if (rs.getInt("followers_count") < 50 || rs.getInt("statuses_count") < 20) {
					result = 1;
				} else if ((rs.getInt("followers_count") * 3 < rs.getInt("statuses_count"))
						&& (rs.getInt("followers_count") < 150)) {
					result = 1;
				} else {
					result = 2;
				}
			}
		}

		return result;
	}

	public int sinaUserQualityInfo1(Long uid) throws SQLException {

		int result = 0;
		String tablename = "sinauserbaseinfo_" + uid % 32;
		String sql = "SELECT `followers_count`,`statuses_count` FROM " + tablename + " WHERE `id` = (?)";
		try (PreparedStatement pstmt = this.conn.prepareStatement(sql);) {
			pstmt.setLong(1, uid);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getInt(1) < 50 || rs.getInt(2) < 20) {
					result = 1;
				} else if ((rs.getInt(1) * 3 < rs.getInt(2)) && (rs.getInt(1) < 150)) {
					result = 1;
				} else {
					result = 2;
				}
			}
		}

		return result;
	}

	/**
	 * 创建皮皮新浪用户粉丝存储表
	 */
	public void createPPSinaUserFansTable(String tablename) throws SQLException {

		String sql = "CREATE TABLE " + tablename + " (" + "`id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '序号',"
				+ "`username` int(10) unsigned NOT NULL COMMENT '用户名',"
				+ "`fansuids` longtext NOT NULL COMMENT '粉丝uid列表',"
				+ "`fanscount` int(10) unsigned NOT NULL COMMENT '粉丝数',"
				+ "`lasttime` int(10) unsigned NOT NULL COMMENT '记录时间',"
				+ "PRIMARY KEY (`id`),UNIQUE KEY `username` (`username`)"
				+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;";
		try (Statement statement = this.conn.createStatement();) {
			statement.execute(sql);
		}
	}

	/**
	 * 插入皮皮新浪用户粉丝数据
	 */
	public void insertPPSinaUserFans(String tablename, String username, String fansuids, int fanscount)
			throws SQLException {

		String sql = "INSERT INTO " + tablename + " (`username`,`fansuids`,`fanscount`,`lasttime`) VALUES (?,?,?,?)";
		try (PreparedStatement pstmt = this.conn.prepareStatement(sql);) {
			pstmt.setLong(1, Long.parseLong(username));
			pstmt.setString(2, fansuids);
			pstmt.setInt(3, fanscount);
			pstmt.setLong(4, System.currentTimeMillis() / 1000);
			pstmt.execute();
		}
	}

	/**
	 * 关闭数据库链接
	 */
	public void sqlClose() {

		try {
			this.conn.close();
		} catch (SQLException e) {
			logger.info("SQLException is occured by sqlClose in WeiboJDBC.");
		}
	}

}
