package cc.pp.sina.dao.price;

import java.util.*;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import cc.pp.sina.dao.BozhuMapper;
import cc.pp.sina.dao.price.BozhuDbConnection;
import cc.pp.sina.dao.price.PriceMapper;
import cc.pp.sina.domain.bozhus.BozhuPrice;
import cc.pp.sina.domain.bozhus.price.Price;
import cc.pp.sina.domain.bozhus.price.PriceSource;
import cc.pp.sina.utils.config.Config;

/**
 * User: chenwei@pp.cc
 * Date: 14-1-11
 * Time: 下午7:01.
 */
public class PriceService {

	private final SqlSessionFactory sessionFactory;

	public PriceService() {
		Properties props = Config.getProps("data_db.properties");
		BozhuDbConnection.connectDb(props.getProperty("bozhu.price.db.url"), props.getProperty("bozhu.price.db.username"), props.getProperty("bozhu.price.db.password"));
		sessionFactory = BozhuDbConnection.getSessionFactory();
	}

	public Price addPrice(BozhuPrice bozhuPrice, int sourceId, Price price) {
		try (SqlSession session = sessionFactory.openSession()) {
			PriceMapper mapper = session.getMapper(PriceMapper.class);
			Price result = replacePrice(bozhuPrice, sourceId, price, mapper);
			session.commit();
			return result;
		}
	}

	public PriceSource addSource(BozhuPrice bozhuPrice, PriceSource source) {
		try (SqlSession session = sessionFactory.openSession()) {
			PriceMapper mapper = session.getMapper(PriceMapper.class);
			if (source.getId() == null) {
				mapper.addSource(source);
			} else {
				mapper.updateSource(source);
			}
			if (source.getPrices() != null && !source.getPrices().isEmpty()) {
				for (Price price : source.getPrices()) {
					replacePrice(bozhuPrice, source.getId(), price, mapper);
				}
			}
			PriceSource result = getSource(bozhuPrice, source.getId(), mapper);
			session.commit();
			return result;
		}
	}

	public void deletePrice(BozhuPrice bozhuPrice, int sourceId, int typeId) {
		try (SqlSession session = sessionFactory.openSession()) {
			PriceMapper mapper = session.getMapper(PriceMapper.class);
			mapper.deletePriceByUsername_sourceId_typeId(bozhuPrice.getUsername(), sourceId, typeId);
			session.commit();
		}
	}

	public void deleteSource(BozhuPrice bozhuPrice, int sourceId) {
		try (SqlSession session = sessionFactory.openSession()) {
			PriceMapper mapper = session.getMapper(PriceMapper.class);
			mapper.deletePriceByUsername_sourceId(bozhuPrice.getUsername(), sourceId);
			mapper.deleteSource(sourceId);
			session.commit();
		}
	}

	public PriceSource getDefaultPriceSource(BozhuPrice bozhuPrice) {
		try (SqlSession session = sessionFactory.openSession()) {
			PriceMapper mapper = session.getMapper(PriceMapper.class);
			PriceSource result = mapper.getDefaultSourceByUsername(bozhuPrice.getUsername());
			if (result != null) {
				result.setPrices(mapper.getPrices(bozhuPrice.getUsername(), result.getId()));
			}
			session.commit();
			return result;
		}
	}

	public Collection<PriceSource> getSourcePrices(BozhuPrice bozhuPrice) {
		try (SqlSession session = sessionFactory.openSession()) {
			PriceMapper mapper = session.getMapper(PriceMapper.class);
			Map<Integer, PriceSource> sources = new HashMap<>();
			for (Price price : mapper.getPricesByUsername(bozhuPrice.getUsername())) {
				PriceSource source = sources.get(price.getSourceId());
				if (source == null) {
					source = mapper.getSource(bozhuPrice.getUsername(), price.getSourceId());
					sources.put(source.getId(), source);
				}
				source.addPrice(price);
			}
			session.commit();
			return new ArrayList<PriceSource>(sources.values());
		}
	}

	public PriceSource getSource(BozhuPrice bozhuPrice, int sourceId) {
		try (SqlSession session = sessionFactory.openSession()) {
			PriceMapper mapper = session.getMapper(PriceMapper.class);
			PriceSource result = getSource(bozhuPrice, sourceId, mapper);
			session.commit();
			return result;
		}
	}

	private PriceSource getSource(BozhuPrice bozhuPrice, int sourceId, PriceMapper mapper) {
		PriceSource result = mapper.getSource(bozhuPrice.getUsername(), sourceId);
		if (result == null) {
			return null;
		}
		for (Price price : mapper.getPrices(bozhuPrice.getUsername(), sourceId)) {
			result.addPrice(price);
		}
		return result;
	}

	public void setDefaultPriceSource(BozhuPrice bozhuPrice, Integer sourceId) {
		try (SqlSession session = sessionFactory.openSession()) {
			BozhuMapper mapper = session.getMapper(BozhuMapper.class);
			mapper.updateDefaultPriceSource(bozhuPrice.getUsername(), sourceId);
			session.commit();
		}
	}

	private Price replacePrice(BozhuPrice bozhuPrice, int sourceId, Price price, PriceMapper mapper) {
		price.setUsername(bozhuPrice.getUsername());
		price.setSourceId(sourceId);
		Price result = mapper.getPrice(bozhuPrice.getUsername(), sourceId, price.getTypeId());
		if (result == null) {
			mapper.addPrice(price);
		} else {
			mapper.updatePrice(price);
		}
		return price;
	}

}