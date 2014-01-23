package cc.pp.sina.domain.params;

public class FansAnalysisInsertParams {

	private String tablename;
	private long username;
	private String result;
	private long lasttime;

	public FansAnalysisInsertParams(String tablename, long username, String result, long lasttime) {
		this.tablename = tablename;
		this.username = username;
		this.result = result;
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

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public long getLasttime() {
		return lasttime;
	}

	public void setLasttime(long lasttime) {
		this.lasttime = lasttime;
	}

}
