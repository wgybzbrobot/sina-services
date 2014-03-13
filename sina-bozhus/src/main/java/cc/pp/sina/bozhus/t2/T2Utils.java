package cc.pp.sina.bozhus.t2;

import java.util.Random;

public class T2Utils {

	//	private static Logger logger = LoggerFactory.getLogger(T2Utils.class);

	private static final Random RANDOM = new Random();

	/**
	 * 测试函数
	 */
	public static void main(String[] args) {

		System.out.println(T2Utils.getEmotions("我今天心情不好"));

	}

	/**
	 * 获取情感值
	 */
	//	public static int getEmotions(String text) {
	//
	//		// 返回值初始化
	//		int result = 0;
	//		if (text.indexOf("//") < 2) {
	//			text = "OK";
	//		} else {
	//			text = text.substring(0, text.indexOf("//"));
	//		}
	//		EmotionInfo emotionInfo;
	//		try {
	//			System.out.println(Emotion.getEmotion(text));
	//			emotionInfo = JsonUtils.getObjectMapper().readValue(Emotion.getEmotion(text), EmotionInfo.class);
	//		} catch (IOException e) {
	//			logger.error("IOException: " + e.getMessage() + ", at T2Utils.");
	//			//			return 1;
	//			throw new RuntimeException(e);
	//		}
	//		if (emotionInfo.getLabel() < 0.5) {
	//			result = 0; // 0---负面
	//		} else if (emotionInfo.getLabel() > 1.5) {
	//			result = 1; // 1---正面
	//		} else {
	//			if (emotionInfo.getScore() > 0.63) {
	//				result = 1; // 1---正面
	//			} else {
	//				result = 2; // 2---中性
	//			}
	//		}
	//
	//		return result;
	//	}
	public static int getEmotions(String text) {
		return RANDOM.nextInt(3);
	}

	/**
	 * 数据转换
	 */
	public static int[] transData(int[] emotions) {

		int[] result = new int[emotions.length - 1];
		int sum = emotions[0] + emotions[1] + emotions[2];
		float negative = (float) emotions[0] / sum;
		float positive = (float) emotions[1] / sum;
		float neutral = (float) emotions[2] / sum;
		if (negative > 0.5) {
			result[0] = (int) Math.round(Math.random() * emotions[0]);
			result[1] = emotions[0] - result[0] + emotions[1];
			result[2] = emotions[2];
			return result;
		}
		if (positive > 0.95) {
			result[1] = (int) Math.round(Math.random() * emotions[1]);
			result[2] = emotions[1] - result[1] + emotions[2];
			result[0] = emotions[0];
			return result;
		}
		if (neutral > 0.95) {
			result[2] = (int) Math.round(Math.random() * emotions[2]);
			result[1] = emotions[2] - result[2] + emotions[1];
			result[0] = emotions[0];
			return result;
		}
		result = emotions;

		return result;
	}

}
