package cc.pp.sina.dao;

import cc.pp.sina.domain.bozhus.BozhuPrice;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by chenwei on 14-1-15.
 */
public interface BozhuMapper {

	@Insert("insert into bozhu (username, ptype) values (#{username}, #{ptype})")
	void add(BozhuPrice bozhuPrice);

	@Select("SELECT username, ptype, default_price_source as defaultPriceSource from bozhu WHERE username = #{0}")
	BozhuPrice get(long username);

	@Update("update bozhu set default_price_source = #{1} where username = #{0}")
	int updateDefaultPriceSource(long username, Integer sourceId);

}
