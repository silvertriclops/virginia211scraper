package me.silvertriclops.getcarescraper;

import java.io.PrintWriter;

import org.jsoup.select.Elements;

import getResults.StatusApp;
import getResults.Strings;

public class Run {

	public static void main(String[] args) throws Exception {
		//System.out.print(Strings.programs.length);
		scrape();
	}
	
	public static void scrape() throws Exception {
		//System.setProperty("jsse.enableSNIExtension", "false");
		String[][] data = new String[10][Strings.programs.length];
		String[][] programs = {
				{"1", "Arts/Culture/Recreation", "Arts", "Arts"},
				{"1", "Arts/Culture/Recreation", "Camps/Parks", "Camps/Parks"},
				{"1", "Arts/Culture/Recreation", "Clubs", "Clubs"}
		};
		for (int a=0; a<programs.length; a++) {
			//String result = 
			GetData.get(programs[a][0], programs[a][1], programs[a][2], programs[a][3]);
		}
	}
	
	public static void scrape2() throws Exception {
		System.setProperty("jsse.enableSNIExtension", "false");
		String[][] programs = Strings.programs;
		PrintWriter file = new PrintWriter("/home/mrmp/Desktop/results.txt");
		StatusApp status = new StatusApp();
		
		status.progressBar.setMaximum(programs.length);
		for (int i=0; i<programs.length; i++) {
			String page = getResults.GetResults.getPage(programs[i][0], programs[i][2]);
			Elements results = getResults.GetResults.resultList(page);
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
	}

}
