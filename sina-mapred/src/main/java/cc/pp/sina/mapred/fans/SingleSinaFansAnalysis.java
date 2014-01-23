package cc.pp.sina.mapred.fans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapFile;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.mapred.sql.WeiboJDBC;

public class SingleSinaFansAnalysis {

	private static Logger logger = LoggerFactory.getLogger(SingleSinaFansAnalysis.class);

	//	public final static String SINA_UID = "sinausers_up_1000";
	private final static String MAPFILE_URL = "/user/hadoop/datacenter/sortedSinaUserFans";

	private static Configuration conf;
	private static FileSystem fs;
	private static MapFile.Reader reader;
	private static WritableComparable<?> key;
	private static Writable value;
	private static WeiboJDBC weiboJDBC;

	/**
	 * 初始化MapFile，并启动
	 */
	public SingleSinaFansAnalysis() throws IOException, SQLException {

		conf = new Configuration();
		conf.addResource("core-site.xml");
		fs = FileSystem.get(URI.create(MAPFILE_URL), conf);
		reader = new MapFile.Reader(fs, MAPFILE_URL, conf);
		key = (WritableComparable<?>) ReflectionUtils.newInstance(reader.getKeyClass(), conf);
		value = (Writable) ReflectionUtils.newInstance(reader.getValueClass(), conf);
		weiboJDBC = new WeiboJDBC("192.168.1.48");
		if (!weiboJDBC.mysqlStatus()) {
			logger.info("Db connected error.");
			return;
		}
	}

	public void close() throws SQLException, IOException {
		weiboJDBC.sqlClose();
		fs.close();
	}

	/**
	 * 主函数
	 * @throws SQLException
	 */
	public static void main(String[] args) throws IOException, SQLException {

		if (args.length != 1) {
			System.err.println("Usage: SingleSinaFansAnalysis <input sinausers_path>");
			System.exit(-1);
		}

		SingleSinaFansAnalysis ssfa = new SingleSinaFansAnalysis();
		Path outFile = new Path("/user/hadoop/datacenter/sinaUserFansQuality");
		LongWritable outKey = new LongWritable();
		Text outValue = new Text();
		Text username = new Text();

		String uid = "";
		int count = 0;

		try (BufferedReader br = new BufferedReader(new FileReader(new File(args[0])));
				SequenceFile.Writer writer = SequenceFile.createWriter(fs, conf, outFile, outKey.getClass(),
						outValue.getClass());) {

			Long start = System.currentTimeMillis();
			while ((uid = br.readLine()) != null) {
				if (count++ > 100) {
					break;
				}
				try {
					username.set(uid);
					outKey.set(Long.parseLong(uid));
					outValue.set(ssfa.fansQuality(username));
					writer.append(outKey, outValue);
					writer.syncFs();

					System.out.printf("[%d]\t%d\t%d\t%s\n", count, outKey.get(), ssfa.findAllValuesByKey(new Text(uid))
							.size(), outValue.toString());

				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			Long end = System.currentTimeMillis();

			System.out.printf("100条纪录计算耗时 %d 毫秒", end - start);
			System.out.println();
		}

		ssfa.close();
	}

	/**
	 * 计算粉丝质量
	 * 分别对应：粉丝存在比例，真实用户比例
	 */
	public String fansQuality(WritableComparable<?> querykey) throws IOException, NumberFormatException, SQLException {

		int[] quality = new int[3];
		reader.get(querykey, value);
		if (value == null) {
			return "Nan";
		}
		computeQuality(quality, value);
		while (reader.next(key, value)) {
			if (key.toString().equals(querykey.toString())) {
				computeQuality(quality, value);
			} else {
				break;
			}
		}
		int sum = quality[0] + quality[1] + quality[2];
		if (sum == 0) {
			return "Nan";
		}

		return Math.round(((float) (quality[1] + quality[2]) / sum) * 10000) / 100 + "%" + ","
				+ Math.round(((float) quality[2] / sum) * 10000) / 100 + "%";
	}

	/**
	 * 计算粉丝质量
	 */
	private void computeQuality(int[] quality, Writable value) throws NumberFormatException, SQLException {

		String[] fans = value.toString().split(",");
		for (String uid : fans) {
			if (uid.length() > 1) {
				quality[weiboJDBC.sinaUserQualityInfo(Long.parseLong(uid))]++;
			}
		}
	}

	/**
	 * 针对粉丝少的可以使用
	 */
	public List<Long> findAllValuesByKey(WritableComparable<?> querykey) throws IOException {

		List<Long> result = new ArrayList<Long>();
		reader.get(querykey, value);
		result.addAll(addUids(value.toString().split(",")));
		while (reader.next(key, value)) {
			if (key.toString().equals(querykey.toString())) {
				result.addAll(addUids(value.toString().split(",")));
			} else {
				break;
			}
		}

		return result;
	}

	private List<Long> addUids(String[] fans) {

		List<Long> result = new ArrayList<Long>();
		for (String uid : fans) {
			if ((uid != null) && (uid.length() > 1)) {
				result.add(Long.parseLong(uid));
			}
		}
		return result;
	}

}
