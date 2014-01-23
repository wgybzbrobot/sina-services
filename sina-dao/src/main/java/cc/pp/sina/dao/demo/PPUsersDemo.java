package cc.pp.sina.dao.demo;

import java.util.HashSet;

import cc.pp.sina.dao.pp.PPUsers;

public class PPUsersDemo {

	public static void main(String[] args) {

		HashSet<Long> uids = PPUsers.getPPUids();
		System.out.println(uids.size());

		uids = PPUsers.getPPUidsNow(System.currentTimeMillis() / 1000 + 86400 * 5);
		System.out.println(uids.size());
	}

}
