package cc.pp.sina.bozhus.trans.uid;

import cc.pp.sina.bozhus.info.SinaUserInfoDao;

import com.sina.weibo.model.User;

public class TransToUid {

	private final SinaUserInfoDao sinaUserInfoDao;

	public TransToUid(SinaUserInfoDao sinaUserInfoDao) throws Exception {
		this.sinaUserInfoDao = sinaUserInfoDao;
	}

	/**
	 * 将Domain转换成Uid
	 */
	public String transDidToUid(String url) {
		return sinaUserInfoDao.transDomainNameToUsername(retriveName(url));
	}

	public User transDidToUser(String url) {
		return sinaUserInfoDao.transDomainNameToUser(retriveName(url));
	}

	/**
	 * 将Nickname转换成Uid
	 */
	public String transNidToUid(String url) {
		return sinaUserInfoDao.transNickNameToUsername(retriveName(url));
	}

	public User transNidToUser(String url) {
		return sinaUserInfoDao.transNickNameToUser(retriveName(url));
	}

	private String retriveName(String url) {
		String name = url.trim();
		if (name.indexOf("?") > 0) {
			name = name.substring(0, name.indexOf("?"));
		}
		if (name.lastIndexOf("/") > 0) {
			name = name.substring(name.lastIndexOf("/") + 1);
		}
		return name;
	}

}
