package cc.pp.sina.domain.bozhus;

public class UserTag {

	private String id;
	private String value;
	private long weight;

	public UserTag(String id, String value, long weight) {
		this.id = id;
		this.value = value;
		this.weight = weight;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public long getWeight() {
		return weight;
	}

	public void setWeight(long weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return "UserTag [id=" + id + ", value=" + value + ", weight=" + weight + "]";
	}

}
