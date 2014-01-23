package cc.pp.sina.dao.bozhus;

import java.util.List;

/**
 * 博主库数据接口
 * @author wgybzb
 *
 */
public interface BozhuLibraryDao {

	/**
	 * 获取博主库中所有博主Uid
	 */
	public List<Long> getBozhuUids();

}
