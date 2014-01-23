package cc.pp.sina.bozhus.sql;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.domain.bozhus.FansAddDaily;
import cc.pp.sina.domain.bozhus.PPUserFansInfo;
import cc.pp.sina.domain.bozhus.UserAllParamsDomain;
import cc.pp.sina.domain.bozhus.WeiboInfo;
import cc.pp.sina.domain.users.SinaUserInfo;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import com.sina.weibo.model.Status;
import com.sina.weibo.model.User;

public class WeiboJDBC {

	private static Logger logger = LoggerFactory.getLogger(WeiboJDBC.class);

	private String db_driver;

	private String db_user;
	private String db_password;
	private String db_name;
	private String db_ip;
	private String db_port;
	private final String db_url;

	private BasicDataSource datasource;

	/**
	 * 线上环境
	 */
	public WeiboJDBC() {
		dbInit();
		db_url = "jdbc:mysql://" + db_ip + ":" + db_port + "/" + db_name + "?useUnicode=true&characterEncoding=utf-8";
	}

	/**
	 * 本地环境
	 */
	public WeiboJDBC(String dbip, String dbuser, String dbpassword, String dbname) {
		dbInit();
		db_user = dbuser;
		db_password = dbpassword;
		db_url = "jdbc:mysql://" + dbip + ":3306/" + dbname + "?useUnicode=true&characterEncoding=utf-8";
	}

	/**
	 * 链接数据库
	 */
	public boolean dbConnection() {
		datasource = new BasicDataSource();
		datasource.setDriverClassName("com.mysql.jdbc.Driver");
		datasource.setUsername(db_user);
		datasource.setPassword(db_password);
		datasource.setUrl(db_url);
		return true;
	}

	/**
	 * 获取链接
	 */
	private Connection getConnection() {
		try {
			return datasource.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}

	/**
	 * 初始化数据库相关参数
	 */
	public void dbInit() {
		Properties props = null;
		try {
			props = WbDbParamsRead.getDbParams();
			db_driver = props.getProperty("db.driver");
			db_user = props.getProperty("db.user");
			db_password = props.getProperty("db.password");
			db_name = props.getProperty("db.name");
			db_ip = props.getProperty("db.ip");
			db_port = props.getProperty("db.port");
		} catch (IOException e) {
			logger.info("Db params read error." + getClass());
		}
	}

	/**
	 * 关闭数据库
	 */
	public void dbClose() {
		try {
			datasource.close();
		} catch (SQLException e) {
			logger.info("Db close error.");
		}
	}

	@Override
	public String toString() {
		return new StringBuffer().append("Db driver: " + db_driver + "\n").append("Db user: " + db_user + "\n")
				.append("Db password: " + db_password + "\n").append("Db name: " + db_name + "\n")
				.append("Db ip: " + db_ip + "\n").append("Db port: " + db_port).toString();
	}

	/**
	 * 创建博主扩展数据表，目前是用户标签数据
	 */
	public void createExtendTable(String tablename) throws SQLException {

		String sql = "CREATE TABLE " + tablename + " (`id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '序号',"
				+ "`username` bigint(20) unsigned NOT NULL COMMENT '用户名',"
				+ "`tags` varchar(1000) NOT NULL COMMENT '标签数据',`isppuser` tinyint(1) NOT NULL COMMENT '是否是皮皮用户',"
				+ "`isreal` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否是真实用户',"
				+ "PRIMARY KEY (`id`),KEY `username` (`username`)) "
				+ "ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='新浪用户扩展数据表' AUTO_INCREMENT=1 ;";
		try (Connection conn = getConnection(); Statement pstmt = conn.createStatement();) {
			pstmt.execute(sql);
		}
	}

	/**
	 * 创建用户粉丝分析数据表
	 */
	public void createFansAnalysisTable(String tablename) throws SQLException {

		String sql = "CREATE TABLE " + tablename + " (`id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '序号',"
				+ "`username` bigint(20) unsigned NOT NULL COMMENT '用户名'," + "`result` text NOT NULL COMMENT '分析结果',"
				+ "`lasttime` int(10) unsigned NOT NULL COMMENT '最后纪录时间',"
				+ "PRIMARY KEY (`id`),KEY `username` (`username`)) "
				+ "ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='粉丝分析结果数据表' AUTO_INCREMENT=1 ;";
		try (Connection conn = getConnection(); Statement statement = conn.createStatement();) {
			statement.execute(sql);
		}
	}

	/**
	 * 创建皮皮新浪用户粉丝存储表
	 */
	public void createPPSinaUserFansTable(String tablename) throws SQLException {

		String sql = "CREATE TABLE " + tablename + " (" + "`id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '序号',"
				+ "`username` bigint(20) unsigned NOT NULL COMMENT '用户名',"
				+ "`fansuids` longtext NOT NULL COMMENT '粉丝uid列表',"
				+ "`fanscount` int(10) unsigned NOT NULL COMMENT '粉丝数',"
				+ "`lasttime` int(10) unsigned NOT NULL COMMENT '记录时间',"
				+ "PRIMARY KEY (`id`),UNIQUE KEY `username` (`username`)"
				+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;";
		try (Connection conn = getConnection(); Statement statement = conn.createStatement();) {
			statement.execute(sql);
		}
	}

	/**
	 * 创建皮皮新浪用户的实时粉丝数据增量表
	 */
	public void createPPUserFansDaily(String tablename) throws SQLException {

		String sql = "CREATE TABLE " + tablename + " (" + "`id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '序号',"
				+ "`username` bigint(20) unsigned NOT NULL COMMENT '用户名',"
				+ "`addfansuids` mediumtext NOT NULL COMMENT '增加的粉丝uid',"
				+ "`addfanscount` mediumint(8) unsigned NOT NULL COMMENT '增加的粉丝数',"
				+ "`allfanscount` int(10) unsigned NOT NULL COMMENT '所有粉丝数',"
				+ "PRIMARY KEY (`id`),UNIQUE KEY `username` (`username`)"
				+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='皮皮用户粉丝增量数据表' AUTO_INCREMENT=1 ;";
		try (Connection conn = getConnection(); Statement statement = conn.createStatement();) {
			statement.execute(sql);
		}
	}

	/**
	 * 创建皮皮新浪用户关注存储表
	 */
	public void createPPSinaUserFriendsTable(String tablename) throws SQLException {

		String sql = "CREATE TABLE " + tablename + " (" + "`id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '序号',"
				+ "`username` bigint(20) unsigned NOT NULL COMMENT '用户名',"
				+ "`friendsuids` longtext NOT NULL COMMENT '关注uid列表',"
				+ "`friendscount` int(10) unsigned NOT NULL COMMENT '关注数',"
				+ "`lasttime` int(10) unsigned NOT NULL COMMENT '记录时间',"
				+ "PRIMARY KEY (`id`),UNIQUE KEY `username` (`username`)"
				+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;";
		try (Connection conn = getConnection(); Statement statement = conn.createStatement();) {
			statement.execute(sql);
		}
	}

	/**
	 * 创建新浪用户基础信息表
	 */
	public void createSinaBaseInfoTable(String tablename) throws SQLException {

		String sql = "CREATE TABLE " + tablename + " (`bid` int(10) NOT NULL AUTO_INCREMENT,"
				+ "`id` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户名',"
				+ "`screen_name` char(50) NOT NULL DEFAULT '0' COMMENT '微博昵称',"
				+ "`name` char(50) NOT NULL DEFAULT '0' COMMENT '友好显示名称',"
				+ "`province` int(5) NOT NULL DEFAULT '0' COMMENT '省份编码',"
				+ "`city` int(5) NOT NULL DEFAULT '0' COMMENT '城市编码',"
				+ "`location` char(50) NOT NULL DEFAULT '0' COMMENT '地址',"
				+ "`description` varchar(1000) NOT NULL DEFAULT '0' COMMENT '个人描述',"
				+ "`url` char(200) NOT NULL DEFAULT '0' COMMENT '用户博客地址',"
				+ "`profile_image_url` char(200) NOT NULL DEFAULT '0' COMMENT '自定义头像',"
				+ "`domain` char(50) NOT NULL DEFAULT '0' COMMENT '用户个性化URL',"
				+ "`gender` char(2) NOT NULL DEFAULT '0' COMMENT '性别,m--男，f--女,n--未知',"
				+ "`followers_count` int(8) NOT NULL DEFAULT '0' COMMENT '粉丝数',"
				+ "`friends_count` int(8) NOT NULL DEFAULT '0' COMMENT '关注数',"
				+ "`statuses_count` int(8) NOT NULL DEFAULT '0' COMMENT '微博数',"
				+ "`favourites_count` int(8) NOT NULL DEFAULT '0' COMMENT '收藏数',"
				+ "`created_at` int(10) NOT NULL DEFAULT '0' COMMENT '用户创建时间',"
				+ "`verified` tinyint(1) NOT NULL DEFAULT '-1' COMMENT '加V与否，是否微博认证用户',"
				+ "`verified_type` int(5) NOT NULL DEFAULT '-1' COMMENT '认证类型',"
				+ "`avatar_large` char(200) NOT NULL DEFAULT '0' COMMENT '大头像地址',"
				+ "`bi_followers_count` int(8) NOT NULL DEFAULT '0' COMMENT '互粉数',"
				+ "`remark` char(250) NOT NULL DEFAULT '0' COMMENT '备注信息',"
				+ "`verified_reason` varchar(500) NOT NULL DEFAULT '0' COMMENT '认证原因',"
				+ "`weihao` char(50) NOT NULL DEFAULT '0' COMMENT '微信号',"
				+ "`lasttime` int(10) NOT NULL COMMENT '记录时间'," + "PRIMARY KEY (`bid`),KEY `id` (`id`)) "
				+ "ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='新浪用户基础信息表（有用信息）' AUTO_INCREMENT=1 ;";
		try (Connection conn = getConnection(); Statement statement = conn.createStatement();) {
			statement.execute(sql);
		}
	}

	/**
	 * 创建博主全部参数数据表
	 */
	public void createSinaBozhuAllParamsTable(String tablename) throws SQLException {

		String sql = "CREATE TABLE " + tablename + " (" + "`id` int(10) NOT NULL AUTO_INCREMENT,"
				+ "`username` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '用户名',"
				+ "`nickname` varchar(30) NOT NULL DEFAULT '0' COMMENT '昵称',"
				+ "`description` varchar(500) NOT NULL DEFAULT '0' COMMENT '描述',"
				+ "`createdtime` smallint(5) unsigned NOT NULL COMMENT '微博创建天数',"
				+ "`usertags` varchar(250) NOT NULL DEFAULT '0' COMMENT '用户标签',"
				+ "`fanscount` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '粉丝数',"
				+ "`weibocount` mediumint(8) unsigned NOT NULL DEFAULT '0' COMMENT '微博数',"
				+ "`averagewbs` float unsigned NOT NULL DEFAULT '0' COMMENT '平均每天发博数',"
				+ "`influence` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '用户影响力',"
				+ "`activation` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '用户活跃度',"
				+ "`activecount` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '活跃粉丝数',"
				+ "`addvratio` float unsigned NOT NULL DEFAULT '0' COMMENT '加V比例',"
				+ "`activeratio` float unsigned NOT NULL DEFAULT '0' COMMENT '活跃粉丝比例',"
				+ "`maleratio` float unsigned NOT NULL DEFAULT '0' COMMENT '男性粉丝比例',"
				+ "`fansexistedratio` float unsigned NOT NULL DEFAULT '0' COMMENT '粉丝存在比例',"
				+ "`verify` smallint(6) NOT NULL DEFAULT '0' COMMENT '用户认证类型',"
				+ "`allfanscount` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '粉丝的粉丝总数',"
				+ "`allactivefanscount` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '粉丝的活跃粉丝总数',"
				+ "`top5provinces` varchar(250) NOT NULL DEFAULT '0' COMMENT '粉丝区域分布中的前5个地区及比例',"
				+ "`oriratio` float unsigned NOT NULL DEFAULT '0' COMMENT '原创微博比例',"
				+ "`aveorirepcom` float unsigned NOT NULL DEFAULT '0' COMMENT '原创微博平均转评数',"
				+ "`averepcom` float unsigned NOT NULL DEFAULT '0' COMMENT '用户所有微博的平均转评数',"
				+ "`wbsource` varchar(250) NOT NULL DEFAULT '0' COMMENT '用户微博来源分布',"
				+ "`averepsbyweek` float unsigned NOT NULL DEFAULT '0' COMMENT '最近一周内的平均微博转评数',"
				+ "`averepsbymonth` float unsigned NOT NULL DEFAULT '0' COMMENT '最近一个月的微博平均转评数',"
				+ "`validrepcombyweek` float unsigned NOT NULL DEFAULT '0' COMMENT '最近一周有效的平均微博转评数',"
				+ "`validrepcombymonth` float unsigned NOT NULL DEFAULT '0' COMMENT '最近一个月有效的平均微博转评数',"
				+ "`avereposterquality` float unsigned NOT NULL DEFAULT '0' COMMENT '平均转发质量',"
				+ "`aveexposionsum` float unsigned NOT NULL DEFAULT '0' COMMENT '用户微博的平均曝光量',"
				+ "`softretweet` float unsigned NOT NULL DEFAULT '0' COMMENT '软广转发',"
				+ "`softtweet` float unsigned NOT NULL DEFAULT '0' COMMENT '软广直发',"
				+ "`hardretweet` float unsigned NOT NULL DEFAULT '0' COMMENT '硬广转发',"
				+ "`hardtweet` float unsigned NOT NULL DEFAULT '0' COMMENT '硬广直发',"
				+ "`lasttime` int(10) NOT NULL DEFAULT '0' COMMENT '最后纪录时间',"
				+ "PRIMARY KEY (`id`),KEY `username` (`username`),KEY `verify` (`verify`)"
				+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;";
		try (Connection conn = getConnection(); Statement statement = conn.createStatement();) {
			statement.execute(sql);
		}
	}

	/**
	 * 创建新浪用户微博数据表
	 */
	public void createSinaUserWeibosTable(String tablename) throws SQLException {

		String sql = "CREATE TABLE " + tablename + " (`id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '序号',"
				+ "`wid` bigint(20) unsigned NOT NULL COMMENT '微博id',"
				+ "`username` bigint(20) unsigned NOT NULL COMMENT '用户id',"
				+ "`repostscount` mediumint(8) unsigned NOT NULL COMMENT '转发数量',"
				+ "`commentscount` mediumint(8) unsigned NOT NULL COMMENT '评论数量',"
				+ "`attitudescount` mediumint(8) unsigned NOT NULL COMMENT '赞数量',"
				+ "`text` varchar(300) NOT NULL COMMENT '微博内容',"
				+ "`createat` int(10) unsigned NOT NULL COMMENT '微博创建时间'," + "`owid` bigint(20) unsigned NOT NULL,"
				+ "`ousername` bigint(20) unsigned NOT NULL COMMENT '被转发或原创用户d',"
				+ "`favorited` tinyint(1) NOT NULL COMMENT '是否已收藏'," + "`geo` varchar(300) NOT NULL COMMENT '地理信息字段',"
				+ "`latitude` float NOT NULL COMMENT '经度'," + "`longitude` float NOT NULL COMMENT '维度',"
				+ "`originalpic` varchar(500) NOT NULL COMMENT '原始图片地址',"
				+ "`source` varchar(30) NOT NULL COMMENT '微博来源',"
				+ "`visible` varchar(30) NOT NULL COMMENT '微博的可见性及指定可见分组信息',"
				+ "`mlevel` smallint(8) unsigned NOT NULL COMMENT '微博等级',"
				+ "`lasttime` int(10) unsigned NOT NULL COMMENT '采集时间'," + "PRIMARY KEY (`id`),KEY `wid` (`wid`)) "
				+ "ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='新浪的用户微博信息' AUTO_INCREMENT=1 ;";
		try (Connection conn = getConnection(); Statement statement = conn.createStatement();) {
			statement.execute(sql);
		}
	}

	/**
	 * 创建存放微博数据表表名的数据表
	 */
	public void createTablenamesTable(String tablename) throws SQLException {

		String sql = "CREATE TABLE " + tablename + " (" + "`id` int(11) NOT NULL AUTO_INCREMENT,"
				+ "`name` char(50) NOT NULL,PRIMARY KEY (`id`)) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;";
		try (Connection conn = getConnection(); Statement statement = conn.createStatement();) {
			statement.execute(sql);
		}
	}

	/**
	 * 根据bid删除某个用户的基础数据
	 */
	public void deleteSinaBaseInfo(String tablename, int bid) throws SQLException {

		String sql = "DELETE FROM " + tablename + " WHERE `bid` = " + bid;
		try (Connection conn = getConnection(); Statement statement = conn.createStatement();) {
			statement.execute(sql);
		}
	}

	/**
	 * 根据id删除某个用户的标签数据
	 */
	public void deleteSinaTags(String tablename, int id) throws SQLException {

		String sql = "DELETE FROM " + tablename + " WHERE `id` = " + id;
		try (Connection conn = getConnection(); Statement statement = conn.createStatement();) {
			statement.execute(sql);
		}
	}

	/**
	 * 删除某个用户基础信息
	 */
	public void deleteSinaUserBaseInfo(String tablename, String username) throws SQLException {

		String sql = "DELETE FROM " + tablename + " WHERE `id` = " + username;
		try (Connection conn = getConnection(); Statement statement = conn.createStatement();) {
			statement.execute(sql);
		}
	}

	/**
	 * 删除数据表
	 */
	public void dropTable(String tablename) throws SQLException {

		String sql = "DROP TABLE " + tablename;
		try (Connection conn = getConnection(); Statement statement = conn.createStatement();) {
			statement.executeUpdate(sql);
		}
	}

	/**
	 * 获取博主id（皮皮平台）列表
	 */
	public List<Long> getBozhuIds(String tablename) throws SQLException {

		List<Long> result = new ArrayList<>();
		String sql = "SELECT `id` FROM " + tablename;
		try (Connection conn = getConnection();
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(sql);) {
			while (rs.next()) {
				result.add(rs.getLong("id"));
			}
		}

		return result;
	}

	/**
	 * 获取最新的表名
	 */
	public String getLastTablename(String tablename) throws SQLException {

		String result = null;
		String sql = "SELECT `tablename` FROM " + tablename + " ORDER BY `id` DESC LIMIT 1";
		try (Connection conn = getConnection();
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(sql);) {
			if (rs.next()) {
				result = rs.getString("tablename");
			}
		}

		return result;
	}

	/**
	 * 获取数据表中最大Id
	 */
	public int getMaxId(String tablename) throws SQLException {

		int max = 0;
		String sql = new String("SELECT MAX(`id`) `id` FROM " + tablename);
		try (Connection conn = getConnection();
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(sql);) {
			if (rs.next()) {
				max = rs.getInt("id");
			}
		}

		return max;
	}

	/**
	 * 获取皮皮新浪用户粉丝数据
	 */
	public PPUserFansInfo getPPSinaUserFans(String tablename, String username) throws SQLException {

		String sql = "SELECT `fansuids`,`fanscount` FROM " + tablename + " WHERE `username` = " + username;
		try (Connection conn = getConnection();
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(sql);) {
			if (rs.next()) {
				String uids = rs.getString("fansuids");
				if ((uids.length() > 1) && (uids.lastIndexOf(",") == uids.length() - 1)) {
					uids = uids.substring(0, uids.length() - 1);
				}
				return new PPUserFansInfo.Builder(Long.parseLong(username), uids, rs.getInt("fanscount")).build();
			} else {
				return null;
			}
		}
	}

	/**
	 * 获取皮皮新浪用户的实时粉丝更新数据
	 */
	public FansAddDaily getPPUserFansDaily(String tablename, String username) throws SQLException {

		String sql = "SELECT `addfansuids`,`addfanscount`,`allfanscount` FROM " + tablename + " WHERE `username` = "
				+ username;
		try (Connection conn = getConnection();
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(sql);) {
			if (rs.next()) {
				return new FansAddDaily.Builder(username).setAddfansuids(rs.getString("addfansuids"))
						.setAddfanscount(rs.getInt("addfanscount")).setAllfanscount(rs.getInt("allfanscount")).build();
			} else {
				return null;
			}
		}
	}

	/**
	 * 从博主库中获取新浪博主参数信息，根据页数
	 */
	public List<UserAllParamsDomain> getSinaBozhuAllParams(String tablename, int low, int high) throws SQLException {

		List<UserAllParamsDomain> result = new ArrayList<UserAllParamsDomain>();
		String sql = "SELECT * FROM " + tablename + " WHERE `id` >= " + low + " AND `id` <= " + high;
		try (Connection conn = getConnection();
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(sql);) {
			while (rs.next()) {
				UserAllParamsDomain temp = new UserAllParamsDomain.Builder(rs.getString("username"))
						.setNickname(rs.getString("nickname")).setDescription(rs.getString("description"))
						.setCreatedtime(rs.getInt("createdtime")).setUsertags(rs.getString("usertags"))
						.setFanscount(rs.getInt("fanscount")).setWeibocount(rs.getInt("weibocount"))
						.setAveragewbs(rs.getFloat("averagewbs")).setInfluence(rs.getInt("influence"))
						.setActivation(rs.getInt("activation")).setActivecount(rs.getInt("activecount"))
						.setAddvratio(rs.getFloat("addvratio")).setActiveratio(rs.getFloat("activeratio"))
						.setMaleratio(rs.getFloat("maleratio")).setFansexistedratio(rs.getFloat("fansexistedratio"))
						.setVerify(rs.getInt("verify")).setAllfanscount(rs.getLong("allfanscount"))
						.setAllactivefanscount(rs.getLong("allactivefanscount"))
						.setTop5provinces(rs.getString("top5provinces")).setOriratio(rs.getFloat("oriratio"))
						.setAveorirepcom(rs.getFloat("aveorirepcom")).setAverepcom(rs.getFloat("averepcom"))
						.setWbsource(rs.getString("wbsource")).setAverepcombyweek(rs.getFloat("averepsbyweek"))
						.setAverepcombymonth(rs.getFloat("averepsbymonth"))
						.setValidrepcombyweek(rs.getFloat("validrepcombyweek"))
						.setValidrepcombymonth(rs.getFloat("validrepcombymonth"))
						.setAvereposterquality(rs.getFloat("avereposterquality"))
						.setAveexposionsum(rs.getLong("aveexposionsum")).build();
				result.add(temp);
			}
		}

		return result;
	}

	/**
	 * 从博主库中获取新浪博主参数信息，根据用户名uid
	 */
	public UserAllParamsDomain getSinaBozhuAllParams(String tablename, String uid) throws SQLException {

		String sql = "SELECT * FROM " + tablename + " WHERE `username` = " + uid;
		try (Connection conn = getConnection();
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(sql);) {
			if (rs.next()) {
				return new UserAllParamsDomain.Builder(rs.getString("username")).setNickname(rs.getString("nickname"))
						.setDescription(rs.getString("description")).setCreatedtime(rs.getInt("createdtime"))
						.setUsertags(rs.getString("usertags")).setFanscount(rs.getInt("fanscount"))
						.setWeibocount(rs.getInt("weibocount")).setAveragewbs(rs.getFloat("averagewbs"))
						.setInfluence(rs.getInt("influence")).setActivation(rs.getInt("activation"))
						.setActivecount(rs.getInt("activecount")).setAddvratio(rs.getFloat("addvratio"))
						.setActiveratio(rs.getFloat("activeratio")).setMaleratio(rs.getFloat("maleratio"))
						.setFansexistedratio(rs.getFloat("fansexistedratio")).setVerify(rs.getInt("verify"))
						.setAllfanscount(rs.getLong("allfanscount"))
						.setAllactivefanscount(rs.getLong("allactivefanscount"))
						.setTop5provinces(rs.getString("top5provinces")).setOriratio(rs.getFloat("oriratio"))
						.setAveorirepcom(rs.getFloat("aveorirepcom")).setAverepcom(rs.getFloat("averepcom"))
						.setWbsource(rs.getString("wbsource")).setAverepcombyweek(rs.getFloat("averepsbyweek"))
						.setAverepcombymonth(rs.getFloat("averepsbymonth"))
						.setValidrepcombyweek(rs.getFloat("validrepcombyweek"))
						.setValidrepcombymonth(rs.getFloat("validrepcombymonth"))
						.setAvereposterquality(rs.getFloat("avereposterquality"))
						.setAveexposionsum(rs.getLong("aveexposionsum")).build();
			} else {
				return null;
			}
		}
	}

	/**
	 * 获取皮皮授权用户的：token
	 */
	public List<String> getSinaToken(String tablename) throws SQLException {

		List<String> result = new ArrayList<String>();
		HashSet<String> hs = new HashSet<>();
		String sql = "SELECT `bind_access_token` FROM " + tablename + " WHERE `expires` > "
				+ System.currentTimeMillis() / 1000;
		try (Connection conn = getConnection();
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(sql);) {
			while (rs.next()) {
				hs.add(rs.getString("bind_access_token"));
			}
		}
		for (String temp : hs) {
			result.add(temp);
		}

		return result;
	}

	/**
	 * 获取皮皮授权用户的：uid
	 */
	public List<String> getSinaUid(String tablename, int num) throws SQLException {

		List<String> result = new ArrayList<String>();
		String sql = "SELECT `username` FROM " + tablename + " LIMIT " + num;
		try (Connection conn = getConnection();
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(sql);) {
			while (rs.next()) {
				result.add(rs.getString("username"));
			}
		}

		return result;
	}

	/**
	 * 获取皮皮授权用户的：uid、token
	 */
	public HashMap<String, String> getSinaUidAndToken(String tablename, int num) throws SQLException {

		HashMap<String, String> result = new HashMap<>();
		String sql = "SELECT `bind_username`,`bind_access_token` FROM " + tablename + " LIMIT " + num;
		try (Connection conn = getConnection();
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(sql);) {
			while (rs.next()) {
				result.put(rs.getString("bind_username"), rs.getString("bind_access_token"));
			}
		}

		return result;
	}

	/**
	 * 获取新浪用户的基础信息
	 */
	public SinaUserInfo getSinaUserBaseInfo(String tablename, String username) throws SQLException {

		String sql = "SELECT * FROM " + tablename + " WHERE `id` = " + username;
		try (Connection conn = getConnection();
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(sql);) {
			if (rs.next()) {
				return new SinaUserInfo.Builder(rs.getLong("id")).setScreen_name(rs.getString("screen_name"))
						.setProvince(rs.getInt("province")).setCity(rs.getInt("city"))
						.setLocation(rs.getString("location")).setDescription(rs.getString("description"))
						.setUrl(rs.getString("url")).setProfile_image_url(rs.getString("profile_image_url"))
						.setDomain(rs.getString("domain")).setGender(rs.getString("gender"))
						.setFollowers_count(rs.getInt("followers_count")).setFriends_count(rs.getInt("friends_count"))
						.setStatuses_count(rs.getInt("statuses_count"))
						.setFavourites_count(rs.getInt("favourites_count")).setCreated_at(rs.getLong("created_at"))
						.setVerified(rs.getBoolean("verified")).setVerified_type(rs.getInt("verified_type"))
						.setAvatar_large(rs.getString("avatar_large"))
						.setBi_followers_count(rs.getInt("bi_followers_count")).setRemark(rs.getString("remark"))
						.setVerified_reason(rs.getString("verified_reason")).setWeihao(rs.getString("weihao")).build();
			} else {
				return null;
			}
		}

	}

	/**
	 * 获取新浪基础信息中的存在用户名
	 */
	public List<String> getSinaUsername(String tablename) throws SQLException {

		List<String> result = new ArrayList<>();
		String sql = "SELECT `id` FROM " + tablename + " WHERE `name` != 'unexisted'";
		try (Connection conn = getConnection();
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(sql);) {
			while (rs.next()) {
				result.add(rs.getString("id"));
			}
		}

		return result;
	}

	/**
	 * 获取新浪用户微博数据
	 */
	public List<WeiboInfo> getSinaUserWeibos(String tablename, int lower, int high) throws SQLException {

		String sql = "SELECT * FROM " + tablename + " WHERE `id` >= " + lower + " `id` <= " + high;
		try (Connection conn = getConnection();
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(sql);) {
			List<WeiboInfo> result = new ArrayList<>();
			while (rs.next()) {
				result.add(new WeiboInfo.Builder(rs.getLong("wid"), rs.getLong("username"))
						.setRepostsCount(rs.getInt("repostsCount")).setCommentsCount(rs.getInt("commentsCount"))
						.setAttitudesCount(rs.getInt("attitudesCount")).setText(rs.getString("text"))
						.setCreateat(rs.getLong("createat")).setOwid(rs.getLong("owid"))
						.setOusername(rs.getLong("ousername")).setFavorited(rs.getBoolean("favorited"))
						.setGeo(rs.getString("geo")).setLatitude(rs.getDouble("latitude"))
						.setLongitude(rs.getDouble("longitude")).setOriginalpic(rs.getString("originalpic"))
						.setSource(rs.getString("source")).setVisible(rs.getString("visible"))
						.setMlevel(rs.getInt("mlevel")).build());
			}
			return result;
		}
	}

	/**
	 * 获取username
	 */
	public List<String> getUids(String tablename, int num) throws SQLException {

		List<String> result = new ArrayList<String>();
		String sql = new String("SELECT `username` FROM " + tablename + " LIMIT " + num);
		try (Connection conn = getConnection();
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(sql);) {
			while (rs.next()) {
				result.add(rs.getString("username"));
			}
		}

		return result;
	}

	public void inserSinaUserBaseinfo(String tablename, SinaUserInfo user) {

		long time = System.currentTimeMillis() / 1000;
		String sql = "INSERT INTO " + tablename + " (`id`,`screen_name`,`name`,`province`,`city`,"
				+ "`location`,`description`,`url`,`profile_image_url`,`domain`,`gender`,`followers_count`,"
				+ "`friends_count`,`statuses_count`,`favourites_count`,`created_at`,`verified`,"
				+ "`verified_type`,`avatar_large`,`bi_followers_count`,`remark`,`verified_reason`,"
				+ "`weihao`,`lasttime`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setLong(1, user.getId());
			pstmt.setString(2, user.getScreen_name());
			pstmt.setString(3, user.getName());
			pstmt.setInt(4, user.getProvince());
			pstmt.setInt(5, user.getCity());
			pstmt.setString(6, user.getLocation());
			pstmt.setString(7, user.getDescription()); // 🌟
			pstmt.setString(8, user.getUrl());
			pstmt.setString(9, user.getProfile_image_url());
			pstmt.setString(10, user.getDomain());
			pstmt.setString(11, user.getGender());
			pstmt.setInt(12, user.getFollowers_count());
			pstmt.setInt(13, user.getFriends_count());
			pstmt.setInt(14, user.getStatuses_count());
			pstmt.setInt(15, user.getFavourites_count());
			pstmt.setLong(16, user.getCreated_at());
			pstmt.setBoolean(17, user.isVerified());
			pstmt.setInt(18, user.getVerified_type());
			pstmt.setString(19, user.getAvatar_large());
			pstmt.setInt(20, user.getBi_followers_count());
			pstmt.setString(21, user.getRemark());
			pstmt.setString(22, user.getVerified_reason());
			pstmt.setString(23, user.getWeihao());
			pstmt.setLong(24, time);
			pstmt.execute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 插入新浪用户的基础信息
	 */
	public void inserSinaUserBaseinfo(String tablename, User user, boolean flag) throws SQLException,
			UnsupportedEncodingException {

		long time = System.currentTimeMillis() / 1000;
		String sql = "INSERT INTO " + tablename + " (`id`,`screen_name`,`name`,`province`,`city`,"
				+ "`location`,`description`,`url`,`profile_image_url`,`domain`,`gender`,`followers_count`,"
				+ "`friends_count`,`statuses_count`,`favourites_count`,`created_at`,`verified`,"
				+ "`verified_type`,`avatar_large`,`bi_followers_count`,`remark`,`verified_reason`,"
				+ "`weihao`,`lasttime`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setLong(1, Long.parseLong(user.getId()));
			pstmt.setString(2, user.getScreenName());
			pstmt.setString(3, user.getName());
			pstmt.setInt(4, user.getProvince());
			pstmt.setInt(5, user.getCity());
			pstmt.setString(6, user.getLocation());
			if (flag) {
				pstmt.setString(7, user.getDescription()); // 🌟
				pstmt.setString(22, user.getVerifiedReason());
			} else {
				pstmt.setString(7, ""); // 🌟
				pstmt.setString(22, "");
			}
			pstmt.setString(8, user.getUrl());
			pstmt.setString(9, user.getProfileImageUrl());
			pstmt.setString(10, user.getUserDomain());
			pstmt.setString(11, user.getGender());
			pstmt.setInt(12, user.getFollowersCount());
			pstmt.setInt(13, user.getFriendsCount());
			pstmt.setInt(14, user.getStatusesCount());
			pstmt.setInt(15, user.getFavouritesCount());
			pstmt.setLong(16, user.getCreatedAt().getTime() / 1000);
			pstmt.setBoolean(17, user.isVerified());
			pstmt.setInt(18, user.getVerifiedType());
			pstmt.setString(19, user.getAvatarLarge());
			pstmt.setInt(20, user.getBiFollowersCount());
			if (user.getRemark() == null) {
				pstmt.setString(21, "0");
			} else {
				pstmt.setString(21, user.getRemark());
			}
			pstmt.setString(23, user.getWeihao());
			pstmt.setLong(24, time);
			pstmt.execute();
		}
	}

	/**
	 * 插入皮皮新浪用户粉丝数据
	 */
	public void insertPPSinaUserFans(String tablename, PPUserFansInfo user) throws SQLException {

		String sql = "INSERT INTO " + tablename + " (`username`,`fansuids`,`fanscount`,`lasttime`) VALUES (?,?,?,?)";
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setLong(1, user.getUsername());
			pstmt.setString(2, user.getFansuids());
			pstmt.setInt(3, user.getFanscount());
			pstmt.setLong(4, System.currentTimeMillis() / 1000);
			pstmt.execute();
		}
	}

	/**
	 * 插入皮皮新浪用户的实时粉丝更新数据
	 */
	public void insertPPUserFansDaily(String tablename, FansAddDaily user) throws SQLException {

		String sql = "INSERT INTO " + tablename
				+ " (`username`,`addfansuids`,`addfanscount`,`allfanscount`) VALUES (?,?,?,?)";
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setString(1, user.getUsername());
			pstmt.setString(2, user.getAddfansuids());
			pstmt.setInt(3, user.getAddfanscount());
			pstmt.setInt(4, user.getAllfanscount());
			pstmt.execute();
		}
	}

	/**
	 * 插入微博主参数数据（不含价格数据）
	 */
	public void insertSinaBozhuAllParams(String tablename, UserAllParamsDomain bozhu) throws SQLException {

		String sql = "INSERT INTO " + tablename
				+ " (`username`,`nickname`,`description`,`createdtime`,`usertags`,`fanscount`,`weibocount`,"
				+ "`averagewbs`,`influence`,`activation`,`activecount`,`addvratio`,`activeratio`,"
				+ "`maleratio`,`fansexistedratio`,`verify`,`allfanscount`,`allactivefanscount`,`top5provinces`,"
				+ "`oriratio`,`aveorirepcom`,`averepcom`,`wbsource`,`averepsbyweek`,`averepsbymonth`,"
				+ "`validrepcombyweek`,`validrepcombymonth`,`avereposterquality`,`aveexposionsum`,`lasttime`) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setString(1, bozhu.getUsername());
			pstmt.setString(2, bozhu.getNickname());
			pstmt.setString(3, bozhu.getDescription());
			pstmt.setInt(4, bozhu.getCreatedtime());
			pstmt.setString(5, bozhu.getUsertags());
			pstmt.setInt(6, bozhu.getFanscount());
			pstmt.setInt(7, bozhu.getWeibocount());
			pstmt.setFloat(8, bozhu.getAveragewbs());
			pstmt.setInt(9, bozhu.getInfluence());
			pstmt.setInt(10, bozhu.getActivation());
			pstmt.setInt(11, bozhu.getActivecount());
			pstmt.setFloat(12, bozhu.getAddvratio());
			pstmt.setFloat(13, bozhu.getActiveratio());
			pstmt.setFloat(14, bozhu.getMaleratio());
			pstmt.setFloat(15, bozhu.getFansexistedratio());
			pstmt.setInt(16, bozhu.getVerify());
			pstmt.setLong(17, bozhu.getAllfanscount());
			pstmt.setLong(18, bozhu.getAllactivefanscount());
			pstmt.setString(19, bozhu.getTop5provinces());
			pstmt.setFloat(20, bozhu.getOriratio());
			pstmt.setFloat(21, bozhu.getAveorirepcom());
			pstmt.setFloat(22, bozhu.getAverepcom());
			pstmt.setString(23, bozhu.getWbsource());
			pstmt.setFloat(24, bozhu.getAverepcombyweek());
			pstmt.setFloat(25, bozhu.getAverepcombymonth());
			pstmt.setFloat(26, bozhu.getValidrepcombyweek());
			pstmt.setFloat(27, bozhu.getValidrepcombymonth());
			pstmt.setFloat(28, bozhu.getAvereposterquality());
			pstmt.setLong(29, bozhu.getAveexposionsum());
			pstmt.setLong(30, System.currentTimeMillis() / 1000);
			try {
				pstmt.execute();
			} catch (MySQLSyntaxErrorException e) {
				// 处理`oriratio`,`aveorirepcom`,`averepcom`为Nan情况，不纪录数据
				logger.info("User: " + bozhu.getUsername() + "'s oriratio/aveorirepcom/averepcom is NaN.");
			}
		}
	}

	/**
	 * 插入新浪用户微博数据
	 */
	public void insertSinaUserWeibo(String tablename, Status weibo, boolean flag) throws SQLException {

		String sql = "INSERT INTO "
				+ tablename
				+ " (`wid`,`username`,`repostscount`,`commentscount`,`attitudescount`,`text`,`createat`,`owid`,`ousername`,"
				+ "`favorited`,`geo`,`latitude`,`longitude`,`originalpic`,`source`,`visible`,`mlevel`,`lasttime`) " //
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setLong(1, Long.parseLong(weibo.getId()));
			pstmt.setLong(2, Long.parseLong(weibo.getUser().getId()));
			pstmt.setInt(3, weibo.getRepostsCount());
			pstmt.setInt(4, weibo.getCommentsCount());
			pstmt.setInt(5, weibo.getAttitudesCount());
			if (flag) {
				pstmt.setString(6, weibo.getText());
			} else {
				pstmt.setString(6, "");
			}
			pstmt.setLong(7, weibo.getCreatedAt().getTime() / 1000);
			if (weibo.getRetweetedStatus() == null) {
				pstmt.setLong(8, 0L);
				pstmt.setLong(9, 0L);
			} else {
				pstmt.setLong(8, Long.parseLong(weibo.getRetweetedStatus().getId()));
				if (weibo.getRetweetedStatus().getUser() == null) {
					pstmt.setLong(9, 0L);
				} else {
					pstmt.setLong(9, Long.parseLong(weibo.getRetweetedStatus().getUser().getId()));
				}
			}
			pstmt.setBoolean(10, weibo.isFavorited());
			pstmt.setString(11, weibo.getGeo());
			pstmt.setDouble(12, weibo.getLatitude());
			pstmt.setDouble(13, weibo.getLongitude());
			pstmt.setString(14, weibo.getOriginalPic());
			pstmt.setString(15, weibo.getSource().getName());
			pstmt.setString(16, weibo.getVisible().getList_id() + "," + weibo.getVisible().getType());
			pstmt.setInt(17, weibo.getMlevel());
			pstmt.setLong(18, System.currentTimeMillis() / 1000);
			pstmt.execute();
		}
	}

	/**
	 * 插入数据表名
	 */
	public void insertTablename(String tablename, String name) throws SQLException {

		String sql = new String("INSERT INTO " + tablename + " (`name`) VALUES (\"" + name + "\")");
		try (Connection conn = getConnection(); Statement statement = conn.createStatement();) {
			statement.executeUpdate(sql);
		}
	}

	/**
	 * 查找是否存在某条微博
	 */
	public boolean isExistingWeibo(String tablename, Long wid) throws SQLException {

		String sql = "SELECT * FROM " + tablename + " WHERE `wid` = " + wid;
		try (Connection conn = getConnection();
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(sql)) {
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * 查看皮皮新浪用户是否存在
	 */
	public boolean isPPSinaUserExisted(String tablename, String username) throws SQLException {

		String sql = "SELECT `username` FROM " + tablename + " WHERE `username` = " + username;
		try (Connection conn = getConnection();
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(sql);) {
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * 查看新浪用户否存在
	 */
	public boolean isSinaUserBaseinfoExist(String tablename, String username) throws SQLException {

		String sql = "SELECT `username` FROM " + tablename + " WHERE `id` = " + username;
		try (Connection conn = getConnection();
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(sql);) {
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * 去除重复的标签数据
	 */
	public void removeSinaRepeatTags(String tablename, int num) throws SQLException {

		HashSet<Long> result = new HashSet<>();
		String sql = new String("SELECT `id`,`username` FROM " + tablename + " LIMIT " + num);
		try (Connection conn = getConnection();
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(sql);) {
			long username;
			while (rs.next()) {
				username = rs.getLong("username");
				if (result.contains(username)) {
					logger.info("User: " + username + " has repeated infos.");
					deleteSinaTags(tablename, rs.getInt("id"));
				} else {
					result.add(username);
				}
			}
		}
	}

	/**
	 * 去除重复的基础数据
	 */
	public void removeSinaRepeatUids(String tablename, int num) throws SQLException {

		HashSet<Long> result = new HashSet<>();
		String sql = new String("SELECT `bid`,`id` FROM " + tablename + " LIMIT " + num);
		try (Connection conn = getConnection();
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(sql);) {
			long id;
			while (rs.next()) {
				id = rs.getLong("id");
				if (result.contains(id)) {
					logger.info("User: " + id + " has repeated infos.");
					deleteSinaBaseInfo(tablename, rs.getInt("bid"));
				} else {
					result.add(id);
				}
			}
		}
	}

	/**
	 * 更新皮皮新浪用户粉丝数据
	 */
	public void updatePPSinaUserFans(String tablename, PPUserFansInfo user) throws SQLException {

		String sql = "UPDATE " + tablename + " SET `fansuids` = \"" + user.getFansuids() + "\",`fanscount` = "
				+ user.getFanscount() + ",`lasttime` = " + System.currentTimeMillis() / 1000 + " WHERE `username` = "
				+ user.getUsername();
		try (Connection conn = getConnection(); Statement statement = conn.createStatement();) {
			statement.executeUpdate(sql);
		}
	}

	/**
	 * 更新新浪不存在的用户信息
	 */
	public void updateSinaUnexistedUser(String tablename, String uid) throws SQLException {

		String sql = "UPDATE " + tablename + " SET `name` = 'unexisted' WHERE `id` = " + uid;
		try (Connection conn = getConnection(); Statement statement = conn.createStatement()) {
			statement.executeUpdate(sql);
		}
	}

	/**
	 * 更新新浪用户基础信息表，只需更新部分字段，适合批量更新
	 */
	public void updateSinaUserBaseInfo(String tablename, User user) throws SQLException {

		String sql = "UPDATE " + tablename + " SET `screen_name` = " + user.getScreenName() + ",`followers_count` = "
				+ user.getFollowersCount() + ",`friends_count` = " + user.getFriendsCount() + ",`statuses_count` = "
				+ user.getStatusesCount() + ",`favourites_count` = " + user.getFavouritesCount()
				+ ",`bi_followers_count` = " + user.getBiFollowersCount() + ",`verified_type` = "
				+ user.getVerifiedType() + " WHERE `id` = " + user.getId();
		try (Connection conn = getConnection(); Statement statement = conn.createStatement();) {
			statement.executeUpdate(sql);
		}
	}

}
