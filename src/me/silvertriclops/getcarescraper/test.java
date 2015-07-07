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
		String s = "<li> <strong> <a class=\"results dontDisabled\" href=\"/211provider/consumer/report.do?directoryEntryId=279817\">Housing Assistance - Alexandria, Final Salute, Inc.</a> </strong><br> Parent Organization:<strong> Final Salute, Inc.</strong> <br> 2331 Mill Road, Suite 100 , Alexandria, VA 22314 <a href=\"http://maps.google.com/maps?q=2331+Mill+Road,+Suite+100+Alexandria+VA+22314\" style=\"font-weight: normal;\">view map</a> - 110 mile(s)<br> (703)224-8845 </li>";
		//regex(s);
		//get();
		//address(s);
		String[] data = parseData(s);
		for (int i=0; i<data.length; i++) {
			System.out.println(data[i]);
		}
	}
	
	public static void address(String i) {
		/*String fulladdr = i.toString()
				.replaceAll(".+</strong> <br>", "")
				.replaceAll("<a href=\".+", "")
				.trim();*/
		Pattern p_zip = Pattern.compile(" , (?!.*( , ))[\\t !\\\"#$%&'()*+,./:;<=>?@A-Z\\[\\\\\\]_`a-z{|}~^-]+");
		Matcher m_zip = p_zip.matcher(i);
		if (m_zip.find()) {
			System.out.println(m_zip.group(0).toString().replaceAll("^ , ", ""));
			i = i.replaceAll(" , (?!.*( , ))[\\t !\\\"#$%&'()*+,./:;<=>?@A-Z\\[\\\\\\]_`a-z{|}~^-]+", "").trim().replaceAll(",$", "").trim();
		}
		System.out.print(i);
	}
	
	public static void regex(String i) {
		Pattern url_p = Pattern.compile(" , (?!.*( , ))[\\t !\\\"#$%&'()*+,./:;<=>?@A-Z\\[\\\\\\]_`a-z{|}~^-]+");
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
	
	public static String[] parseData(String r) {
		String[] data = new String[8];
		
		// Agency
		//data[2] = r;
		data[0] = Jsoup.parse(r).getElementsByClass("results").text();
		
		// Parent Organization
		Pattern po_p = Pattern.compile("Parent Organization:<strong> [\\w\\t !\"#$%&'()*+,./:;<=>?@\\[\\\\\\]`{|}~^-]+</strong> <br>");
		Matcher po_m = po_p.matcher(r.toString());
		if (po_m.find()) {
			data[1] = po_m.group(0).replace("Parent Organization:<strong> ", "").replace("</strong> <br>", "").trim();
		}
		
		// Separate Address, State, Zip
		String fulladdr = r.toString()
				.replaceAll(".+</strong> <br>", "")
				.replaceAll("<a href=\".+", "")
				.trim();
		
		// Zip
		Pattern p_zip = Pattern.compile("\\d{5}(-\\d+)?$");
		Matcher m_zip = p_zip.matcher(fulladdr);
		if (m_zip.find()) {
			data[5] = m_zip.group(0).toString();
			fulladdr = fulladdr.replaceAll("\\d{5}(-\\d+)?$", "").trim();
		}
		
		// State
		Pattern p_st = Pattern.compile("[A-Z]{2}$");
		Matcher m_st = p_st.matcher(fulladdr);
		if (m_st.find()) {
			data[4] = m_st.group(0).toString();
			fulladdr = fulladdr.replaceAll("[A-Z]{2}$", "").trim().replaceAll(",$", "").trim();
		}
		
		// City 
		Pattern p_city = Pattern.compile(" , (?!.*( , ))[\\t !\\\"#$%&'()*+,./:;<=>?@A-Z\\[\\\\\\]_`a-z{|}~^-]+");
		Matcher m_city = p_city.matcher(fulladdr);
		if (m_city.find()) {
			data[3] = m_city.group(0).toString().replaceAll("^ , ", "");
			fulladdr = fulladdr.replaceAll(" , (?!.*( , ))[\\t !\\\"#$%&'()*+,./:;<=>?@A-Z\\[\\\\\\]_`a-z{|}~^-]+", "").trim();
		}
		
		// Street Address
		data[2] = fulladdr.replace(" , ", ", ");
		
		// Phone
		Pattern ph_p = Pattern.compile("\\(\\d{3}\\) ?\\d{3}-\\d{4}( Ext \\d+)?");
		Matcher ph_m = ph_p.matcher(r.toString());
		if (ph_m.find()) {
			String ph = ph_m.group(0);
			if (!ph.contains(") ")) {
				ph = ph.replace(")", ") ");
			}
			data[6] = ph;
		}
		
		// URL
		Pattern url_p = Pattern.compile("href=\"/211provider/consumer/report\\.do\\?directoryEntryId=[0-9]+\">");
		Matcher url_m = url_p.matcher(r.toString());
		if (url_m.find()) {
			data[7] = url_m.group(0).replace("href=\"", "https://211.getcare.com").replace("\">", "").trim();
		}
		
		return data;
		
	}

}
