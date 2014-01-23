package cc.pp.sina.bozhus.single.weibo;

import cc.pp.sina.tokens.service.TokenService;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Created by chenwei@pp.cc on 14-1-12.
 */
public class BaseSinaApi {

	protected static String server = "https://api.weibo.com";

	protected static CloseableHttpClient httpclient = HttpClients.createMinimal();

	protected static TokenService tokenService = new TokenService();

}
