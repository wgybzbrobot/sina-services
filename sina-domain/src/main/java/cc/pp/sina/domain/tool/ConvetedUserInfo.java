package cc.pp.sina.domain.tool;

public class ConvetedUserInfo {

	private String url;
	private String username;
	private String nickname;

	public ConvetedUserInfo() {
		//
	}

	public ConvetedUserInfo(String url, String username, String nickname) {
		this.url = url;
		this.username = username;
		this.nickname = nickname;
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
