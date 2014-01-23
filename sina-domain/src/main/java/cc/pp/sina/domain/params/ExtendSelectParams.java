package cc.pp.sina.domain.params;

public class ExtendSelectParams {

	private String tablename;
	private long username;

	public ExtendSelectParams(String tablename, long username) {
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
