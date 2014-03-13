package cc.pp.sina.bozhus.weibos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.bozhus.info.SinaWeiboInfoDao;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDaoImpl;
import cc.pp.sina.bozhus.sql.WeiboJDBC;
import cc.pp.sina.tokens.service.TokenService;

import com.sina.weibo.model.Status;
import com.sina.weibo.model.StatusWapper;
import com.sina.weibo.model.WeiboException;

public class UserWeibosDump {

	private static Logger logger = LoggerFactory.getLogger(UserWeibosDump.class);


	/**
	 * 测试函数
	 */
	public static void main(String[] args) throws Exception {

		List<String> sinaTokens = new ArrayList<String>();
		sinaTokens.add("2.00vyKmPBdcZIJCab7e4cdb505dUSLC");
		sinaTokens.add("2.00vyKmPBdcZIJCab7e4cdb505dUSLC");
		TokenService tokenService = new TokenService(sinaTokens);
		SinaWeiboInfoDao sinaWeiboInfoDao = new SinaWeiboInfoDaoImpl(tokenService);

		WeiboJDBC weiboJDBC = new WeiboJDBC("127.0.0.1", "root", "root", "pp_fenxi");

		//		weiboJDBC.createSinaUserWeibosTable("sina_user_weibos");
		UserWeibosDump.weibosDump(weiboJDBC, "sina_user_weibos", "1649155730", sinaWeiboInfoDao, 20);

		weiboJDBC.dbClose();
	}

	/**
	 * 分析函数
	 *
	 * @param pageCount：微博页数
	 */
	public static void weibosDump(WeiboJDBC weiboJDBC, String tablename, String uid, SinaWeiboInfoDao sinaWeiboInfoDao,
								  int pageCount) throws WeiboException, SQLException {

		/*
		 * 采集用户微博信息
		 */
		int cursor = 1;
		StatusWapper status = sinaWeiboInfoDao.getSinaUserWeibos(uid, cursor);
		if (status == null) {
			logger.info("User: " + uid + " has no weibos.");
			return;
		}

		while (cursor * 100 < status.getTotalNumber() + 100) {

			for (Status weibo : status.getStatuses()) {
				// 存在微博数
				if (weibo.getId() == null) {
					continue;
				}
				try {
					weiboJDBC.insertSinaUserWeibo(tablename, weibo, true);
				} catch (SQLException e) {
					logger.info("Weibo: " + weibo.getId() + "'s text is non-utf-8 encoded.");
					weiboJDBC.insertSinaUserWeibo(tablename, weibo, false);
				}
			}
			if (cursor > Math.min(pageCount, 20)) { // 限制pageCount次，最大20次
				break;
			}

			System.out.println(cursor);

			status = sinaWeiboInfoDao.getSinaUserWeibos(uid, ++cursor);
		}

	}

}
