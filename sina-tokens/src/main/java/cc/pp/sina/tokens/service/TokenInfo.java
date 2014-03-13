package cc.pp.sina.tokens.service;

public class TokenInfo {

	private long bind_username;
	private String bind_access_token;
	private long expires;

	public TokenInfo(long bind_username, String bind_access_token, long expires) {
		this.bind_username = bind_username;
		this.bind_access_token = bind_access_token;
		this.expires = expires;
	}

	public long getBind_username() {
		return bind_username;
	}

	public void setBind_username(long bind_username) {
		this.bind_username = bind_username;
	}

	public String getBind_access_token() {
		return bind_access_token;
	}

	public void setBind_access_token(String bind_access_token) {
		this.bind_access_token = bind_access_token;
	}

	public long getExpires() {
		return expires;
	}

	public void setExpires(long expires) {
		this.expires = expires;
	}

}
