package cc.pp.sina.domain.bozhus;

/**
 * 用户扩展数据
 * @author wgybzb
 *
 */
public class UserExtendInfo {

	private long username;
	private String tags;
	private boolean isppuser;
	private boolean isreal;

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

	public boolean isIsreal() {
		return isreal;
	}

	public void setIsreal(boolean isreal) {
		this.isreal = isreal;
	}

}
