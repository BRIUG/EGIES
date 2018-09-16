package org.archive.crawler.postprocessor;

import org.archive.crawler.datamodel.CandidateURI;

/*
 * 定制专业获取URL的类型
 * 2017-03-23 22:18:41
 */
public class EGISFrontierScheduler extends FrontierScheduler {

	private static final long serialVersionUID = 5623226422553891411L;

	public EGISFrontierScheduler(String name) {
		super(name);
	}
	
	 protected void schedule(CandidateURI caUri) {
		 if(caUri.toString().contains("cneb")){
	        getController().getFrontier().schedule(caUri);
		 }
	 }
}
