package cc.pp.sina.domain.bozhus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import cc.pp.sina.domain.bozhus.UserAllWeibosDomain;

public class UserAllWeibosDaoTest {

	@Test
	public void testWithData() {
		// 八个参数
		List<String> lastweiboids = new ArrayList<String>();
		lastweiboids.add("1234567890");
		lastweiboids.add("9876543210");
		HashMap<String, Integer> wbsource = new HashMap<>();
		wbsource.put("pc", 1);
		wbsource.put("ipad", 2);
		UserAllWeibosDomain uawd = new UserAllWeibosDomain.Builder().setAveorirepcom(0.0f).setAverepcom(1.0f)
				.setAverepcombymonth(2.0f).setAverepcombyweek(3.0f).setLastweiboids(lastweiboids).setOriratio(4.0f)
				.setWbsource(wbsource).setWeibosum(5L).build();
		assertEquals(0.0f, uawd.getAveorirepcom(), 0.0f);
		assertEquals(1.0f, uawd.getAverepcom(), 0.0f);
		assertEquals(2.0f, uawd.getAverepcombymonth(), 0.0f);
		assertEquals(3.0f, uawd.getAverepcombyweek(), 0.0f);
		assertEquals("1234567890", uawd.getLastweiboids().get(0));
		assertEquals("9876543210", uawd.getLastweiboids().get(1));
		assertEquals(4.0f, uawd.getOriratio(), 0.0f);
		assertEquals(wbsource.get("pc"), uawd.getWbsource().get("pc"));
		assertEquals(wbsource.get("ipad"), uawd.getWbsource().get("ipad"));
		assertEquals(5L, uawd.getWeibosum());
	}

	@Test
	public void testWithoutData() {
		// 八个参数
		UserAllWeibosDomain uawd = new UserAllWeibosDomain.Builder().build();
		assertEquals(0.0f, uawd.getAveorirepcom(), 0.0f);
		assertEquals(0.0f, uawd.getAverepcom(), 0.0f);
		assertEquals(0.0f, uawd.getAverepcombymonth(), 0.0f);
		assertEquals(0.0f, uawd.getAverepcombyweek(), 0.0f);
		assertNull(uawd.getLastweiboids());
		assertEquals(0.0f, uawd.getOriratio(), 0.0f);
		assertNull(uawd.getWbsource());
		assertEquals(0L, uawd.getWeibosum());
	}

}
