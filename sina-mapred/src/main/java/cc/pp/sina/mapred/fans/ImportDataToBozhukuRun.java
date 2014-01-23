package cc.pp.sina.mapred.fans;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import net.sf.json.JSONArray;

import org.apache.commons.httpclient.HttpException;

import cc.pp.sina.domain.bozhus.BozhuAdd;
import cc.pp.sina.domain.mapred.SimpleBozhuInfoOne;
import cc.pp.sina.mapred.baseinfo.SinaUserBaseInfoDao;
import cc.pp.sina.mapred.common.BozhukuHttpUtils;

import com.sina.weibo.model.User;

public class ImportDataToBozhukuRun implements Runnable {

	private final String username;
	private final SimpleBozhuInfoOne bozhuInfo;
	private final SinaUserBaseInfoDao sinaUserBaseinfoDao;

	public ImportDataToBozhukuRun(String username, SimpleBozhuInfoOne bozhuInfo,
			SinaUserBaseInfoDao sinaUserBaseinfoDao) {
		this.username = username;
		this.bozhuInfo = bozhuInfo;
		this.sinaUserBaseinfoDao = sinaUserBaseinfoDao;
	}

	private static AtomicInteger count = new AtomicInteger(0);

	@Override
	public void run() {

		System.out.println(count.addAndGet(1));

		try {
			User baseInfo = sinaUserBaseinfoDao.getUserBaseInfo(username);
			if (baseInfo != null) {
				/**
				 * 添加博主基础信息或者更新
				 */
				BozhuAdd bozhuAdd = new BozhuAdd.Builder().setUsername(Long.parseLong(username))
						.setNickname(baseInfo.getName()).setDescription(baseInfo.getDescription()).setPtype("sina")
						.setIsppuser(0).setVerify(baseInfo.getVerifiedType()).build();
				BozhukuHttpUtils.doBozhuInfoConnectionKeepAlive(BozhukuHttpUtils.getBozhuInfoUrl(), JSONArray
						.fromObject(bozhuAdd).get(0).toString(), "post");
				/**
				 * 更新统计信息
				 */
				BozhukuHttpUtils.doBozhuInfoConnectionKeepAlive(BozhukuHttpUtils.getBozhuInfoUrl(username, "sina"),
						JSONArray.fromObject(bozhuInfo).get(0).toString(), "put");
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
