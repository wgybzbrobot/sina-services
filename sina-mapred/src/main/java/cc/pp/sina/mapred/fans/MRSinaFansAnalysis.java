package cc.pp.sina.mapred.fans;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.mapred.lib.input.IgnoreEofSequenceFileInputFormat;
import cc.pp.sina.mapred.sql.WeiboJDBC;

import com.google.common.base.Splitter;

public class MRSinaFansAnalysis {

	private static Logger logger = LoggerFactory.getLogger(MRSinaFansAnalysis.class);

	private final static WeiboJDBC weiboJDBC;

	static {
		weiboJDBC = new WeiboJDBC("192.168.1.48");
		if (!weiboJDBC.mysqlStatus()) {
			logger.info("Mysql connected error.");
		}
	}

	public static void closed() {
		weiboJDBC.sqlClose();
	}

	public static class SinaFansAnalysisMapper extends Mapper<Text, Text, Text, Text> {

		private static int[] quality = null;
		private static String username = null;
		private final Text result = new Text();

		@Override
		protected void map(Text uid, Text value, Context context) throws IOException, InterruptedException {

			quality = new int[3];
			Iterator<String> it = Splitter.on(',') //
					.trimResults() //
					.omitEmptyStrings() //
					.split(value.toString()) //
					.iterator();
			while (it.hasNext()) {
				username = it.next();
				if (username.length() > 1) {
						try {
							quality[weiboJDBC.sinaUserQualityInfo(Long.parseLong(username))]++;
						} catch (NumberFormatException | SQLException e) {
						throw new RuntimeException(e);
						}
				}
			}
			result.set(new Text(quality[0] + "," + quality[1] + "," + quality[2]));
			context.write(uid, result);
		}

	}

	public static class SinaFansAnalysisReducer extends Reducer<Text, Text, Text, Text> {

		private final Text fansQuality = new Text();
		private static int[] qualitys = null;
		private static String[] qinfo = null;

		@Override
		protected void reduce(Text uid, Iterable<Text> values, Context context) throws IOException,
				InterruptedException {

			qualitys = new int[3];
			for (Text value : values) {
				qinfo = value.toString().split(",");
				qualitys[0] += Integer.parseInt(qinfo[0]);
				qualitys[1] += Integer.parseInt(qinfo[1]);
				qualitys[2] += Integer.parseInt(qinfo[2]);
			}
			int sum = qualitys[0] + qualitys[1] + qualitys[2];
			if (sum == 0) {
				fansQuality.set("Nan");
			} else {
				fansQuality.set(Math.round(((float) (qualitys[1] + qualitys[2]) / sum) * 10000) / 100 + "%" + ","
						+ Math.round(((float) qualitys[2] / sum) * 10000) / 100 + "%");
			}

			context.write(uid, fansQuality);
		}
	}

	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if (otherArgs.length != 2) {
			System.err.println("Usage: SinaFansAnalysis <in> <out>");
			System.exit(2);
		}
		Job job = new Job(conf, "SinaFansAnalysis by MapReduce");
		job.setJarByClass(MRSinaFansAnalysis.class);

		job.setMapperClass(SinaFansAnalysisMapper.class);
		job.setCombinerClass(SinaFansAnalysisReducer.class);
		job.setReducerClass(SinaFansAnalysisReducer.class);
		job.setInputFormatClass(IgnoreEofSequenceFileInputFormat.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setNumReduceTasks(40);

		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

		boolean exitCode = job.waitForCompletion(true);
		MRSinaFansAnalysis.closed();

		System.exit(exitCode ? 0 : 1);
	}

}
