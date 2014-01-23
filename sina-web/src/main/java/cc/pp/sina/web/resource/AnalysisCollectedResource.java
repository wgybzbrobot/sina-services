package cc.pp.sina.web.resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.algorithms.top.sort.PPSort;
import cc.pp.sina.domain.bozhus.UserAllParamsDomain;
import cc.pp.sina.utils.json.JsonUtils;
import cc.pp.sina.web.application.BozhuLibraryApplication;
import cc.pp.sina.web.domain.CollectBozhusInfo;

public class AnalysisCollectedResource extends ServerResource {

	private static Logger logger = LoggerFactory.getLogger(AnalysisCollectedResource.class);

	private List<String> uidsList;

	@Override
	public void doInit() {
		String uids = (String) getRequest().getAttributes().get("uids");
		uidsList = new ArrayList<>();
		for (String uid : uids.split(",")) {
			uidsList.add(uid);
		}
	}

	@Get("json")
	public Representation getCollectBozhus() {
		BozhuLibraryApplication application = (BozhuLibraryApplication) getApplication();
		CollectBozhusInfo bozhusCollection = collectBozhus(application.getBozhusInfo(uidsList));
		return new JsonRepresentation(JsonUtils.toJson(bozhusCollection));
	}

	/**
	 * 汇总数据
	 */
	private CollectBozhusInfo collectBozhus(List<UserAllParamsDomain> bozhus) {

		HashMap<String, Float> gender = new HashMap<>();
		float maleRatio = 0;
		HashMap<String, String> ageclass = new HashMap<>(); // 粉丝年龄层次，还没有
		ageclass.put("70前", "5.00%");
		ageclass.put("70后", "25.0%");
		ageclass.put("80后", "44.0%");
		ageclass.put("90后", "26.0%");
		HashMap<String, Float> province = new HashMap<>();
		long allFans = 0;
		for (UserAllParamsDomain bozhu : bozhus) {
			maleRatio += bozhu.getMaleratio();
			AnalysisCollectedResource.collectProvince(province, bozhu.getTop5provinces());
			allFans += bozhu.getAllfanscount();
		}
		province = PPSort.sortedToHashMap(province, 5);
		for (Entry<String, Float> entry : province.entrySet()) {
			province.put(entry.getKey(), entry.getValue() / (100 * bozhus.size()));
		}
		gender.put("m", maleRatio / bozhus.size());
		gender.put("f", 1 - maleRatio / bozhus.size());

		return new CollectBozhusInfo.Builder().setGender(transMap(gender)).setAgeClass(ageclass)
				.setProvince(transMap(province)).setAllFans(allFans).build();
	}

	public static HashMap<String, String> transMap(HashMap<String, Float> data) {

		HashMap<String,String> result = new HashMap<>();
		for (Entry<String,Float> temp : data.entrySet()) {
			result.put(temp.getKey(), Float.toString((float) Math.round(temp.getValue() * 10000) / 100) + "%");
		}

		return result;
	}

	public static void collectProvince(HashMap<String, Float> province, String provinces) {

		HashMap<String, Float> temp = mapperProvinces(provinces);
		for (Entry<String, Float> entry : temp.entrySet()) {
			if (province.get(entry.getKey()) == null) {
				province.put(entry.getKey(), entry.getValue());
			} else {
				province.put(entry.getKey(), province.get(entry.getKey()) + entry.getValue());
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, Float> mapperProvinces(String provinces) {
		try {
			HashMap<String, Float> result = new HashMap<>();
			HashMap<String, String> temp = (HashMap<String, String>) JsonUtils.getObjectMapper()
					.readValue(provinces, List.class).get(0);
			for (Entry<String, String> entry : temp.entrySet()) {
				result.put(entry.getKey(),
						Float.parseFloat(entry.getValue().substring(0, entry.getValue().length() - 1)));
			}
			return result;
		} catch (JsonParseException e) {
			logger.info("JsonParseException is occur on provinces dejson.");
			return null;
		} catch (JsonMappingException e) {
			logger.info("JsonMappingException is occur on provinces dejson.");
			return null;
		} catch (IOException e) {
			logger.info("IOException is occur on provinces dejson.");
			return null;
		}
	}

}
