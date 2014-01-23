package cc.pp.sina.bozhus.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.bozhus.baseinfo.PPUserBaseInfoMain;
import cc.pp.sina.bozhus.baseinfo.RemoveRepeat;
import cc.pp.sina.bozhus.baseinfo.TimerPPBaseInfo;
import cc.pp.sina.bozhus.baseinfo.UserBaseInfoMain;
import cc.pp.sina.bozhus.extend.CreateExtendTables;
import cc.pp.sina.bozhus.fans.FansAnalysis;
import cc.pp.sina.bozhus.fans.FansAnalysisDemo;
import cc.pp.sina.bozhus.fans.FansAnalysisMain;
import cc.pp.sina.bozhus.fans.PPUserFansDemo;
import cc.pp.sina.bozhus.fans.PPUserFansUpdateMain;
import cc.pp.sina.bozhus.friends.CreateFriendsTable;
import cc.pp.sina.bozhus.library.BozhuLibraryMain;
import cc.pp.sina.bozhus.library.BozhusLeftGetMain;
import cc.pp.sina.bozhus.pp.HighQualityUsersMain;
import cc.pp.sina.bozhus.tags.BatchTagsMain;
import cc.pp.sina.bozhus.tags.RemoveRepeatsForTags;
import cc.pp.sina.bozhus.weibos.HistoryWeibosDumpMain;
import cc.pp.sina.bozhus.weibos.SingleThreadPublicWeibos;

/**
 * 驱动类
 * @author wgybzb
 *
 */
public class SinaBozhusDriver {

	private static Logger logger = LoggerFactory.getLogger(SinaBozhusDriver.class);

	public static void main(String[] args) {

		if (args.length == 0) {
			System.err.println("Usage: Driver <class-name>");
			System.exit(-1);
		}

		String[] restArgs = new String[args.length - 1];
		System.arraycopy(args, 1, restArgs, 0, restArgs.length);

		switch (args[0]) {
		case "singleThreadPublicWeibos":
			logger.info("单线程采集新浪公共微博数据： ");
			SingleThreadPublicWeibos.main(restArgs);
			break;
		case "historyWeibosDumpMain":
			logger.info("采集用户微博数据： ");
			HistoryWeibosDumpMain.main(null);
			break;
		case "bozhuLibraryMain":
			logger.info("采集用户特性数据： ");
			BozhuLibraryMain.main(null);
			break;
		case "ppUserFans":
			logger.info("采集皮皮用户粉丝数据： ");
			PPUserFansDemo.main(null);
			break;
		case "ppUserFansUpdateMain":
			logger.info("更新皮皮用户粉丝数据： ");
			PPUserFansUpdateMain.main(null);
			break;
		case "bozhusLeftGetMain":
			logger.info("更新博主库其他用户粉丝特性数据： ");
			BozhusLeftGetMain.main(null);
			break;
		case "ppUserBaseInfoMain":
			logger.info("采集皮皮新浪用户基础数据: ");
			PPUserBaseInfoMain.main(null);
			break;
		case "createExtendTables":
			logger.info("创建新浪用户扩展信息表： ");
			CreateExtendTables.main(restArgs);
			break;
		case "removeRepeat":
			logger.info("去除新浪重复的用户基础信息： ");
			RemoveRepeat.main(null);
			break;
		case "batchTagsMain":
			logger.info("批量获取用户标签数据: ");
			BatchTagsMain.main(null);
			break;
		case "removeRepeatsForTags":
			logger.info("去除新浪重复的用户标签数据: ");
			RemoveRepeatsForTags.main(null);
			break;
		case "userBaseInfoMain":
			logger.info("全量用户基础信息采集： ");
			UserBaseInfoMain.main(null);
			break;
		case "fansAnalysisDemo":
			logger.info("粉丝分析数据测试： ");
			FansAnalysisDemo.main(restArgs);
			break;
		case "fansAnalysis":
			logger.info("皮皮用户粉丝分析测试： ");
			FansAnalysis.main(restArgs);
			break;
		case "fansAnalysisMain":
			logger.info("皮皮用户粉丝分析： ");
			FansAnalysisMain.main(null);
			break;
		case "timerPPBaseInfo":
			logger.info("定时采集皮皮用户基础信息: ");
			TimerPPBaseInfo.main(null);
			break;
		case "highQualityUsersMain":
			logger.info("获取高质量的皮皮用户信息: ");
			HighQualityUsersMain.main(null);
			break;
		case "createFriendsTable":
			logger.info("创建新浪用户关注数据表: ");
			CreateFriendsTable.main(restArgs);
			break;
		default:
			return;
		}
	}

}
