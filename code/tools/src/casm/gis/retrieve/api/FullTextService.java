package casm.gis.retrieve.api;

/*
 * Create a full-text service interface
 * 2017-04-30 14:17:22
 */
public interface FullTextService {

	//Start service
	public int beginService(String serverName);
	
	public int beginService(String serverName,String url);
	
	//flag: 0:IndexWriter 1:IndexSearcher
	public int beginService(String serverName,String flag,String indexPath);
	
	public void setServerName(String serverName);
	
	//Close service
	public int endService(String serverName);
	
	//index
	public void doIndex(FullTextIndexParams fullTextIndexParams);
	
	//Things to do before indexing
	public void preIndexMethod();
	
	//Things to do after indexing
	public void afterIndexMethod();
	
	//Modify index
	public void updateIndex(FullTextIndexParams fullTextIndexParams);
	
	//Modify what to do before
	public void preUpdateIndexMethod();
	
	//Things to do after modification
	public void afterUpdateIndexMethod();
	
	//Delete index
	public void deleteIndex(FullTextIndexParams fullTextIndexParams);
	
	//Delete what you have to do before
	public void preDeleteIndexMethod();
	
	//Things to do after deletion
	public void afterDeleteIndexMethod();
	
	//Search
	public FullTextResult doQuery(FullTextSearchParams fullTextSearchParams);
}
