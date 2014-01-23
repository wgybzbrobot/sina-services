package cc.pp.sina.algorithms.utils;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class SortUtilsTest {

	private static HashMap<String, Integer> hs1;
	private static HashMap<Integer, Integer> hs2;

	@Before
	public void prepared() {
		hs1 = new HashMap<>();
		hs1.put("bbb", 22);
		hs1.put("ccc", 33);
		hs1.put("aaa", 11);
		hs2 = new HashMap<>();
		hs2.put(1, 22);
		hs2.put(2, 33);
		hs2.put(3, 11);
	}

	@Test
	public void testSortedToString() {
		assertEquals("ccc=33,bbb=22,aaa=11", SortUtils.sortedToString(hs1));
	}

	@Test
	public void testSortedToStrings() {
		String[] result = SortUtils.sortedToStrings(hs1);
		assertEquals("ccc=33", result[0]);
		assertEquals("bbb=22", result[1]);
		assertEquals("aaa=11", result[2]);
	}

	@Test
	public void testSortedToHashMap() {
		List<String> result = SortUtils.sortedToHashMap(hs1);
		assertEquals("ccc=33", result.get(0));
		assertEquals("bbb=22", result.get(1));
		assertEquals("aaa=11", result.get(2));
	}

	@Test
	public void testSortedToDoubleMap() {
		assertEquals("[{value=33, key=2}, {value=22, key=1}, {value=11, key=3}]",
				SortUtils.sortedToDoubleMap(hs2, "key", "value", 3).toString());
	}

}
