package cc.pp.sina.domain.content.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 内容库每天使用的统计数据
 * @author wgybzb
 *
 */
public class ContentResultDaily {

	private String type; // 平台类型
	private String recordType; // 纪录类型
	private String date; // 0、统计日期
	private float errorRatio; // 1、内容库错误记录率
	private int contentCount; // 2、每天内容总数
	private float powerRatio; // 3、每天来自皮皮动力的比率
	private float fromContentRatio; // 4、每天来自内容库内容的比率
	private HashMap<Integer, String> fromContentRatioByHour = new HashMap<>(); // 5、每天每小时内容库内容的使用比率
	private int userCount; // 6、每天使用内容库的用户数
	private HashMap<Integer, Integer> userByHour = new HashMap<>(); // 7、每天每小时的真实用户数
	private HashMap<String, String> userQuality = new HashMap<>(); // 8、每天使用用户的质量
	private HashMap<Integer, String> userQualityByHour = new HashMap<>(); // 9、每天每小时的真实用户比例
	private HashMap<String, String> contentUsedQuality = new HashMap<>(); // 10、每天内容库被使用的质量(real,mask分别使用的内容量比率)
	private HashMap<Integer, String> realContentUsedByHour = new HashMap<>(); // 11、每天每小时真实用户使用内容库的比例
	private HashMap<String, Integer> columnUsedCount = new HashMap<>(); // 12、每天每个栏目的使用数量
	private List<HashMap<String, String>> columnUsedSort = new ArrayList<>(); // 13、每天各栏目的使用量排序
	private List<List<HashMap<String, String>>> columnTop10ByHour = new ArrayList<>(); // 14、每天每小时使用量排名前10的栏目
	private List<HashMap<String, String>> contentTop100 = new ArrayList<>(); // 15、每天内容库使用量前100的内容
	private HashMap<String, String> picVideoMusicRatio = new HashMap<>(); // 16、每天所有内容中分别含图片、视频、音乐的比例
	private List<HashMap<String, String>> top10CloumnByPicUsed = new ArrayList<>(); // 17、每天使用的所有图片排名前10的类型/栏目
	private List<HashMap<String, String>> top100Ips = new ArrayList<>(); // 18、每天内容库使用量前100的IP
	private List<HashMap<String, String>> top10IpSection = new ArrayList<>(); // 19、排名前20的IP段
	private HashMap<String, String> source = new HashMap<>(); // 20、自定义来源分布
	private HashMap<String, String> sendStatus = new HashMap<>(); // 21、发送状态情况
	private HashMap<String, Integer> allKeywords = new HashMap<>(); // 22、所有关键词(按词频)
	private HashMap<String, Integer> keywordsOfTopNContent = new HashMap<>(); // 23、top100内容中的关键词

	private HashMap<String, ColumnUsedDetailed> columnUsedDetailed = new HashMap<>(); // 栏目使用详细数据

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public float getErrorRatio() {
		return errorRatio;
	}

	public void setErrorRatio(float errorRatio) {
		this.errorRatio = errorRatio;
	}

	public int getContentCount() {
		return contentCount;
	}

	public void setContentCount(int contentCount) {
		this.contentCount = contentCount;
	}

	public float getPowerRatio() {
		return powerRatio;
	}

	public void setPowerRatio(float powerRatio) {
		this.powerRatio = powerRatio;
	}

	public float getFromContentRatio() {
		return fromContentRatio;
	}

	public void setFromContentRatio(float fromContentRatio) {
		this.fromContentRatio = fromContentRatio;
	}

	public HashMap<Integer, String> getFromContentRatioByHour() {
		return fromContentRatioByHour;
	}

	public void setFromContentRatioByHour(HashMap<Integer, String> fromContentRatioByHour) {
		this.fromContentRatioByHour = fromContentRatioByHour;
	}

	public int getUserCount() {
		return userCount;
	}

	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}

	public HashMap<Integer, Integer> getUserByHour() {
		return userByHour;
	}

	public void setUserByHour(HashMap<Integer, Integer> userByHour) {
		this.userByHour = userByHour;
	}

	public HashMap<String, String> getUserQuality() {
		return userQuality;
	}

	public void setUserQuality(HashMap<String, String> userQuality) {
		this.userQuality = userQuality;
	}

	public HashMap<Integer, String> getUserQualityByHour() {
		return userQualityByHour;
	}

	public void setUserQualityByHour(HashMap<Integer, String> userQualityByHour) {
		this.userQualityByHour = userQualityByHour;
	}

	public HashMap<String, String> getContentUsedQuality() {
		return contentUsedQuality;
	}

	public void setContentUsedQuality(HashMap<String, String> contentUsedQuality) {
		this.contentUsedQuality = contentUsedQuality;
	}

	public HashMap<Integer, String> getRealContentUsedByHour() {
		return realContentUsedByHour;
	}

	public void setRealContentUsedByHour(HashMap<Integer, String> realContentUsedByHour) {
		this.realContentUsedByHour = realContentUsedByHour;
	}

	public HashMap<String, Integer> getColumnUsedCount() {
		return columnUsedCount;
	}

	public void setColumnUsedCount(HashMap<String, Integer> columnUsedCount) {
		this.columnUsedCount = columnUsedCount;
	}

	public List<HashMap<String, String>> getColumnUsedSort() {
		return columnUsedSort;
	}

	public void setColumnUsedSort(List<HashMap<String, String>> columnUsedSort) {
		this.columnUsedSort = columnUsedSort;
	}

	public List<List<HashMap<String, String>>> getColumnTop10ByHour() {
		return columnTop10ByHour;
	}

	public void setColumnTop10ByHour(List<List<HashMap<String, String>>> columnTop10ByHour) {
		this.columnTop10ByHour = columnTop10ByHour;
	}

	public List<HashMap<String, String>> getContentTop100() {
		return contentTop100;
	}

	public void setContentTop100(List<HashMap<String, String>> contentTop100) {
		this.contentTop100 = contentTop100;
	}

	public HashMap<String, String> getPicVideoMusicRatio() {
		return picVideoMusicRatio;
	}

	public void setPicVideoMusicRatio(HashMap<String, String> picVideoMusicRatio) {
		this.picVideoMusicRatio = picVideoMusicRatio;
	}

	public List<HashMap<String, String>> getTop10CloumnByPicUsed() {
		return top10CloumnByPicUsed;
	}

	public void setTop10CloumnByPicUsed(List<HashMap<String, String>> top10CloumnByPicUsed) {
		this.top10CloumnByPicUsed = top10CloumnByPicUsed;
	}

	public List<HashMap<String, String>> getTop100Ips() {
		return top100Ips;
	}

	public void setTop100Ips(List<HashMap<String, String>> top100Ips) {
		this.top100Ips = top100Ips;
	}

	public List<HashMap<String, String>> getTop10IpSection() {
		return top10IpSection;
	}

	public void setTop10IpSection(List<HashMap<String, String>> top10IpSection) {
		this.top10IpSection = top10IpSection;
	}

	public HashMap<String, String> getSource() {
		return source;
	}

	public void setSource(HashMap<String, String> source) {
		this.source = source;
	}

	public HashMap<String, String> getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(HashMap<String, String> sendStatus) {
		this.sendStatus = sendStatus;
	}

	public HashMap<String, Integer> getAllKeywords() {
		return allKeywords;
	}

	public void setAllKeywords(HashMap<String, Integer> allKeywords) {
		this.allKeywords = allKeywords;
	}

	public HashMap<String, Integer> getKeywordsOfTopNContent() {
		return keywordsOfTopNContent;
	}

	public void setKeywordsOfTopNContent(HashMap<String, Integer> keywordsOfTopNContent) {
		this.keywordsOfTopNContent = keywordsOfTopNContent;
	}

	public HashMap<String, ColumnUsedDetailed> getColumnUsedDetailed() {
		return columnUsedDetailed;
	}

	public void setColumnUsedDetailed(HashMap<String, ColumnUsedDetailed> columnUsedDetailed) {
		this.columnUsedDetailed = columnUsedDetailed;
	}


}
