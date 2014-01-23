package cc.pp.sina.bozhus.tags;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.bozhus.extend.UserExtendInfoDao;
import cc.pp.sina.bozhus.info.SinaUserInfoDao;
import cc.pp.sina.domain.bozhus.UserTagWapper;
import cc.pp.sina.domain.params.ExtendInsertParams;
import cc.pp.sina.utils.json.JsonUtils;

public class BatchTagsRun implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(BatchTagsRun.class);

	private static SqlSessionFactory sqlSessionFactory;

	static {
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("mybatis-config.xml"));
	}

	private final SinaUserInfoDao sinaUserInfoDao;
	private final String uids;

	public BatchTagsRun(SinaUserInfoDao sinaUserInfoDao, String uids) {
		this.sinaUserInfoDao = sinaUserInfoDao;
		this.uids = uids;
	}

	private static AtomicInteger count = new AtomicInteger(0);

	@Override
	public void run() {

		logger.info("Tackle at: " + count.addAndGet(1));

		List<UserTagWapper> userTagWappers = sinaUserInfoDao.getSinaUsersBatchTags(uids, 20);

		if (userTagWappers != null) {
			try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
				UserExtendInfoDao userExtendInfoDao = sqlSession.getMapper(UserExtendInfoDao.class);
				for (UserTagWapper temp : userTagWappers) {
					ExtendInsertParams extendInsertParams = new ExtendInsertParams();
					extendInsertParams.setTablename("sina_user_extend_info_" + temp.getUsername() % 10);
					extendInsertParams.setUsername(temp.getUsername());
					extendInsertParams.setTags(JsonUtils.toJsonWithoutPretty(temp.getTags()));
					extendInsertParams.setIsppuser(true);
					userExtendInfoDao.insertExtendInfo(extendInsertParams);
				}
			}
		}

	}

}
