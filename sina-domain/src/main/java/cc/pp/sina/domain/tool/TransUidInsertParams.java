package cc.pp.sina.domain.tool;

public class TransUidInsertParams {

	private String tablename;
	private String identify;
	private String url;
	private String username;
	private String nickname;

	public TransUidInsertParams(String tablename, String identify, String url, String username, String nickname) {
		this.tablename = tablename;
		this.identify = identify;
		this.url = url;
		this.username = username;
		this.nickname = nickname;
	}

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

}
