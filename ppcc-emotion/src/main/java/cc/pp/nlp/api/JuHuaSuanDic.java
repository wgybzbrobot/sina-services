package cc.pp.nlp.api;

public class JuHuaSuanDic {

	private static final String CITYS = new String("北京市上海市天津市重庆市香港澳门河北省邯郸市石家庄保定市张家口承德市唐山市廊坊市沧州市" +
			"衡水市邢台市秦皇岛浙江省衢州市杭州市湖州市嘉兴市宁波市绍兴市台州市温州市丽水市金华市舟山市辽宁省沈阳市铁岭市大连市鞍山市抚顺市本溪市丹东市锦州市" +
			"营口市阜新市辽阳市朝阳市盘锦市葫芦岛湖北省武汉市襄城市鄂州市孝感市黄州市黄石市咸宁市荆沙市宜昌市恩施市十堰市随枣市荆门市江汉市江苏省南京市无锡市" +
			"镇江市苏州市南通市扬州市盐城市徐州市淮阴市淮安市连云港常州市泰州市内蒙古海拉尔呼和浩特包头市乌海市集宁市通辽市赤峰市东胜市临河市锡林浩特乌兰浩特" +
			"阿拉善左旗江西省新余市南昌市九江市上饶市临川市宜春市吉安市赣州市景德镇萍乡市鹰潭市山西省忻州市太原市大同市阳泉市榆次市长治市晋城市临汾市离石市" +
			"运城市甘肃省临夏市兰州市定西市平凉市西峰市武威市张掖市酒泉市天水市甘南州白银市山东省菏泽市济南市青岛市淄博市德州市烟台市淮坊市济宁市泰安市临沂市" +
			"黑龙江阿城市哈尔滨齐齐哈尔牡丹江佳木斯绥化市黑河市加格达奇伊春市大庆市福建省福州市厦门市宁德市莆田市泉州市晋江市漳州市龙岩市三明市南平市广东省" +
			"广州市韶关市惠州市梅州市汕头市深圳市珠海市佛山市肇庆市湛江市中山市河源市清远市顺德市云浮市潮州市东莞市汕尾市潮阳市阳江市揭西市四川省成都市涪陵市" +
			"重庆市攀枝花自贡市永川市绵阳市南充市达县市万县市遂宁市广安市巴中市泸州市宜宾市内江市乐山市西昌市雅安市康定市马尔康德阳市广元市泸州市湖南省岳阳市" +
			"长沙市湘潭市株州市衡阳市郴州市常德市益阳市娄底市邵阳市吉首市张家界怀化市永州冷河南省商丘市郑州市安阳市新乡市许昌市平顶山信阳市南阳市开封市洛阳市" +
			"焦作市鹤壁市濮阳市周口市漯河市驻马店三门峡云南省昭通市昆明市大理市个旧市曲靖市保山市文山市玉溪市楚雄市思茅市景洪市潞西市东川市临沧市六库市中甸市" +
			"丽江市安徽省滁州市合肥市蚌埠市芜湖市淮南市马鞍山安庆市宿州市阜阳市黄山市淮北市铜陵市宣城市六安市巢湖市贵池市吉林省长春市吉林市延吉市四平市通化市" +
			"白城市辽源市松原市浑江市珲春市广西省防城港南宁市柳州市桂林市梧州市玉林市百色市钦州市河池市北海市贵州省贵阳市遵义市安顺市都均市凯里市铜仁市毕节市" +
			"六盘水兴义市陕西省西安市咸阳市延安市榆林市渭南市商洛市安康市汉中市宝鸡市铜川市青海省西宁市海东市同仁市共和市玛沁市玉树市德令哈宁夏银川市石嘴山" +
			"吴忠市固原市海南省儋州市海口市三亚市西藏拉萨市日喀则山南市");

	@SuppressWarnings("unused")
	private static final String JUHUASUAN = new String("今日团团购品牌团整点聚聚名品聚定制吃喝玩乐全部美食休闲电影生活服务丽人卡券甜点摄影" +
			"培训粮油生鲜旅游酒店汽车服务通讯房产汽车旅游境内游境外游酒店住宿机票景点门票邮轮游跟团自由行聚家装服饰配饰美妆鞋包运动母婴食品电器数码家居百货更多");

	//今日团、品牌团、整点聚、聚名品、吃喝玩乐、旅游酒店、聚家装、聚刺激、聚经典、聚火爆、聚奢侈（10）
	private static final String[] OPT_NAME = {"今日团","品牌团","整点聚","聚名品","吃喝玩乐","旅游酒店","聚家装","聚刺激","聚经典","聚火爆","聚奢侈"};
	private static final String[] OPT = {"今日团今天团购","品牌团","整点聚","聚名品正品","吃喝玩乐餐饮","旅游酒店住宿宾馆","聚家装家政","聚刺激","聚经典","聚火爆","聚奢侈"};

	//今日团：服饰、配饰、美妆、鞋包、运动、母婴、食品、电器、数码、家具、百货（11）
	private static final String[] JRT_NAME = {"服饰","配饰","美妆","鞋包","运动","母婴","食品","电器","数码","家具","百货"};
	private static final String[] CATEGORY_JRT = {"服饰服装男装女装衣服","配饰配件首饰装饰品","美妆美女化妆品","鞋包鞋子鞋袜LV包包",
		"运动体育锻炼","母婴婴儿小孩宝宝母亲","食品食物","电器洗衣机冰箱彩电电视","数码相机电脑平板手机智能机苹果单反摄像机","家具床铺桌椅","百货商店"};

	//吃喝玩乐：美食、休闲、电影、生活服务、丽人、卡券、摄影、甜点、KTV酒吧、培训、旅游、酒店、粮油生鲜、汽车服务、通讯、房产汽车（16）
	private static final String[] CHWL_NAME = {"美食","休闲","电影","生活服务","丽人","卡券","摄影","甜点","KTV酒吧","培训","旅游","酒店","粮油生鲜","汽车服务","通讯","房产汽车"};
	private static final String[] CATEGORY_CHWL = {"美食食品食物","休闲娱乐","电影院电影票","生活服务","丽人佳丽美女",
		"卡券优惠券优惠卡","摄影拍照照片","甜点点心甜食","KTV酒吧","培训课程教育","旅游游玩郊游","酒店","粮油生鲜海鲜龙虾",
		"汽车服务","通讯电信","房产汽车"};

	//旅游酒店：境内游、境外游、酒店住宿、机票、景点门票、邮轮游、跟团、自由行（8）
	private static final String[] LYJD_NAME = {"境内游","境外游","酒店住宿","机票","景点门票","邮轮游","跟团","自由行"};
	private static final String[] CATEGORY_LYJD = {"境内游","境外游","酒店住宿","飞机票","景点门票","邮轮游","跟团抱团","自由行"};

	/**
	 * 判断关键词类型
	 * @param keyword
	 * @return opt/city/category/other
	 */
	public static String[] getCategory(String keyword) {

		String[] result = new String[2];
		boolean flag = true;
		if (CITYS.contains(keyword)) {
			result[0] = "city";
			result[1] = keyword;
			flag = false;
		}
		//今日团、品牌团、整点聚、聚名品、吃喝玩乐、旅游酒店、聚家装、聚刺激、聚经典、聚火爆、聚奢侈
		if (flag) {
			for (int i = 0; i < OPT_NAME.length; i++) {
				if (OPT[i].contains(keyword)) {
					result[0] = "opt";
					result[1] = OPT_NAME[i];
					flag = false;
					break;
				} else {
					continue;
				}
			}
		}
		if (flag) {
			for (int j = 0; j < CATEGORY_JRT.length; j++) {
				if (CATEGORY_JRT[j].contains(keyword)) {
					result[0] = "category";
					result[1] = JRT_NAME[j];
					flag = false;
					break;
				} else {
					continue;
				}
			}
		}
		if (flag) {
			for (int j = 0; j < CATEGORY_CHWL.length; j++) {
				if (CATEGORY_CHWL[j].contains(keyword)) {
					result[0] = "category";
					result[1] = CHWL_NAME[j];
					flag = false;
					break;
				} else {
					continue;
				}
			}
		}
		if (flag) {
			for (int j = 0; j < CATEGORY_LYJD.length; j++) {
				if (CATEGORY_LYJD[j].contains(keyword)) {
					result[0] = "category";
					result[1] = LYJD_NAME[j];
					flag = false;
					break;
				} else {
					continue;
				}
			}
		}
		if (flag) {
			result[0] = "other";
//			result[1] = "帮助";
			result[1] = keyword;
		}

		return result;
	}

	/**
	 * 测试
	 * @param args
	 */
	public static void main(String[] args) {
//		String[] result = JuHuaSuanDic.getCategory("吃");
//		System.out.println(result[0]);
//		System.out.println(result[1]);
	}

}
