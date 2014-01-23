package cc.pp.sina.bozhus.pp;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.bozhus.info.SinaUserInfoDao;
import cc.pp.sina.bozhus.sql.WeiboJDBC;
import cc.pp.sina.domain.users.SinaUserInfo;

import com.sina.weibo.model.User;

public class HighQualityUsersRun implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(HighQualityUsersRun.class);

	public static final String PP_HIGH_QUALITY_USERS = "pp_high_quality_users";
	public static final String SINA_USER_BASEINFO = "sinauserbaseinfo_";

	private final WeiboJDBC weiboJDBC;
	private final SinaUserInfoDao sinaUserInfoDao;
	private final String uid;

	public HighQualityUsersRun(WeiboJDBC weiboJDBC, SinaUserInfoDao sinaUserInfoDao, String uid) {
		this.weiboJDBC = weiboJDBC;
		this.sinaUserInfoDao = sinaUserInfoDao;
		this.uid = uid;
	}

	private static AtomicInteger count = new AtomicInteger(0);

	@Override
	public void run() {

		System.out.println(count.addAndGet(1));

		try {
			String tablename = SINA_USER_BASEINFO + Long.parseLong(uid) % 32;
			SinaUserInfo baseInfo = null;
			try {
				baseInfo = weiboJDBC.getSinaUserBaseInfo(tablename, uid);
			} catch (SQLException e2) {
				logger.error("Gettting SinaUserBaseInfo of '" + uid + "' occurs error.");
			}
			if (baseInfo != null) {
				if (baseInfo.getFollowers_count() > 1_0000) {
					weiboJDBC.inserSinaUserBaseinfo(PP_HIGH_QUALITY_USERS, baseInfo);
				}
			} else {
				User user = sinaUserInfoDao.getSinaUserBaseInfo(uid);
				if (user != null) {
					try {
						/**
						 * 删除该用户信息，不管有无
						 */
						weiboJDBC.deleteSinaUserBaseInfo(tablename, uid);
						/**
						 * 插入用户基础数据
						 */
						weiboJDBC.inserSinaUserBaseinfo(tablename, user, true);
						if (user.getFollowersCount() > 1_0000) {
							weiboJDBC.inserSinaUserBaseinfo(PP_HIGH_QUALITY_USERS, user, true);
						}
					} catch (SQLException | UnsupportedEncodingException e) {
						logger.info("User: " + uid + "'s baseinfo is non-utf8 encoded.");
						try {
							weiboJDBC.inserSinaUserBaseinfo(tablename, user, false);
							if (user.getFollowersCount() > 1_0000) {
								weiboJDBC.inserSinaUserBaseinfo(PP_HIGH_QUALITY_USERS, user, false);
							}
						} catch (UnsupportedEncodingException | SQLException e1) {
							logger.info("Weibo: " + user.getId() + "'s text is UnsupportedEncodingException.");
						}
					}
				} else {
					try {
						weiboJDBC.updateSinaUnexistedUser(tablename, uid);
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}
				}
			}
		} catch (RuntimeException e) {
			logger.error("RuntimeException: " + e.getMessage());
		}
	}

}
