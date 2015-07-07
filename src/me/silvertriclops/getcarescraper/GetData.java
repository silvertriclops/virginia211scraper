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


public class GetData {
	
	public static void main(String[] args) throws Exception {
		String needName = "Education";
		String programName = "Education";
		String needId = "4";
		String programTypeName = "College/University";
		get(needId, needName, programTypeName, programName);
	}

	public static void get(String needId, String needName, String programTypeName, String programName) throws Exception {
		System.setProperty("jsse.enableSNIExtension", "false");
		
		String page = getPage(needId, programTypeName);
		Elements results = resultList(page);
		int rlength = results.size();
		String data[][] = new String[rlength][10];
		
		for (int i=0; i<rlength; i++) {
			String[] result = new String[10];
			result = extractDataFromItem(results, i, needName, programName); // save each to new row in data[][]
			for (int c=0; c<10; c++) {
				data[i][c] = result[c];
			}
		}
		
		for (int i=0; i<rlength; i++) {
			for (int c=0; c<10; c++) {
				System.out.println(data[i][c]);
			}
			System.out.println();
		}
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
	
	/*public static String[] extractData(String d) {
		String[] data = new String[5];
		
		Document doc = Jsoup.parse(d);
		Element results = doc.getElementById("results");
		//Elements resultlist = results.getElementsByClass("results");
		Elements resultlist = results.getElementsByTag("li");
		resultlist.select(".results").remove();
		//return results.toString();
		//System.out.println(resultlist.size());
		//return resultlist.get(4).toString();
		data[0] = resultlist.get(4).getElementsByClass("results").text();
		//data[1] = resultdoc.select("a").remove().toString();
		System.out.print(resultlist.get(3).select(".results").remove().toString());
		//System.out.println(resultlist.get(4));
		//return resultlist.toString();
		return data;
	}*/
	
	public static Elements resultList(String r) {
		Document doc = Jsoup.parse(r);
		Element results = doc.getElementById("results");
		return results.getElementsByTag("li");
	}
	
	/*public static String getTitle(String d) {
		Document doc = Jsoup.parse(d);
		Element results = doc.getElementById("results");
		Elements resultlist = results.getElementsByTag("li");
		return resultlist.get(4).getElementsByClass("results").text();
	}*/
	
	public static String[] extractDataFromItem(Elements results, int num, String needName, String programName) {
		String[] data = new String[10];
		Element i = results.get(num);
		
		// Need
		data[0] = needName;
		
		// Program
		data[1] = programName;
		
		// Agency
		data[2] = i.getElementsByClass("results").text();
		
		// Parent Organization
		Pattern po_p = Pattern.compile("Parent Organization:<strong> [\\w\\t !\"#$%&'()*+,./:;<=>?@\\[\\\\\\]`{|}~^-]+</strong> <br>");
		Matcher po_m = po_p.matcher(i.toString());
		if (po_m.find()) {
			data[3] = po_m.group(0).replace("Parent Organization:<strong> ", "").replace("</strong> <br>", "").trim();
		}
		
		// Separate Address, State, Zip
		String fulladdr = i.toString()
				.replaceAll("<li> <strong> <a class=\"results dontDisabled\" href=\"/211provider/consumer/report\\.do\\?directoryEntryId=\\d+\">[\\w\\t !\"#$%&'()*+,./:;<=>?@\\[\\\\\\]`{|}~^-]+</a> </strong><br> Parent Organization:<strong> [\\w\\t !\"#$%&'()*+,./:;<=>?@\\[\\\\\\]`{|}~^-]+</strong> <br> ", "")
				.replaceAll(" <a href=\"\\bhttp://maps\\.google\\.com/maps\\?q(?:=[\\w!\"#$%'()*+,./:;<=>?@\\[\\\\\\]`{|}~^-]*)?\" style=\"font-weight: normal;\">view map</a> - \\d+ mile\\(s\\)<br> \\(\\d{3}\\) ?\\d{3}-\\d{4} </li>", "")
				.trim();
		String zip = // \d{5}(?:-\d+)?$
		
		// Address
		/*Pattern addr_p = Pattern.compile("</strong> <br> [\\w\\t !\"#$%&'()*+,./:;<=>?@\\[\\\\\\]`{|}~^-]+? , ");
		Matcher addr_m = addr_p.matcher(i.toString());
		if (addr_m.find()) {
			data[4] = addr_m.group(0).replace("</strong> <br> ", "").replaceAll("(?: , ?|, $)", "").trim();
		}*/
		data[4] = fulladdr;
		
		// City
		Pattern city_p = Pattern.compile("(?: , ?|, )[\\t !\"#$%&'()*+,./:;<=>?@A-Z\\[\\\\\\]_`a-z{|}~^-]+, [A-Z]{2}");
		Matcher city_m = city_p.matcher(i.toString());
		if (city_m.find()) {
			data[5] = city_m.group(0).replaceAll("^(?: , ?|, )", "").trim().replaceAll(", [A-Z]{2}$", "");
		}
		
		// State
		Pattern st_p = Pattern.compile("(?: , ?|, )[\\t !\"#$%&'()*+,./:;<=>?@A-Z\\[\\\\\\]_`a-z{|}~^-]+, [A-Z]{2}");
		Matcher st_m = st_p.matcher(i.toString());
		if (st_m.find()) {
			data[6] = st_m.group(0).replaceAll("^(?: , ?|, )", "").trim().replaceAll("^[\\w\\t !\"#$%&'()*+,./:;<=>?@\\[\\\\\\]`{|}~^-]+, ", "");
		}
		
		// Zip
		Pattern zip_p = Pattern.compile(", [A-Z]{2} [\\d-]{5,10} <a");
		Matcher zip_m = zip_p.matcher(i.toString());
		if (zip_m.find()) {
			data[7] = zip_m.group(0).replaceAll(", [A-Z]{2} ", "").replace(" <a", "");
		}
		
		// Phone
		Pattern ph_p = Pattern.compile("\\(\\d{3}\\) ?\\d{3}-\\d{4}( Ext \\d+)?");
		Matcher ph_m = ph_p.matcher(i.toString());
		if (ph_m.find()) {
			String ph = ph_m.group(0);
			if (!ph.contains(") ")) {
				ph.replace(")", ") ");
			}
			data[8] = ph;
		}
		
		// URL
		Pattern url_p = Pattern.compile("href=\"/211provider/consumer/report\\.do\\?directoryEntryId=[0-9]+\">");
		Matcher url_m = url_p.matcher(i.toString());
		if (url_m.find()) {
			data[9] = url_m.group(0).replace("href=\"", "https://211.getcare.com").replace("\">", "").trim();
		}
		
		return data;
		
	}

}
