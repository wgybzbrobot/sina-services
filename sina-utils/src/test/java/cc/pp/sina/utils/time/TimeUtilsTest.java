package cc.pp.sina.utils.time;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimeUtilsTest {

	@Test
	public void testGetTodayDaily() {
		assertTrue(TimeUtils.getTodayDaily().length() == 8);
	}

	@Test
	public void testGetDailyByTime() {
		long time = System.currentTimeMillis() / 1000;
		assertTrue(TimeUtils.getDailyByTime(time).length() == 8);
	}

	@Test
	public void testGetSomeDayTime() {
		long time = TimeUtils.getSomeDayTime(0);
		assertTrue(time >= System.currentTimeMillis() / 1000 - 86400);
		assertTrue(time <= System.currentTimeMillis() / 1000 + 86400);
	}

}
