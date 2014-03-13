package cc.pp.sina.bozhus.fans;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

import cc.pp.sina.bozhus.sql.WeiboJDBC;

public class PPUserFansDemo {

	//	private static Logger logger = LoggerFactory.getLogger(PPUserFansDemo.class);

	private static final String PP_USER_FANS = "pp_sina_fans";

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		try (BufferedReader br = new BufferedReader(new FileReader(new File("pp_sina_uids_all")));
				BufferedWriter bw = new BufferedWriter(new FileWriter(new File("pp_sina_uids")));) {
			WeiboJDBC weiboJDBC = new WeiboJDBC("192.168.1.48", "pp_fenxi", "q#tmuYzC@sqB6!ok@sHd", "pp_fenxi");

			String uid;
			int count = 1;
			while ((uid = br.readLine()) != null) {
				System.out.println(count++);
				if (!weiboJDBC.isPPSinaUserExisted(PP_USER_FANS, uid)) {
					bw.append(uid);
					bw.newLine();
				}
			}
			weiboJDBC.dbClose();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
