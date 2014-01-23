package cc.pp.sina.web.bozhu;

import java.util.Properties;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import cc.pp.sina.dao.BozhuMapper;
import cc.pp.sina.dao.price.BozhuDbConnection;
import cc.pp.sina.domain.bozhus.Bozhu;
import cc.pp.sina.utils.config.Config;

/**
 * Created by chenwei on 14-1-15.
 */
public class BozhuServiceDb implements BozhuService {

	private final SqlSessionFactory sessionFactory;

	private final BozhuService innerService;

	public BozhuServiceDb() {
		Properties props = Config.getProps("data_db.properties");
		BozhuDbConnection.connectDb(props.getProperty("bozhu.price.db.url"), props.getProperty("bozhu.price.db.username"), props.getProperty("bozhu.price.db.password"));
		sessionFactory = BozhuDbConnection.getSessionFactory();
		innerService = new BozhuServiceRemote();
	}

	@Override
	public Bozhu get(long username) {
		try (SqlSession session = sessionFactory.openSession()) {
			BozhuMapper mapper = session.getMapper(BozhuMapper.class);
			Bozhu result = mapper.get(username);
			if (result == null) {
				result = innerService.get(username);
				if (result != null) {
					mapper.add(result);
				}
			}
			session.commit();
			return result;
		}
	}

}
