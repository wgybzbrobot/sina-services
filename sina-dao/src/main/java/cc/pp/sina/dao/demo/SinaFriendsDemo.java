package cc.pp.sina.dao.demo;

import cc.pp.sina.dao.friends.SinaFriends;
import cc.pp.sina.domain.friends.FriendsInfo;
import cc.pp.sina.utils.json.JsonUtils;

public class SinaFriendsDemo {

	public static void main(String[] args) {

		String tablename = "sina_user_friends_0";
		String friendsUids = "2612799563,2131445654,2521766162,1883507447,2816792060,1680990025,2473082931,"
				+ "1731285993,2087165373,1831424603,1686966310,2154212385,2465183882,1895687474,2216308940,1286891293,2435885602,"
				+ "2606908714,2014612763,3265434462,1642351362,2840829464,3097562687,2420646424,2932032447,3101455353,3186317333,"
				+ "3002129675,2049514822,2160549737,3102940971,1950847310,2764123772,3032956082,2845846713,3015326051,2119118092,"
				+ "2729273903,3134141095,2001629237,1888717083,2569070582,1192329374";
		long username = 2304305670L;

		SinaFriends.insertSinaFriendsInfo(tablename, username, friendsUids, 43, 1390102446L);

		FriendsInfo friends = SinaFriends.getSinaFriendsInfo(tablename, username);
		System.out.println(JsonUtils.toJson(friends));
		friends = SinaFriends.getSinaFriendsInfo(tablename, 123456789L);
		System.out.println(friends == null);

		SinaFriends.updateSinaFriendsInfo(tablename, username, "123456789,987654321", 200, 12233333L);

		friends = SinaFriends.getSinaFriendsInfo(tablename, username);
		System.out.println(JsonUtils.toJson(friends));

		SinaFriends.deleteSinaFriendsInfo(tablename, username);

	}

}
