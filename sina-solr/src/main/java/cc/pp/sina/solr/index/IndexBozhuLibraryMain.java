package cc.pp.sina.solr.index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.datacenter.bozhudao.BozhuDao;
import cc.pp.datacenter.bozhudao.BozhuDbConnection;
import cc.pp.datacenter.bozhudao.BozhuDetail;
import cc.pp.datacenter.bozhudao.BozhuNotFoundException;
import cc.pp.sina.dao.bozhus.BozhuLibrary;
import cc.pp.sina.dao.users.SinaUsers;
import cc.pp.sina.domain.bozhus.UserAllParamsDomain;
import cc.pp.sina.domain.users.UserInfo;

/**
 * 索引博主库数据，单机器
 * @author wgybzb
 *
 */
public class IndexBozhuLibraryMain {

	private static Logger logger = LoggerFactory.getLogger(IndexBozhuLibraryMain.class);

	public static final String DB_URL = "jdbc:mysql://192.168.1.5:3306/pp_fenxi";
	public static final String DB_USERNAME = "pp_fenxi";
	public static final String DB_PASSWORD = "q#tmuYzC@sqB6!ok@sHd";

	private static final String BASE_URL = "http://114.112.65.13:8983/solr/bozhu_library";
	private final SolrServer server;

	public IndexBozhuLibraryMain() {
		server = new HttpSolrServer(BASE_URL);
	}

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		logger.info("读取300多万博主Uid：");
		List<Long> uids = BozhuLibrary.getBozhuUids();
		logger.info("Uids' size=" + uids.size());

		logger.info("循环索引每一个博主数据：");
		IndexBozhuLibraryMain indexBozhus = new IndexBozhuLibraryMain();

		BozhuDbConnection.connectDb(DB_URL, DB_USERNAME, DB_PASSWORD);
		BozhuDao dao = new BozhuDao();
		int count = 0;
		List<UserAllParamsDomain> bozhus = new ArrayList<>();
		for (long uid : uids) {
			if (++count % 1_0000 == 0) {
				logger.info("Index at: " + count);
				indexBozhus.addDocsToSolr(bozhus);
				bozhus = new ArrayList<>();
			}
			UserInfo baseInfo = SinaUsers.getSinaUserInfo("sinauserbaseinfo_" + uid % 32, uid);
			BozhuDetail detail = null;
			try {
				detail = dao.getBozhuDetail(uid);
			} catch (BozhuNotFoundException e) {
				continue;
			} catch (Exception e) {
				continue;
			}
			if (baseInfo == null || detail == null) {
				continue;
			}
			UserAllParamsDomain userAllParamsDomain = new UserAllParamsDomain.Builder(String.valueOf(uid))
					.setNickname(baseInfo.getScreen_name()).setDescription(baseInfo.getDescription())
					.setFanscount(baseInfo.getFollowers_count()).setWeibocount(baseInfo.getStatuses_count())
					.setAveragewbs(detail.getWb_avg_daily()).setInfluence(detail.getInfluence())
					.setActivation(detail.getActive()).setActivecount(detail.getAct_fan())
					.setAddvratio(detail.getVrate()).setActiveratio(detail.getAct_fan_rate())
					.setMaleratio(detail.getMalerate()).setFansexistedratio(detail.getExsit_fan_rate())
					.setVerify(baseInfo.getVerified_type()).setAllfanscount(detail.getFan_fans())
					.setAllactivefanscount(detail.getAct_fan_fans()).setTop5provinces("0")
					.setOriratio(detail.getOrig_wb_rate()).setAveorirepcom(detail.getOrig_wb_avg_repost())
					.setAverepcom(detail.getWb_avg_repost()).setWbsource("0")
					.setAverepcombyweek(detail.getWb_avg_repost_lastweek())
					.setAverepcombymonth(detail.getWb_avg_repost_lastmonth())
					.setAvereposterquality(detail.getRt_user_avg_quality())
					.setAveexposionsum(detail.getAvg_valid_fan_cover_last100())
					.setValidrepcombyweek(detail.getWb_avg_valid_repost_lastweek())
					.setValidrepcombymonth(detail.getWb_avg_valid_repost_lastmonth()).setUsertags("0")
					.setIdentitytype("0").setIndustrytype("0").setFanstags("0").setFansage("0").build();
			//				.setCreatedtime(0)  // 暂时去掉，包括价格数据等几项
			bozhus.add(userAllParamsDomain);
		}
		indexBozhus.addDocsToSolr(bozhus);
		indexBozhus.close();
	}

	/**
	 * 索引数据
	 */
	public void addDocsToSolr(List<UserAllParamsDomain> bozhus) {
		Collection<SolrInputDocument> docs = new ArrayList<>();
		for (UserAllParamsDomain bozhu : bozhus) {
			docs.add(getDoc(bozhu));
		}
		try {
			server.add(docs);
			server.commit();
		} catch (SolrServerException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	private SolrInputDocument getDoc(UserAllParamsDomain bozhu) {
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", bozhu.getUsername());
		doc.addField("username", bozhu.getUsername());
		doc.addField("screenname", bozhu.getNickname(), 3.0f);
		doc.addField("description", bozhu.getDescription(), 2.0f);
		doc.addField("followerscount", bozhu.getFanscount());
		doc.addField("statusescount", bozhu.getWeibocount());
		doc.addField("verifiedtype", bozhu.getVerify());
		doc.addField("averagewbs", bozhu.getAveragewbs());
		doc.addField("influence", bozhu.getInfluence());
		doc.addField("activation", bozhu.getActivation());
		doc.addField("activecount", bozhu.getActivecount());
		doc.addField("addvratio", bozhu.getAddvratio());
		doc.addField("activeratio", bozhu.getActiveratio());
		doc.addField("maleratio", bozhu.getMaleratio());
		doc.addField("top5provinces", bozhu.getTop5provinces());
		doc.addField("createdtime", bozhu.getCreatedtime());
		doc.addField("fansexistedratio", bozhu.getFansexistedratio());
		doc.addField("allfanscount", bozhu.getAllfanscount());
		doc.addField("allactivefanscount", bozhu.getAllactivefanscount());
		doc.addField("oriratio", bozhu.getOriratio());
		doc.addField("aveorirepcom", bozhu.getAveorirepcom());
		doc.addField("averepcom", bozhu.getAverepcom());
		doc.addField("wbsource", bozhu.getWbsource());
		doc.addField("averepcombyweek", bozhu.getAverepcombyweek());
		doc.addField("averepcombymonth", bozhu.getAverepcombymonth());
		doc.addField("avereposterquality", bozhu.getAvereposterquality());
		doc.addField("aveexposionsum", bozhu.getAveexposionsum());
		doc.addField("usertags", bozhu.getUsertags());
		doc.addField("validrepcombyweek", bozhu.getValidrepcombyweek());
		doc.addField("validrepcombymonth", bozhu.getValidrepcombymonth());
		doc.addField("pricesource", bozhu.getPricesource());
		doc.addField("softretweet", bozhu.getSoftretweet());
		doc.addField("softtweet", bozhu.getSofttweet());
		doc.addField("hardretweet", bozhu.getHardretweet());
		doc.addField("hardtweet", bozhu.getHardtweet());
		doc.addField("fansage", bozhu.getFansage());
		doc.addField("fanstags", bozhu.getFanstags());
		doc.addField("identitytype", bozhu.getIdentitytype());
		doc.addField("industrytype", bozhu.getIndustrytype());
		return doc;
	}

	public void close() {
		server.shutdown();
	}

}
