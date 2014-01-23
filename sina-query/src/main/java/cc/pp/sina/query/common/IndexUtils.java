package cc.pp.sina.query.common;

import org.apache.lucene.document.Document;

import cc.pp.sina.domain.bozhus.UserAllParamsDomain;
import cc.pp.sina.domain.query.BozhuParamsInfo;

public class IndexUtils {

	/**
	 * 博主转换
	 */
	public static UserAllParamsDomain bozhuTrans(BozhuParamsInfo bozhu) {

		return new UserAllParamsDomain.Builder(bozhu.getUsername()).setNickname(bozhu.getNickname()).setDescription("")
				.setFanscount(bozhu.getFanscount()).setWeibocount(bozhu.getWeibocount())
				.setAveragewbs(bozhu.getAveragewbs()).setInfluence(bozhu.getInfluence())
				.setActivation(bozhu.getActivation()).setActivecount(bozhu.getActivecount())
				.setAddvratio(bozhu.getAddvratio()).setActiveratio(bozhu.getActiveratio())
				.setMaleratio(bozhu.getMaleratio()).setFansexistedratio(bozhu.getFansexistedratio())
				.setVerify(bozhu.getVerify()).setAllfanscount(bozhu.getAllfanscount())
				.setAllactivefanscount(bozhu.getAllactivefanscount()).setTop5provinces(bozhu.getTop5provinces())
				.setOriratio(bozhu.getOriratio()).setAveorirepcom(bozhu.getAveorirepcom())
				.setAverepcom(bozhu.getAverepcom()).setWbsource(bozhu.getWbsource())
				.setAverepcombyweek(bozhu.getAverepcombyweek()).setAverepcombymonth(bozhu.getAverepcombymonth())
				.setAvereposterquality(bozhu.getAvereposterquality()).setAveexposionsum(bozhu.getAveexposionsum())
				.setValidrepcombyweek(bozhu.getValidrepcombyweek())
				.setValidrepcombymonth(bozhu.getValidrepcombymonth()).setUsertags(bozhu.getUsertags())
				.setCreatedtime(bozhu.getCreatedtime()).build();
	}

	/**
	 * 博主转换
	 */
	public static BozhuParamsInfo bozhuTrans(UserAllParamsDomain bozhu, String nickname) {

		BozhuParamsInfo result = new BozhuParamsInfo();
		result.setUsername(bozhu.getUsername());
		result.setNickname(nickname);
		result.setDescription(bozhu.getDescription());
		result.setFanscount(bozhu.getFanscount());
		result.setWeibocount(bozhu.getWeibocount());
		result.setAveragewbs(bozhu.getAveragewbs());
		result.setInfluence(bozhu.getInfluence());
		result.setActivation(bozhu.getActivation());
		result.setActivecount(bozhu.getActivecount());
		result.setAddvratio(bozhu.getAddvratio());
		result.setActiveratio(bozhu.getActiveratio());
		result.setMaleratio(bozhu.getMaleratio());
		result.setFansexistedratio(bozhu.getFansexistedratio());
		result.setVerify(bozhu.getVerify());
		result.setAllfanscount(bozhu.getAllfanscount());
		result.setTop5provinces(bozhu.getTop5provinces());
		result.setOriratio(bozhu.getOriratio());
		result.setAveorirepcom(bozhu.getAveorirepcom());
		result.setAverepcom(bozhu.getAverepcom());
		result.setWbsource(bozhu.getWbsource());
		result.setAverepcombyweek(bozhu.getAverepcombyweek());
		result.setAverepcombymonth(bozhu.getAverepcombymonth());
		result.setAvereposterquality(bozhu.getAvereposterquality());
		result.setAveexposionsum(bozhu.getAveexposionsum());
		result.setValidrepcombyweek(bozhu.getValidrepcombyweek());
		result.setValidrepcombymonth(bozhu.getValidrepcombymonth());
		result.setUsertags(bozhu.getUsertags());
		result.setCreatedtime(bozhu.getCreatedtime());

		return result;
	}

	/**
	 * 博主转换
	 */
	public static UserAllParamsDomain bozhuTrans(Document doc) {

		return new UserAllParamsDomain.Builder(doc.get("username")).setNickname(doc.get("nickname"))
				.setDescription(doc.get("description")).setFanscount(Integer.parseInt(doc.get("fanscount")))
				.setWeibocount(Integer.parseInt(doc.get("weibocount")))
				.setAveragewbs(Float.parseFloat(doc.get("averagewbs")))
				.setInfluence(Integer.parseInt(doc.get("influence")))
				.setActivation(Integer.parseInt(doc.get("activation")))
				.setActivecount(Integer.parseInt(doc.get("activecount")))
				.setAddvratio(Float.parseFloat(doc.get("addvratio")))
				.setActiveratio(Float.parseFloat(doc.get("activeratio")))
				.setMaleratio(Float.parseFloat(doc.get("maleratio")))
				.setFansexistedratio(Float.parseFloat(doc.get("fansexistedratio")))
				.setVerify(Integer.parseInt(doc.get("verify")))
				.setAllfanscount(Long.parseLong(doc.get("allfanscount")))
				.setAllactivefanscount(Long.parseLong(doc.get("Allactivefanscount")))
				.setTop5provinces(doc.get("top5provinces")).setOriratio(Float.parseFloat(doc.get("oriratio")))
				.setAveorirepcom(Float.parseFloat(doc.get("aveorirepcom")))
				.setAverepcom(Float.parseFloat(doc.get("averepcom"))).setWbsource(doc.get("wbsource"))
				.setAverepcombyweek(Float.parseFloat(doc.get("averepcombyweek")))
				.setAverepcombymonth(Float.parseFloat(doc.get("averepcombymonth")))
				.setAvereposterquality(Float.parseFloat(doc.get("avereposterquality")))
				.setAveexposionsum(Long.parseLong(doc.get("aveexposionsum")))
				.setValidrepcombyweek(Float.parseFloat(doc.get("Validrepcombyweek")))
				.setValidrepcombymonth(Float.parseFloat(doc.get("validrepcombymonth")))
				.setUsertags(doc.get("usertags")).setCreatedtime(Integer.parseInt(doc.get("createdtime"))).build();
	}

}
