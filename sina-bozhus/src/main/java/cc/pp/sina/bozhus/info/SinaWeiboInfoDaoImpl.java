package cc.pp.sina.bozhus.info;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.bozhus.common.SinaErrorCode;
import cc.pp.sina.tokens.service.TokenService;

import com.sina.weibo.api.Timeline;
import com.sina.weibo.model.Paging;
import com.sina.weibo.model.StatusWapper;
import com.sina.weibo.model.WeiboException;

public class SinaWeiboInfoDaoImpl implements SinaWeiboInfoDao {

	private static Logger logger = LoggerFactory.getLogger(SinaWeiboInfoDaoImpl.class);

	/**
	 * 存在线程安全，但不重要;
	 * 可能在删除token时删除错误，但是无效token占少数;
	 * 否则对象不能共用
	 */
	private static Timeline timeLine = new Timeline();

	private final TokenService tokenService;

	public SinaWeiboInfoDaoImpl(TokenService tokenService) {
		this.tokenService = tokenService;
	}

	/**
	 * 测试函数
	 */
	public static void main(String[] args) throws WeiboException {

		TokenService tokenService = new TokenService();
		List<String> sinaTokens = new ArrayList<String>();
		sinaTokens.add("2.00vyKmPBdcZIJCab7e4cdb505dUSLC");
		sinaTokens.add("2.00vyKmPBdcZIJCab7e4cdb505dUSLC");
		tokenService.setSinaTokens(sinaTokens);
		SinaWeiboInfoDao sinaWeiboInfoDao = new SinaWeiboInfoDaoImpl(tokenService);
		//		StatusWapper statusWapper = sinaWeiboInfoDao.getSinaSingleWeiboResposts("3596657608114610", 1);
		StatusWapper statusWapper = sinaWeiboInfoDao.getSinaUserWeibos("2698966464", 1);
		System.out.println(statusWapper.getTotalNumber());
		System.out.println(JSONArray.fromObject(statusWapper.getStatuses()));
	}

	/**
	 * 单条微博转发数据
	 * @param cursor：从1开始
	 */
	@Override
	public StatusWapper getSinaSingleWeiboResposts(String wid, int cursor) {

		String token = tokenService.getRandomToken();
		StatusWapper status = null;
		try {
			status = timeLine.getRepostTimeline(wid, new Paging(cursor, 200), token);
		} catch (WeiboException e) {
			if (e.getErrorCode() == SinaErrorCode.ERROR_CODE_20003) {
				logger.info("The wid： " + wid + " does not exists!");
				return null;
			}
			if (isRetry(e, token)) {
				return getSinaSingleWeiboResposts(wid, cursor);
			}
			throw new RuntimeException(String.format("WeiboException: %d\t%s\t%s", e.getErrorCode(), wid, token), e);
		}

		return status;
	}

	/**
	 * 用户微博
	 * @param cursor：从1开始
	 */
	@Override
	public StatusWapper getSinaUserWeibos(String uid, int cursor) {

		String token = tokenService.getRandomToken();
		StatusWapper status = null;
		try {
			status = timeLine.getUserTimelineByUid(uid, new Paging(cursor, 100), 0, 0, token);
			return status;
		} catch (WeiboException e) {
			if (e.getErrorCode() == SinaErrorCode.ERROR_CODE_20003) {
				logger.info("User： " + uid + " does not exists!");
				return null;
			}
			if (isRetry(e, token)) {
				return getSinaUserWeibos(uid, cursor);
			}
			throw new RuntimeException(String.format("WeiboException: %d\t%s\t%s", e.getErrorCode(), uid, token), e);
		}
	}

	@Override
	public StatusWapper getPublicWeibos() {

		String token = tokenService.getRandomToken();
		StatusWapper status = null;
		try {
			status = timeLine.getPublicTimeline(200, 0, token);
			return status;
		} catch (WeiboException e) {
			if (e.getErrorCode() == SinaErrorCode.ERROR_CODE_20003) {
				return null;
			}
			if (isRetry(e, token)) {
				return getPublicWeibos();
			}
			throw new RuntimeException(String.format("WeiboException: %d\t%s\t%s", e.getErrorCode(), token), e);
		}
	}

	/**
	 * 重试
	 */
	private boolean isRetry(WeiboException e, String token) {
		switch (e.getErrorCode()) {
		case SinaErrorCode.ERROR_CODE_IP_REQUESTS_OUT_OF_RATE_LIMIT:
			try {
				logger.info("weibo API: IP requests out of rate limit.");
				// 休眠一段时间，以降低请求频率
				Thread.sleep(60 * 1000);
			} catch (InterruptedException e1) {
				// Ignore
			}
			return true;
		case SinaErrorCode.ERROR_CODE_SYSTEM_ERROR:
			logger.info("weibo API: System error.");
			return true;
		case SinaErrorCode.ERROR_CODE_EXPIRED_TOKEN:
		case SinaErrorCode.ERROR_CODE_TOKEN_1:
		case SinaErrorCode.ERROR_CODE_TOKEN_2:
		case SinaErrorCode.ERROR_CODE_TOKEN_3:
		case SinaErrorCode.ERROR_CODE_TOKEN_4:
		case SinaErrorCode.ERROR_CODE_APPKEY_MISSING:
		case SinaErrorCode.ERROR_CODE_REMOTE_SERVICE:
		case SinaErrorCode.ERROR_CODE_502:
		case SinaErrorCode.ERROR_CODE_21332:
			tokenService.deleteInValidToken(token);
			return true;
		default:
			return false;
		}
	}

}
