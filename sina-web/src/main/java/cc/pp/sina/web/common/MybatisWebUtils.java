package cc.pp.sina.web.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MybatisWebUtils {

	public enum ServerEnum {
		local, wuhu, beijing, tongling
	}

	private static Map<ServerEnum, SqlSessionFactory> sessionFactorys = new HashMap<ServerEnum, SqlSessionFactory>();

	static {
		for (ServerEnum server : ServerEnum.values()) {
			try (InputStream inputStream = Resources.getResourceAsStream("mybatis-timer-config.xml");) {
				sessionFactorys.put(server, new SqlSessionFactoryBuilder().build(inputStream, server.name()));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static SqlSessionFactory getSqlSessionFactory(ServerEnum database) {
		return sessionFactorys.get(database);
	}

}
