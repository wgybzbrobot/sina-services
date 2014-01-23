package cc.pp.sina.dao.price;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class BozhuDbConnection {

	private static final Logger logger = LoggerFactory
			.getLogger(BozhuDbConnection.class);

	private static SqlSessionFactoryBuilder sqlSessionFactoryBuilder;
	private static SqlSessionFactory sqlSessionFactory = null;

	/**
	 * 建立数据库连接，创建Mybatis的SqlSessionFactory
	 *
	 * @param url      数据库连接url
	 * @param username 数据库帐号
	 * @param password 数据库密码
	 */
	public static void connectDb(String url, String username, String password) {
		Properties db = new Properties();
		db.put("driver", "com.mysql.jdbc.Driver");
		db.put("url", url);
		db.put("username", username);
		db.put("password", password);

		sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
		sqlSessionFactory = sqlSessionFactoryBuilder.build(BozhuDbConnection.class.getClassLoader().getResourceAsStream("mybatis_bozhu_price.xml"), db);
		logger.info("Connect Database : " + url);
	}

	public static SqlSessionFactory getSessionFactory() {
		if (sqlSessionFactory == null) {
			throw new NullPointerException("尚未调用DbConnection.connectDb方法连接DB");
		} else {
			return sqlSessionFactory;
		}
	}

}
