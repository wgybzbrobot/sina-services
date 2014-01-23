package cc.pp.sina.bozhus.fans;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.algorithms.uids.repeat.RetriveUnrepeats;
import cc.pp.sina.bozhus.info.SinaUserInfoDao;
import cc.pp.sina.bozhus.sql.WeiboJDBC;
import cc.pp.sina.domain.bozhus.FansAddDaily;
import cc.pp.sina.domain.bozhus.PPUserFansInfo;
import cc.pp.sina.domain.users.SinaUserInfo;
import cc.pp.sina.utils.time.TimeUtils;

import com.sina.weibo.model.User;

/**
 * 更新皮皮用户的粉丝数据
 * @author wgybzb
 *
 */
public class PPUserFansUpdateRun implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(PPUserFansUpdateRun.class);

	private static final String PP_SINA_FANS = "pp_sina_fans";

	public static final String PP_SINA_FANS_DAILY = "pp_sina_fans_daily_";
	private static final String SINA_USER_BASEINFO = "sinauserbaseinfo_";

	private final WeiboJDBC weiboJDBC;
	private final String uid;
	private final SinaUserInfoDao sinaUserInfoDao;

	public PPUserFansUpdateRun(WeiboJDBC weiboJDBC, String uid, SinaUserInfoDao sinaUserInfoDao) {
		this.weiboJDBC = weiboJDBC;
		this.uid = uid;
		this.sinaUserInfoDao = sinaUserInfoDao;
	}

	private static AtomicInteger count = new AtomicInteger(0);

	@Override
	public void run() {

		System.out.println(count.addAndGet(1));

		try {
			/**
			 * 获取新粉丝数据
			 */
			String[] newFans = sinaUserInfoDao.getSinaUserFansIds(uid);
			// 没有获取到用户粉丝
			if (newFans == null) {
				logger.info("User: " + uid + "'s fans is not existed.");
				return;
			}
			/**
			 * 获取用户信息，先从我们的数据库中获取，没有的话再从线上调取。
			 */
			SinaUserInfo sinaUserInfo = weiboJDBC.getSinaUserBaseInfo(SINA_USER_BASEINFO + Long.parseLong(uid) % 32,
					uid);
			if (sinaUserInfo == null) {
				// 如果不存在
				User userInfo = sinaUserInfoDao.getSinaUserBaseInfo(uid);
				if (userInfo == null) {
					logger.info("User '" + uid + "' does not exists!");
					return;
				}
				sinaUserInfo = userTrans(userInfo);
				// 插入基础信息
				try {
					weiboJDBC.inserSinaUserBaseinfo(SINA_USER_BASEINFO + Long.parseLong(uid) % 32, userInfo, true);
				} catch (SQLException | UnsupportedEncodingException e) {
					logger.info("User: " + userInfo.getId() + "'s description is non-utf-8 encoded.");
					try {
						weiboJDBC.inserSinaUserBaseinfo(SINA_USER_BASEINFO + Long.parseLong(uid) % 32, userInfo, false);
					} catch (UnsupportedEncodingException | SQLException e1) {
						logger.info("User: " + userInfo.getId() + "'s description is UnsupportedEncodingException.");
					}
				}
			}
			/**
			 * 获取老粉丝数据
			 */
			PPUserFansInfo userFans = weiboJDBC.getPPSinaUserFans(PP_SINA_FANS, uid);
			/**
			 * 判断用户是否存在
			 */
			if (userFans == null) {
				// 如果不存在，插入数据
				weiboJDBC.insertPPSinaUserFans(PP_SINA_FANS, new PPUserFansInfo.Builder(Long.parseLong(uid),
						strArrToStr(newFans), sinaUserInfo.getFollowers_count()).build());
			} else {
				// 如果存在，更新数据
				/**
				 * 计算粉丝增量、合并粉丝
				 */
				String addFans = strArrToStr(newFans);
				String contactFans = strArrToStr(newFans);
				// 如果库里面的粉丝uid不为空
				if (userFans.getFansuids().length() > 5) {
					addFans = RetriveUnrepeats.unRepeatUids(userFans.getFansuids(), newFans);
					contactFans = RetriveUnrepeats.contactUids(userFans.getFansuids(), newFans);
				}
				/**
				 * 更新粉丝数据
				 */
				weiboJDBC.updatePPSinaUserFans(PP_SINA_FANS, new PPUserFansInfo.Builder(Long.parseLong(uid),
						contactFans, sinaUserInfo.getFollowers_count()).build());
				/**
				 * 插入添加粉丝数据
				 */
				weiboJDBC.insertPPUserFansDaily(
						PP_SINA_FANS_DAILY + TimeUtils.getTodayDaily(),
						new FansAddDaily.Builder(uid).setAddfansuids(addFans)
								.setAddfanscount(addFans.split(",").length)
								.setAllfanscount(sinaUserInfo.getFollowers_count()).build());
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public static String strArrToStr(String[] arr) {
		StringBuffer result = new StringBuffer();
		for (String str : arr) {
			result.append(str).append(",");
		}
		return result.substring(0, result.length() - 1);
	}

	public static SinaUserInfo userTrans(User user) {
		return new SinaUserInfo.Builder(Long.parseLong(user.getId())).setScreen_name(user.getScreenName())
				.setProvince(user.getProvince())
				.setCity(user.getCity()).setLocation(user.getLocation()).setDescription(user.getDescription())
				.setUrl(user.getUrl()).setProfile_image_url(user.getProfileImageUrl()).setDomain(user.getUserDomain())
				.setGender(user.getGender()).setFollowers_count(user.getFollowersCount())
				.setFriends_count(user.getFriendsCount()).setStatuses_count(user.getStatusesCount())
				.setFavourites_count(user.getFavouritesCount()).setCreated_at(user.getCreatedAt().getTime() / 1000)
				.setVerified(user.isVerified()).setVerified_type(user.getVerifiedType())
				.setAvatar_large(user.getAvatarLarge()).setBi_followers_count(user.getBiFollowersCount())
				.setRemark(user.getRemark()).setVerified_reason(user.getVerifiedReason()).setWeihao(user.getWeihao())
				.build();
	}

}
