package cc.pp.sina.dao.bozhus;

import java.util.*;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import cc.pp.sina.dao.BozhuMapper;
import cc.pp.sina.dao.price.BozhuDbConnection;
import cc.pp.sina.dao.price.PriceMapper;
import cc.pp.sina.domain.bozhus.Bozhu;
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

	public Price addPrice(Bozhu bozhu, int sourceId, Price price) {
		try (SqlSession session = sessionFactory.openSession()) {
			PriceMapper mapper = session.getMapper(PriceMapper.class);
			Price result = replacePrice(bozhu, sourceId, price, mapper);
			session.commit();
			return result;
		}
	}

	public PriceSource addSource(Bozhu bozhu, PriceSource source) {
		try (SqlSession session = sessionFactory.openSession()) {
			PriceMapper mapper = session.getMapper(PriceMapper.class);
			if (source.getId() == null) {
				mapper.addSource(source);
			} else {
				mapper.updateSource(source);
			}
			if (source.getPrices() != null && !source.getPrices().isEmpty()) {
				for (Price price : source.getPrices()) {
					replacePrice(bozhu, source.getId(), price, mapper);
				}
			}
			PriceSource result = getSource(bozhu, source.getId(), mapper);
			session.commit();
			return result;
		}
	}

	public void deletePrice(Bozhu bozhu, int sourceId, int typeId) {
		try (SqlSession session = sessionFactory.openSession()) {
			PriceMapper mapper = session.getMapper(PriceMapper.class);
			mapper.deletePriceByUsername_sourceId_typeId(bozhu.getUsername(), sourceId, typeId);
			session.commit();
		}
	}

	public void deleteSource(Bozhu bozhu, int sourceId) {
		try (SqlSession session = sessionFactory.openSession()) {
			PriceMapper mapper = session.getMapper(PriceMapper.class);
			mapper.deletePriceByUsername_sourceId(bozhu.getUsername(), sourceId);
			mapper.deleteSource(sourceId);
			session.commit();
		}
	}

	public PriceSource getDefaultPriceSource(Bozhu bozhu) {
		try (SqlSession session = sessionFactory.openSession()) {
			PriceMapper mapper = session.getMapper(PriceMapper.class);
			PriceSource result = mapper.getDefaultSourceByUsername(bozhu.getUsername());
			if (result != null) {
				result.setPrices(mapper.getPrices(bozhu.getUsername(), result.getId()));
			}
			session.commit();
			return result;
		}
	}

	public Collection<PriceSource> getSourcePrices(Bozhu bozhu) {
		try (SqlSession session = sessionFactory.openSession()) {
			PriceMapper mapper = session.getMapper(PriceMapper.class);
			Map<Integer, PriceSource> sources = new HashMap<>();
			for (Price price : mapper.getPricesByUsername(bozhu.getUsername())) {
				PriceSource source = sources.get(price.getSourceId());
				if (source == null) {
					source = mapper.getSource(bozhu.getUsername(), price.getSourceId());
					sources.put(source.getId(), source);
				}
				source.addPrice(price);
			}
			session.commit();
			return new ArrayList<PriceSource>(sources.values());
		}
	}

	public PriceSource getSource(Bozhu bozhu, int sourceId) {
		try (SqlSession session = sessionFactory.openSession()) {
			PriceMapper mapper = session.getMapper(PriceMapper.class);
			PriceSource result = getSource(bozhu, sourceId, mapper);
			session.commit();
			return result;
		}
	}

	private PriceSource getSource(Bozhu bozhu, int sourceId, PriceMapper mapper) {
		PriceSource result = mapper.getSource(bozhu.getUsername(), sourceId);
		if (result == null) {
			return null;
		}
		for (Price price : mapper.getPrices(bozhu.getUsername(), sourceId)) {
			result.addPrice(price);
		}
		return result;
	}

	public void setDefaultPriceSource(Bozhu bozhu, Integer sourceId) {
		try (SqlSession session = sessionFactory.openSession()) {
			BozhuMapper mapper = session.getMapper(BozhuMapper.class);
			mapper.updateDefaultPriceSource(bozhu.getUsername(), sourceId);
			session.commit();
		}
	}

	private Price replacePrice(Bozhu bozhu, int sourceId, Price price, PriceMapper mapper) {
		price.setUsername(bozhu.getUsername());
		price.setSourceId(sourceId);
		Price result = mapper.getPrice(bozhu.getUsername(), sourceId, price.getTypeId());
		if (result == null) {
			mapper.addPrice(price);
		} else {
			mapper.updatePrice(price);
		}
		return price;
	}

}