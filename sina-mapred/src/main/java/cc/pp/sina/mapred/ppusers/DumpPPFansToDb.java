package cc.pp.sina.mapred.ppusers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.MapFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.mapred.sql.WeiboJDBC;
import cc.pp.sina.utils.net.Nettool;
import cc.pp.sina.utils.threads.pool.ApplyThreadPool;

public class DumpPPFansToDb {

	private static Logger logger = LoggerFactory.getLogger(DumpPPFansToDb.class);

	public static void main(String[] args) throws IOException, InterruptedException {

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

		String uid = "";
		try (BufferedReader br = new BufferedReader(new FileReader(new File("pp_sina_uids")));) {
			while (((uid = br.readLine()) != null) && (!pool.isShutdown())) {
				pool.execute(new DumpPPFansToDbRun(reader, weiboJDBC, uid));
			}
		}

		//		fs.close();
		//		weiboJDBC.sqlClose();

		pool.shutdown();
		pool.awaitTermination(300, TimeUnit.SECONDS);
	}

}
