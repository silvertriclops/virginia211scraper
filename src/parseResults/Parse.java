package parseResults;

import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parse {
	/*
	 * data[i][0] = Need
	 * data[i][1] = Program
	 * data[i][2] = Agency
	 * data[i][3] = Parent Organization
	 * data[i][4] = Street Address
	 * data[i][5] = City
	 * data[i][6] = State
	 * data[i][7] = Zip
	 * data[i][8] = Phone
	 * data[i][9] = URL
	 */

	public static void main(String[] args) throws FileNotFoundException {
		String[][] results = getResults();
		String[][] data = new String[results.length][10];
		for (int i=0; i<results.length; i++) {
			data[i][0] = results[i][0]; // Need
			data[i][1] = results[i][1]; // Program
			String[] parsed = parseData(results[i][2]);
			for (int j=0; j<=7; j++) {
				data[i][j+2] = parsed[j];
			}
		}
		
		SaveCSV.save(data);
	}
	
	public static String[][] getResults() {
		int length = results.R1.a.length + results.R2.a.length + results.R3.a.length + results.R4.a.length;
		String[][] data = new String[length][3];
		int n = 0;
		for (int i=0; i<results.R1.a.length; i++) {
			data[n] = results.R1.a[i];
			n++;
		}
		for (int i=0; i<results.R2.a.length; i++) {
			data[n] = results.R2.a[i];
			n++;
		}
		for (int i=0; i<results.R3.a.length; i++) {
			data[n] = results.R3.a[i];
			n++;
		}
		for (int i=0; i<results.R4.a.length; i++) {
			data[n] = results.R4.a[i];
			n++;
		}
		return data;
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
