package me.silvertriclops.getcarescraper;

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

}
