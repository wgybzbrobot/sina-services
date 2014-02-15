package cc.pp.sina.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import cc.pp.sina.domain.bozhus.BozhuPrice;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cc.pp.sina.dao.price.BozhuDbConnection;

/**
 * Created by chenwei on 14-1-15.
 */
public class BozhuPriceMapperTest {

	private BozhuMapper mapper;
	private SqlSession session;

	@Before
	public void setUp() throws Exception {
		BozhuDbConnection.connectDb(DbConfig.url, DbConfig.username, DbConfig.password);
		SqlSessionFactory sessionFactory = BozhuDbConnection.getSessionFactory();
		session = sessionFactory.openSession();
		mapper = session.getMapper(BozhuMapper.class);
	}

	@After
	public void shtudown() {
		session.rollback();
	}

	@Ignore
	@Test
	public void testGet() throws Exception {
		assertNull(mapper.get(1));
		mapper.add(new BozhuPrice(1, "sina"));
		assertEquals("BozhuPrice{username=1, ptype='sina', defaultPriceSource=null}", mapper.get(1).toString());

		assertEquals(1, mapper.updateDefaultPriceSource(1, 2));
		assertEquals("BozhuPrice{username=1, ptype='sina', defaultPriceSource=2}", mapper.get(1).toString());
	}

}
