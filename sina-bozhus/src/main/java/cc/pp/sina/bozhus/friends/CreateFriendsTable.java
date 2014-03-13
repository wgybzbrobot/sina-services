package cc.pp.sina.bozhus.friends;

import java.sql.SQLException;

import cc.pp.sina.bozhus.sql.WeiboJDBC;

/**
 * 创建新浪用户关注数据表
 * @author wgybzb
 *
 */
public class CreateFriendsTable {

	public static void main(String[] args) {

		if (args.length != 2) {
			System.err.println("Usage: <lower> <high>");
			System.exit(-1);
		}

		//		WeiboJDBC weiboJDBC = new WeiboJDBC("127.0.0.1", "root", "root", "pp_fenxi");
		WeiboJDBC weiboJDBC = new WeiboJDBC();

		final String friend_table = "sina_user_friends_";

		for (int i = Integer.parseInt(args[0]); i <= Integer.parseInt(args[1]); i++) {
			try {
				weiboJDBC.createPPSinaUserFriendsTable(friend_table + i);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		weiboJDBC.dbClose();
	}

}
