package cc.pp.sina.dao.price;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cc.pp.sina.domain.bozhus.price.Price;
import cc.pp.sina.domain.bozhus.price.PriceSource;

public interface PriceMapper {

	@Insert("insert into `bozhu_price` (`username`, `sourceid`, `typeid`, `price`, update_time) VALUES (#{username}, #{sourceId}, #{typeId}, #{price}, now())")
	void addPrice(Price price);

	@Insert("insert into `bozhu_price_source` (`name`, `qq`, `telephone`) VALUES (#{name}, #{qq}, #{telephone})")
	@Options(useGeneratedKeys = true, keyProperty = "id", flushCache = true)
	void addSource(PriceSource source);

	@Delete("delete from bozhu_price where username = #{0} and sourceid = #{1}")
	void deletePriceByUsername_sourceId(long username, int sourceId);

	@Delete("delete from bozhu_price where username = #{0} and sourceid = #{1} and typeid = #{2}")
	void deletePriceByUsername_sourceId_typeId(long username, int sourceId, int typeId);

	@Delete("delete from bozhu_price_source where id = #{id}")
	int deleteSource(int id);

	@Select("SELECT s.`id`,s.`name`,s.`qq`,s.`telephone`, if(b.default_price_source is null,0,1) as isDefault " +
			" FROM bozhu_price_source s left join bozhu b on s.id = b.default_price_source" +
			" WHERE b.username = #{username}")
	PriceSource getDefaultSourceByUsername(long username);

	@Select("SELECT username, sourceid, typeid ,price, update_time as updateTime FROM bozhu_price WHERE username = #{0} and sourceid = #{1} and typeid = #{2}")
	Price getPrice(long username, int sourceId, int typeId);

	@Select("SELECT username, sourceid, typeid ,price, update_time as updateTime FROM bozhu_price WHERE username = #{0} and sourceid = #{1}")
	List<Price> getPrices(long username, int sourceId);

	@Select("SELECT username, sourceid, typeid ,price, update_time as updateTime FROM bozhu_price WHERE username = #{username}")
	ArrayList<Price> getPricesByUsername(long username);

	@Select("SELECT s.`id`,s.`name`,s.`qq`,s.`telephone`, if(b.default_price_source is null,0,1) as isDefault " +
			" FROM bozhu_price_source s left join bozhu b on s.id = b.default_price_source and b.username = #{0} " +
			" WHERE s.id = #{1}")
	PriceSource getSource(long username, int sourceId);

	@Update("update bozhu_price set price = #{price}, update_time = now() where username = #{username} and sourceid = #{sourceId} and typeid = #{typeId}")
	void updatePrice(Price price);

	@Update("update bozhu_price_source set name = #{name}, qq = #{qq}, telephone = #{telephone} where id = #{id}")
	void updateSource(PriceSource source);

}
