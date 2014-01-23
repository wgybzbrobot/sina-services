package cc.pp.sina.bozhus.common;

import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sina.weibo.model.WeiboException;

/**
 * 皮皮数据中心新浪数据接口
 * @author wgybzb
 *
 */
public class PPSinaApi {

	private static Logger logger = LoggerFactory.getLogger(PPSinaApi.class);

	/*
	 * 基本链接
	 */
	private static final String BASE_URL = "http://60.169.74.48:2222/sina/";

	/*
	 * 博主相关链接
	 */
	private static final String BOZHU_INFO = "bozhu/uid/";

	private static final String BOZHU_BATCH_INFOS = "bozhu/uids/";

	/*
	 * 单用户基础数据URL
	 */
	private static String bozhuBaseInfoUrl(String uid) {
		return BASE_URL + BOZHU_INFO + uid + "/baseinfo";
	}

	/*
	 * 批量用户基础数据URL
	 */
	private static String bozhuBatchBaseInfoUrl(String uids) {
		return BASE_URL + BOZHU_BATCH_INFOS + uids + "/baseinfos";
	}

	/*
	 * 用户粉丝uid列表URL
	 */
	private static String bozhuFansIdsUrl(String uid) {
		return BASE_URL + BOZHU_INFO + uid + "/fansids";
	}

	/*
	 * 用户粉丝基础数据URL
	 */
	private static String bozhuFansBaseInfosUrl(String uid) {
		return BASE_URL + BOZHU_INFO + uid + "/fansinfos";
	}

	/**
	 * 单用户基础数据
	 */
	public static String getBozhuBaseInfo(String uid) {
		try {
			return doGet(bozhuBaseInfoUrl(uid));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 批量用户基础数据
	 */
	public static String getBatchBozhuBaseInfo(String uids) {
		try {
			return doGet(bozhuBatchBaseInfoUrl(uids));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 用户粉丝uid列表
	 */
	public static String getBozhuFansIds(String uid) {
		try {
			return doGet(bozhuFansIdsUrl(uid));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 粉丝基础数据
	 */
	public static String getbozhuFansBaseInfos(String uid) {
		try {
			return doGet(bozhuFansBaseInfosUrl(uid));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * GET方法
	 */
	public static String doGet(String url) throws HttpException, IOException {

		GetMethod httpMethod = new GetMethod(url);
		MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
		HttpConnectionManagerParams params = connectionManager.getParams();
		params.setMaxTotalConnections(200);
		params.setDefaultMaxConnectionsPerHost(150);
		params.setConnectionTimeout(30_000);
		params.setSoTimeout(30_000);
		HttpClientParams clientParams = new HttpClientParams();
		clientParams.setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
		HttpClient client = new HttpClient(clientParams, connectionManager);
		httpMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, true));
		httpMethod.addRequestHeader("Connection", "close");
		client.getParams().setBooleanParameter("http.protocol.expect-continue", false);

		client.executeMethod(httpMethod);
		if (httpMethod.getStatusCode() != 200 && httpMethod.getStatusCode() != 201) {
			logger.info(httpMethod.getResponseBodyAsString());
			return null;
		}

		return httpMethod.getResponseBodyAsString();
	}

	/**
	 * POST和PUT方法
	 */
	public static void doPostOrPut(String url, String data, String method) throws HttpException,
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
		RequestEntity requestEntity = new StringRequestEntity(data, null, "UTF-8");
		httpMethod.setRequestEntity(requestEntity);
		MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
		HttpConnectionManagerParams params = connectionManager.getParams();
		params.setMaxTotalConnections(200);
		params.setDefaultMaxConnectionsPerHost(150);
		params.setConnectionTimeout(30000);
		params.setSoTimeout(30000);
		HttpClientParams clientParams = new HttpClientParams();
		clientParams.setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
		HttpClient client = new HttpClient(clientParams, connectionManager);
		httpMethod.getParams()
				.setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
		httpMethod.addRequestHeader("Connection", "close");
		client.getParams().setBooleanParameter("http.protocol.expect-continue", false);

		client.executeMethod(httpMethod);
		if (httpMethod.getStatusCode() != 200 && httpMethod.getStatusCode() != 201) {
			logger.info(httpMethod.getResponseBodyAsString());
		}
		httpMethod.releaseConnection();
	}

	/**
	 * 测试函数
	 */
	public static void main(String[] args) {
		System.out.println(PPSinaApi.getBozhuFansIds("2991367121"));
		System.out.println(PPSinaApi.getBozhuBaseInfo("2991367121"));
		System.out.println(PPSinaApi.getbozhuFansBaseInfos("2991367121"));
	}

}
