package cc.pp.sina.domain.bozhus;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Created by chenwei on 14-1-15.
 */
public class Bozhu {

	private Long username;
	private String ptype;
	@JsonIgnore
	private Integer defaultPriceSource;

	public Integer getDefaultPriceSource() {
		return defaultPriceSource;
	}

	public void setDefaultPriceSource(Integer defaultPriceSource) {
		this.defaultPriceSource = defaultPriceSource;
	}

	public Bozhu() {
	}

	public Bozhu(long username, String ptype) {
		this.username = username;
		this.ptype = ptype;
	}

	public String getPtype() {
		return ptype;
	}

	public Long getUsername() {
		return username;
	}

	public void setPtype(String ptype) {
		this.ptype = ptype;
	}

	public void setUsername(Long username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "Bozhu{" +
				"username=" + username +
				", ptype='" + ptype + '\'' +
				", defaultPriceSource=" + defaultPriceSource +
				'}';
	}
}
