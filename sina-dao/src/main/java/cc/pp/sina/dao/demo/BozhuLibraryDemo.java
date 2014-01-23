package cc.pp.sina.dao.demo;

import java.util.List;

import cc.pp.sina.dao.bozhus.BozhuLibrary;

public class BozhuLibraryDemo {

	public static void main(String[] args) {

		List<Long> uids = BozhuLibrary.getBozhuUids();
		System.out.println(uids.size());

	}

}
