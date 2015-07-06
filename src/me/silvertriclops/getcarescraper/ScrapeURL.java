package me.silvertriclops.getcarescraper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ScrapeURL {

	public static void main(String[] args) {
		System.setProperty("jsse.enableSNIExtension", "false");
		
		try {
			String page = getPage("https://211.getcare.com/211provider/consumer/report.do?directoryEntryId=236434");
			getThings(page);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getPage(String iurl) throws Exception {
		StringBuilder moo = new StringBuilder();
		URL url = new URL(iurl);
		HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Accept", "*/*");
		//con.setRequestProperty("Accept-Encoding", "gzip, deflate");
		con.setUseCaches(false);
		con.setDoInput(true);
		con.setDoOutput(true);
	/*	InputStream ins = con.getInputStream();
		InputStreamReader isr = new InputStreamReader(ins);
		BufferedReader in = new BufferedReader(isr);
		String output = in.readLine();
		in.close();*/
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		//System.out.print(response);
		return response.toString();
    }
	
	public static String getThings(String i) {
		Document doc = Jsoup.parse(i);
		Elements row = doc.getElementsByTag("tr");
		System.out.print(row);
		
		return null;
	}

}
