package cc.pp.sina.bozhus.single.weibo;

import cc.pp.sina.utils.json.JsonUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenwei@pp.cc on 14-1-12.
 */
public class Statuses extends BaseSinaApi {

	private static final Statuses empty = new Statuses(new ArrayList<Status>());
	private List<Status> statuses;
	private int total_number;

	public Statuses() {
	}

	public Statuses(List<Status> statuses) {
		this.statuses = statuses;
		total_number = statuses.size();
	}

	public static Statuses showBatch(Long... ids) {
		if (ids.length == 0) {
			return empty;
		}
		final String resource = "/2/statuses/show_batch.json";
		StringBuilder idsStr = new StringBuilder(ids.length * 17);
		for (Long id : ids) {
			idsStr.append(id).append(",");
		}
		idsStr.deleteCharAt(idsStr.length() - 1);
		HttpGet httpget = new HttpGet(String.format("%s%s?access_token=%s&ids=%s", server, resource, tokenService.getRandomToken(), idsStr.toString()));
		try (CloseableHttpResponse response = httpclient.execute(httpget)) {
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return JsonUtils.getObjectMapper().readValue(response.getEntity().getContent(), Statuses.class);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	public List<Status> getStatuses() {
		return statuses;
	}

	public int getTotal_number() {
		return total_number;
	}
}
