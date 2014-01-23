package cc.pp.sina.domain.bozhus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;

import org.junit.Test;

import cc.pp.sina.domain.bozhus.UserAndFansDomain;

public class UserAndFansDaoTest {

	@Test
	public void testWithData() {

		HashMap<String, String> top5provinces = new HashMap<>();
		top5provinces.put("北京", "2.91%");
		top5provinces.put("四川", "7.77%");
		top5provinces.put("广东", "6.8%");

		UserAndFansDomain uafd = new UserAndFansDomain.Builder("1234567890").setNickname("wgybzb")
				.setDescription("testdata").setFanscount(1).setWeibocount(2).setVerify(3).setAveragewbs(4.0f)
				.setInfluence(5).setActivecount(6).setActivation(7).setAddvratio(8.0f).setActiveratio(9.0f)
				.setMaleratio(10.0f).setCreatedtime(11).setFansexistedratio(12.0f).setAllfanscount(13L)
				.setAllactivefanscount(14L).setTop5provinces(top5provinces).build();
		// 18个参数
		assertEquals("1234567890", uafd.getUsername());
		assertEquals("wgybzb", uafd.getNickname());
		assertEquals("testdata", uafd.getDescription());
		assertEquals(1, uafd.getFanscount());
		assertEquals(2, uafd.getWeibocount());
		assertEquals(3, uafd.getVerify());
		assertEquals(4.0f, uafd.getAveragewbs(), 0.0f);
		assertEquals(5, uafd.getInfluence());
		assertEquals(6, uafd.getActivecount());
		assertEquals(7, uafd.getActivation());
		assertEquals(8.0f, uafd.getAddvratio(), 0.0f);
		assertEquals(9.0f, uafd.getActiveratio(), 0.0f);
		assertEquals(10.0f, uafd.getMaleratio(), 0.0f);
		assertEquals(11, uafd.getCreatedtime());
		assertEquals(12.0f, uafd.getFansexistedratio(), 0.0f);
		assertEquals(13L, uafd.getAllfanscount());
		assertEquals(14L, uafd.getAllactivefanscount());

		assertEquals("2.91%", uafd.getTop5provinces().get("北京"));
		assertEquals("7.77%", uafd.getTop5provinces().get("四川"));
		assertEquals("6.8%", uafd.getTop5provinces().get("广东"));
	}

	@Test
	public void testWithoutData() {

		UserAndFansDomain uafd = new UserAndFansDomain.Builder("").build();
		// 18个参数
		assertEquals("", uafd.getUsername());
		assertEquals("", uafd.getNickname());
		assertEquals("", uafd.getDescription());
		assertEquals(0, uafd.getFanscount());
		assertEquals(0, uafd.getWeibocount());
		assertEquals(0, uafd.getVerify());
		assertEquals(0.0f, uafd.getAveragewbs(), 0.0f);
		assertEquals(0, uafd.getInfluence());
		assertEquals(0, uafd.getActivecount());
		assertEquals(0, uafd.getActivation());
		assertEquals(0.0f, uafd.getAddvratio(), 0.0f);
		assertEquals(0.0f, uafd.getActiveratio(), 0.0f);
		assertEquals(0.0f, uafd.getMaleratio(), 0.0f);
		assertEquals(0, uafd.getCreatedtime());
		assertEquals(0.0f, uafd.getFansexistedratio(), 0.0f);
		assertEquals(0L, uafd.getAllfanscount());
		assertEquals(0L, uafd.getAllactivefanscount());

		assertNull(uafd.getTop5provinces());
	}

}
