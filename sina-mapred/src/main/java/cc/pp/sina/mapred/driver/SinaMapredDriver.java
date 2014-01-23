package cc.pp.sina.mapred.driver;

import org.apache.hadoop.util.ProgramDriver;

import cc.pp.sina.mapred.fans.ImportDataToBozhuku;
import cc.pp.sina.mapred.fans.MRSinaFansAnalysis;
import cc.pp.sina.mapred.fans.SinaUserFollower;
import cc.pp.sina.mapred.fans.SingleSinaFansAnalysis;
import cc.pp.sina.mapred.fans.ThreadsSinaFansAnalysis;
import cc.pp.sina.mapred.friends.FriendsHdfsToDb;
import cc.pp.sina.mapred.mapfile.MapFileFixer;
import cc.pp.sina.mapred.mapfile.MapFileReader;
import cc.pp.sina.mapred.mapfile.MergeSequenceFiles;
import cc.pp.sina.mapred.ppusers.DumpPPFansToDb;
import cc.pp.sina.mapred.sql.SqlUtils;
import cc.pp.sina.mapred.sql.Test;
import cc.pp.sina.mapred.userrank.SinaUserFriendsCount;

public class SinaMapredDriver {

	public static void main(String argv[]) {

		int exitCode = -1;
		ProgramDriver pgd = new ProgramDriver();
		try {
			pgd.addClass("sinaUserFollower", SinaUserFollower.class, "新浪用户粉丝倒排索引");
			pgd.addClass("mergeSequenceFiles", MergeSequenceFiles.class, "新浪粉丝序列化文件合并");
			pgd.addClass("mapFileFixer", MapFileFixer.class, "将序列化文件转换成MapFile文件");
			pgd.addClass("mapFileReader", MapFileReader.class, "在MapFile文件中查找某个key");
			pgd.addClass("sqlUtils", SqlUtils.class, "提取新浪粉丝大于1000的用户uid");
			pgd.addClass("singleSinaFansAnalysis", SingleSinaFansAnalysis.class, "单线程查找用户粉丝Uid，并分析结果");
			pgd.addClass("test", Test.class, "线上测试");
			pgd.addClass("threadsSinaFansAnalysis", ThreadsSinaFansAnalysis.class, "多线程查找用户粉丝Uid，并分析结果");
			pgd.addClass("mrSinaFansAnalysis", MRSinaFansAnalysis.class, "使用MapReduce进行用户粉丝分析");
			pgd.addClass("importDataToBozhuku", ImportDataToBozhuku.class, "将博主质量结果以Http形式导入博主库");
			pgd.addClass("sinaUserFriendsCount", SinaUserFriendsCount.class, "计算新浪用户的关注数量");
			pgd.addClass("dumpPPFansToDb", DumpPPFansToDb.class, "从HDFS中将皮皮的新浪授权用户粉丝导入数据库中");
			pgd.addClass("friendsHdfsToDb", FriendsHdfsToDb.class, "从HDFS中将新浪全量用户关注数据导入数据库中");
			pgd.driver(argv);

			// Success
			exitCode = 0;
		} catch (Throwable e) {
			e.printStackTrace();
		}

		System.exit(exitCode);
	}
}
