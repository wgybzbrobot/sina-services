package cc.pp.sina.dao.timer.pictures;

import java.util.List;

import cc.pp.sina.domain.timer.library.PPTimerSelectParams;
import cc.pp.sina.domain.timer.library.TimerInsertParams;


/**
 * 获取皮皮定时器数据
 * @author wgybzb
 *
 */
public interface PPTimerDao {

	/**
	 * 获取某天定时器中的图片数据
	 */
	public List<String> getTimerPicInfos(PPTimerSelectParams ppTimerSelectParams);

	/**
	 * 插入统计结果数据
	 */
	public void insertTimerPicResult(TimerInsertParams timerInsertParams);

}
