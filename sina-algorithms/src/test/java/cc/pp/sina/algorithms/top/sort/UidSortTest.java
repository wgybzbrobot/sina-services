package cc.pp.sina.algorithms.top.sort;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import cc.pp.sina.algorithms.top.sort.UidSort;

public class UidSortTest {

	private static final String[] UIDS = { "3242227264", "2642878912", "2919434560", "1050530784", "2497321824",
			"2634461024", "1301737824", "1945937664", "1836516864", "2389036160", "3242227264", "3242227264",
			"2642878912", "3242227264", "2642878912", "2919434560", "3242227264", "2642878912", "2919434560",
			"1050530784", "3242227264", "2642878912", "2919434560", "1050530784", "2497321824", "3242227264",
			"2642878912", "2919434560", "1050530784", "2497321824", "2634461024", "3242227264", "2642878912",
			"2919434560", "1050530784", "2497321824", "2634461024", "1301737824", "3242227264", "2642878912",
			"2919434560", "1050530784", "2497321824", "2634461024", "1301737824", "1945937664", "3242227264",
			"2642878912", "2919434560", "1050530784", "2497321824", "2634461024", "1301737824", "1945937664",
			"1836516864", "3242227264", "2642878912", "2919434560", "1050530784", "2497321824", "2634461024",
			"1301737824", "1945937664", "1836516864", "2389036160" };

	private static final String[] rightSort = { "3242227264=11", "2642878912=10", "2919434560=9", "1050530784=8",
			"2497321824=7", "2634461024=6", "1301737824=5", "1945937664=4", "1836516864=3", "2389036160=2" };

	private static final String[] rrightSort = { "3242227264=10", "2642878912=9", "2919434560=8", "1050530784=7",
			"2497321824=6", "2634461024=5", "1301737824=4", "1945937664=3", "1836516864=2", "2389036160=1" };

	@Test
	public void testUidsSort() {

		// 设置待排序uid总数、top数量
		UidSort uidSort = new UidSort(UIDS.length, 10);
		assertEquals(UIDS.length, uidSort.getQueue());
		assertEquals(10, uidSort.getTop());
		// 插入数据
		for (String uid : UIDS) {
			uidSort.popUid(uid);
		}
		// 排序
		List<String> topUids = uidSort.sort();
		// 验证结果
		for (int i = 0; i < uidSort.getTop(); i++) {
			assertEquals(rightSort[i], topUids.get(i));
			//			System.out.println(topUids.get(i));
		}

		// 重置uid总数、top数量
		uidSort.reset(UIDS.length - 10, 5);
		assertEquals(UIDS.length - 10, uidSort.getQueue());
		assertEquals(5, uidSort.getTop());
		for (int i = 0; i < UIDS.length - 10; i++) {
			uidSort.popUid(UIDS[i]);
		}
		topUids = uidSort.sort();
		for (int i = 0; i < uidSort.getTop(); i++) {
			assertEquals(rrightSort[i], topUids.get(i));
			//			System.out.println(topUids.get(i));
		}

	}

}
