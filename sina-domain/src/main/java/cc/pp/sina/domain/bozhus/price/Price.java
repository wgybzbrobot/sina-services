package cc.pp.sina.domain.bozhus.price;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.Date;

public class Price {
	@JsonIgnore
	private long username;
	@JsonIgnore
	private int sourceId;
	private int typeId;
	private float price;
	private Date updateTime;

	public Price() {
	}

	public Price(long username, int sourceId, int typeId, float price) {
		this.username = username;
		this.sourceId = sourceId;
		this.typeId = typeId;
		this.price = price;
	}

	public Price(int typeId, float price) {
		this.typeId = typeId;
		this.price = price;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Price price1 = (Price) o;

		if (Float.compare(price1.price, price) != 0) return false;
		if (sourceId != price1.sourceId) return false;
		if (typeId != price1.typeId) return false;
		if (username != price1.username) return false;

		return true;
	}

	@Override
	public String toString() {
		return "Price{" +
				"username=" + username +
				", sourceId=" + sourceId +
				", typeId=" + typeId +
				", price=" + price +
				", updateTime=" + updateTime +
				'}';
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public float getPrice() {
		return price;
	}

	public int getSourceId() {
		return sourceId;
	}

	public int getTypeId() {
		return typeId;
	}

	public long getUsername() {
		return username;
	}

	@Override
	public int hashCode() {
		int result = (int) (username ^ (username >>> 32));
		result = 31 * result + sourceId;
		result = 31 * result + typeId;
		result = 31 * result + (price != +0.0f ? Float.floatToIntBits(price) : 0);
		return result;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public void setUsername(long username) {
		this.username = username;
	}

}
