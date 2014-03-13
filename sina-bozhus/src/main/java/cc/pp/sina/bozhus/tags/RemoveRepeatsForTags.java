package cc.pp.sina.bozhus.tags;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.bozhus.sql.WeiboJDBC;

/**
 * 去除标签数据中重复的用户
 * @author wgybzb
 *
 */
public class RemoveRepeatsForTags {

	private static Logger logger = LoggerFactory.getLogger(RemoveRepeatsForTags.class);

	private static final String EXTEND_TABLE = "sina_user_extend_info_";
	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		RemoveRepeatsForTags.rmDuplication();
	}

	/**
	 * 去重函数
	 */
	public static void rmDuplication() {

		WeiboJDBC weiboJDBC = new WeiboJDBC("192.168.1.48", "pp_fenxi", "q#tmuYzC@sqB6!ok@sHd", "pp_fenxi");

		for (int i = 0; i < 10; i++) {
			logger.info("Tackle at: " + i);
			try {
				weiboJDBC.removeSinaRepeatTags(EXTEND_TABLE + i, 100_0000);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}

		weiboJDBC.dbClose();
	}

}
