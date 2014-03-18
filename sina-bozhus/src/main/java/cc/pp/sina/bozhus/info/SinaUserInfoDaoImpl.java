package cc.pp.sina.bozhus.info;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.bozhus.common.SinaErrorCode;
import cc.pp.sina.domain.bozhus.UserTag;
import cc.pp.sina.domain.bozhus.UserTagWapper;
import cc.pp.sina.tokens.service.TokenService;
import cc.pp.sina.utils.java.JavaPattern;

import com.sina.weibo.api.Comments;
import com.sina.weibo.api.Friendships;
import com.sina.weibo.api.Tags;
import com.sina.weibo.api.Users;
import com.sina.weibo.model.Comment;
import com.sina.weibo.model.CommentWapper;
import com.sina.weibo.model.Paging;
import com.sina.weibo.model.Tag;
import com.sina.weibo.model.TagWapper;
import com.sina.weibo.model.User;
import com.sina.weibo.model.UserWapper;
import com.sina.weibo.model.WeiboException;

public class SinaUserInfoDaoImpl implements SinaUserInfoDao {

	private static Logger logger = LoggerFactory.getLogger(SinaUserInfoDaoImpl.class);

	private static final int MAX_FANS = 5_000;

	private static Users users = new Users();
	private static Friendships friendships = new Friendships();
	private static Tags tags = new Tags();
	private static Comments comments = new Comments();

	private final TokenService tokenService;

	public SinaUserInfoDaoImpl(TokenService tokenService) {
		this.tokenService = tokenService;
	}

	/**
	 * 用户基础信息
	 */
	@Override
	public User getSinaUserBaseInfo(String uid) {

		String token = tokenService.getRandomToken();
		User user = null;
		try {
			user = users.showUserById(uid, token);
			return user;
		} catch (WeiboException e) {
			if (e.getErrorCode() == SinaErrorCode.ERROR_CODE_20003) {
				logger.info("User '" + uid + "' does not exists!");
				return null;
			}
			if (isRetry(e, token)) {
				return getSinaUserBaseInfo(uid);
			}
			throw new RuntimeException(String.format("WeiboException: %d\t%s\t%s", e.getErrorCode(), uid, token), e);
		}
	}

	/**
	 * 用户粉丝数据
	 */
	@Override
	public UserWapper getSinaUserFans(String uid, int cursor) {

		String token = tokenService.getRandomToken();
		UserWapper followers = null;
		try {
			followers = friendships.getFollowersById(uid, 200, cursor * 200, token);
			return followers;
		} catch (WeiboException e) {
			if (e.getErrorCode() == SinaErrorCode.ERROR_CODE_20003) {
				logger.info("User '" + uid + "' does not exists!");
				return null;
			}
			if (isRetry(e, token)) {
				return getSinaUserFans(uid, cursor);
			}
			throw new RuntimeException(String.format("WeiboException: %d\t%s\t%s", e.getErrorCode(), uid, token), e);
		}
	}

	/**
	 * 用户粉丝Uid列表，一次性返回，最多5000
	 */
	@Override
	public String[] getSinaUserFansIds(String uid) {

		String token = tokenService.getRandomToken();
		String[] followers = null;
		try {
			followers = friendships.getFollowersIdsById(uid, MAX_FANS, 0, token);
			return followers;
		} catch (WeiboException e) {
			if (e.getErrorCode() == SinaErrorCode.ERROR_CODE_20003) {
				logger.info("User '" + uid + "' does not exists!");
				return null;
			}
			if (isRetry(e, token)) {
				return getSinaUserFansIds(uid);
			}
			throw new RuntimeException(String.format("WeiboException: %d\t%s\t%s", e.getErrorCode(), uid, token), e);
		} catch (RuntimeException e) {
			return null;
		}
	}

	/**
	 * 用户标签数据
	 */
	@Override
	public List<UserTag> getSinaUserTags(String uid, int maxTags) {

		String token = tokenService.getRandomToken();
		List<UserTag> tagsList = null;
		try {
			tagsList = transTagsList(tags.getTags(uid, token), maxTags);
			return tagsList;
		} catch (WeiboException e) {
			if (e.getErrorCode() == SinaErrorCode.ERROR_CODE_20003) {
				logger.info("User '" + uid + "' does not exists!");
				return null;
			}
			if (e.getErrorCode() == SinaErrorCode.ERROR_CODE_10025) {
				logger.info("User '" + uid + "' fuid is wrong!");
				return null;
			}
			if (isRetry(e, token)) {
				return getSinaUserTags(uid, maxTags);
			}
			throw new RuntimeException(String.format("WeiboException: %d\t%s\t%s", e.getErrorCode(), uid, token), e);
		}
	}

	/**
	 * 批量获取用户标签数据
	 */
	@Override
	public List<UserTagWapper> getSinaUsersBatchTags(String uids, int maxTags) {

		String token = tokenService.getRandomToken();
		List<TagWapper> tagWapper = null;
		try {
			tagWapper = tags.getTagsBatch(uids, token);
			if (tagWapper != null) {
				List<UserTagWapper> result = new ArrayList<>();
				for (TagWapper tag : tagWapper) {
					result.add(new UserTagWapper(transTagsList(tag.getTags(), maxTags), Long.parseLong(tag.getId())));
				}
				return result;
			} else {
				return null;
			}
		} catch (WeiboException e) {
			if (isRetry(e, token)) {
				return getSinaUsersBatchTags(uids, maxTags);
			}
			throw new RuntimeException(String.format("WeiboException: %d\t%s\t%s", e.getErrorCode(), uids, token), e);
		}
	}

	/**
	 * 标签数据转换
	 */
	private List<UserTag> transTagsList(List<Tag> tagsList, int maxTags) {

		List<UserTag> result = new ArrayList<>();
		for (int i = 0; i < Math.min(tagsList.size(), maxTags); i++) {
			result.add(new UserTag(tagsList.get(i).getId(), tagsList.get(i).getValue(), Long
					.parseLong(tagsList.get(i).getWeight())));
		}

		return result;
	}

	/**
	 * 用户域名转用户名
	 */
	@Override
	public String transDomainNameToUsername(String domain) {

		String token = tokenService.getRandomToken();
		User userInfo = null;
		try {
			userInfo = users.showUserByDomain(domain, token);
			return userInfo.getId();
		} catch (WeiboException e) {
			if (e.getErrorCode() == 10008) {
				logger.info("Domain name \"" + domain + "\" is illegal.");
				return domain;
			} else {
				if (isRetry(e, token)) {
					return transDomainNameToUsername(domain);
				}
				throw new RuntimeException(
						String.format("WeiboException: %d\t%s\t%s", e.getErrorCode(), domain, token), e);
			}
		}
	}

	@Override
	public User transDomainNameToUser(String domain) {

		String token = tokenService.getRandomToken();
		try {
			return users.showUserByDomain(domain, token);
		} catch (WeiboException e) {
			if (e.getErrorCode() == 10008) {
				logger.info("Domain name \"" + domain + "\" is illegal.");
				if (JavaPattern.isAllNum(domain)) {
					return getSinaUserBaseInfo(domain);
				} else {
					return null;
				}
			} else {
				if (isRetry(e, token)) {
					return transDomainNameToUser(domain);
				}
				throw new RuntimeException(
						String.format("WeiboException: %d\t%s\t%s", e.getErrorCode(), domain, token), e);
			}
		}
	}

	/**
	 * 用户昵称转用户名
	 */
	@Override
	public String transNickNameToUsername(String nickName) {

		String token = tokenService.getRandomToken();
		User userInfo = null;
		try {
			userInfo = users.showUserByScreenName(nickName, token);
			return userInfo.getId();
		} catch (WeiboException e) {
			if (e.getErrorCode() == 10008) {
				logger.info("NickName name \"" + nickName + "\" is illegal.");
				return null;
			} else {
				if (isRetry(e, token)) {
					return transNickNameToUsername(nickName);
				}
				throw new RuntimeException(String.format("WeiboException: %d\t%s\t%s", e.getErrorCode(), nickName,
						token), e);
			}
		}
	}

	@Override
	public User transNickNameToUser(String nickName) {

		String token = tokenService.getRandomToken();
		try {
			return users.showUserByScreenName(nickName, token);
		} catch (WeiboException e) {
			if (e.getErrorCode() == 10008) {
				logger.info("NickName name \"" + nickName + "\" is illegal.");
				return null;
			} else {
				if (isRetry(e, token)) {
					return transNickNameToUser(nickName);
				}
				throw new RuntimeException(String.format("WeiboException: %d\t%s\t%s", e.getErrorCode(), nickName,
						token), e);
			}
		}
	}

	/**
	 * 获取@我的评论
	 */
	@Override
	public List<Comment> getCommentMentions(String uid, long time) {

		List<Comment> result = new ArrayList<>();
		int page = 1;
		boolean flag = true;
		while (flag) {
			CommentWapper temp = null;
			try {
				temp = getCommentMention(uid, page);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			if (page * 200 > temp.getTotalNumber() + 200) {
				flag = false;
			}
			for (int i = 0; i < temp.getComments().size(); i++) {
				if (temp.getComments().get(i).getCreatedAt().getTime() / 1000 < time - 86400) {
					flag = false;
					break;
				}
				result.add(temp.getComments().get(i));
			}
		}

		return result;
	}

	@Override
	public CommentWapper getCommentMention(String uid, int page) {

		String token = tokenService.getRandomToken();
		CommentWapper result = null;
		try {
			result = comments.getCommentMentions(new Paging(page++, 200), 0, 0, token);
		} catch (WeiboException e) {
			if (e.getErrorCode() == SinaErrorCode.ERROR_CODE_20003) {
				logger.info("User '" + uid + "' does not exists!");
				return null;
			} else {
				if (isRetry(e, token)) {
					return getCommentMention(uid, page);
				}
				throw new RuntimeException(String.format("WeiboException: %d\t%s\t%s", e.getErrorCode(), uid,
						token), e);
			}
		}

		return result;
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
		case SinaErrorCode.ERROR_CODE_10011:
			logger.info("RPC error.");
			return true;
		case SinaErrorCode.ERROR_CODE_EXPIRED_TOKEN:
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
