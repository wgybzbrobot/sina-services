package cc.pp.sina.bozhus.common;

import org.junit.Ignore;

public class OutStreamUtilsTest {

	private final String[] STR_ARR_A = { "aa", "bb", "cc" };
	private final String[] STR_ARR_B = { "dd", "ee", "ff" };
	private final int[] INT_ARR_A = { 1, 2, 3 };
	private final int[] INT_ARR_B = { 4, 5, 6 };
	private final long[] LONG_ARR_A = { 1L, 2L, 3L };
	private final long[] LONG_ARR_B = { 4L, 5L, 6L };

	@Ignore
	public void testOutStrArr() {
		System.err.println("Single StrArr:");
		OutStreamUtils.outStrArr(STR_ARR_A);
		System.out.println();
		OutStreamUtils.outStrArr(STR_ARR_B);
	}

	@Ignore
	public void testOutStrArrs() {
		System.err.println("Multi StrArrs:");
		OutStreamUtils.outStrArr(STR_ARR_A, STR_ARR_B);
	}

	@Ignore
	public void testOutIntArr() {
		System.err.println("Single IntArr:");
		OutStreamUtils.outIntArr(INT_ARR_A);
		System.out.println();
		OutStreamUtils.outIntArr(INT_ARR_B);
	}

	@Ignore
	public void testOutIntArrs() {
		System.err.println("Multi IntArrs:");
		OutStreamUtils.outIntArr(INT_ARR_A, INT_ARR_B);
	}

	@Ignore
	public void testOutLongArr() {
		System.err.println("Single LongArr:");
		OutStreamUtils.outLongArr(LONG_ARR_A);
		System.out.println();
		OutStreamUtils.outLongArr(LONG_ARR_B);
	}

	@Ignore
	public void testOutLongArrs() {
		System.err.println("Multi LongArrs:");
		OutStreamUtils.outLongArr(LONG_ARR_A, LONG_ARR_B);
	}

}
