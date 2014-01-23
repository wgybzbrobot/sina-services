package cc.pp.sina.dao.demo;

import java.util.List;

import cc.pp.sina.dao.weibos.SinaWeibos;
import cc.pp.sina.domain.weibos.WeiboInfo;
import cc.pp.sina.domain.weibos.WeiboInfoOld;
import cc.pp.sina.utils.json.JsonUtils;

import com.sina.weibo.model.WeiboException;

public class SinaWeibosDemo {

	public static void main(String[] args) throws WeiboException {

		String tablename = "sina_user_weibos_0";
		String tablenameOld = "sina_user_weibos_old";
		String tablenames = "sina_user_weibos_tablenames";

		int maxId = SinaWeibos.getMaxBid(tablename);
		System.out.println(maxId);

		//		Timeline timeline = new Timeline();
		//		Status weibo = timeline.showStatus("3368347375721684", "2.006zvWCCdcZIJC7dd8bc0eabChfjiC");
		//		System.out.println(weibo);
		//		SinaWeibos.insertWeiboInfo(tablename, weibo);

		List<WeiboInfo> weibos = SinaWeibos.getWeibosInfo(tablename, 1, 50);
		System.out.println(weibos.size());

		List<WeiboInfoOld> weibosOld = SinaWeibos.getWeibosInfoOld(tablenameOld, 1, 50);
		System.out.println(weibosOld.size());

		WeiboInfo weibo = SinaWeibos.getWeiboInfo(tablename, 3569473666031852L);
		System.out.println(JsonUtils.toJsonWithoutPretty(weibo));

		SinaWeibos.deleteWeiboInfo(tablename, 3569473666031852L);
		weibo = SinaWeibos.getWeiboInfo(tablename, 3569473666031852L);
		System.out.println(JsonUtils.toJsonWithoutPretty(weibo));

		List<String> tables = SinaWeibos.getTablenames(tablenames);
		System.out.println(tables.size());
		SinaWeibos.insertTablenames(tablenames, "test");
		tables = SinaWeibos.getTablenames(tablenames);
		System.out.println(tables.size());


	}

}
