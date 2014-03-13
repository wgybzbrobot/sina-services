package cc.pp.sina.domain.bozhus.price;

import org.codehaus.jackson.map.annotate.JsonFilter;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;

import java.util.ArrayList;
import java.util.List;

@JsonFilter("priceSourceFilter")
public class PriceSource {
	private Integer id;
	private String name;
	private String qq;
	private String telephone;
	private Integer isDefault;
	private List<Price> prices;

	public static final FilterProvider emptyFilters = new SimpleFilterProvider().addFilter("priceSourceFilter",
			SimpleBeanPropertyFilter.serializeAllExcept());

	public static final FilterProvider basicInfoFilters = new SimpleFilterProvider().addFilter("priceSourceFilter",
			SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "qq", "telephone"));

	public PriceSource() {
	}

	public PriceSource(String name) {
		this.name = name;
	}

	public PriceSource(Integer id, String name, String qq, String telephone) {
		this.id = id;
		this.name = name;
		this.qq = qq;
		this.telephone = telephone;
	}

	public PriceSource(Integer id, String name, String qq, String telephone, Integer isDefault) {
		this.id = id;
		this.name = name;
		this.qq = qq;
		this.telephone = telephone;
		this.isDefault = isDefault;
	}

	public PriceSource(String name, String qq, String telephone) {
		this.name = name;
		this.qq = qq;
		this.telephone = telephone;
	}

	public PriceSource addPrice(Price price) {
		if (prices == null) {
			prices = new ArrayList<>();
		}
		prices.add(price);
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PriceSource that = (PriceSource) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (isDefault != null ? !isDefault.equals(that.isDefault) : that.isDefault != null) return false;
		if (name != null ? !name.equals(that.name) : that.name != null) return false;
		if (prices != null ? !prices.equals(that.prices) : that.prices != null) return false;
		if (qq != null ? !qq.equals(that.qq) : that.qq != null) return false;
		if (telephone != null ? !telephone.equals(that.telephone) : that.telephone != null) return false;

		return true;
	}

	public Integer getId() {
		return id;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public String getName() {
		return name;
	}

	public List<Price> getPrices() {
		return prices;
	}

	public String getQq() {
		return qq;
	}

	public String getTelephone() {
		return telephone;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (qq != null ? qq.hashCode() : 0);
		result = 31 * result + (telephone != null ? telephone.hashCode() : 0);
		result = 31 * result + (isDefault != null ? isDefault.hashCode() : 0);
		result = 31 * result + (prices != null ? prices.hashCode() : 0);
		return result;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrices(List<Price> prices) {
		this.prices = prices;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@Override
	public String toString() {
		return "PriceSource{" +
				"id=" + id +
				", name='" + name + '\'' +
				", qq='" + qq + '\'' +
				", telephone='" + telephone + '\'' +
				", isDefault=" + isDefault +
				", prices=" + prices +
				'}';
	}
}
