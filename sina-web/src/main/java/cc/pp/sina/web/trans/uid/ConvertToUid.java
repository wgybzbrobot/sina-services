package cc.pp.sina.web.trans.uid;

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

	//	private static Logger logger = LoggerFactory.getLogger(ConvertToUid.class);

	private final TransToUid transToUid;

	public ConvertToUid() {
		//		TokenService tokenService = new TokenService("127.0.0.1", "root", "root", "pp_fenxi");
		TokenService tokenService = new TokenService();
		try {
			transToUid = new TransToUid(new SinaUserInfoDaoImpl(tokenService));
		} catch (Exception e) {
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
