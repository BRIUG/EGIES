package casm.gis.util;

import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;

import casm.gis.config.ConstantParams;

public class HtmlUtils {

	/*
	 * Get all the content of the webpage: all the content of the label, text, etc. addressUrl: webpage link, webpage address encoding: the encoding format of the webpage, webpage encoding
	 * return：web pages
	 * 
	 * 2017-04-02 14:18:46
	 */
	public static String getHtmlContent(String addressUrl, String encoding) {
		String result = "";
		if (StringUtils.isEmpty(addressUrl)) {
			return "";
		}

		try {
			URL url = new URL(addressUrl);
			InputStreamReader isr = new InputStreamReader(url.openStream(),
					encoding);
			BufferedReader br = new BufferedReader(isr);
			String temp = "";
			while ((temp = br.readLine()) != null) {
				result += temp + ConstantParams.CHANGE_LINE;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/*
	 * Method overloading for full content acquisition of web pages: Integrating web content and web page coding into one method. Get web content and get webpage code automatically
	 * 2017-04-02 15:41:47
	 */
	public static String getHtmlContent(String addressUrl) {
		String result = "";
		if (StringUtils.isEmpty(addressUrl)) {
			return "";
		}
		try {
			URL url = new URL(addressUrl);
			String code = getCode(url);
			// Determine if the page code is obtained, and if not, use the default UTF-8 code.
			if (StringUtils.isEmpty(code)) {
				code = "UTF-8";
			}

			result = getHtmlContent(addressUrl, code);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/*
	 * Use the cpdetector tool jar package to automatically obtain the encoding format of the web page. Obtain the webpage encoding.
	 *  2017-04-02 15:21:46
	 */
	public static String getCode(URL url) {
		String result = "";
		try {
			CodepageDetectorProxy detector = CodepageDetectorProxy
					.getInstance();
			detector.add(JChardetFacade.getInstance());
			Charset charsert = detector.detectCodepage(url);
			result = charsert.name();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/*
	 * Use the HtmlParser tool to get the specified content of the webpage
	 * addressUrl:web url
	 *  2017-04-03 09:49:55
	 */
	public static String getHtmlContentUseParser(String addressUrl,
			String encoding) {
		String result = "";
		if (StringUtils.isEmpty(addressUrl)) {
			return result;
		}

		if (StringUtils.isEmpty(encoding)) {
			encoding = "UTF-8";
		}

		try {
			Parser parser = new Parser(addressUrl);
			parser.setEncoding(encoding);
			
/*			NodeFilter nodeFilter = new AndFilter(new TagNameFilter("p"),
					new HasAttributeFilter("class", "title"));
			NodeList nodeList = parser.parse(nodeFilter);
			parser.reset();
			Node node = nodeList.elementAt(0);
			result = node.toPlainTextString();*/
			
	/*		NodeFilter nodeFilter = new AndFilter(new TagNameFilter("p"),new HasAttributeFilter("class", "subhead"));
			
			NodeList nodeList = parser.parse(nodeFilter);
			parser.reset();
			Node node = nodeList.elementAt(0);
			result = node.getFirstChild().toPlainTextString();*/
			
			NodeFilter nodeFilter = new AndFilter(new TagNameFilter("p"),new HasAttributeFilter("class", "subhead"));
			
			NodeList nodeList = parser.parse(nodeFilter);
			parser.reset();
			Node node = nodeList.elementAt(0);
			result = node.getLastChild().toPlainTextString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
