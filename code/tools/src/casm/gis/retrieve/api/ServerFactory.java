package casm.gis.retrieve.api;

import java.util.Map;

import casm.gis.config.ConstantParams;
import casm.gis.retrieve.spi.SolrService;
import casm.gis.util.StringUtils;

/*
 * Create a service factory class
 * 2017-04-30 16:20:49
 */
public class ServerFactory {

	public FullTextService beginService(Map<String,String> configParams){
		FullTextService fullTextService = null;
		try {
			String serverName = configParams.get("serverName");
			String type = configParams.get("type");
			String className = configParams.get("className");
			Class<?> c = Class.forName(className);
			if("solr".equals(type)){
				String url = configParams.get("url");
				
				if(StringUtils.isEmpty(url)){
					url = StringUtils.getConfigParam(ConstantParams.SOLR_URL, "", ConstantParams.SEARCH_CONFIG);
				}
				fullTextService = (FullTextService)c.newInstance();
				fullTextService.beginService(serverName,url);
			}
			if("lucene".equals(type)){
				String flag = configParams.get("flag");
				String indexPath = configParams.get("indexPath");
				fullTextService = (FullTextService)c.newInstance();
				if(StringUtils.isEmpty(indexPath)){
					fullTextService.beginService(serverName, flag);
				}else{
					fullTextService.beginService(serverName, flag, indexPath);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fullTextService;
	}
}
