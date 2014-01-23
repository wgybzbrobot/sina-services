package cc.pp.sina.web.company;

import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;

import cc.pp.sina.domain.query.QueryResult;
import cc.pp.sina.query.query.PPCompanyQuery;

public class PPCompanyQueryDemo {

	public static void main(String[] args) throws IOException, ParseException {

		PPCompanyQuery ppCompanyQuery = new PPCompanyQuery("ppcompany_index");
		QueryResult queryResult = ppCompanyQuery.getQueryResultByKeywords("201312", "all", "美食娱乐美容", 1);
		System.out.println(queryResult.getTotalNumber());
	}

}
