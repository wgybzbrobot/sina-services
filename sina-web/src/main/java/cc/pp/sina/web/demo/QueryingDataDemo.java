package cc.pp.sina.web.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;

import cc.pp.sina.domain.query.IntRangeDomain;
import cc.pp.sina.domain.query.QueryResult;
import cc.pp.sina.query.query.BozhuQuery;

public class QueryingDataDemo {

	public static void main(String[] args) throws IOException, ParseException {

		BozhuQuery bozhuQuery = new BozhuQuery();

		// 1、关键词查询类型
		//		List<String> result = bozhuQuery.getKeywordsQueryResult("activecount", "fanscount", "influence", "电梯");
		// 2、数值范围查询类型
		List<IntRangeDomain> intRangeDomains = new ArrayList<IntRangeDomain>();
		//		intRangeDomains.add(new IntRangeDomain.Builder("influence", 70, 100).build());
		//		intRangeDomains.add(new IntRangeDomain.Builder("activation", 6, 10).build());
		//		intRangeDomains.add(new IntRangeDomain.Builder("fanscount", 5_0000, 100_0000).build());
		//		List<FloatRangeDomain> floatRangeDomains = new ArrayList<FloatRangeDomain>();
		//		floatRangeDomains.add(new FloatRangeDomain.Builder("averagewbs", 1.0f, 3.0f).build());
		//		List<String> sortedNames = new ArrayList<String>();
		//		sortedNames.add("activecount");
		//		sortedNames.add("fanscount");
		//		sortedNames.add("influence");
		//		List<String> result = bozhuQuery.getRangeQueryResult(intRangeDomains, floatRangeDomains, sortedNames);
		//		List<String> result = bozhuQuery.getCompositeQueryResult(intRangeDomains, floatRangeDomains, "商业娱乐美食", "0", 2);
		QueryResult queryResult = bozhuQuery.getCompositeQueryByFans(intRangeDomains, 0, "北京上海", "娱乐美食", "", 1);
		System.err.println(queryResult.getTotalNumber());
		System.out.println("\n");
		for (String temp : queryResult.getData()) {
			System.out.println(temp);
		}
		bozhuQuery.close();
	}

}
