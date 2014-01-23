package cc.pp.sina.mapred.ppusers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.hadoop.io.MapFile;
import org.apache.hadoop.io.MapFile.Reader;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import cc.pp.sina.mapred.sql.WeiboJDBC;

public class DumpPPFansToDbRun implements Runnable {

	private static final String PP_USER_FANS_TABLE = "pp_sina_fans";

	private final MapFile.Reader reader;
	private final WritableComparable<?> key;
	private final Writable value;
	private final WeiboJDBC weiboJDBC;
	private final String uid;

	private static AtomicInteger count = new AtomicInteger(0);

	public DumpPPFansToDbRun(Reader reader, WeiboJDBC weiboJDBC, String uid) {
		this.reader = reader;
		this.weiboJDBC = weiboJDBC;
		this.uid = uid;
		this.key = new Text();
		this.value = new Text();
	}

	@Override
	public void run() {

		System.out.println(count.addAndGet(1));

		try {
			weiboJDBC.insertPPSinaUserFans(PP_USER_FANS_TABLE, uid, getUserAllFans(new Text(uid)), 0);
		} catch (NumberFormatException | IOException | SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获取用户全部粉丝
	 */
	public String getUserAllFans(WritableComparable<?> querykey) throws IOException, NumberFormatException,
			SQLException {

		StringBuffer result = new StringBuffer();
		reader.get(querykey, value);
		if (value == null) {
			return null;
		}
		result.append(value.toString()).append(",");
		while (reader.next(key, value)) {
			if (key.toString().equals(querykey.toString())) {
				result.append(value.toString()).append(",");
			} else {
				break;
			}
		}

		return result.toString().substring(0, result.length() - 1);
	}

}
