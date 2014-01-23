package cc.pp.sina.dao.company;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import cc.pp.sina.dao.common.MybatisConfig;
import cc.pp.sina.domain.sql.AllCompanyParams;
import cc.pp.sina.domain.sql.CompanySelectParams;
import cc.pp.sina.domain.sql.UserBaseInfo;

public class PPCompany {

	//	private static Logger logger = LoggerFactory.getLogger(PPCompany.class);

	private static final String PP_SINA_COMPANY = "pp_sina_company_baseinfo_";
	private static final String PP_SINA_COMPANY_ADD = "pp_sina_company_add_";
	private static final String PP_SINA_COMPANY_LEAVE = "pp_sina_company_leave_";

	private static SqlSessionFactory sqlSessionFactory;

	static {
		sqlSessionFactory = MybatisConfig.getSqlSessionFactory(MybatisConfig.ServerEnum.tongling);
	}

	/**
	 * 获取某个月全部的企业用户基础信息
	 */
	public static List<UserBaseInfo> getAllCompanyInfo(String tablename) {
		AllCompanyParams allCompanyParams = new AllCompanyParams(tablename);
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			PPCompanyDao ppCompanyDao = sqlSession.getMapper(PPCompanyDao.class);
			return ppCompanyDao.getAllCompanyInfos(allCompanyParams);
		}
	}

	/**
	 * 获取某个月企业用户基础信息
	 */
	public static UserBaseInfo getCompanyInfo(String yearMonth, long username) {
		return getCompanyInfo(new CompanySelectParams(PP_SINA_COMPANY + yearMonth, username));
	}

	/**
	 * 获取某个月增加的企业用户基础信息
	 */
	public static UserBaseInfo getAddCompanyInfo(String yearMonth, long username) {
		return getCompanyInfo(new CompanySelectParams(PP_SINA_COMPANY_ADD + yearMonth, username));
	}

	/**
	 * 获取某个月减少的企业用户基础信息
	 */
	public static UserBaseInfo getLeaveCompanyInfo(String yearMonth, long username) {
		return getCompanyInfo(new CompanySelectParams(PP_SINA_COMPANY_LEAVE + yearMonth, username));
	}

	public static UserBaseInfo getCompanyInfo(CompanySelectParams companySelectParams) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			PPCompanyDao ppCompanyDao = sqlSession.getMapper(PPCompanyDao.class);
			return ppCompanyDao.getCompanyInfo(companySelectParams);
		}
	}

}
