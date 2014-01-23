package cc.pp.sina.web.company;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.dao.company.PPCompany;
import cc.pp.sina.domain.sql.UserBaseInfo;
import cc.pp.sina.query.index.PPCompanyIndex;

/**
 * 索引皮皮企业用户的数据
 * @author wgybzb
 *
 */
public class PPCompanyIndexMain {

	private static Logger logger = LoggerFactory.getLogger(PPCompanyIndexMain.class);

	private static final String[] tables = { "pp_sina_company_baseinfo_201308", "pp_sina_company_baseinfo_201312",
			"pp_sina_company_add_201312", "pp_sina_company_leave_201312" };
	private static final String[] yearmonths = { "201308", "201312", "201312", "201312" };
	private static final String[] types = { "all", "all", "add", "leave" };

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			System.err.println("Usage: <indexDir>");
			System.exit(-1);
		}
		String indexDir = args[0];

		PPCompanyIndex ppCompanyIndex = null;
		try {
			ppCompanyIndex = new PPCompanyIndex(indexDir);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		if (ppCompanyIndex != null) {
			List<UserBaseInfo> ppCompanyInfos = null;
			for (int i = 0;i < 4; i++) {
				logger.info("Start indexing '" + tables[i] + "'.");
				ppCompanyInfos = PPCompany.getAllCompanyInfo(tables[i]);
				int count = 0;
				for (UserBaseInfo user : ppCompanyInfos) {
					if (count++ % 1000 == 0) {
						logger.info("Indexing at: " + count);
					}
					try {
						ppCompanyIndex.addIndexData(user, yearmonths[i], types[i]);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
				logger.info("Indexing '" + tables[i] + "' data successful.");
			}
		}

		ppCompanyIndex.close();

	}

}
