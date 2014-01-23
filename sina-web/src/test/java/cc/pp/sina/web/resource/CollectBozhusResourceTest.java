package cc.pp.sina.web.resource;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;

public class CollectBozhusResourceTest {

	private static String str = "[{\"北京\":\"4.95%\",\"安徽\":\"2.75%\",\"广东\":\"2.2%\",\"河北\":\"2.2%\",\"澳门\":\"1.1%\"}]";

	@Test
	public void testMapperProvinces() {
		HashMap<String, Float> provinces = AnalysisCollectedResource.mapperProvinces(str);
		assertEquals(4.95f, provinces.get("北京"), 0.0f);
		assertEquals(2.75f, provinces.get("安徽"), 0.0f);
		assertEquals(2.2f, provinces.get("广东"), 0.0f);
		assertEquals(2.2f, provinces.get("河北"), 0.0f);
		assertEquals(1.1f, provinces.get("澳门"), 0.0f);
	}

	@Test
	public void testCollectProvince() {

		HashMap<String, Float> provinces = new HashMap<>();
		provinces.put("北京", 1.1f);
		provinces.put("安徽", 2.2f);
		AnalysisCollectedResource.collectProvince(provinces, str);
		assertEquals(6.0499997f, provinces.get("北京"), 0.0f);
		assertEquals(4.95f, provinces.get("安徽"), 0.0f);
		assertEquals(2.2f, provinces.get("广东"), 0.0f);
		assertEquals(2.2f, provinces.get("河北"), 0.0f);
		assertEquals(1.1f, provinces.get("澳门"), 0.0f);
	}

}
