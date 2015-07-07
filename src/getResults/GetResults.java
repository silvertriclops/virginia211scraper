package getResults;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GetResults {

	public static void main(String[] args) throws Exception {
		System.setProperty("jsse.enableSNIExtension", "false");
		/*String[][] programs = {
				{"1", "Arts/Culture/Recreation", "Arts", "Arts"},
				{"1", "Arts/Culture/Recreation", "Camps/Parks", "Camps/Parks"},
				{"1", "Arts/Culture/Recreation", "Clubs", "Clubs"}
		};*/
		String[][] programs = Strings.programs;
		PrintWriter file = new PrintWriter("/home/mrmp/Desktop/results.txt");
		StatusApp status = new StatusApp();
		
		status.progressBar.setMaximum(programs.length);
		for (int i=0; i<programs.length; i++) {
			String page = getPage(programs[i][0], programs[i][2]);
			Elements results = resultList(page);
			for (int m=0; m<results.size(); m++) {
				String escaped = results.get(m).toString()
						.replace(new String("\\"), new String("\\\\"))
						.replace(new String("\""), new String("\\\""));
				String line = ("{\""
						+ programs[i][1]
						+ "\", \""
						+ programs[i][3]
						+ "\", \""
						+ escaped
						+ "\"},");
				file.println(line);
			}
			status.progressBar.setValue(i+1);
		}
		
		file.close();
		System.out.print("Done");
		
		//System.out.print(results);
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
		Elements resultList = results.getElementsByTag("li");
		for (int i=0; i<resultList.size(); i++) {
			
		}
		return results.getElementsByTag("li");
	}

}
