package cc.pp.sina.domain.sql;

public class CompanySelectParams {

	private String tablename;
	private long username;

	public CompanySelectParams(String tablename, long username) {
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

	@Override
	public String toString() {
		return "CompanySelectParams: [tablename=" + tablename + ",username=" + username + "]";
	}

}
