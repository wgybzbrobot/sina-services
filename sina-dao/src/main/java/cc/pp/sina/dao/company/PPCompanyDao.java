package cc.pp.sina.dao.company;

import java.util.List;

import cc.pp.sina.domain.sql.AllCompanyParams;
import cc.pp.sina.domain.sql.CompanySelectParams;
import cc.pp.sina.domain.sql.UserBaseInfo;

/**
 * 企业用户信息获取接口
 * @author wgybzb
 *
 */
public interface PPCompanyDao {

	/**
	 * 获取单个企业用户信息
	 */
	public UserBaseInfo getCompanyInfo(CompanySelectParams companySelectParams);

	/**
	 * 获取全部企业用户信息
	 */
	public List<UserBaseInfo> getAllCompanyInfos(AllCompanyParams allCompanyParams);

}
