package me.silvertriclops.getcarescraper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class test {

	public static void main(String[] args) throws Exception {
		//String s = "<li> <strong> <a class=\"results dontDisabled\" href=\"/211provider/consumer/report.do?directoryEntryId=237848\">Autism Services - Richmond, Commonwealth Autism</a> </strong><br> Parent Organization:<strong> Commonwealth Autism</strong> <br> 4108 East Parham Road , Richmond, VA 23228 <a href=\"http://maps.google.com/maps?q=4108+East+Parham+Road+Richmond+VA+23228\" style=\"font-weight: normal;\">view map</a> - 28 mile(s)<br> (804)355-0300 </li>\r\n";
		//regex(s);
		get();
	}
	
	public static void regex(String i) {
		Pattern url_p = Pattern.compile("<br> \\(\\d{3}\\) ?\\d{3}-\\d{4}( Ext \\d+)? </li>");
		Matcher url_m = url_p.matcher(i.toString());
		if (url_m.find()) {
			System.out.print(url_m.group(0).replace("<br> ", "").replace(" </li>", "").replace(")", ") "));
		}
		
		
	}
	
	public static void get() throws Exception {
		System.setProperty("jsse.enableSNIExtension", "false");
		String serviceAreaType = "2";
		String cityCounty = "Petersburg City";
		String needId = "4";
		String programTypeName = "College/University";
		
		String page = getPage(needId, programTypeName);
		Elements results = resultList(page);
		System.out.print(results);
	}
	
	public static String getPage(String i, String p) throws Exception {
		StringBuilder moo = new StringBuilder();
		URL url = new URL("https://211.getcare.com/211provider/consumer/listingsearch211.do");
		Map<String,Object> params = new LinkedHashMap<>();
		params.put("serviceAreaType", "2");
		params.put("cityCounty", "Petersburg City");
		params.put("needId", i);
		params.put("programTypeName", p);
		
		StringBuilder postData = new StringBuilder();
		for (Map.Entry<String,Object> param : params.entrySet()) {
		    if (postData.length() != 0) postData.append('&');
		    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
		    postData.append('=');
		    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
		}
		byte[] postDataBytes = postData.toString().getBytes("UTF-8");
		
		HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
		conn.setDoOutput(true);
		conn.getOutputStream().write(postDataBytes);
		
		Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		for (int c = in.read(); c != -1; c = in.read()) {
			//System.out.print((char)c);
			moo.append((char)c);
		}
		String html = moo.toString();
		return html;
    }
	
	public static Elements resultList(String r) {
		Document doc = Jsoup.parse(r);
		Element results = doc.getElementById("results");
		return results.getElementsByTag("li");
	}

}
