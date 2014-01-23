package cc.pp.sina.domain.friends;

public class FriendsInsertParams {

	private String tablename;
	private long username;
	private String friendsuids;
	private int friendscount;
	private long lasttime;

	public FriendsInsertParams() {
		//
	}

	public FriendsInsertParams(String tablename, long username, String friendsuids, int friendscount, long lasttime) {
		this.tablename = tablename;
		this.username = username;
		this.friendsuids = friendsuids;
		this.friendscount = friendscount;
		this.lasttime = lasttime;
	}

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public long getUsername() {
		return username;
	}

	public void setUsername(long username) {
		this.username = username;
	}

	public String getFriendsuids() {
		return friendsuids;
	}

	public void setFriendsuids(String friendsuids) {
		this.friendsuids = friendsuids;
	}

	public int getFriendscount() {
		return friendscount;
	}

	public void setFriendscount(int friendscount) {
		this.friendscount = friendscount;
	}

	public long getLasttime() {
		return lasttime;
	}

	public void setLasttime(long lasttime) {
		this.lasttime = lasttime;
	}

}
