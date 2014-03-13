package cc.pp.content.picture;

public class ActiveUserInfo {

	private String username;
	private String nickname;
	private String url;
	private boolean isAddV;
	private int verifiedType;

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isAddV() {
		return isAddV;
	}

	public void setAddV(boolean isAddV) {
		this.isAddV = isAddV;
	}

	public int getVerifiedType() {
		return verifiedType;
	}

	public void setVerifiedType(int verifiedType) {
		this.verifiedType = verifiedType;
	}

}
