package cc.pp.sina.mapred.fans;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;

import cc.pp.sina.domain.mapred.SimpleBozhuInfoOne;
import cc.pp.sina.mapred.baseinfo.SinaUserBaseInfoDao;
import cc.pp.sina.mapred.baseinfo.SinaUserBaseInfoDaoImpl;
import cc.pp.sina.mapred.sql.WeiboJDBC;
import cc.pp.sina.utils.net.Nettool;
import cc.pp.sina.utils.threads.pool.ApplyThreadPool;

public class ImportDataToBozhuku {

	public static void main(String[] args) throws IOException, InterruptedException, SQLException {

		if (args.length != 1) {
			System.err.println("Usage: <input sequence_file>");
			System.exit(-1);
		}

		final ThreadPoolExecutor pool = ApplyThreadPool.getThreadPoolExector();

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				pool.shutdown();
			}
		}));

		WeiboJDBC weiboJDBC = new WeiboJDBC(Nettool.getServerLocalIp());
		if (!weiboJDBC.mysqlStatus()) {
			System.out.println("Db connected error.");
			return;
		}

		List<String> tokens = weiboJDBC.getSinaAllTokens();

		System.out.println(tokens.size());

		SinaUserBaseInfoDao sinaUserBaseinfoDao = new SinaUserBaseInfoDaoImpl(tokens);

		String uri = args[0];
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		Path path = new Path(uri);

		try (SequenceFile.Reader reader = new SequenceFile.Reader(fs, path, conf);) {

			Writable key = (Writable) ReflectionUtils.newInstance(reader.getKeyClass(), conf);
			Writable value = (Writable) ReflectionUtils.newInstance(reader.getValueClass(), conf);
			String[] info = null;
			while (reader.next(key, value)) {
				if (!value.toString().contains("Nan")) {
					info = value.toString().split(",");
					SimpleBozhuInfoOne bozhuInfo = new SimpleBozhuInfoOne.Builder()
							.setExistFanRate((float) Integer.parseInt(info[0].substring(0, info[0].length() - 1)) / 100)
							.setActFanRate((float) Integer.parseInt(info[1].substring(0, info[1].length() - 1)) / 100)
							.build();
					if (!pool.isShutdown()) {
						pool.execute(new ImportDataToBozhukuRun(key.toString(), bozhuInfo, sinaUserBaseinfoDao));
					}
				}
			}
		}

		pool.shutdown();
		pool.awaitTermination(30, TimeUnit.SECONDS);
		weiboJDBC.sqlClose();
		fs.close();
	}

}
