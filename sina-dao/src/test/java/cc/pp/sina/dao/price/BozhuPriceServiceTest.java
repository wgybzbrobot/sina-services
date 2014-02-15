package cc.pp.sina.dao.price;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by chenwei on 14-1-15.
 */
public class BozhuPriceServiceTest {

	private final BozhuService service = new BozhuServiceDb();

	@Ignore
	@Test
	public void testGet() throws Exception {
		assertNull(service.get(1));
		assertEquals("BozhuPrice{username=1772605247, ptype='sina', defaultPriceSource=1}", service.get(1772605247)
				.toString());
	}
}
