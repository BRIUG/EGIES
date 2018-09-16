package casm.gis.lucene;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.ConstantScoreQuery;
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.RegexpQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class QueryDemo {

	/*
	 * Create a Lucene index file for Query
	 * 2017-04-12 04:34:54
	 * The participle in IK, true: intelligent participle, false: fine-grained participle, not written is the default for fine-grained word segmentation
	 */
	private static Analyzer analyzer = new IKAnalyzer();
	private static String indexPath = "O:/WorkSpace/JAVA/EGISTest/sampleTest/6.lucene/4.indexPathTXTIKQuery/indexPath";
	public static void queryIndex(){
		try {
			Directory dir = FSDirectory.open(new File(indexPath));
			//Need to pass the version of lucene.
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46, analyzer);
			IndexWriter iw = new IndexWriter(dir, config);
			
			Document doc = new Document();
			IndexableField idField = new IntField("id", 1, Field.Store.YES);
			IndexableField titleField = new StringField("title", "北京市海淀区莲花池西路28号",Field.Store.YES);
			IndexableField dateField = new StringField("date", "2017-04-12 04:37:00",Field.Store.YES);
			IndexableField sourceField = new StringField("source", "中国国家应急广播",Field.Store.YES);
			IndexableField contentField = new TextField("content","应急地理信息提取研究，国家测绘地理信息局。",Field.Store.YES);
			
			doc.add(idField);
			doc.add(titleField);
			doc.add(dateField);
			doc.add(sourceField);
			doc.add(contentField);
			
			Document doc1 = new Document();
			IndexableField idField1 = new IntField("id", 2, Field.Store.YES);
			IndexableField titleField1 = new StringField("title", "北京柳絮又飘起来了",Field.Store.YES);
			IndexableField dateField1 = new StringField("date", "2016-04-13 01:50:16",Field.Store.YES);
			IndexableField sourceField1 = new StringField("source", "中国国家应急广播",Field.Store.YES);
			IndexableField contentField1 = new TextField("content","研究应急灾害的评估和使用网络获取数据源。美国下暴雪。",Field.Store.YES);
			
			doc1.add(idField1);
			doc1.add(titleField1);
			doc1.add(dateField1);
			doc1.add(sourceField1);
			doc1.add(contentField1);
			
			Document doc2 = new Document();
			IndexableField idField2 = new IntField("id", 3, Field.Store.YES);
			IndexableField titleField2 = new StringField("title", "我爱北京天安门",Field.Store.YES);
			IndexableField dateField2 = new StringField("date", "2015-04-13 01:53:36",Field.Store.YES);
			IndexableField sourceField2 = new StringField("source", "中国中央电视台",Field.Store.YES);
			IndexableField contentField2 = new TextField("content","美国白宫发生恐怖袭击事件，总统被犬刑",Field.Store.YES);
			
			doc2.add(idField2);
			doc2.add(titleField2);
			doc2.add(dateField2);
			doc2.add(sourceField2);
			doc2.add(contentField2);
			
			iw.addDocument(doc);
			iw.addDocument(doc1);
			iw.addDocument(doc2);
			
			iw.commit();
			iw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Use the termQuery method
	 * Basic word query, query undivided fields, entry search
	 * 2017-04-12 04:49:21
	 */
	public static void termQueryDemo(){
		try {
			Directory dir = FSDirectory.open(new File(indexPath));
			IndexReader reader = DirectoryReader.open(dir);
			IndexSearcher is = new IndexSearcher(reader);
			
			Query query = new TermQuery(new Term("content", "应急"));
			TopDocs topDocs = is.search(query, 10);
			
			int hits = topDocs.totalHits;
			System.out.println("hits:"+hits);
			ScoreDoc[] scoreDoc = topDocs.scoreDocs;
			for(ScoreDoc sd : scoreDoc){
				int docID = sd.doc;
				Document doc = is.doc(docID);
				//Id: file serial number, name: file name, content: file content
				System.out.println(doc.get("id")+":"+doc.get("title")+":"+doc.get("content"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Query method for combining query conditions
	 * 2017-04-12 22:54:13
	 */
	public static void booleanQueryDemo(){
		try {
			Directory dir = FSDirectory.open(new File(indexPath));
			IndexReader reader = DirectoryReader.open(dir);
			IndexSearcher is = new IndexSearcher(reader);
			
			Query query1 = new TermQuery(new Term("content", "应急"));
			Query query2 = new TermQuery(new Term("content", "评估"));
			
			//Method of combining conditional queries
			BooleanQuery query = new BooleanQuery();
			query.add(query1, BooleanClause.Occur.MUST);
			query.add(query2, BooleanClause.Occur.MUST_NOT);
			
			TopDocs topDocs = is.search(query, 10);
			
			int hits = topDocs.totalHits;
			System.out.println("hits:"+hits);
			ScoreDoc[] scoreDoc = topDocs.scoreDocs;
			for(ScoreDoc sd : scoreDoc){
				int docID = sd.doc;
				Document doc = is.doc(docID);
				//Id: file serial number, name: file name, content: file content
				System.out.println(doc.get("id"));
				System.out.println(doc.get("title"));
				System.out.println(doc.get("content"));
				System.out.println();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Fuzzy query: query method using wildcards
	 * The wildcard is best placed at the back, and it needs to be iterated many times before it takes time.
	 * 2017-04-12 23:14:41
	 */
	public static void wildcardQueryDemo(){
		try {
			Directory dir = FSDirectory.open(new File(indexPath));
			IndexReader reader = DirectoryReader.open(dir);
			IndexSearcher is = new IndexSearcher(reader);
			
			//Wildcard query method
			WildcardQuery query = new WildcardQuery(new Term("content","应急*"));
			
			TopDocs topDocs = is.search(query, 10);
			
			int hits = topDocs.totalHits;
			System.out.println("hits:"+hits);
			ScoreDoc[] scoreDoc = topDocs.scoreDocs;
			for(ScoreDoc sd : scoreDoc){
				int docID = sd.doc;
				Document doc = is.doc(docID);
				//Id: file serial number, name: file name, content: file content
				System.out.println(doc.get("id"));
				System.out.println(doc.get("title"));
				System.out.println(doc.get("content"));
				System.out.println();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Phrase query
	 * 2017-04-12 23:23:46
	 */
	public static void phraseQueryDemo(){
		try {
			Directory dir = FSDirectory.open(new File(indexPath));
			IndexReader reader = DirectoryReader.open(dir);
			IndexSearcher is = new IndexSearcher(reader);
			
			//Phrase query
			PhraseQuery query = new PhraseQuery();
			//Here you need to calculate the relative location of the disaster and data source：研究  应急  灾害  的  评估  和  使用  网络  获取  数据源  ->（10-3）=7
//			query.add(new Term("content","灾害"));
//			query.add(new Term("content","数据源"),7);
			
//			query.add(new Term("content","应急"));
//			query.add(new Term("content","数据源"),8);
			
			//Slop: to match the distance between words, the default is 0
//			query.add(new Term("content","应急"));
//			query.add(new Term("content","数据源"));
//			query.setSlop(8);
			
			query.add(new Term("content","应急"));
			query.add(new Term("content","数据源"));
			//More than 6 have results
			query.setSlop(200);
			
			TopDocs topDocs = is.search(query, 10);
			
			int hits = topDocs.totalHits;
			System.out.println("hits:"+hits);
			ScoreDoc[] scoreDoc = topDocs.scoreDocs;
			for(ScoreDoc sd : scoreDoc){
				int docID = sd.doc;
				Document doc = is.doc(docID);
				//Id: file serial number, name: file name, content: file content
				System.out.println(doc.get("id"));
				System.out.println(doc.get("title"));
				System.out.println(doc.get("content"));
				System.out.println();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Implement prefix matching query
	 * 2017-04-13 00:54:43
	 */
	public static void prefixQueryDemo(){
		try {
			Directory dir = FSDirectory.open(new File(indexPath));
			IndexReader reader = DirectoryReader.open(dir);
			IndexSearcher is = new IndexSearcher(reader);
			
			//Prefix match query
			PrefixQuery query = new PrefixQuery(new Term("content","数据源"));
			
			TopDocs topDocs = is.search(query, 10);
			
			int hits = topDocs.totalHits;
			System.out.println("hits:"+hits);
			ScoreDoc[] scoreDoc = topDocs.scoreDocs;
			for(ScoreDoc sd : scoreDoc){
				int docID = sd.doc;
				Document doc = is.doc(docID);
				//Id: file serial number, name: file name, content: file content
				System.out.println(doc.get("id"));
				System.out.println(doc.get("title"));
				System.out.println(doc.get("content"));
				System.out.println();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Multiple phrase query
	 * 2017-04-13 01:05:33
	 */
	public static void multiPhraseQueryDemo(){
		try {
			Directory dir = FSDirectory.open(new File(indexPath));
			IndexReader reader = DirectoryReader.open(dir);
			IndexSearcher is = new IndexSearcher(reader);
			
			//Multiple phrase query
			MultiPhraseQuery query = new MultiPhraseQuery();
			Term term1 = new Term("content","应急");
			Term term2 = new Term("content","提取");
			Term[] terms = {term1,term2};
			query.add(terms);
			
			TopDocs topDocs = is.search(query, 10);
			
			int hits = topDocs.totalHits;
			System.out.println("hits:"+hits);
			ScoreDoc[] scoreDoc = topDocs.scoreDocs;
			for(ScoreDoc sd : scoreDoc){
				int docID = sd.doc;
				Document doc = is.doc(docID);
				//Id: file serial number, name: file name, content: file content
				System.out.println(doc.get("id"));
				System.out.println(doc.get("title"));
				System.out.println(doc.get("content"));
				System.out.println();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Fuzzy query
	 * 2017-04-13 01:22:09
	 */
	public static void fuzzyQueryDemo(){
		try {
			Directory dir = FSDirectory.open(new File(indexPath));
			IndexReader reader = DirectoryReader.open(dir);
			IndexSearcher is = new IndexSearcher(reader);
			
			//Fuzzy query
			FuzzyQuery query = new FuzzyQuery(new Term("content","应急"), 0, 1, 1, true);
			
			TopDocs topDocs = is.search(query, 10);
			
			int hits = topDocs.totalHits;
			System.out.println("hits:"+hits);
			ScoreDoc[] scoreDoc = topDocs.scoreDocs;
			for(ScoreDoc sd : scoreDoc){
				int docID = sd.doc;
				Document doc = is.doc(docID);
				//Id: file serial number, name: file name, content: file content
				System.out.println(doc.get("id"));
				System.out.println(doc.get("title"));
				System.out.println(doc.get("content"));
				System.out.println();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Regular expression query
	 * 2017-04-13 01:30:35
	 */
	public static void regexpQueryDemo(){
		try {
			Directory dir = FSDirectory.open(new File(indexPath));
			IndexReader reader = DirectoryReader.open(dir);
			IndexSearcher is = new IndexSearcher(reader);
			
			//Regular expression query
//			RegexpQuery query = new RegexpQuery(new Term("title","北京(.*)28号"));
			RegexpQuery query = new RegexpQuery(new Term("content","应(.?)"));
			
			TopDocs topDocs = is.search(query, 10);
			
			int hits = topDocs.totalHits;
			System.out.println("hits:"+hits);
			ScoreDoc[] scoreDoc = topDocs.scoreDocs;
			for(ScoreDoc sd : scoreDoc){
				int docID = sd.doc;
				Document doc = is.doc(docID);
				//id:文件序号，name：文件名，content：文件内容
				System.out.println(doc.get("id"));
				System.out.println(doc.get("title"));
				System.out.println(doc.get("content"));
				System.out.println();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 术语范围查询
	 * 2017-04-13 01:44:53
	 */
	public static void termRangeQueryDemo(){
		try {
			Directory dir = FSDirectory.open(new File(indexPath));
			IndexReader reader = DirectoryReader.open(dir);
			IndexSearcher is = new IndexSearcher(reader);
			
			//术语范围查询
			BytesRef lowerTerm = new BytesRef("2015-04-13 01:53:36");
			BytesRef upperTerm = new BytesRef("2017-04-12 04:37:00");
			TermRangeQuery query = new TermRangeQuery("date", lowerTerm, upperTerm, false, true);
			
			TopDocs topDocs = is.search(query, 10);
			
			int hits = topDocs.totalHits;
			System.out.println("hits:"+hits);
			ScoreDoc[] scoreDoc = topDocs.scoreDocs;
			for(ScoreDoc sd : scoreDoc){
				int docID = sd.doc;
				Document doc = is.doc(docID);
				//Id: file serial number, name: file name, content: file content
				System.out.println(doc.get("id"));
				System.out.println(doc.get("date"));
				System.out.println(doc.get("title"));
				System.out.println(doc.get("content"));
				System.out.println();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Digital range query
	 * 2017-04-13 02:05:38
	 */
	public static void numericRangeQueryDemo(){
		try {
			Directory dir = FSDirectory.open(new File(indexPath));
			IndexReader reader = DirectoryReader.open(dir);
			IndexSearcher is = new IndexSearcher(reader);
			
			//Digital range query
			Query query = NumericRangeQuery.newIntRange("id", 1, 3, false, true);
			
			TopDocs topDocs = is.search(query, 10);
			
			int hits = topDocs.totalHits;
			System.out.println("hits:"+hits);
			ScoreDoc[] scoreDoc = topDocs.scoreDocs;
			for(ScoreDoc sd : scoreDoc){
				int docID = sd.doc;
				Document doc = is.doc(docID);
				System.out.println(doc.get("id"));
				System.out.println(doc.get("date"));
				System.out.println(doc.get("title"));
				System.out.println(doc.get("content"));
				System.out.println();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Implement constant score query
	 * 2017-04-13 02:13:08
	 */
	public static void constantScoreQueryDemo(){
		try {
			Directory dir = FSDirectory.open(new File(indexPath));
			IndexReader reader = DirectoryReader.open(dir);
			IndexSearcher is = new IndexSearcher(reader);
			
			//Constant score query
			Filter filter = new QueryWrapperFilter(new TermQuery(new Term("content","应急")));
			ConstantScoreQuery query = new ConstantScoreQuery(filter);
			
			TopDocs topDocs = is.search(query, 10);
			
			int hits = topDocs.totalHits;
			System.out.println("hits:"+hits);
			ScoreDoc[] scoreDoc = topDocs.scoreDocs;
			for(ScoreDoc sd : scoreDoc){
				int docID = sd.doc;
				Document doc = is.doc(docID);
				System.out.println(doc.get("id"));
				System.out.println(doc.get("date"));
				System.out.println(doc.get("title"));
				System.out.println(doc.get("content"));
				System.out.println();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Separate maximum score query
	 * 2017-04-13 02:20:31
	 */
	public static void disjunctionMaxQueryDemo(){
		try {
			Directory dir = FSDirectory.open(new File(indexPath));
			IndexReader reader = DirectoryReader.open(dir);
			IndexSearcher is = new IndexSearcher(reader);
			
			//Separate maximum score query
			DisjunctionMaxQuery query = new DisjunctionMaxQuery(0.1f);
			query.add(new TermQuery(new Term("content","应急")));
			
			TopDocs topDocs = is.search(query, 10);
			
			int hits = topDocs.totalHits;
			System.out.println("hits:"+hits);
			ScoreDoc[] scoreDoc = topDocs.scoreDocs;
			for(ScoreDoc sd : scoreDoc){
				int docID = sd.doc;
				Document doc = is.doc(docID);
				System.out.println(doc.get("id"));
				System.out.println(doc.get("date"));
				System.out.println(doc.get("title"));
				System.out.println(doc.get("content"));
				System.out.println();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * a query that matches all documents
	 * 2017-04-13 02:27:24
	 */
	public static void matchAllDocsQueryDemo(){
		try {
			Directory dir = FSDirectory.open(new File(indexPath));
			IndexReader reader = DirectoryReader.open(dir);
			IndexSearcher is = new IndexSearcher(reader);
			
			MatchAllDocsQuery query = new MatchAllDocsQuery();
			
			TopDocs topDocs = is.search(query, 10);
			
			int hits = topDocs.totalHits;
			System.out.println("hits:"+hits);
			ScoreDoc[] scoreDoc = topDocs.scoreDocs;
			for(ScoreDoc sd : scoreDoc){
				int docID = sd.doc;
				Document doc = is.doc(docID);
				System.out.println(doc.get("id"));
				System.out.println(doc.get("date"));
				System.out.println(doc.get("title"));
				System.out.println(doc.get("content"));
				System.out.println();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Multi-domain query
	 * 2017-04-13 02:42:21
	 */
	public static void multiFieldQueryParserDemo(){
		try {
			Directory dir = FSDirectory.open(new File(indexPath));
			IndexReader reader = DirectoryReader.open(dir);
			IndexSearcher is = new IndexSearcher(reader);
			
			//Multi-domain query method
			String[] fields = {"id","content"};
			Map<String,Float> boosts = new HashMap<String,Float>();
			//Determine the content and weight of the search
			boosts.put("title", 0.01f);
			boosts.put("content", 10.0f);
			
			QueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_46, fields, analyzer, boosts);
			Query query = queryParser.parse("美国");
			
			TopDocs topDocs = is.search(query, 10);
			
			int hits = topDocs.totalHits;
			System.out.println("hits:"+hits);
			ScoreDoc[] scoreDoc = topDocs.scoreDocs;
			for(ScoreDoc sd : scoreDoc){
				int docID = sd.doc;
				Document doc = is.doc(docID);
				System.out.println(doc.get("id"));
				System.out.println(doc.get("date"));
				System.out.println(doc.get("title"));
				System.out.println(doc.get("content"));
				System.out.println();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
