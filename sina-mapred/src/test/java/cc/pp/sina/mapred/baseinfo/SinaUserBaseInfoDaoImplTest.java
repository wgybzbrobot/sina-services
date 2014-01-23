package cc.pp.sina.mapred.baseinfo;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cc.pp.sina.mapred.baseinfo.SinaUserBaseInfoDao;
import cc.pp.sina.mapred.baseinfo.SinaUserBaseInfoDaoImpl;

public class SinaUserBaseInfoDaoImplTest {

	@SuppressWarnings("unused")
	@Test
	public void testSinaUserBaseInfoDaoImpl() {

		List<String> tokens = new ArrayList<String>();
		tokens.add("2.00eEQZzBdcZIJCdb7f41b1ebOlNq3B");
		tokens.add("2.0000onHtdcZIJC5ded8d28dc0CtU8F");

		SinaUserBaseInfoDao sinaUserBaseinfoDao = new SinaUserBaseInfoDaoImpl(tokens);
		//		User userInfo = sinaUserBaseinfoDao.getUserBaseInfo("1768044590");
		//		assertEquals("1768044590", userInfo.getId());
	}

}
