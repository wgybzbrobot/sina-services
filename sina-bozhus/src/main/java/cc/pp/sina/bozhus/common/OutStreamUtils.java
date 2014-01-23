package cc.pp.sina.bozhus.common;

public class OutStreamUtils {

	public static void outStrArr(String... strArr) {
		for (String str : strArr) {
			System.out.print(str + ",");
		}
		System.out.println();
	}

	public static void outStrArr(String[]... strArrs) {
		for (String[] strs : strArrs) {
			outStrArr(strs);
		}
	}

	public static void outIntArr(int... iNT_ARR_A) {
		for (Integer integer : iNT_ARR_A) {
			System.out.print(integer + ",");
		}
		System.out.println();
	}

	public static void outIntArr(int[]... intArrs) {
		for (int[] integers : intArrs) {
			outIntArr(integers);
		}
	}

	public static void outLongArr(long... longArr) {
		for (long bigint : longArr) {
			System.out.print(bigint + ",");
		}
		System.out.println();
	}

	public static void outLongArr(long[]... longArrs) {
		for (long[] bigints : longArrs) {
			outLongArr(bigints);
		}
	}

	public static int sumIntArr(int... intArr) {
		int sum = 0;
		for (int arr : intArr) {
			sum += arr;
		}
		return sum;
	}

}
