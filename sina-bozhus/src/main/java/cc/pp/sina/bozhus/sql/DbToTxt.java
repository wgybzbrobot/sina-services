package cc.pp.sina.bozhus.sql;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DbToTxt {

	public static void main(String[] args) throws SQLException, IOException {

		WeiboJDBC weiboJDBC = new WeiboJDBC("127.0.0.1", "root", "root", "pp_fenxi");
		if (!weiboJDBC.dbConnection()) {
			System.err.println("Db connected error.");
		}

		List<String> uids = weiboJDBC.getUids("a_bz_bozhu", 500_0000);

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File("sina_bozhus_uids")));) {
			for (String uid : uids) {
				bw.append(uid);
				bw.newLine();
			}
		}

		weiboJDBC.dbClose();
	}

}
