package cc.pp.sina.mapred.fans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.MapFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.mapred.baseinfo.SinaUserBaseInfoDao;
import cc.pp.sina.mapred.baseinfo.SinaUserBaseInfoDaoImpl;
import cc.pp.sina.mapred.sql.WeiboJDBC;
import cc.pp.sina.utils.net.Nettool;
import cc.pp.sina.utils.threads.pool.ApplyThreadPool;

public class ThreadsSinaFansAnalysis {

	private static Logger logger = LoggerFactory.getLogger(ThreadsSinaFansAnalysis.class);

	public static void main(String[] args) throws InterruptedException, SQLException, IOException {

		final ThreadPoolExecutor pool = ApplyThreadPool.getThreadPoolExector();

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				pool.shutdown();
			}
		}));

		Configuration conf = new Configuration();
		conf.addResource("core-site.xml");
		final String MAPFILE_URL = "/user/hadoop/datacenter/sortedSinaUserFans";
		FileSystem fs = FileSystem.get(URI.create(MAPFILE_URL), conf);
		MapFile.Reader reader = new MapFile.Reader(fs, MAPFILE_URL, conf);

		WeiboJDBC weiboJDBC = new WeiboJDBC(Nettool.getServerLocalIp());
		if (!weiboJDBC.mysqlStatus()) {
			logger.info("Db connected error.");
		}

		BufferedReader br = new BufferedReader(new FileReader(new File("sinaDataserver/sinausers_low_10000_up_1000")));

		List<String> tokens = weiboJDBC.getSinaAllTokens();
		SinaUserBaseInfoDao sinaUserBaseinfoDao = new SinaUserBaseInfoDaoImpl(tokens);

		String uid = null;
		int count = 0;
		while ((uid = br.readLine()) != null) {
			count++;
			if (count > 86_0000) {
				if ((uid.length() > 1) && (!pool.isShutdown())) {
					pool.execute(new SinaFansAnalysisRun(sinaUserBaseinfoDao, reader, weiboJDBC, uid));
				}
			}
		}
		br.close();
		fs.close();
		weiboJDBC.sqlClose();

		pool.shutdown();
		pool.awaitTermination(300, TimeUnit.SECONDS);
	}

}
