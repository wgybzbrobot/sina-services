package cc.pp.sina.mapred.sql;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import cc.pp.sina.utils.net.Nettool;

public class SqlUtils {

	public static void main(String[] args) throws IOException, SQLException {

		WeiboJDBC weiboJDBC = new WeiboJDBC(Nettool.getServerLocalIp());
		if (!weiboJDBC.mysqlStatus()) {
			System.err.println("Db cannot connected.");
			return;
		}

		SqlUtils.filterSinaUserByFansCount(weiboJDBC, "sinausers_up_1000", 1000);

		weiboJDBC.sqlClose();
	}

	public static void filterSinaUserByFansCount(WeiboJDBC weiboJDBC, String file, int threshold) throws IOException,
			SQLException {

		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(file)));
		String tablename = "sinauserbaseinfo_";

		List<String> sinauids = null;
		for (int i = 0; i < 32; i++) {
			int max = weiboJDBC.getMaxId(tablename + i);
			for (int j = 0; j < max / 10_0000; j++) {

				System.out.println(tablename + i + "    ,    " + j);

				sinauids = weiboJDBC.getSinaUidAndFanscount(tablename + i, j, (j + 1) * 10_0000, threshold);
				for (String uid : sinauids) {
					bw.append(uid);
					bw.newLine();
				}
			}
			sinauids = weiboJDBC.getSinaUidAndFanscount(tablename + i, max / 10_0000, max, threshold);
			for (String uid : sinauids) {
				bw.append(uid);
				bw.newLine();
			}
		}

		bw.close();
	}

}
