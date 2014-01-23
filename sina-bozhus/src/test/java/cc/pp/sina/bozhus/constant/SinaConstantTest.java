package cc.pp.sina.bozhus.constant;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SinaConstantTest {

	@Test
	public void testGetProvinceProps() {

		assertEquals("北京", SinaConstant.getProvince("11"));
		assertEquals("浙江", SinaConstant.getProvince("33"));
		assertEquals("上海", SinaConstant.getProvince("31"));
		assertEquals("广东", SinaConstant.getProvince("44"));
	}

	@Test
	public void testGetVerify() {

		assertEquals("黄V", SinaConstant.getVerify("0"));
		assertEquals("蓝V", SinaConstant.getVerify("1"));
		assertEquals("蓝V", SinaConstant.getVerify("2"));
		assertEquals("蓝V", SinaConstant.getVerify("3"));
		assertEquals("蓝V", SinaConstant.getVerify("4"));
		assertEquals("蓝V", SinaConstant.getVerify("5"));
		assertEquals("蓝V", SinaConstant.getVerify("6"));
		assertEquals("蓝V", SinaConstant.getVerify("7"));
		assertEquals("蓝V", SinaConstant.getVerify("8"));
		assertEquals("微女郎", SinaConstant.getVerify("10"));
		assertEquals("达人", SinaConstant.getVerify("200"));
		assertEquals("达人", SinaConstant.getVerify("220"));
		assertEquals("已故V", SinaConstant.getVerify("400"));
		assertEquals("普通用户", SinaConstant.getVerify("-1"));
	}

	@Test
	public void testGetDeatilVerify() {

		assertEquals("名人", SinaConstant.getDetailVerify("0"));
		assertEquals("政府", SinaConstant.getDetailVerify("1"));
		assertEquals("企业", SinaConstant.getDetailVerify("2"));
		assertEquals("媒体", SinaConstant.getDetailVerify("3"));
		assertEquals("校园", SinaConstant.getDetailVerify("4"));
		assertEquals("网站", SinaConstant.getDetailVerify("5"));
		assertEquals("应用", SinaConstant.getDetailVerify("6"));
		assertEquals("团体/机构", SinaConstant.getDetailVerify("7"));
		assertEquals("待审企业", SinaConstant.getDetailVerify("8"));
		assertEquals("微博女郎", SinaConstant.getDetailVerify("10"));
		assertEquals("初级达人", SinaConstant.getDetailVerify("200"));
		assertEquals("中高级达人", SinaConstant.getDetailVerify("220"));
		assertEquals("已故V用户", SinaConstant.getDetailVerify("400"));
		assertEquals("普通用户", SinaConstant.getDetailVerify("-1"));
	}

}
