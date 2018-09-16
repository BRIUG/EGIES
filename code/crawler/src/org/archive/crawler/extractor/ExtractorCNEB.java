package org.archive.crawler.extractor;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.archive.crawler.datamodel.CrawlURI;
import org.archive.io.ReplayCharSequence;
import org.archive.util.HttpRecorder;

/*
 * Custom extraction rules
 *
 * CNEB：http://www.cneb.gov.cn/
 * 2017-03-23 22:42:25
 */
public class ExtractorCNEB extends Extractor {

	private static final long serialVersionUID = 4507143610438589585L;

	public ExtractorCNEB(String name, String description) {
		super(name, description);
	}

	public ExtractorCNEB(String name) {
		super(name, "CNEB EGIS Extractors");
	}
	
//	<a class="" href="xxx" target="_blank">
//	http://www.cneb.gov.cn/2017/03/23/ARTI1490271757899658.shtml
//	private static final String A_HERF = "<a(.*)href\\s*=\\s*(\"([^\"]*)\"|[^\\s>])(.*)>";
	private static final String A_HREF = "<a(.*)href\\s*=\\s*(\"([^\"]*)\"|[^\\s>]*)(.*)>";
	private static final String EGIS_CNEB = "http://www.cneb.gov.cn/(.*)/(.*)/(.*)/ARTI(.*).shtml";

	@Override
	protected void extract(CrawlURI curi) {
		String url = "";
		try {
			HttpRecorder hr = curi.getHttpRecorder();
			if (hr == null) {
				throw new IOException("HttpRecorder is null");
			}
			
			ReplayCharSequence rcs = hr.getReplayCharSequence();
			if (rcs == null) {
				return;
			}
			
			String context = rcs.toString();
			
			Pattern pattern = Pattern.compile(A_HREF, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(context);
			while(matcher.find()){
				url = matcher.group(2);
//				System.out.println("url:"+url);
//				url = url.replace("\"", "");
				//Filter single and double quotes link information.
				url = url.replace("\"", "").replace("\'", "");
				if(url.matches(EGIS_CNEB)){
//					System.out.println("true");
					curi.createAndAddLinkRelativeToBase(url, context, Link.NAVLINK_HOP);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
