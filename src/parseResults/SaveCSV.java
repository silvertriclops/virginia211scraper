package parseResults;

import getResults.StatusApp;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class SaveCSV {

	public static void save(String[][] data, StatusApp status) throws FileNotFoundException {
		PrintWriter file = new PrintWriter(System.getProperty("user.home") + "/Documents/results.csv");
		
		System.out.println("Saving...");
		status.progressBar.setMaximum(data.length);
		status.progressBar.setValue(0);
		status.txtStatus.setText("Saving...");
		for (int i=0; i<data.length; i++) {
			for (int j=0; j<data[i].length; j++) {
				String thing = data[i][j];
				if (thing == null) {
					thing = "";
				}
				file.print(
						new String("\""
								+ (thing.replaceAll("\"", "\"\""))
								+ "\","
							)
						);
			}
			file.println();
			status.progressBar.setValue(i+1);
		}
		
		file.close();
		System.out.print("Done");

	}
}
