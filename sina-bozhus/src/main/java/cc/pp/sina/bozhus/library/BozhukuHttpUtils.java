package cc.pp.sina.bozhus.library;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map.Entry;

import net.sf.json.JSONArray;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import cc.pp.sina.domain.bozhus.BozhuAdd;
import cc.pp.sina.domain.bozhus.BozhuInfo;

import com.sina.weibo.http.Response;
import com.sina.weibo.model.WeiboException;

/**
 * GET 查询
 * POST 添加
 * PUT 更新
 * DELETE 删除
 */
public class BozhukuHttpUtils {

	private static final String BOZHU_URL = "http://114.112.81.3:8083/bozhus";

	/**
	 * 测试函数
	 */
	public static void main(String[] args) throws IOException, WeiboException, SQLException {

		/**
		 * 添加博主基础信息或者更新
		 */
		BozhuAdd addBozhu = new BozhuAdd.Builder().setUsername(3481175457L).setNickname("铁血战神O")
				.setDescription("【铁血战神】是喜讯无限2013年的倾心力作，铁血游戏为硬派3D格斗手游，采用Unity3D引擎打造，精美的暗黑魔幻画风中蕴含着中国民族特...")
				.setIsppuser(1).setPtype("sina").setVerify(2).build();
		BozhukuHttpUtils.doBozhuInfoConnectionKeepAlive(BozhukuHttpUtils.getBozhuInfoUrl(),
				JSONArray.fromObject(addBozhu).get(0).toString(), "post");
		/**
		 * 更新统计信息
		 */
		BozhuInfo bozhuInfo = new BozhuInfo.Builder().setInfluence(63).setActive(8).setWbnum(472).setFannum(15612)
				.setMalerate(0.5072283148765564f).setVrate(0.01679999940097332f).setAct_fan((int) (15612 * 0.5745f))
				.setFan_fans(13097783L).setAct_fan_fans(12809549L).setWb_avg_daily(2.309999942779541f)
				.setWb_avg_repost_lastweek(5.75f).setWb_avg_repost_lastmonth(6.0714287757873535f)
				.setWb_avg_repost(178.27330017089844f).setOrig_wb_rate(0.5762711763381958f)
				.setOrig_wb_avg_repost(290.1029357910156f).setWb_avg_valid_repost_lastweek(0f)
				.setWb_avg_valid_repost_lastmonth(0f).setRt_user_avg_quality(0.01f)
				.setAvg_valid_fan_cover_last100(15612).build();
		BozhukuHttpUtils.doBozhuInfoConnectionKeepAlive(BozhukuHttpUtils.getBozhuInfoUrl("3481175457", "sina"),
				JSONArray.fromObject(bozhuInfo).get(0).toString(), "put");
	}

	public static void doBozhuInfoConnectionKeepAlive(String url, String data, String method) throws HttpException,
			IOException, WeiboException {

		EntityEnclosingMethod httpMethod = null;
		if ("post".equals(method)) {
			httpMethod = new PostMethod(url);
		} else if ("put".equals(method)) {
			httpMethod = new PutMethod(url);
		} else {
			return;
		}
		httpMethod.setContentChunked(true);
		RequestEntity requestEntity = new StringRequestEntity(data, null,
				"UTF-8");
		httpMethod.setRequestEntity(requestEntity);
		MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
		HttpConnectionManagerParams params = connectionManager.getParams();
		params.setMaxTotalConnections(200);
		params.setDefaultMaxConnectionsPerHost(150);
		params.setConnectionTimeout(30000);
		params.setSoTimeout(30000);
		HttpClientParams clientParams = new HttpClientParams();
		clientParams.setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
		org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient(
				clientParams, connectionManager);
		httpMethod.getParams()
				.setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
		httpMethod.addRequestHeader("Connection", "close");
		client.getParams().setBooleanParameter("http.protocol.expect-continue", false);

		client.executeMethod(httpMethod);
		int responseCode = httpMethod.getStatusCode();
		if (responseCode != 200 && responseCode != 201) {
			System.err.println(responseCode);
			Response response = new Response();
			response.setResponseAsString(httpMethod.getResponseBodyAsString());
			System.err.println(response.toString());
		}
		httpMethod.releaseConnection();
	}

	/**
	 * 返回博主信息的url
	 */
	public static String getBozhuInfoUrl(String username, String ptype) {
		return BOZHU_URL + "/" + username + "/" + ptype + "/statistics";
	}

	public static String getBozhuInfoUrl() {
		return BOZHU_URL + "/";
	}

	/**
	 * 查看博主价格url
	 */
	public static String getBozhuPriceUrl(String username, String ptype) {
		return BOZHU_URL + "/" + username + "/" + ptype + "/price";
	}

	public static String getBozhuPriceUrl(HashMap<String, String> params) {

		String url = BOZHU_URL + "/1/price?";
		for (Entry<String, String> param : params.entrySet()) {
			url = url + param.getKey() + "=" + param.getValue() + "&";
		}

		return url.substring(0, url.length() - 1);
	}

	public static String getBozhuPriceUrl() {
		return BOZHU_URL + "/1/price";
	}

}
