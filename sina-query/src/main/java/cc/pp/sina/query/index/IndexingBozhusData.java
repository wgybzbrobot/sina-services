package cc.pp.sina.query.index;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.bozhus.sql.WeiboJDBC;

/**
 * 索引300w博主数据
 * @author wgybzb
 *
 */
public class IndexingBozhusData {

	private static Logger logger = LoggerFactory.getLogger(IndexingBozhusData.class);

	/**
	 * 主函数
	 */
	public static void main(String[] args) throws SQLException {

		logger.info("连接数据库 ...");
		WeiboJDBC weiboJDBC = new WeiboJDBC("127.0.0.1", "root", "root", "pp_fenxi");
		if (!weiboJDBC.dbConnection()) {
			logger.info("Db connection error.");
			return;
		}
		logger.info("获取300万博主id ...");
		List<Long> ids = weiboJDBC.getBozhuIds("a_bz_bozhu");
		System.err.println(ids.size());

	}

}
