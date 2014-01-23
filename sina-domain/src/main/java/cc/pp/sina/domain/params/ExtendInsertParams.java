package cc.pp.sina.domain.params;

public class ExtendInsertParams {

	private String tablename;
	private long username;
	private String tags;
	private boolean isppuser;

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

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public boolean isIsppuser() {
		return isppuser;
	}

	public void setIsppuser(boolean isppuser) {
		this.isppuser = isppuser;
	}

}
