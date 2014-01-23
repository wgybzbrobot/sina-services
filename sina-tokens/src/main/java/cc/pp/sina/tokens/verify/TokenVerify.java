package cc.pp.sina.tokens.verify;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import cc.pp.sina.domain.tokens.TokenInfo;

public class TokenVerify {

	private static final String BASEURL = "https://api.weibo.com/oauth2/get_token_info?access_token=";
	private static HttpUtils httpUtils;
	private static ObjectMapper mapper;

	static {
		httpUtils = new HttpUtils();
		mapper = new ObjectMapper();
	}

	public static long getTokenExpirein(String token) throws JsonParseException, JsonMappingException, IOException {

		String url = BASEURL + token;
		TokenInfo tokenInfo = mapper.readValue(httpUtils.doPost(url, "utf-8"), TokenInfo.class);

		return tokenInfo.getExpire_in();
	}

	public static List<String> filterInvalidTokens(List<String> tokens) {

		List<String> result = new ArrayList<String>();
		int count = 0;
		for (String token : tokens) {
			System.out.println(count++);
			try {
				if (getTokenExpirein(token) > 0) {
					result.add(token);
				}
			} catch (Exception e) {
				continue;
			}
		}

		return result;
	}

}
