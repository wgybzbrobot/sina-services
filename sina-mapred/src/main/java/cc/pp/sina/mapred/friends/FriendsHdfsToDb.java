package cc.pp.sina.mapred.friends;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.dao.common.MybatisConfig;
import cc.pp.sina.dao.friends.SinaFriendsDis;

/**
 * 从HDFS中将新浪用户关注数据导入数据库
 *     注： 0～31表：wuhu008
 *         32～63表：wuhu009
 * @author wgybzb
 *
 */
public class FriendsHdfsToDb {

	private static Logger logger = LoggerFactory.getLogger(FriendsHdfsToDb.class);

	private static final String FRIENDS_URL = "/user/hadoop/datacenter/sinaUserFriends/";
	private static final String SINA_USER_FRIENDS = "sina_user_friends_";
	private static SinaFriendsDis sinaFriends1 = new SinaFriendsDis(MybatisConfig.ServerEnum.friend1);
	private static SinaFriendsDis sinaFriends2 = new SinaFriendsDis(MybatisConfig.ServerEnum.friend2);

	private final FileSystem fs;
	private final SequenceFile.Reader reader;
	private final Writable key;
	private final Writable value;

	public FriendsHdfsToDb(String fileName) throws IOException {
		Configuration conf = new Configuration();
		conf.addResource("core-site.xml");
		logger.info("HDFS Url: " + conf.get("fs.default.name"));
		fs = FileSystem.get(URI.create(FRIENDS_URL + fileName), conf);
		reader = new SequenceFile.Reader(fs, new Path(FRIENDS_URL + fileName), conf);
		key = (Writable) ReflectionUtils.newInstance(reader.getKeyClass(), conf);
		value = (Writable) ReflectionUtils.newInstance(reader.getValueClass(), conf);
	}

	public static List<String> getFiles() {

		List<String> result = new ArrayList<>();
		Configuration conf = new Configuration();
		conf.addResource("core-site.xml");
		logger.info("HDFS Url: " + conf.get("fs.default.name"));
		try {
			FileSystem fileSystem = FileSystem.get(URI.create(FRIENDS_URL), conf);
			FileStatus[] status = fileSystem.listStatus(new Path(FRIENDS_URL));
			Path[] listedPaths = FileUtil.stat2Paths(status);
			for (Path p : listedPaths) {
				result.add(p.getName());
			}
			return result;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		List<String> files = FriendsHdfsToDb.getFiles();
		for (int i = 0; i < files.size(); i++) {
			FriendsHdfsToDb friendsHdfsToDb = null;
			try {
				logger.info("Read at: " + i);
				friendsHdfsToDb = new FriendsHdfsToDb(files.get(i));
				friendsHdfsToDb.dumpDataToDb();
			} catch (IOException e) {
				logger.info("IOException: " + e.getMessage());
				//			throw new RuntimeException(e);
			} finally {
				if (friendsHdfsToDb != null) {
					friendsHdfsToDb.close();
				}
			}
		}
	}

	/**
	 * 向数据库中导入关注数据
	 */
	public void dumpDataToDb() throws IOException {

		long position = reader.getPosition();
		logger.info("Start at: " + position);

		StringBuffer stringBuffer = new StringBuffer();
		long username;
		Writable currentKey = null;
		int count = 0;
		while (reader.next(key, value)) {
			if (currentKey == null) {
				currentKey = key;
				logger.info("Current count: " + ++count);
			}
			if (currentKey.toString().equalsIgnoreCase(key.toString())) {
				username = Long.parseLong(key.toString());
				if (stringBuffer.length() > 5) {
					if (username % 64 < 32) {
						if (sinaFriends1.getSinaFriendsInfo(SINA_USER_FRIENDS + username % 64, username) == null) {
							sinaFriends1.insertSinaFriendsInfo(SINA_USER_FRIENDS + username % 64, username,
									stringBuffer.substring(0, stringBuffer.length() - 1), stringBuffer.toString()
											.split(",").length, System.currentTimeMillis() / 1000);
						}
					} else {
						if (sinaFriends2.getSinaFriendsInfo(SINA_USER_FRIENDS + username % 64, username) == null) {
							sinaFriends2.insertSinaFriendsInfo(SINA_USER_FRIENDS + username % 64, username,
									stringBuffer.substring(0, stringBuffer.length() - 1), stringBuffer.toString()
											.split(",").length, System.currentTimeMillis() / 1000);
						}
					}
				}
				currentKey = key;
				logger.info("Current count: " + ++count);
				stringBuffer = new StringBuffer();
			}
			stringBuffer.append(value.toString()).append(",");
			position = reader.getPosition();
			//			logger.info("Position at: " + position);
		}
	}

	public void close() {
		try {
			fs.close();
			IOUtils.closeStream(reader);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
