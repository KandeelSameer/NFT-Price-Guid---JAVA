package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class LogicforFiles {

	public LogicforFiles() {
		// TODO Auto-generated constructor stub
	}
	public static ArrayList<String> readFromFile(String fileName){
		ArrayList<String> result = new ArrayList<>();
		BufferedReader br = null;

		try {

			br = Files.newBufferedReader(Paths.get("CollectionNameFile.txt"));

			String line;
			while ((line = br.readLine()) != null) {
				result.add(line);
			}
			if (br != null) {
				br.close();
			}
			return result;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} 
			
			
	}

}
