package cc.pp.sina.bozhus.single.weibo;

import java.util.HashMap;
import java.util.List;

import cc.pp.sina.algorithms.top.sort.InsertSort;
import cc.pp.sina.bozhus.constant.SourceType;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDao;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDaoImpl;
import cc.pp.sina.bozhus.t2.T2Utils;
import cc.pp.sina.dao.common.MybatisConfig;
import cc.pp.sina.dao.t2.T2SingleWeibo;
import cc.pp.sina.domain.error.ErrorResponse;
import cc.pp.sina.tokens.service.TokenService;
import cc.pp.sina.utils.json.JsonUtils;

import com.sina.weibo.model.Status;
import com.sina.weibo.model.StatusWapper;

public class SimpleWeiboAnalysis {

	//	private static Logger logger = LoggerFactory.getLogger(SimpleWeiboAnalysis.class);

	private final SinaWeiboInfoDao sinaWeiboInfoDao;
	private final T2SingleWeibo singleWeibo = new T2SingleWeibo(MybatisConfig.ServerEnum.fenxi);

	public SimpleWeiboAnalysis(SinaWeiboInfoDao sinaWeiboInfoDao) {
		this.sinaWeiboInfoDao = sinaWeiboInfoDao;
	}

	/**
	 * 测试函数
	 */
	public static void main(String[] args) {

		SinaWeiboInfoDao sinaWeiboInfoDao = new SinaWeiboInfoDaoImpl(new TokenService());
		SimpleWeiboAnalysis simpleWeiboAnalysis = new SimpleWeiboAnalysis(sinaWeiboInfoDao);
		simpleWeiboAnalysis.insertSingleWeiboResult();
	}

	/**
	 * 分析所有待分析微博
	 */
	public void insertSingleWeiboResult() {
		List<String> wids = singleWeibo.getNewWids("sina");
		for (String wid : wids) {
			SimpleWeiboResult result = singleWeiboAnalysis(wid);
			if (result != null) {
				singleWeibo.updateSingleWeibo("sina", Long.parseLong(wid), "", JsonUtils.toJsonWithoutPretty(result));
			} else {
				singleWeibo.updateSingleWeibo("sina", Long.parseLong(wid), "", JsonUtils
						.toJsonWithoutPretty(new ErrorResponse.Builder(20003, "wid has none reposters.").build()));
			}
		}
	}

	/**
	 * 分析函数
	 */
	public SimpleWeiboResult singleWeiboAnalysis(String wid) {

		SimpleWeiboResult result = new SimpleWeiboResult();
		/***********结果变量初始化***********/
		int exposionsum = 0; // 1、总曝光量
		int[] emotions = new int[3]; // 4、情感值
		String[] keyusersbyreps = new String[50]; // 5、关键转发用户，按转发量
		SWeiboUtils.initStrArray(keyusersbyreps);
		String[] keyusersbyfans = new String[50]; // 5、关键账号,按粉丝量
		SWeiboUtils.initStrArray(keyusersbyfans);
		int[] reposttimelineby24H = new int[24]; // 6、24小时内的转发分布
		HashMap<String, Integer> reposttimelinebyDay = new HashMap<String, Integer>(); //3、转发时间线--按天
		int[] gender = new int[2]; // 7、性别分析
		int[] location = new int[101]; // 8、区域分布
		int[] wbsource = new int[6]; // 9、终端分布
		int[] reposterquality = new int[2]; // 10、转发用户质量
		int[] verifiedtype = new int[14]; //认证分布
		HashMap<String, Integer> suminclass = new HashMap<String, Integer>(); // 2、层级分析 //@个数确定
		String str = ""; // str为待分词的所有微博内容
		// 总体评价
		// 中间变量初始化
		int existwb = 0, wbsum = 0, repostercount, cursor = 1;
		String head, name, sex, hour, text, source, date;
		int city, fanssum, weibosum, verifytype;
		boolean addv;
		long reposttime;
		// 获取该微博数据
		Status wbinfo = sinaWeiboInfoDao.getSingleWeiboDetail(wid);
		// 存放该原创用户信息
		try {
			SWeiboUtils.originalUserArrange(result, wbinfo);
		} catch (RuntimeException e) {
			return null;
		}
		// 存放该微博内容信息
		result.setWbcontent(wbinfo.getText().replaceAll("\"", "\\\""));
		// 存放转发和评论数据
		result.setRepostcount(Integer.toString(wbinfo.getRepostsCount())); // 2、总转发量
		result.setCommentcount(Integer.toString(wbinfo.getCommentsCount())); // 3、总评论量

		// 采集微博转发数据
		StatusWapper status = sinaWeiboInfoDao.getSinaSingleWeiboResposts(wid, cursor);
		wbsum = (int) status.getTotalNumber();
		if (wbinfo.getUser() == null) { //转发微博0时
			return null;
		} else {
			exposionsum += wbinfo.getUser().getFollowersCount();
		}
		/************开始循环计算***************/
		while ((cursor * 200 < wbsum + 200) && (cursor < 11))
		{
			if (status == null) {
				break;
			}
			cursor++;
			for(Status reposter : status.getStatuses())
			{
				if (reposter.getId() == null) {
					continue;
				} else {
					existwb++;
				}
				name = reposter.getUser().getName();
				sex = reposter.getUser().getGender();
				city = reposter.getUser().getProvince();
				fanssum = reposter.getUser().getFollowersCount();
				head = reposter.getUser().getProfileImageUrl();
				repostercount = reposter.getRepostsCount();
				weibosum = reposter.getUser().getStatusesCount();
				addv = reposter.getUser().isVerified();
				reposttime = reposter.getCreatedAt().getTime()/1000;
				hour = SWeiboUtils.getHour(reposttime);
				text = reposter.getText();
				str += text;
				source = reposter.getSource().getName();
				verifytype = reposter.getUser().getVerifiedType();
				date = SWeiboUtils.getDate(reposttime);
				/****************5、关键账号****************/
				keyusersbyreps = InsertSort.toptable(keyusersbyreps, reposter.getUser().getId() + "," + name + ","
						+ head + "," + fanssum + "," + verifytype + "," + reposttime + "=" + repostercount);
				keyusersbyfans = InsertSort.toptable(keyusersbyreps, reposter.getUser().getId() + "," + name + ","
						+ head + "," + fanssum + "," + verifytype + "," + reposttime + "=" + fanssum);
				/************6、转发时间线（24小时）************/
				reposttimelineby24H[Integer.parseInt(hour)]++;
				/************3、转发时间线（按天）**************/
				SWeiboUtils.putRepostByDay(reposttimelinebyDay, date);
				/***************7、性别分布*****************/
				gender[SWeiboUtils.checkSex(sex)]++;
				/***************8、区域分布*****************/
				location[SWeiboUtils.checkCity(city)]++;
				/***************9、终端分布*****************/
				wbsource[SourceType.getCategory(source)]++;
				/***************10、水军比例****************/
				reposterquality[SWeiboUtils.checkQuality(addv, fanssum, weibosum)]++;
				/***************1、总曝光量**************/
				exposionsum += fanssum;
				/***************4、情感值***************/
				if (cursor < 2) {
					emotions[T2Utils.getEmotions(text)]++;
				}
				/***************认证分布*****************/
				verifiedtype[SWeiboUtils.checkVerifyType(verifytype)]++;
				/***************层级分析*****************/
				SWeiboUtils.putSumInClass(suminclass, text);
			}
				status = sinaWeiboInfoDao.getSinaSingleWeiboResposts(wid, cursor);
		}
		if (existwb == 0) {
			return null;
		}
		/******************整理数据结果*******************/
		/************3、转发时间线（24小时）************/
		SWeiboUtils.reposttime24HArrange(result, reposttimelineby24H, existwb);
		/************3、转发时间线（按天）**************/
		result.setReposttimelinebyDay(reposttimelinebyDay);
		/***************4、性别分布*****************/
		SWeiboUtils.genderArrange(result, gender, existwb);
		/***************6、区域分布*****************/
		SWeiboUtils.locationArrange(result, location, existwb);
		/***************11、水军比例****************/
		SWeiboUtils.qualityArrange(result, reposterquality, existwb);
		/***************12、总曝光量**************/
		long allexposion = (long) exposionsum * wbinfo.getRepostsCount() / existwb;
		result.setExposionsum(Long.toString(allexposion));
		/****************15、关键账号****************/
		SWeiboUtils.repskeyusersArrange(result, keyusersbyreps);
		SWeiboUtils.fanskeyusersArrange(result, keyusersbyfans);
		/****************17、情感值*****************/
		SWeiboUtils.emotionArrange(result, emotions);
		/********************微博来源分布**********************/
		SWeiboUtils.sourceArrange(result, wbsource, existwb);
		/***************7、认证分布*****************/
		SWeiboUtils.verifyArrange(result, verifiedtype, existwb);
		/*****************层级分析*****************/
		result.setSuminclass(suminclass);
		/***************关键词提取*****************/
		SWeiboUtils.keywordsArrange(result, str);
		/***************总体评价*****************/
		SWeiboUtils.lastCommentArrange(result, allexposion, emotions, gender, location, reposterquality);

		return result;
	}

}
