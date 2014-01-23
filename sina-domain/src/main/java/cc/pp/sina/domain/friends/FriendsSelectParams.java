package cc.pp.sina.domain.friends;

public class FriendsSelectParams {

	private String tablename;
	private long username;

	public FriendsSelectParams() {
		//
	}

	public FriendsSelectParams(String tablename, long username) {
		this.tablename = tablename;
		this.username = username;
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

}
