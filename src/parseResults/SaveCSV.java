package parseResults;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.jsoup.select.Elements;

import getResults.StatusApp;

public class SaveCSV {

	public static void save(String[][] data) throws FileNotFoundException {
		PrintWriter file = new PrintWriter("/home/mrmp/Desktop/results.csv");
		StatusApp status = new StatusApp();
		
		status.progressBar.setMaximum(data.length);
		for (int i=0; i<data.length; i++) {
			for (int j=0; j<data[i].length; j++) {
				file.print(data[i][j]
						.replace(new String("\""), new String("\"\"")));
			}
			file.println();
			status.progressBar.setValue(i+1);
		}
		
		file.close();
		System.out.print("Done");

	}
}
