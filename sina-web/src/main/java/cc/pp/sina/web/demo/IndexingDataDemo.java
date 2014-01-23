package cc.pp.sina.web.demo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.bozhus.sql.WeiboJDBC;
import cc.pp.sina.domain.bozhus.UserAllParamsDomain;
import cc.pp.sina.query.index.BozhuIndex;

public class IndexingDataDemo {

	private static Logger logger = LoggerFactory.getLogger(IndexingDataDemo.class);

	/**
	 * 主函数
	 */
	public static void main(String[] args) throws SQLException, IOException {

		/**
		 * 从数据库中读数据
		 */
		WeiboJDBC weiboJDBC = new WeiboJDBC("127.0.0.1", "root", "root", "pp_fenxi");
		if (!weiboJDBC.dbConnection()) {
			logger.info("Db connection error.");
			return;
		}
		List<UserAllParamsDomain> bozhus = weiboJDBC.getSinaBozhuAllParams("sina_bozhus_library", 1, 10_0000);
		weiboJDBC.dbClose();

		BozhuIndex bozhuIndex = new BozhuIndex(BozhuIndex.INDEX_DIR);

		// 写入索引文件
		for (int i = 0; i < bozhus.size(); i++) {
			System.out.println(i);
			bozhuIndex.addIndexData(bozhus.get(i));
		}

		bozhuIndex.close();

	}

}
