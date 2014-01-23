package cc.pp.sina.domain.params;

public class BaseInfoParams {

	private String tablename;
	private long uid;

	public BaseInfoParams(String tablename, long uid) {
		this.tablename = tablename;
		this.uid = uid;
	}

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

}
