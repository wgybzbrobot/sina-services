package cc.pp.sina.bozhus.weibos;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.bozhus.info.SinaWeiboInfoDao;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDaoImpl;
import cc.pp.sina.bozhus.sql.WeiboJDBC;
import cc.pp.sina.tokens.service.TokenService;

import com.sina.weibo.model.Status;
import com.sina.weibo.model.StatusWapper;

public class SingleThreadPublicWeibos {

	private static Logger logger = LoggerFactory.getLogger(SingleThreadPublicWeibos.class);

	private static final String SINA_USER_WEIBOS_TABLENAMES = "sina_user_weibos_tablenames";
	private static final String SINA_USER_WEIBOS = "sina_user_weibos_";

	/**
	 * 测试函数
	 */
	public static void main(String[] args) {

		try {
			SingleThreadPublicWeibos.start();
		} catch (SQLException e) {
			logger.error("SQLException: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 启动
	 */
	public static void start() throws SQLException {

		/**
		 * 从150上获取token数据
		 */
		TokenService tokenService = new TokenService();

		SinaWeiboInfoDao sinaWeiboInfoDao = new SinaWeiboInfoDaoImpl(tokenService);

		/**
		 * 链接数据库
		 */
		WeiboJDBC weiboJDBC = new WeiboJDBC("192.168.1.39", "pp_fenxi", "q#tmuYzC@sqB6!ok@sHd", "pp_fenxi");
		if (!weiboJDBC.dbConnection()) {
			logger.info("Db connected error.");
			return;
		}

		// 创建存放表名表
		weiboJDBC.createTablenamesTable(SINA_USER_WEIBOS_TABLENAMES);
		// 获取表名
		String tablename = SINA_USER_WEIBOS + System.currentTimeMillis() / 1000;
		// 创建微博数据表
		weiboJDBC.createSinaUserWeibosTable(tablename);
		// 插入表名
		weiboJDBC.insertTablename(SINA_USER_WEIBOS_TABLENAMES, tablename);

		int count = 0;
		for (int i = 0; i < 10_0000_0000; i++) {
			count++;
			/**
			 * 检查是否更换微博数据表
			 */
			if (count % 1_0000 == 0) {
				logger.info("Read at: " + count);
				// 查看微博数据表最大值
				int max = weiboJDBC.getMaxId(tablename);
				if (max > 1000_0000) {
					// 获取表名
					tablename = SINA_USER_WEIBOS + System.currentTimeMillis() / 1000;
					// 创建微博数据表
					weiboJDBC.createSinaUserWeibosTable(tablename);
					// 插入表名
					weiboJDBC.insertTablename(SINA_USER_WEIBOS_TABLENAMES, tablename);
				}
			}
			StatusWapper publicWeibos = sinaWeiboInfoDao.getPublicWeibos();
			if ((publicWeibos == null) || (publicWeibos.getStatuses().size() == 0)) {
				logger.info("Uid unused or api error.");
				continue;
			}
			for (Status weibo : publicWeibos.getStatuses()) {
				Boolean flag = weiboJDBC.isExistingWeibo(tablename, Long.parseLong(weibo.getId()));
				if (!flag) {
					try {
						weiboJDBC.insertSinaUserWeibo(tablename, weibo, true);
					} catch (SQLException e) {
						weiboJDBC.insertSinaUserWeibo(tablename, weibo, false);
					}
				}
			}
		}

		//		Thread.sleep(50 * 1000);
		//		weiboJDBC.dbClose();
	}

}
