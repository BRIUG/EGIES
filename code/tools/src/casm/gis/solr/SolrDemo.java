package casm.gis.solr;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.RangeFacet;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import casm.gis.util.DateUtils;

public class SolrDemo {
	
	/*
	 * Solr's index
	 * 2017-04-19 00:38:25
	 */
	public static void solrIndex(){
		try {
			String url = "http://localhost:8080/solr";
			HttpSolrServer server = new HttpSolrServer(url);
			SolrInputDocument doc = new SolrInputDocument();
			doc.addField("id", "1");
			doc.addField("name", "中国");
			doc.addField("place", 25);
			doc.addField("date", DateUtils.getYear("2017"));
			doc.addField("content", "陕西白河县发生山体滑坡 3人死亡3人失踪千余人受灾");
			doc.addField("testik", "陕西白河县发生山体滑坡 3人死亡3人失踪千余人受灾，汕头市潮南区陈店镇一内衣厂发生火灾 1人受伤");
			
			
			SolrInputDocument doc1 = new SolrInputDocument();
			doc1.addField("id", "2");
			doc1.addField("name", "北京");
			doc1.addField("place", 30);
			doc1.addField("date", DateUtils.getYear("2018"));
			doc1.addField("content", "22日北京将有6级阵风 外出注意防风补水");
			doc1.addField("testik", "22日北京将有6级阵风 外出注意防风补水，汕头市潮南区陈店镇一内衣厂发生火灾 1人受伤");
			
			server.add(doc);
			server.add(doc1);
			server.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Solr search
	 *
	 * 2017-04-19 00:38:42
	 */
	public static void solrSearcher(){
		try {
			String url = "http://localhost:8080/solr";
			HttpSolrServer server = new HttpSolrServer(url);
			SolrQuery params = new SolrQuery("testik:汕头市");
//			SolrQuery params = new SolrQuery("陕西");
			
			//Default search domain
//			params.setParam("df", "name");
			
			//Display domain
			String[] fields = {"id","name","date","content","testik","place"};
			params.setFields(fields);
			
			//Highlight
			params.addHighlightField("testik");
			params.setHighlight(true);
			params.setHighlightSimplePre("<em class=\"highlight\" >");
			params.setHighlightSimplePost("</em>");
			//Set the number of words highlighted
			params.setHighlightFragsize(10);
			
			//Sort
			params.addSort("place",ORDER.desc);
			
			//filter
			/*String[] fqs = {"name:中国"};
			params.addFilterQuery(fqs);*/
			
			//Pagination
			params.setStart(0);
			//Number of pages per page
			params.setRows(10);
			
			//facet:statistical results
			String[] ftf = {"name","content"};
			params.addFacetField(ftf);
			
//			params.addNumericRangeFacet("place", 1, 26, 26);
			params.addDateRangeFacet("date", DateUtils.getYear("1999"), DateUtils.getYear("2020"), "+5YEAR");
			
			QueryResponse response = server.query(params);
			
		/*	 List<FacetField> listField = response.getFacetFields();
			 for(FacetField facetField : listField){
				 System.out.println(facetField.getName());
				 List<Count> counts = facetField.getValues();
				 for(Count c : counts){
					 System.out.println(c.getName()+":"+c.getCount());
				 }
			 }*/
			
			 List<RangeFacet> listFacet = response.getFacetRanges();
			 for(RangeFacet rf : listFacet){
				 List<RangeFacet.Count> listCounts = rf.getCounts();
				 for(RangeFacet.Count count : listCounts){
					 System.out.println("facet:"+count.getValue()+":"+count.getCount());
				 }
			 }
			
			SolrDocumentList list = response.getResults();
			 Map<String,Map<String,List<String>>> map = response.getHighlighting();
			System.out.println("total hits:"+list.getNumFound());
			for(SolrDocument doc : list){
				System.out.println("name:"+doc.getFieldValue("name"));
				System.out.println("hl:" + map.get(doc.getFieldValue("id")).get("testik").get(0));
				System.out.println("content:"+doc.get("content"));
				System.out.println("place:"+doc.get("place"));
				System.out.println("date:"+doc.get("date"));
				System.out.println("testik:"+doc.get("testik"));
				System.out.println("----------------------------------------");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/*
	 * Solr delete index
	 * 2017-04-20 03:45:47
	 */
	public static void solrDelIndex(){
		try {
			String url = "http://localhost:8080/solr";
			HttpSolrServer server = new HttpSolrServer(url);
			server.deleteById("1");
			server.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
