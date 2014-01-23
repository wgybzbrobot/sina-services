package cc.pp.sina.web.bozhu;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.bozhus.sql.WeiboJDBC;
import cc.pp.sina.domain.bozhus.UserAllParamsDomain;
import cc.pp.sina.utils.json.JsonUtils;

/**
 * 用户信息查询模块，遗留问题，需要更换为Mybatis
 * @author wgybzb
 *
 */
public class AnalysisInfo {

	private static Logger logger = LoggerFactory.getLogger(AnalysisInfo.class);

	private static final String SINA_BOZHUS_LIBRARY = "sina_bozhus_library";

	public AnalysisInfo() {
		//
	}

	public UserAllParamsDomain getBozhusInfoByUid(String uid) {
		//		WeiboJDBC weiboJDBC = new WeiboJDBC("127.0.0.1", "root", "root", "pp_fenxi");
		//		WeiboJDBC weiboJDBC = new WeiboJDBC("192.168.1.47", "pp_fenxi", "q#tmuYzC@sqB6!ok@sHd", "pp_fenxi");
		WeiboJDBC weiboJDBC = new WeiboJDBC("192.168.1.6", "pp_fenxi", "Gd3am#nB0kiSbiN!o@4KQ", "pp_fenxi");
		if (!weiboJDBC.dbConnection()) {
			System.err.println("数据库连接错误！");
		}
		try {
			UserAllParamsDomain bozhu = weiboJDBC.getSinaBozhuAllParams(SINA_BOZHUS_LIBRARY, uid);
			weiboJDBC.dbClose();
			return bozhu;
		} catch (SQLException e) {
			logger.info("User: " + uid + " is not exists or has no info.");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 测试函数
	 */
	public static void main(String[] args) {

		AnalysisInfo bozhusInfoByUid = new AnalysisInfo();
		UserAllParamsDomain bozhu = bozhusInfoByUid.getBozhusInfoByUid("1000871311");
		System.out.println(JsonUtils.toJson(bozhu));
	}

}
