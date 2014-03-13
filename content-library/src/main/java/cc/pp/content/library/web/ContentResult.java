package cc.pp.content.library.web;

public class ContentResult {

	private int id;
	private String type; // 平台类型
	private String recordType; // 纪录类型
	private String date; // 0、统计日期
	private float errorRatio; // 1、内容库错误记录率
	private int contentCount; // 2、每天内容总数
	private float powerRatio; // 3、每天来自皮皮动力的比率
	private float fromContentRatio; // 4、每天来自内容库内容的比率
	private String fromContentRatioByHour; // 5、每天每小时内容库内容的使用比率
	private int userCount; // 6、每天使用内容库的用户数
	private String userByHour; // 7、每天每小时的真实用户数
	private String userQuality; // 8、每天使用用户的质量
	private String userQualityByHour; // 9、每天每小时的真实用户比例
	private String contentUsedQuality; // 10、每天内容库被使用的质量(real,mask分别使用的内容量比率)
	private String realContentUsedByHour; // 11、每天每小时真实用户使用内容库的比例
	private String columnUsedCount; // 12、每天每个栏目的使用数量
	private String columnUsedSort; // 13、每天各栏目的使用量排序
	private String columnTop10ByHour; // 14、每天每小时使用量排名前10的栏目
	private String contentTop100; // 15、每天内容库使用量前100的内容
	private String picVideoMusicRatio; // 16、每天所有内容中分别含图片、视频、音乐的比例
	private String top10CloumnByPicUsed; // 17、每天使用的所有图片排名前10的类型/栏目
	private String top100Ips; // 18、每天内容库使用量前100的IP
	private String top10IpSection; // 19、排名前20的IP段
	private String source; // 20、自定义来源分布
	private String sendStatus; // 21、发送状态情况
	private String allKeywords; // 22、所有关键词(按词频)
	private String keywordsOfTopNContent; // 23、top100内容中的关键词

	private String columnUsedDetailed; // 栏目使用详细数据

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public String getFromContentRatioByHour() {
		return fromContentRatioByHour;
	}

	public void setFromContentRatioByHour(String fromContentRatioByHour) {
		this.fromContentRatioByHour = fromContentRatioByHour;
	}

	public int getUserCount() {
		return userCount;
	}

	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}

	public String getUserByHour() {
		return userByHour;
	}

	public void setUserByHour(String userByHour) {
		this.userByHour = userByHour;
	}

	public String getUserQuality() {
		return userQuality;
	}

	public void setUserQuality(String userQuality) {
		this.userQuality = userQuality;
	}

	public String getUserQualityByHour() {
		return userQualityByHour;
	}

	public void setUserQualityByHour(String userQualityByHour) {
		this.userQualityByHour = userQualityByHour;
	}

	public String getContentUsedQuality() {
		return contentUsedQuality;
	}

	public void setContentUsedQuality(String contentUsedQuality) {
		this.contentUsedQuality = contentUsedQuality;
	}

	public String getRealContentUsedByHour() {
		return realContentUsedByHour;
	}

	public void setRealContentUsedByHour(String realContentUsedByHour) {
		this.realContentUsedByHour = realContentUsedByHour;
	}

	public String getColumnUsedCount() {
		return columnUsedCount;
	}

	public void setColumnUsedCount(String columnUsedCount) {
		this.columnUsedCount = columnUsedCount;
	}

	public String getColumnUsedSort() {
		return columnUsedSort;
	}

	public void setColumnUsedSort(String columnUsedSort) {
		this.columnUsedSort = columnUsedSort;
	}

	public String getColumnTop10ByHour() {
		return columnTop10ByHour;
	}

	public void setColumnTop10ByHour(String columnTop10ByHour) {
		this.columnTop10ByHour = columnTop10ByHour;
	}

	public String getContentTop100() {
		return contentTop100;
	}

	public void setContentTop100(String contentTop100) {
		this.contentTop100 = contentTop100;
	}

	public String getPicVideoMusicRatio() {
		return picVideoMusicRatio;
	}

	public void setPicVideoMusicRatio(String picVideoMusicRatio) {
		this.picVideoMusicRatio = picVideoMusicRatio;
	}

	public String getTop10CloumnByPicUsed() {
		return top10CloumnByPicUsed;
	}

	public void setTop10CloumnByPicUsed(String top10CloumnByPicUsed) {
		this.top10CloumnByPicUsed = top10CloumnByPicUsed;
	}

	public String getTop100Ips() {
		return top100Ips;
	}

	public void setTop100Ips(String top100Ips) {
		this.top100Ips = top100Ips;
	}

	public String getTop10IpSection() {
		return top10IpSection;
	}

	public void setTop10IpSection(String top10IpSection) {
		this.top10IpSection = top10IpSection;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}

	public String getAllKeywords() {
		return allKeywords;
	}

	public void setAllKeywords(String allKeywords) {
		this.allKeywords = allKeywords;
	}

	public String getKeywordsOfTopNContent() {
		return keywordsOfTopNContent;
	}

	public void setKeywordsOfTopNContent(String keywordsOfTopNContent) {
		this.keywordsOfTopNContent = keywordsOfTopNContent;
	}

	public String getColumnUsedDetailed() {
		return columnUsedDetailed;
	}

	public void setColumnUsedDetailed(String columnUsedDetailed) {
		this.columnUsedDetailed = columnUsedDetailed;
	}

}
