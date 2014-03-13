package cc.pp.sina.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cc.pp.sina.dao.price.BozhuDbConnection;
import cc.pp.sina.dao.price.PriceMapper;
import cc.pp.sina.domain.bozhus.price.Price;
import cc.pp.sina.domain.bozhus.price.PriceSource;


/**
 * User: chenwei@pp.cc
 * Date: 14-1-11
 * Time: 下午7:30.
 */
public class PriceMapperTest {

    SqlSession session;
	PriceMapper mapper;

	@Before
	public void setUp() throws Exception {
		BozhuDbConnection.connectDb(DbConfig.url, DbConfig.username, DbConfig.password);
		SqlSessionFactory sessionFactory = BozhuDbConnection.getSessionFactory();
		session = sessionFactory.openSession();
		mapper = session.getMapper(PriceMapper.class);
	}

    @After
    public void shutdown() {
        session.rollback();
    }

	@Test
	public void testGetSources() throws Exception {
		List<PriceSource> sources = mapper.getSources(1, 2);
		assertEquals("[PriceSource{id=2, name='分析价', qq='123456', telephone='', isDefault=null, prices=null}" +
				", PriceSource{id=3, name='微博易价', qq='', telephone='', isDefault=null, prices=null}]", sources.toString());
	}

	@Test
	public void testGetSources2() throws Exception {
		List<PriceSource> sources = mapper.getSourcesByKeyword("微", 0, 20);
		assertEquals("[PriceSource{id=3, name='微博易价', qq='', telephone='', isDefault=null, prices=null}" +
				", PriceSource{id=4, name='微任务价', qq='', telephone='', isDefault=null, prices=null}]", sources.toString());

		assertEquals("[PriceSource{id=2, name='分析价', qq='123456', telephone='', isDefault=null, prices=null}]", mapper.getSourcesByKeyword("123456", 0, 20).toString());
		assertEquals("[PriceSource{id=1, name='博主报价', qq='', telephone='18888888888', isDefault=null, prices=null}]", mapper.getSourcesByKeyword("18888888888", 0, 20).toString());
	}

	@Test
	public void testGetSourceCount() throws Exception {
		assertEquals(4, mapper.getSourceCount());
		assertEquals(2, mapper.getSourceCountByKeyword("微"));
		assertEquals(1, mapper.getSourceCountByKeyword("123456"));
		assertEquals(1, mapper.getSourceCountByKeyword("18888888888"));
	}

	@Test
	public void testGetPriceByBzId() throws Exception {
		ArrayList<Price> prices = mapper.getPricesByUsername(1772605247);
		assertEquals(6, prices.size());
		assertEquals("Price{username=1772605247, sourceId=1, typeId=1, price=5.0, updateTime=Sat Oct 12 01:02:03 CST 2013}", prices.get(0).toString());
	}

	@Test
	public void testGetDefaultSourceByUsername() {
		assertEquals("PriceSource{id=1, name='博主报价', qq='', telephone='18888888888', isDefault=1, prices=null}", mapper.getDefaultSourceByUsername(1772605247).toString());
	}

	@Test
	public void testPrice_CRUD() {
		mapper.addPrice(new Price(1699668020, 1, 3, 6));
		assertTrue(mapper.getPrice(1699668020, 1, 3).toString().startsWith("Price{username=1699668020, sourceId=1, typeId=3, price=6.0, updateTime="));
		// 因为price表是三个id联合主键，测试重复replace
		mapper.updatePrice(new Price(1699668020, 1, 3, 8));
		assertTrue(mapper.getPrice(1699668020, 1, 3).toString().startsWith("Price{username=1699668020, sourceId=1, typeId=3, price=8.0, updateTime="));

		mapper.deletePriceByUsername_sourceId_typeId(1699668020, 1, 3);
		assertNull(mapper.getPrice(1699668020, 1, 3));
	}

	@Test
	public void testSource_CRUD() {
		PriceSource source = new PriceSource("测试渠道", "测试QQ", "测试手机号");
		mapper.addSource(source);
		assertTrue(source.getId() != 0);
		assertEquals(new PriceSource(source.getId(), "测试渠道", "测试QQ", "测试手机号", 0), mapper.getSource(1772605247, source.getId()));

		source.setName("测试渠道名称修改");
		mapper.updateSource(source);
		assertEquals(new PriceSource(source.getId(), "测试渠道名称修改", "测试QQ", "测试手机号", 0), mapper.getSource(1772605247, source.getId()));

		mapper.deleteSource(source.getId());
		assertNull(mapper.getSource(1772605247, source.getId()));

	}

	@Test
	public void testGetPrice() {
		List<Price> prices = mapper.getPrices(1772605247, 2);
		assertEquals("[Price{username=1772605247, sourceId=2, typeId=1, price=5.0, updateTime=Sat Oct 12 01:02:03 CST 2013}, Price{username=1772605247, sourceId=2, typeId=3, price=5.0, updateTime=Sat Oct 12 01:02:03 CST 2013}]", prices.toString());
	}

}
