package cc.pp.sina.domain.bozhus;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cc.pp.sina.domain.bozhus.UserServalWeibosDomain;

public class UserServalWeibosDaoTest {

	@Test
	public void testWithData() {

		UserServalWeibosDomain uswd = new UserServalWeibosDomain.Builder(0.6666f, 1234567890L).build();
		assertEquals(0.6666f, uswd.getAvereposterquality(), 0.0f);
		assertEquals(1234567890L, uswd.getAveexposionsum());
	}

	@Test
	public void testWithoutData() {

		UserServalWeibosDomain uswd = new UserServalWeibosDomain.Builder(0, 0).build();
		assertEquals(0, uswd.getAvereposterquality(), 0);
		assertEquals(0, uswd.getAveexposionsum());
	}

}
