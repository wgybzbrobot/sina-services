package cc.pp.sina.domain.bozhus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import cc.pp.sina.domain.bozhus.UserSingleWeiboDomain;

public class UserSingleWeiboDaoTest {

	@Test
	public void testWithData() {

		UserSingleWeiboDomain uswd = new UserSingleWeiboDomain.Builder(new int[] { 1, 2 }, 1234567890L).build();
		assertEquals(1, uswd.getReposterquality()[0]);
		assertEquals(2, uswd.getReposterquality()[1]);
		assertEquals(1234567890L, uswd.getExposionsum());
	}

	@Test
	public void testWithoutData() {

		UserSingleWeiboDomain uswd = new UserSingleWeiboDomain.Builder(null, 0).build();
		assertNull(uswd.getReposterquality());
		assertEquals(0, uswd.getExposionsum());
	}

}
