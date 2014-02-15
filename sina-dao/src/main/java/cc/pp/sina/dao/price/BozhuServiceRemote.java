package cc.pp.sina.dao.price;

import java.io.IOException;

import cc.pp.sina.domain.bozhus.BozhuPrice;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.domain.bozhus.BozhuBaseInfo;
import cc.pp.sina.utils.config.Config;
import cc.pp.sina.utils.json.JsonUtils;

/**
 * Created by chenwei on 14-1-15.
 */
public class BozhuServiceRemote implements BozhuService {

	private static final Logger logger = LoggerFactory.getLogger(BozhuServiceRemote.class);

	private static CloseableHttpClient httpClient = HttpClients.createMinimal();

	public static void main(String[] args) {
		BozhuService service = new BozhuServiceRemote();
		BozhuPrice bozhuPrice = service.get(2991367121L);
		System.out.println(bozhuPrice);

		System.out.println(service.get(1));
	}

	@Override
	public BozhuPrice get(long username) {
		HttpGet httpGet = new HttpGet(Config.get("server.sina.user") + "/sina/bozhu/uid/" + username + "/baseinfo");
		try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
			if (200 == response.getStatusLine().getStatusCode()) {
				BozhuBaseInfo baseInfo = JsonUtils.getObjectMapper().readValue(response.getEntity().getContent(), BozhuBaseInfo.class);
				if (baseInfo == null) {
					return null;
				}
				return new BozhuPrice(baseInfo.getId(), "sina");
			} else {
				logger.warn("get sina user from server, status = {}", response.getStatusLine().getStatusCode());
				return null;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
