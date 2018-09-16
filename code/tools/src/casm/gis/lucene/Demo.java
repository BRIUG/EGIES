package casm.gis.lucene;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
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
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

/*
 * Create a class and build an index using Lucene 4.6
 * 2017-04-11 01:47:46
 */
public class Demo {

	/*
	 * Create a lucene index
	 * 2017-04-11 01:59:56
	 */
	public static void indexDemo(String indexPath){
		try {
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
			Directory dir = FSDirectory.open(new File(indexPath));
			//Need to pass the version of lucene
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46, analyzer);
			IndexWriter iw = new IndexWriter(dir, config);
			
			Document doc = new Document();
			IndexableField idField = new IntField("id", 1, Field.Store.YES);
			IndexableField titleField = new StringField("title", "key1 key2",Field.Store.YES);
			IndexableField dateField = new StringField("date", "key3 key4",Field.Store.YES);
			IndexableField sourceField = new StringField("source", "key5 key6",Field.Store.YES);
			IndexableField contentField = new TextField("content","key7 key8",Field.Store.YES);
			
			doc.add(idField);
			doc.add(titleField);
			doc.add(dateField);
			doc.add(sourceField);
			doc.add(contentField);
			
			iw.addDocument(doc);
			//Optimized index: merge of indexes
			iw.forceMerge(3);
			iw.commit();
			iw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Implement the rewrite of the indexDemo method, and put the data into the list.
	 * 2017-04-12 01:45:17
	 */
	public static void indexDemo(List<Map<String,Object>> list,String indexPath){
		try {
//			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
			//Use IKAnalyzer as a tokenizer
			Analyzer analyzer = new IKAnalyzer();
//			ICTCLAS50 analyzer = new ICTCLAS50();
			Directory dir = FSDirectory.open(new File(indexPath));
			//Need to pass the version of lucene
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46, analyzer);
			IndexWriter iw = new IndexWriter(dir, config);
			
			for(Map<String,Object> map : list){
				Document doc = new Document();
				Set<String> set = map.keySet();
				Iterator<String> iter = set.iterator();
				while(iter.hasNext()){
					String key = iter.next();
					String value = (String) map.get(key);
					IndexableField field = new TextField(key, value,Field.Store.YES);
					doc.add(field);
				}
				iw.addDocument(doc);
			}
			
			iw.commit();
			iw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Create a Lucene search
	 * 2017-04-11 03:22:15
	 */
	public static void searcherDemo(String indexPath){
		try {
			Directory dir = FSDirectory.open(new File(indexPath));
			IndexReader reader = DirectoryReader.open(dir);
			IndexSearcher is = new IndexSearcher(reader);
			
			Query query = new TermQuery(new Term("content", "key7"));
//			Query query = new TermQuery(new Term("id", "2"));
//			Query query = new TermQuery(new Term("content", "应急"));
//			Query query = NumericRangeQuery.newIntRange("id",2,4,true,true);
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
	 * Delete the index created by Lucene
	 * 2017-04-12 02:40:34
	 */
	public static void deleteIndex(String indexPath){
		try {
			Analyzer analyzer = new IKAnalyzer();
			Directory dir = FSDirectory.open(new File(indexPath));
			//Need to pass the version of lucene.
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46, analyzer);
			IndexWriter iw = new IndexWriter(dir, config);
			
			//Delete all indexes
//			iw.deleteAll();
			//Delete specified content index
			iw.deleteDocuments(new Term("id", "1"));
			iw.commit();
			iw.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Modify Lucene to create an index
	 * 2017-04-12 03:14:55
	 */
	public static void updateIndex(String indexPath){
		try {
			Analyzer analyzer = new IKAnalyzer();
			Directory dir = FSDirectory.open(new File(indexPath));
			//Need to pass the version of lucene.
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46, analyzer);
			IndexWriter iw = new IndexWriter(dir, config);
			
			Document doc = new Document();
			IndexableField field = new TextField("id", "100",Field.Store.YES);
			doc.add(field);
			//Use 100 to modify the content of 2, after the successful modification, the content of 2 cannot be searched.
			iw.updateDocument(new Term("id", "2"), doc);
			iw.commit();
			iw.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Highlight search results
	 * 2017-04-12 03:48:29
	 */
	public static void highlightSearcherDemo(String indexPath){
		try {
			Analyzer analyzer = new IKAnalyzer();
			Directory dir = FSDirectory.open(new File(indexPath));
			IndexReader reader = DirectoryReader.open(dir);
			IndexSearcher is = new IndexSearcher(reader);
			
			Query query = new TermQuery(new Term("content", "key8"));
			
			//Highlighted in red
			String preTag = "<font color = \"#DD4B39\" >";
			String postTag = "</font>";
			Formatter formatter = new SimpleHTMLFormatter(preTag, postTag);
			Scorer fragmentScorer = new QueryScorer(query);
			Highlighter highlighter = new Highlighter(formatter, fragmentScorer);
			//Highlight 50 words
			Fragmenter fragmenter = new SimpleFragmenter(50);
			highlighter.setTextFragmenter(fragmenter);
			
			TopDocs topDocs = is.search(query, 10);
			
			int hits = topDocs.totalHits;
			System.out.println("hits:"+hits);
			ScoreDoc[] scoreDoc = topDocs.scoreDocs;
			for(ScoreDoc sd : scoreDoc){
				int docID = sd.doc;
				Document doc = is.doc(docID);
				String hid = highlighter.getBestFragment(analyzer, "id",doc.get("id"));
				String ht = highlighter.getBestFragment(analyzer, "title",doc.get("title"));
				String hd = highlighter.getBestFragment(analyzer, "date",doc.get("date"));
				String hs = highlighter.getBestFragment(analyzer, "source",doc.get("source"));
				String hc = highlighter.getBestFragment(analyzer, "content",doc.get("content"));
				if(hid == null){
					hid = doc.get("id");
				}
				if(ht == null){
					ht = doc.get("title");
				}
				if(hd == null){
					hd = doc.get("date");
				}
				if(hs == null){
					hs = doc.get("source");
				}
				if(hc == null){
					hc = doc.get("content");
				}
				//Id: file serial number, name: file name, content: file content
				System.out.println(hid+":"+ht+":"+hd+":"+hs+":"+hc);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
