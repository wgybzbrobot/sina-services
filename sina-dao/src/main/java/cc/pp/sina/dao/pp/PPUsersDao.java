package cc.pp.sina.dao.pp;

import java.util.List;

/**
 * 皮皮用户接口
 * @author wgybzb
 *
 */
public interface PPUsersDao {

	/**
	 * 获取所有皮皮用户Uid
	 */
	public List<Long> getPPUids();

	/**
	 * 获取当前皮皮用户Uid
	 */
	public List<Long> getPPUidsNow(long lasttime);

}
