package cc.pp.sina.bozhus.baseinfo;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.bozhus.sql.WeiboJDBC;

/**
 * 去除基础信息中重复的信息
 * @author wgybzb
 *
 */
public class RemoveRepeat {

	private static Logger logger = LoggerFactory.getLogger(RemoveRepeat.class);

	private static final String BASEINFO_TABLE = "sinauserbaseinfo_";

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		RemoveRepeat.rmDuplication();
	}

	/**
	 * 去重函数
	 */
	public static void rmDuplication() {

		WeiboJDBC weiboJDBC = new WeiboJDBC("192.168.1.48", "pp_fenxi", "q#tmuYzC@sqB6!ok@sHd", "pp_fenxi");
		if (!weiboJDBC.dbConnection()) {
			System.err.println("数据库链接失败！");
		}
		for (int i = 0; i < 32; i++) {
			logger.info("Tackle at: " + i);
			try {
				weiboJDBC.removeSinaRepeatUids(BASEINFO_TABLE + i, 1100_0000);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}

		weiboJDBC.dbClose();
	}

}
