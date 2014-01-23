package cc.pp.sina.domain.bozhus;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cc.pp.sina.domain.bozhus.UserAllParamsDomain;

public class UserAllParamsDaoTest {

	@Test
	public void testWithData() {

		String top5provinces = "[{\"广东\":\"18.18%\",\"河南\":\"4.55%\",\"湖北\":\"6.82%\",\"湖南\":\"4.55%\",\"福建\":\"6.82%\"}]";
		String wbsource = "[{\"iPad客户端\":216,\"专业版微博\":1,\"云中小鸟\":4,\"新浪微博\":5,\"时尚类\":771}]";
		String usertags = "[{\"0\":\"美女\",\"1\":\"幽默\",\"2\":\"美食\",\"3\":\"语录\",\"4\":\"摄影爱好者\"}]";

		UserAllParamsDomain uapd = new UserAllParamsDomain.Builder("1234567890").setNickname("wgybzb")
				.setDescription("testdata").setFanscount(1).setWeibocount(2).setAveragewbs(3.0f).setInfluence(4)
				.setActivation(5).setActivecount(6).setAddvratio(7.0f).setActiveratio(8.0f).setMaleratio(9.0f)
				.setFansexistedratio(10.0f).setVerify(11).setAllfanscount(12L).setAllactivefanscount(13L)
				.setTop5provinces(top5provinces).setOriratio(14.0f).setAveorirepcom(15.0f).setAverepcom(16.0f)
				.setWbsource(wbsource.toString()).setAverepcombyweek(17.0f).setAverepcombymonth(18.0f)
				.setAvereposterquality(19.0f).setAveexposionsum(20L).setValidrepcombyweek(21.0f)
				.setValidrepcombymonth(22.0f).setCreatedtime(23).setUsertags(usertags).build();

		// 29个参数
		assertEquals("1234567890", uapd.getUsername());
		assertEquals("wgybzb", uapd.getNickname());
		assertEquals("testdata", uapd.getDescription());
		assertEquals(1, uapd.getFanscount());
		assertEquals(2, uapd.getWeibocount());
		assertEquals(3.0f, uapd.getAveragewbs(), 0.0f);
		assertEquals(4, uapd.getInfluence());
		assertEquals(5, uapd.getActivation());
		assertEquals(6, uapd.getActivecount());
		assertEquals(7.0f, uapd.getAddvratio(), 0.0f);
		assertEquals(8.0f, uapd.getActiveratio(), 0.0f);
		assertEquals(9.0f, uapd.getMaleratio(), 0.0f);
		assertEquals(10.0f, uapd.getFansexistedratio(), 0.0f);
		assertEquals(11, uapd.getVerify());
		assertEquals(12L, uapd.getAllfanscount());
		assertEquals(13L, uapd.getAllactivefanscount());
		assertEquals(top5provinces.toString(), uapd.getTop5provinces());
		assertEquals(14.0f, uapd.getOriratio(), 0.0f);
		assertEquals(15.0f, uapd.getAveorirepcom(), 0.0f);
		assertEquals(16.0f, uapd.getAverepcom(), 0.0f);
		assertEquals(wbsource.toString(), uapd.getWbsource());
		assertEquals(17.0f, uapd.getAverepcombyweek(), 0.0f);
		assertEquals(18.0f, uapd.getAverepcombymonth(), 0.0f);
		assertEquals(19.0f, uapd.getAvereposterquality(), 0.0f);
		assertEquals(20L, uapd.getAveexposionsum());
		assertEquals(21.0f, uapd.getValidrepcombyweek(), 0.0f);
		assertEquals(22.0f, uapd.getValidrepcombymonth(), 0.0f);
		assertEquals(23, uapd.getCreatedtime());
		assertEquals(usertags.toString(), uapd.getUsertags());
	}

	@Test
	public void testWithoutData() {

		UserAllParamsDomain uapd = new UserAllParamsDomain.Builder("").build();

		// 29个参数
		assertEquals("", uapd.getUsername());
		assertEquals("", uapd.getNickname());
		assertEquals("", uapd.getDescription());
		assertEquals(0, uapd.getFanscount());
		assertEquals(0, uapd.getWeibocount());
		assertEquals(0.0f, uapd.getAveragewbs(), 0.0f);
		assertEquals(0, uapd.getInfluence());
		assertEquals(0, uapd.getActivation());
		assertEquals(0, uapd.getActivecount());
		assertEquals(0.0f, uapd.getAddvratio(), 0.0f);
		assertEquals(0.0f, uapd.getActiveratio(), 0.0f);
		assertEquals(0.0f, uapd.getMaleratio(), 0.0f);
		assertEquals(0.0f, uapd.getFansexistedratio(), 0.0f);
		assertEquals(0, uapd.getVerify());
		assertEquals(0L, uapd.getAllfanscount());
		assertEquals(0L, uapd.getAllactivefanscount());
		assertEquals("", uapd.getTop5provinces());
		assertEquals(0.0f, uapd.getOriratio(), 0.0f);
		assertEquals(0.0f, uapd.getAveorirepcom(), 0.0f);
		assertEquals(0.0f, uapd.getAverepcom(), 0.0f);
		assertEquals("", uapd.getWbsource());
		assertEquals(0.0f, uapd.getAverepcombyweek(), 0.0f);
		assertEquals(0.0f, uapd.getAverepcombymonth(), 0.0f);
		assertEquals(0.0f, uapd.getAvereposterquality(), 0.0f);
		assertEquals(0L, uapd.getAveexposionsum());
		assertEquals(0.0f, uapd.getValidrepcombyweek(), 0.0f);
		assertEquals(0.0f, uapd.getValidrepcombymonth(), 0.0f);
		assertEquals(0, uapd.getCreatedtime());
		assertEquals("", uapd.getUsertags());
	}

}
