package cc.pp.sina.bozhus.baseinfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.bozhus.info.SinaUserInfoDao;
import cc.pp.sina.bozhus.info.SinaUserInfoDaoImpl;
import cc.pp.sina.bozhus.sql.WeiboJDBC;
import cc.pp.sina.bozhus.trans.uid.TransToUid;
import cc.pp.sina.tokens.service.TokenService;

import com.sina.weibo.model.User;

/**
 * 临时采集新浪用户基础数据
 * @author wgybzb
 *
 */
public class LocalBaseInfoDump {

	public static Logger logger = LoggerFactory.getLogger(LocalBaseInfoDump.class);

	/**
	 * 主函数
	 */
	public static void main(String[] args) throws Exception {

		LocalBaseInfoDump.dumpBaseinfo();

	}

	/**
	 * 采集基础数据
	 */
	public static void dumpBaseinfo() throws Exception {

		WeiboJDBC weiboJDBC = new WeiboJDBC("127.0.0.1", "root", "root", "pp_fenxi");

		TokenService tokenService = new TokenService();
		List<String> sinaTokens = weiboJDBC.getSinaToken("wb_member_bind_sina");
		tokenService.setSinaTokens(sinaTokens);

		SinaUserInfoDao sinaUserInfoDao = new SinaUserInfoDaoImpl(tokenService);

		TransToUid domainToUid = new TransToUid(sinaUserInfoDao);

		/**
		 * 创建数据表
		 */
		String tablename = "sina_user_baseinfo_star_20140109";
		weiboJDBC.createSinaBaseInfoTable(tablename);

		//		List<String> uids = weiboJDBC.getSinaUid("pp_sina_compamy", 10_0000);
		//
		//		int count = 0;
		//		for (String uid : uids) {
		//			logger.info("Read at: " + count++);
		//			User user = sinaUserInfoDao.getSinaUserBaseInfo(new Users(), uid);
		//			if (user != null) {
		//				try {
		//					weiboJDBC.inserSinaUserBaseinfo(tablename, user, true);
		//				} catch (SQLException e) {
		//					weiboJDBC.inserSinaUserBaseinfo(tablename, user, false);
		//				}
		//			}
		//		}

		BufferedReader fr = new BufferedReader(new FileReader(new File("users/sina_users")));
		String url, uid;
		int count = 0;
		while ((url = fr.readLine()) != null) {

			logger.info("Read at: " + count++);

			uid = domainToUid.transDidToUid(url);

			User user = null;
			try {
				user = sinaUserInfoDao.getSinaUserBaseInfo(uid);
			} catch (Exception e) {
				//
			}
			if (user != null) {
				try {
					weiboJDBC.inserSinaUserBaseinfo(tablename, user, true);
				} catch (SQLException e) {
					logger.info("SQLException: " + e.getMessage());
					weiboJDBC.inserSinaUserBaseinfo(tablename, user, false);
				}
			}
		}
		fr.close();
		weiboJDBC.dbClose();
	}
}
