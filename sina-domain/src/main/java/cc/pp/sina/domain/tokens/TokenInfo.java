package cc.pp.sina.domain.tokens;

public class TokenInfo {

	private String uid;
	private String appkey;
	private String scope;
	private long create_at;
	private long expire_in;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public long getCreate_at() {
		return create_at;
	}

	public void setCreate_at(long create_at) {
		this.create_at = create_at;
	}

	public long getExpire_in() {
		return expire_in;
	}

	public void setExpire_in(long expire_in) {
		this.expire_in = expire_in;
	}

}
