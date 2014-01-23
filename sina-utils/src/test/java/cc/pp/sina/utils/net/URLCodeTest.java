package cc.pp.sina.utils.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URLDecoder;
import java.net.URLEncoder;

import org.junit.Test;

public class URLCodeTest {

	private static final String TEXT = "数据挖掘";
	private static final String ENCODE_TEXT = "%E6%95%B0%E6%8D%AE%E6%8C%96%E6%8E%98";

	@Test
	public void testIsUtf8Url() {
		assertTrue(URLCode.isUtf8Url(ENCODE_TEXT));
		assertFalse(URLCode.isUtf8Url(TEXT));
	}

	@Test
	public void testUtf8URLdecode() {
		assertEquals(TEXT, URLCode.Utf8UrlDecode(ENCODE_TEXT));
	}

	@Test
	public void testUtf8URLencode() {
		assertEquals(ENCODE_TEXT, URLCode.Utf8UrlEncode(TEXT));
	}

	@Test
	public void testJavaURLCodec() throws Exception {
		assertEquals("http%3A%2F%2Fwww.weibo.com%2Fbaiduguanfang",
				URLEncoder.encode("http://www.weibo.com/baiduguanfang", "utf-8"));
		assertEquals("http://www.weibo.com/baiduguanfang",
				URLDecoder.decode("http%3A%2F%2Fwww.weibo.com%2Fbaiduguanfang", "utf-8"));
	}

}
