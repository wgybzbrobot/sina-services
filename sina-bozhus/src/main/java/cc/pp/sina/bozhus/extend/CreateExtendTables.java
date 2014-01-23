package cc.pp.sina.bozhus.extend;

import java.sql.SQLException;

import cc.pp.sina.bozhus.sql.WeiboJDBC;

/**
 * 创建新浪用户扩展数据表
 * @author wgybzb
 *
 */
public class CreateExtendTables {

	private static final String EXTEND_TABLE = "sina_user_extend_info_";

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			System.err.println("Usage: <tableNum>");
			System.exit(-1);
		}
		int num = Integer.parseInt(args[0]);
		CreateExtendTables.createTables(num);
	}

	/**
	 * 创建表格
	 */
	public static void createTables(int num) {

		//		WeiboJDBC weiboJDBC = new WeiboJDBC("127.0.0.1", "root", "root", "pp_fenxi");
		WeiboJDBC weiboJDBC = new WeiboJDBC("192.168.1.48", "pp_fenxi", "q#tmuYzC@sqB6!ok@sHd", "pp_fenxi");
		if (!weiboJDBC.dbConnection()) {
			System.err.println("数据库链接失败！");
		}
		for (int i = 0; i < num; i++) {
			try {
				weiboJDBC.createExtendTable(EXTEND_TABLE + i);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		weiboJDBC.dbClose();
	}

}
