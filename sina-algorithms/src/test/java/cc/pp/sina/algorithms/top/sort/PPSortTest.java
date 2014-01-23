package cc.pp.sina.algorithms.top.sort;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

public class PPSortTest {

	private HashMap<String, Integer> in_sts;
	private String[] out_sts;
	private static final int[] IN_INT_ARR = { 1, 4, 2, 9, 7, 3, 6 };
	private static final int OUT_MAX_INDEX = 3;

	@Before
	public void prepared() {

		in_sts = new HashMap<>();
		out_sts = new String[10];
		for (int i = 0; i < out_sts.length; i++) {
			in_sts.put("sts_" + i, i);
			out_sts[i] = "sts_" + (out_sts.length - i - 1) + "=" + (out_sts.length - i - 1);
		}

	}

	@Test
	public void testSortedToString_将HashMap排序成字符串数组() {

		String[] strs = PPSort.sortedToStrings(in_sts);
		for (int i = 0; i < strs.length; i++) {
			assertEquals(out_sts[i], strs[i]);
		}
	}

	@Test
	public void testGetMaxId_找出一个数组中的最大值对应的下标() {
		assertEquals(OUT_MAX_INDEX, PPSort.getMaxId(IN_INT_ARR));
	}

	@Test
	public void testSortedMapToList() {

		HashMap<String, Float> data = new HashMap<>();
		data.put("aaa", 2.2f);
		data.put("bbb", 4.4f);
		data.put("ccc", 3.3f);
		assertEquals("{aaa=2.2, ccc=3.3, bbb=4.4}", PPSort.sortedToHashMap(data, 3).toString());
		assertEquals("{ccc=3.3, bbb=4.4}", PPSort.sortedToHashMap(data, 2).toString());
		assertEquals("{bbb=4.4}", PPSort.sortedToHashMap(data, 1).toString());
	}

}
