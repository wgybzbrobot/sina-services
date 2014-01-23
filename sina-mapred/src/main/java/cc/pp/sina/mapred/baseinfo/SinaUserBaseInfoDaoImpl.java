package cc.pp.sina.mapred.baseinfo;

import java.util.List;
import java.util.Random;

import com.sina.weibo.api.Users;
import com.sina.weibo.model.User;
import com.sina.weibo.model.WeiboException;

public class SinaUserBaseInfoDaoImpl implements SinaUserBaseInfoDao {

	private static final Random RANDOM = new Random();
	private final List<String> tokens;
	private final Users user;

	public SinaUserBaseInfoDaoImpl(List<String> tokens) {
		this.tokens = tokens;
		user = new Users();
	}

	@Override
	public User getUserBaseInfo(String uid) {

		User userInfo = null;
		try {
			userInfo = user.showUserById(uid, getRandomToken());
		} catch (WeiboException e) {
			//			e.printStackTrace();
		}

		return userInfo;
	}

	private String getRandomToken() {
		return tokens.get(RANDOM.nextInt(tokens.size()));
	}

}
