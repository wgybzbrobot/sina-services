package cc.pp.sina.web.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.bozhus.info.SinaUserInfoDaoImpl;
import cc.pp.sina.bozhus.trans.uid.TransToUid;
import cc.pp.sina.tokens.service.TokenService;

import com.sina.weibo.model.User;

/**
 * 用户域名转换成用户模块
 *
 * @author wgybzb
 */
public class ConvertToUid {

	private static Logger logger = LoggerFactory.getLogger(ConvertToUid.class);

	private final TransToUid transToUid;

	public ConvertToUid() {
		try {
			transToUid = new TransToUid(new SinaUserInfoDaoImpl(new TokenService()));
		} catch (Exception e) {
			logger.error("Exception: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public String convertDomainToUid(String url) {
		return transToUid.transDidToUid(url.trim());
	}

	public User convertDomainToUser(String url) {
		return transToUid.transDidToUser(url.trim());
	}

	public String convertNickToUid(String url) {
		return transToUid.transNidToUid(url.trim());
	}

	public User convertNickToUser(String url) {
		return transToUid.transNidToUser(url.trim());
	}

}
