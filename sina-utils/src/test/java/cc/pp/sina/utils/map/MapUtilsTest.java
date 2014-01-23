package cc.pp.sina.utils.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class MapUtilsTest {

	private static HashMap<Integer, Integer> test1;
	private static HashMap<Long, Integer> map1;
	private static HashMap<Long, Integer> map2;
	private static HashMap<Long, Integer> map3;
	private static HashMap<Integer, String> map4;

	@Before
	public void prepared() {
		test1 = new HashMap<>();
		test1.put(1, 10);
		test1.put(2, 40);
		test1.put(3, 50);
		map1 = new HashMap<>();
		map1.put(11L, 12);
		map1.put(22L, 22);
		map2 = new HashMap<>();
		map2.put(111L, 122);
		map2.put(22L, 2);
		map3 = new HashMap<>();
		map3.put(11L, 12);
		map3.put(22L, 22);
		map3.put(33L, 342);
		map3.put(44L, 96);
		map4 = new HashMap<>();
		map4.put(123, "aa");
		map4.put(231, "bb");
		map4.put(321, "cc");
	}

	@Test
	public void testTranToRatio() {
		assertEquals("{3=50.0%, 2=40.0%, 1=10.0%}", MapUtils.tranToRatio(test1).toString());
	}

	@Test
	public void testUnionMaps() {

		List<HashMap<Long, Integer>> table = new ArrayList<HashMap<Long, Integer>>();
		table.add(map1);
		table.add(map2);

		HashMap<Long, Integer> result = MapUtils.unionMaps(table);
		assertTrue(result.get(11L) == 12);
		assertTrue(result.get(22L) == 24);
		assertTrue(result.get(111L) == 122);
	}

	@Test
	public void testGetSum() {
		assertEquals(34, MapUtils.getSum(map1));
	}

	@Test
	public void testGetRealUses() {
		assertEquals(2, MapUtils.getRealUses(map3, 90)[0]);
		assertEquals(438, MapUtils.getRealUses(map3, 90)[1]);
	}

	@Test
	public void testGetRealRatio() {
		String[] result = MapUtils.getRealRatio(map3, 90);
		assertEquals("50.0%", result[0]);
		assertEquals("7.2%", result[1]);
	}

	@Test
	public void testGetMaxKey() {
		assertTrue(MapUtils.getMaxKey(map4) == 321);
	}

}
