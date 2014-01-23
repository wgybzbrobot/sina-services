package cc.pp.content.library.sql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.domain.content.library.ContentInfo;
import cc.pp.sina.domain.content.library.ContentResultDaily;
import cc.pp.sina.domain.content.library.SendInfo;
import cc.pp.sina.utils.java.JavaPattern;
import cc.pp.sina.utils.json.JsonUtils;

public class ContentJDBC {

	private static Logger logger = LoggerFactory.getLogger(ContentJDBC.class);

	private Connection conn;
	private String db_driver;
	private String db_user;
	private String db_password;
	private String db_name;
	private String db_ip;
	private String db_port;
	private final String db_url;

	/**
	 * 初始化数据库相关参数
	 */
	public void dbInit() {
		Properties props = null;
		try {
			props = ContentDbParamsRead.getDbParams();
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
	 * 线上环境
	 */
	public ContentJDBC() {
		dbInit();
		db_url = "jdbc:mysql://" + db_ip + ":" + db_port + "/" + db_name + "?useUnicode=true&characterEncoding=utf-8";
	}

	/**
	 * 本地环境
	 */
	public ContentJDBC(String dbip, String port, String dbuser, String dbpassword, String dbname) {
		dbInit();
		db_user = dbuser;
		db_password = dbpassword;
		db_url = "jdbc:mysql://" + dbip + ":" + port + "/" + dbname + "?useUnicode=true&characterEncoding=utf-8";
	}

	@Override
	public String toString() {
		return new StringBuffer().append("Db driver: " + db_driver + "\n").append("Db user: " + db_user + "\n")
				.append("Db password: " + db_password + "\n").append("Db name: " + db_name + "\n")
				.append("Db ip: " + db_ip + "\n").append("Db port: " + db_port).toString();
	}

	/**
	 * 链接数据库
	 */
	public boolean dbConnection() {
		boolean result = true;
		try {
			Class.forName(this.db_driver);
			conn = DriverManager.getConnection(db_url, db_user, db_password);
		} catch (Exception e) {
			logger.info("Db connection error.");
			result = false;
		}
		return result;
	}

	/**
	 * 关闭数据库
	 */
	public void dbClose() {
		try {
			conn.close();
		} catch (SQLException e) {
			logger.info("Db close error.");
		}
	}

	/**
	 * 读取定时发送数据
	 */
	public List<SendInfo> getLibraryRecord(String tablename, String ptype, String recordType, long start, long end)
			throws SQLException {

		List<SendInfo> result = new ArrayList<SendInfo>();
		String sql = "SELECT `cid`,`username`,`status`,`ispower`,`" + recordType + "` FROM " + tablename + " WHERE `"
				+ recordType + "` >= " + start + " AND `" + recordType + "` <= " + end + " AND `ptype` = \"" + ptype
				+ "\"";
		try (Statement statement = this.conn.createStatement(); ResultSet rs = statement.executeQuery(sql);) {
			while (rs.next()) {
				// 针对新浪平台，内容库有错误纪录，将非数字串纪录为username，所以这里加上判断
				if (JavaPattern.isAllNum(rs.getString("username"))) {
					result.add(new SendInfo.Builder().setCid(rs.getInt("cid")).setIspower(rs.getInt("ispower"))
							.setSendtime(rs.getLong(recordType)).setStatus(rs.getInt("status"))
							.setUsername(rs.getLong("username")).build());
				}
			}
		}

		return result;
	}

	/**
	 * 读取内容库内容数据
	 */
	public ContentInfo getWbLibrary(String tablename, int cid) throws SQLException {

		String sql = "SELECT `cid`,`tid`,`content`,`picture`,`video`,`music` FROM " + tablename + " WHERE `cid` = "
				+ cid;
		try (Statement statement = this.conn.createStatement(); ResultSet rs = statement.executeQuery(sql);) {
			if (rs.next()) {
				return new ContentInfo.Builder().setCid(rs.getInt("cid")).setContent(rs.getString("content"))
						.setMusic(rs.getString("music")).setPicture(rs.getString("picture")).setTid(rs.getInt("tid"))
						.setVideo(rs.getString("video")).build();
			} else {
				return null;
			}
		}
	}

	/**
	 * 读取内容库微博类别
	 */
	public HashMap<Integer, String> getWbLibraryType(String tablename) throws SQLException {

		HashMap<Integer, String> result = new HashMap<>();
		String sql = "SELECT `tid`,`name` FROM " + tablename;
		try (Statement statement = this.conn.createStatement(); ResultSet rs = statement.executeQuery(sql);) {
			while (rs.next()) {
				result.put(rs.getInt("tid"), rs.getString("name"));
			}
		}

		return result;
	}

	/**
	 * 创建内容库数据统计表
	 */
	public void createContentResultTable(String tablename) throws SQLException {

		String sql = "CREATE TABLE " + tablename + " (" + "`id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '序号',"
				+ "`type` varchar(10) NOT NULL COMMENT '平台类型'," + "`date` varchar(10) NOT NULL COMMENT '统计日期',"
				+ "`recordType` varchar(15) NOT NULL COMMENT '纪录类型',"
				+ "`errorRatio` float unsigned NOT NULL COMMENT '内容库错误记录率',"
				+ "`contentCount` int(10) unsigned NOT NULL COMMENT '每天内容总数',"
				+ "`powerRatio` float unsigned NOT NULL COMMENT '每天来自皮皮动力的比率',"
				+ "`fromContentRatio` float unsigned NOT NULL COMMENT '每天来自内容库内容的比率',"
				+ "`fromContentRatioByHour` varchar(500) NOT NULL COMMENT '每天每小时内容库内容的使用比率',"
				+ "`userCount` mediumint(8) unsigned NOT NULL COMMENT '每天使用内容库的用户数',"
				+ "`userByHour` varchar(500) NOT NULL COMMENT '每天每小时的真实用户数',"
				+ "`userQuality` varchar(200) NOT NULL COMMENT '每天使用用户的质量',"
				+ "`userQualityByHour` varchar(500) NOT NULL COMMENT '每天每小时的真实用户比例',"
				+ "`contentUsedQuality` varchar(500) NOT NULL COMMENT '每天内容库被使用的质量(real,mask分别使用的内容量比率)',"
				+ "`realContentUsedByHour` varchar(500) NOT NULL COMMENT '每天每小时真实用户使用内容库的比例',"
				+ "`columnUsedCount` text NOT NULL COMMENT '每天每个栏目的使用数量',"
				+ "`columnUsedSort` text NOT NULL COMMENT '每天各栏目的使用量排序',"
				+ "`columnTop10ByHour` text NOT NULL COMMENT '每天每小时使用量排名前10的栏目',"
				+ "`contentTop100` text NOT NULL COMMENT '每天内容库使用量前100的内容',"
				+ "`picVideoMusicRatio` varchar(100) NOT NULL COMMENT '每天所有内容中分别含图片、视频、音乐的比例',"
				+ "`top10CloumnByPicUsed` text NOT NULL COMMENT '每天使用的所有图片排名前10的类型/栏目',"
				+ "`top100Ips` varchar(200) NOT NULL COMMENT '每天内容库使用量前100的IP',"
				+ "`top10IpSection` varchar(200) NOT NULL COMMENT '排名前20的IP段',"
				+ "`source` varchar(200) NOT NULL COMMENT '自定义来源分布',"
				+ "`sendStatus` varchar(200) NOT NULL COMMENT '发送状态情况',"
				+ "`allKeywords` text NOT NULL COMMENT '所有关键词(按词频)',"
				+ "`keywordsOfTopNContent` text NOT NULL COMMENT 'top100内容中的关键词',"
				+ " PRIMARY KEY (`id`) KEY `type` (`type`),KEY `date` (`date`)) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='内容库统计数据表' AUTO_INCREMENT=1 ;";
		try (Statement statement = this.conn.createStatement();) {
			statement.execute(sql);
		}
	}

	/**
	 * 插入内容库统计数据
	 */
	public void insertContentResultTable(String tablename, ContentResultDaily result) throws SQLException {

		String sql = "INSERT INTO " + tablename
				+ " (`type`,`date`,`recordType`,`errorRatio`,`contentCount`,`powerRatio`,`fromContentRatio`,`fromContentRatioByHour`,"
				+ "`userCount`,`userByHour`,`userQuality`,`userQualityByHour`,`contentUsedQuality`,`realContentUsedByHour`,"
				+ "`columnUsedCount`,`columnUsedSort`,`columnTop10ByHour`,`contentTop100`,`picVideoMusicRatio`,`top10CloumnByPicUsed`,"
				+ "`top100Ips`,`top10IpSection`,`source`,`sendStatus`,`allKeywords`,`keywordsOfTopNContent`) VALUES"
				+ " (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try (PreparedStatement pstmt = this.conn.prepareStatement(sql);) {
			pstmt.setString(1, result.getType());
			pstmt.setString(2, result.getDate());
			pstmt.setString(3, result.getRecordType());
			pstmt.setFloat(4, result.getErrorRatio());
			pstmt.setInt(5, result.getContentCount());
			pstmt.setFloat(6, result.getPowerRatio());
			pstmt.setFloat(7, result.getFromContentRatio());
			pstmt.setString(8, JsonUtils.toJson(result.getFromContentRatioByHour()));
			pstmt.setInt(9, result.getUserCount());
			pstmt.setString(10, JsonUtils.toJson(result.getUserByHour()));
			pstmt.setString(11, JsonUtils.toJson(result.getUserQuality()));
			pstmt.setString(12, JsonUtils.toJson(result.getUserQualityByHour()));
			pstmt.setString(13, JsonUtils.toJson(result.getContentUsedQuality()));
			pstmt.setString(14, JsonUtils.toJson(result.getRealContentUsedByHour()));
			pstmt.setString(15, JsonUtils.toJson(result.getColumnUsedCount()));
			pstmt.setString(16, JsonUtils.toJson(result.getColumnUsedSort()));
			pstmt.setString(17, JsonUtils.toJson(result.getColumnTop10ByHour()));
			pstmt.setString(18, JsonUtils.toJson(result.getContentTop100()));
			pstmt.setString(19, JsonUtils.toJson(result.getPicVideoMusicRatio()));
			pstmt.setString(20, JsonUtils.toJson(result.getTop10CloumnByPicUsed()));
			pstmt.setString(21, JsonUtils.toJson(result.getTop100Ips()));
			pstmt.setString(22, JsonUtils.toJson(result.getTop10IpSection()));
			pstmt.setString(23, JsonUtils.toJson(result.getSource()));
			pstmt.setString(24, JsonUtils.toJson(result.getSendStatus()));
			pstmt.setString(25, JsonUtils.toJson(result.getAllKeywords()));
			pstmt.setString(26, JsonUtils.toJson(result.getKeywordsOfTopNContent()));
			pstmt.execute();
		}
	}

}
