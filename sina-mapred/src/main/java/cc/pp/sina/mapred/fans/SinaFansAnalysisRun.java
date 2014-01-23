package cc.pp.sina.mapred.fans;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import net.sf.json.JSONArray;

import org.apache.hadoop.io.MapFile;
import org.apache.hadoop.io.MapFile.Reader;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import cc.pp.sina.domain.bozhus.BozhuAdd;
import cc.pp.sina.domain.mapred.SimpleBozhuInfoTwo;
import cc.pp.sina.mapred.baseinfo.SinaUserBaseInfoDao;
import cc.pp.sina.mapred.common.BozhukuHttpUtils;
import cc.pp.sina.mapred.sql.WeiboJDBC;

import com.sina.weibo.model.User;

public class SinaFansAnalysisRun implements Runnable {

	private final SinaUserBaseInfoDao sinaUserBaseinfoDao;
	private final MapFile.Reader reader;
	private final WritableComparable<?> key;
	private final Writable value;
	private final WeiboJDBC weiboJDBC;
	private final String uid;

	private static AtomicInteger count = new AtomicInteger(0);

	public SinaFansAnalysisRun(SinaUserBaseInfoDao sinaUserBaseinfoDao, Reader reader, WeiboJDBC weiboJDBC, String uid) {
		this.sinaUserBaseinfoDao = sinaUserBaseinfoDao;
		this.reader = reader;
		this.key = new Text();
		this.value = new Text();
		this.weiboJDBC = weiboJDBC;
		this.uid = uid;
	}

	@Override
	public void run() {

		System.out.println(count.addAndGet(1));

		try {
			User baseInfo = sinaUserBaseinfoDao.getUserBaseInfo(uid);
			if (baseInfo != null) {
				/**
				 * 添加博主基础信息或者更新
				 */
				BozhuAdd bozhuAdd = bozhuAddTrans(baseInfo);
				BozhukuHttpUtils.doBozhuInfoConnectionKeepAlive(BozhukuHttpUtils.getBozhuInfoUrl(), JSONArray
						.fromObject(bozhuAdd).get(0).toString(), "post");
				/**
				 * 更新统计信息
				 */
				float[] quality = fansQuality(new Text(uid));
				if (quality != null) {
					SimpleBozhuInfoTwo bozhuInfo = bozhuInfoTrans(baseInfo, quality[0], quality[1]);
					BozhukuHttpUtils.doBozhuInfoConnectionKeepAlive(BozhukuHttpUtils.getBozhuInfoUrl(uid, "sina"),
							JSONArray.fromObject(bozhuInfo).get(0).toString(), "put");
				}
			} else {
				BozhuAdd addBozhu1 = new BozhuAdd.Builder().setUsername(Long.parseLong(uid)).setNickname("nan")
						.setDescription("已删除用户").setIsppuser(0).setPtype("sina").setVerify(-1).build();
				BozhukuHttpUtils.doBozhuInfoConnectionKeepAlive(BozhukuHttpUtils.getBozhuInfoUrl(), JSONArray
						.fromObject(addBozhu1).get(0).toString(), "post");
			}
		} catch (NumberFormatException | IOException | SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 计算粉丝质量
	 * 分别对应：粉丝存在比例，真实用户比例
	 */
	public float[] fansQuality(WritableComparable<?> querykey) throws IOException, NumberFormatException, SQLException {

		int[] quality = new int[3];
		reader.get(querykey, value);
		if (value == null) {
			return null;
		}
		computeQuality(quality, value);
		while (reader.next(key, value)) {
			if (key.toString().equals(querykey.toString())) {
				computeQuality(quality, value);
			} else {
				break;
			}
		}
		int sum = quality[0] + quality[1] + quality[2];
		if (sum == 0) {
			return null;
		}

		/**
		 * 返回4位有效数字
		 */
		float[] result = new float[2];
		result[0] = (float) (quality[1] + quality[2]) / sum;
		result[1] = (float) quality[2] / sum;

		return result;
	}

	/**
	 * 计算粉丝质量
	 */
	private void computeQuality(int[] quality, Writable value) throws NumberFormatException, SQLException {

		String[] fans = value.toString().split(",");
		for (String uid : fans) {
			if (uid.length() > 1) {
				quality[weiboJDBC.sinaUserQualityInfo(Long.parseLong(uid))]++;
			}
		}
	}

	public BozhuAdd bozhuAddTrans(User baseInfo) {

		BozhuAdd addBozhu = new BozhuAdd.Builder().setUsername(Long.parseLong(baseInfo.getId()))
				.setNickname(baseInfo.getName()).setDescription(baseInfo.getDescription()).setIsppuser(0)
				.setPtype("sina").setVerify(baseInfo.getVerifiedType()).build();

		return addBozhu;
	}

	public SimpleBozhuInfoTwo bozhuInfoTrans(User baseInfo, float exsitFanRate, float actFanRate) {

		int act_fan = (int) (baseInfo.getFollowersCount() * actFanRate);
		SimpleBozhuInfoTwo simpleBozhuInfo = new SimpleBozhuInfoTwo.Builder().setWbnum(baseInfo.getStatusesCount())
				.setFannum(baseInfo.getFollowersCount()).setActfan(act_fan).setActFanRate(actFanRate)
				.setExsitFanRate(exsitFanRate).build();

		return simpleBozhuInfo;
	}
}
