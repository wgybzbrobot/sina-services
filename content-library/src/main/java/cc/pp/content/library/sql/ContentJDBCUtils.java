package cc.pp.content.library.sql;

import java.sql.SQLException;

public class ContentJDBCUtils {

	/**
	 * 测试函数
	 */
	public static void main(String[] args) throws SQLException {

		System.out.println(ContentJDBCUtils.getTablesNum("pp_sina_company_"));

	}

	/**
	 * 根据数据表名的前缀查看该类数据表的个数
	 * @param tablePrefix：如：wb_library_record_
	 * @return int
	 */
	public static int getTablesNum(String tablePrefix) {

		ContentJDBC contentJDBC = new ContentJDBC();
		try {
			int count = 0;
			for (String table : contentJDBC.getTablesInCurrentDB()) {
				if (table.contains(tablePrefix)) {
					count++;
				}
			}
			return count;
		} catch (SQLException e) {
			return 0;
		} finally {
			contentJDBC.dbClose();
		}
	}

}
