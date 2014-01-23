package cc.pp.sina.domain.bozhus;

import java.util.List;

public class UserTagWapper {

	private List<UserTag> tags;
	private long username;

	public UserTagWapper(List<UserTag> tags, long username) {
		super();
		this.tags = tags;
		this.username = username;
	}

	public List<UserTag> getTags() {
		return tags;
	}

	public void setTags(List<UserTag> tags) {
		this.tags = tags;
	}

	public long getUsername() {
		return username;
	}

	public void setUsername(long username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "UserTagWapper [username=" + username + ", tags=" + tags + "]";
	}

}
