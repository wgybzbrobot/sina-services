package cc.pp.sina.bozhus.t2;

import java.util.List;

import cc.pp.sina.bozhus.info.SinaUserInfoDao;
import cc.pp.sina.bozhus.info.SinaUserInfoDaoImpl;
import cc.pp.sina.tokens.service.TokenService;

import com.sina.weibo.model.Comment;

/**
 * 用户@数据分析
 * @author wgybzb
 *
 */
public class AtInfoAnalysis {

	//	private static Logger logger = LoggerFactory.getLogger(AtInfoAnalysis.class);

	private final SinaUserInfoDao sinaUserInfoDao;

	public AtInfoAnalysis(SinaUserInfoDao sinaUserInfoDao) {
		this.sinaUserInfoDao = sinaUserInfoDao;
	}

	/**
	 * 测试函数
	 */
	public static void main(String[] args) {

		System.out.println("ok1");
		TokenService tokenService = new TokenService();
		System.out.println("ok2");
		AtInfoAnalysis atInfoAnalysis = new AtInfoAnalysis(new SinaUserInfoDaoImpl(tokenService));
		System.out.println("ok3");
		int[] result = atInfoAnalysis.userAtInfoAnalysis("2004568612");
		System.out.println("ok4");
		for (int r : result) {
			System.out.println(r);
		}
		//		tokenService.stop();

	}

	/**
	 * result[0]----负面情感数
	 * result[1]----正面情感数
	 * result[2]----中性情感数
	 * result[3]----@数量
	 */
	public int[] userAtInfoAnalysis(String uid) {

		int[] result = new int[4];
		List<Comment> comments = sinaUserInfoDao.getCommentMentions(uid, System.currentTimeMillis() / 1000);
		for (Comment commentInfo : comments) {
			result[T2Utils.getEmotions(commentInfo.getText())]++;
		}
		result[3] = comments.size();

		return result;
	}

}
