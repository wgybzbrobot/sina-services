package cc.pp.sina.bozhus.constant;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SourceTypeTest {

	@Test
	public void testGetCategory() {

		assertEquals(0, SourceType.getCategory("新浪微博"));
		assertEquals(1, SourceType.getCategory("FaWave定时8"));
		assertEquals(2, SourceType.getCategory("智能手机三星"));
		assertEquals(3, SourceType.getCategory("iPhone红围脖"));
		assertEquals(4, SourceType.getCategory("平板OS"));
	}

}
