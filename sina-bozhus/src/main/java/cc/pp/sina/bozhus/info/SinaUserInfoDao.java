package cc.pp.sina.bozhus.info;

import java.util.List;

import cc.pp.sina.domain.bozhus.UserTag;
import cc.pp.sina.domain.bozhus.UserTagWapper;

import com.sina.weibo.model.User;
import com.sina.weibo.model.UserWapper;

public interface SinaUserInfoDao {

	/**
	 * 用户基础信息
	 */
	public User getSinaUserBaseInfo(String uid);

	/**
	 * 用户粉丝数据
	 */
	public UserWapper getSinaUserFans(String uid, int cursor);

	/**
	 * 用户粉丝Uid列表，一次性返回，最多5000
	 */
	public String[] getSinaUserFansIds(String uid);

	/**
	 * 用户标签数据
	 */
	public List<UserTag> getSinaUserTags(String uid, int maxTags);

	/**
	 * 批量获取用户标签数据
	 */
	public List<UserTagWapper> getSinaUsersBatchTags(String uids, int maxTags);

	/**
	 * 用户域名转用户名
	 */
	public String transDomainNameToUsername(String domain);

	public User transDomainNameToUser(String domain);

	/**
	 * 用户昵称转用户名
	 */
	public String transNickNameToUsername(String nickName);

	public User transNickNameToUser(String nickName);

}
