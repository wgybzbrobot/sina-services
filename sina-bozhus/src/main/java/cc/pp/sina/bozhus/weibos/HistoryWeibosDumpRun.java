package cc.pp.sina.bozhus.weibos;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.bozhus.info.SinaWeiboInfoDao;
import cc.pp.sina.bozhus.sql.WeiboJDBC;

import com.sina.weibo.model.Status;
import com.sina.weibo.model.StatusWapper;

public class HistoryWeibosDumpRun implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(HistoryWeibosDumpRun.class);

	private final SinaWeiboInfoDao sinaWeiboInfoDao;

	private final WeiboJDBC weiboJDBC;
	private final String uid;
	private final String tablename;

	private static final int PAGE_COUNT = 20;

	public HistoryWeibosDumpRun(WeiboJDBC weiboJDBC, String tablename, String uid, SinaWeiboInfoDao sinaWeiboInfoDao) {
		this.weiboJDBC = weiboJDBC;
		this.tablename = tablename;
		this.uid = uid;
		this.sinaWeiboInfoDao = sinaWeiboInfoDao;
	}

	//	private static AtomicInteger count = new AtomicInteger(0);

	@Override
	public void run() {

		//		System.out.println(count.addAndGet(1));

		try {

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
						try {
							weiboJDBC.insertSinaUserWeibo(tablename, weibo, false);
						} catch (SQLException e1) {
							logger.error("SQLException: " + e.getMessage());
						}
					}
				}
				if (cursor > Math.min(PAGE_COUNT, 20)) { // 限制pageCount次，最大20次
					break;
				}

				status = sinaWeiboInfoDao.getSinaUserWeibos(uid, ++cursor);
				if (status == null) {
					break;
				}
			}
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
//			logger.info("User: " + uid + "'s weibos has RuntimeException: " + e.getMessage());
		}

	}

}
