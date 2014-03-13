package cc.pp.sina.dao.price;

import cc.pp.sina.domain.bozhus.price.Price;
import cc.pp.sina.domain.bozhus.price.PriceSource;
import cc.pp.sina.domain.bozhus.price.PriceType;

import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.List;

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

	@Select("select count(id) from bozhu_price_source")
	int getSourceCount();

	@Select("SELECT count(`id`) " +
			" FROM bozhu_price_source " +
			" where name like CONCAT('%', #{keyword}, '%') or qq = #{keyword} or telephone = #{keyword}")
	int getSourceCountByKeyword(String keyword);

	@Select("SELECT `id`,`name`,`qq`,`telephone` " +
			" FROM bozhu_price_source " +
			" limit #{0}, #{1}")
	List<PriceSource> getSources(int offset, int limit);

	@Select("SELECT `id`,`name`,`qq`,`telephone` " +
			" FROM bozhu_price_source " +
			" where name like '%${param1}%' or qq = #{0} or telephone = #{0}" +
			" limit #{1}, #{2}")
	List<PriceSource> getSourcesByKeyword(String keyword, int offset, int limit);

	@Select("select id, name from bozhu_price_type")
	List<PriceType> getTypes();

	@Update("update bozhu_price set price = #{price}, update_time = now() where username = #{username} and sourceid = #{sourceId} and typeid = #{typeId}")
	void updatePrice(Price price);

	@Update("update bozhu_price_source set name = #{name}, qq = #{qq}, telephone = #{telephone} where id = #{id}")
	void updateSource(PriceSource source);

	@Select("SELECT `id`,`name`,`qq`,`telephone` " +
			" FROM bozhu_price_source " +
			" where qq = #{0}")
	PriceSource getSourcesByQq(String qq);

	@Select("SELECT `id`,`name`,`qq`,`telephone` " +
			" FROM bozhu_price_source " +
			" where telephone = #{0}")
	PriceSource getSourcesByTelephone(String telephone);

}
