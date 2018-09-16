package casm.gis.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.JsonNode;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.Response;

import casm.gis.config.ConstantParams;
import net.sf.json.JSONObject;

/*
 * Place name acquisition latitude and longitude tool class
 * 2017-04-30 16:38:47
 */
public class LngAndLatUtils {

	// Baidu map: only suitable for local services
	public static void getLngAndLatBMap(String inputPath, String outputPath) {
		String result = "";
		List<String> list = SimilaryUtils.getAllWords(inputPath);
		Map<String, Double> map = new HashMap<String, Double>();
		// String url =
		// "http://api.map.baidu.com/geocoder/v2/?address="+address+"&output=json&ak=ak";
		// String url =
		String url = "http://api.map.baidu.com/geocoder/v2/?address=" + list
				+ "&output=json&ak=your baidu map ak";
		String json = loadJSON(url);

		JSONObject obj = JSONObject.fromObject(json);
		if (obj.get("status").toString().equals("0")) {
			double lng = obj.getJSONObject("result").getJSONObject("location")
					.getDouble("lng");
			double lat = obj.getJSONObject("result").getJSONObject("location")
					.getDouble("lat");
			map.put("lng", lng);
			map.put("lat", lat);

		} else {
			System.out.println("未找到相匹配的经纬度！");
		}

		for (String w : list) {
			result += w + ConstantParams.TAB + "经度：" + map.get("lng")
					+ ConstantParams.TAB + "纬度：" + map.get("lat");
		}
		StringUtils.string2File(result, outputPath);
	}

	public static String loadJSON(String url) {
		StringBuilder json = new StringBuilder();
		try {
			URL oracle = new URL(url);
			URLConnection yc = oracle.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				json.append(inputLine);
			}
			in.close();
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return json.toString();
	}

	// Gaode Map
	public static String getLngAndLatAMap(String str) {

		// 1. Get latitude and longitude from Gaode map through java api
		String result = "";
		str = StringUtils.replaceBlank(str);
		String url = "http://restapi.amap.com/v3/geocode/geo?address=" + str
				+ "&output=JSON&key=your Gaode map key";

		AsyncHttpClientConfig.Builder builder = new AsyncHttpClientConfig.Builder();
		builder.setCompressionEnabled(true).setAllowPoolingConnection(true);
		builder.setRequestTimeoutInMs((int) TimeUnit.MINUTES.toMillis(1));
		builder.setIdleConnectionTimeoutInMs((int) TimeUnit.MINUTES.toMillis(1));

		AsyncHttpClient client = new AsyncHttpClient(builder.build());
		try {
			ListenableFuture<Response> future = client.prepareGet(url)
					.execute();
			JsonNode jsonNode = new com.fasterxml.jackson.databind.ObjectMapper()
					.readTree(future.get().getResponseBody());
			if (jsonNode.findValue("status").textValue().equals("1")) {
				JsonNode listSource = jsonNode.findValue("location");
				if (listSource == null) {
					return result = null;
				} else {
					result = listSource.asText();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (client != null) {
				client.close();
			}
		}
		return result;
	}
}