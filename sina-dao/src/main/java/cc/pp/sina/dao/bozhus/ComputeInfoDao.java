package cc.pp.sina.dao.bozhus;

/**
 * 计算的结果数据接口，包括粉丝分析、单条微博分析、微博分析等
 * @author wgybzb
 *
 */
public interface ComputeInfoDao {

	/**
	 * 获取粉丝分析结果数据
	 */
	public String getFansAnalysisResult(long uid);

}
