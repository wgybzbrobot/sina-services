package cc.pp.sina.dao.timer.pictures;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import cc.pp.sina.dao.common.MybatisConfig;
import cc.pp.sina.domain.timer.library.PPTimerSelectParams;
import cc.pp.sina.domain.timer.library.TimerInsertParams;

public class PPTimer {

	/**
	 * 获取某天定时器中的图片数据
	 */
	public static List<String> getTimerPicInfos(String tablename, long starttime, long endtime) {
		PPTimerSelectParams ppTimerSelectParams = new PPTimerSelectParams(tablename, starttime, endtime);
		try (SqlSession sqlSession = MybatisConfig.getSqlSessionFactory(MybatisConfig.ServerEnum.read)
				.openSession();) {
			PPTimerDao ppTimerDao = sqlSession.getMapper(PPTimerDao.class);
			return ppTimerDao.getTimerPicInfos(ppTimerSelectParams);
		}
	}

	/**
	 * 插入统计结果数据
	 */
	public static void insertTimerPicResult(String tablename, String date, String name, int count) {
		TimerInsertParams timerInsertParams = new TimerInsertParams(tablename, date, name, count);
		try (SqlSession sqlSession = MybatisConfig.getSqlSessionFactory(MybatisConfig.ServerEnum.write)
				.openSession();) {
			PPTimerDao ppTimerDao = sqlSession.getMapper(PPTimerDao.class);
			ppTimerDao.insertTimerPicResult(timerInsertParams);
		}
	}

}
