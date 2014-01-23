package cc.pp.sina.utils.time;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DailyUtilsTest {

	@Test
	public void testGetSomeDayDate() {
		assertTrue(DailyUtils.getSomeDayDate(0).length() == 8);
	}

	@Test
	public void testGetHour() {
		assertTrue(DailyUtils.getHour(System.currentTimeMillis() / 1000) >= 0);
		assertTrue(DailyUtils.getHour(System.currentTimeMillis() / 1000) <= 23);
	}

}
