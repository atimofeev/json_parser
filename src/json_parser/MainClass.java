package json_parser;

import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainClass {
	public static void main(String[] args) {

		if (args.length != 2) { // Checking for arguments
			System.out.println("Please provide two arguments (<in_file> <out_file>)");
			System.exit(1);
		}

		String inFilePath = args[0]; // Passing file paths as arguments
		String outFilePath = args[1];

		PrintWriter out = null; // Check if destination file is writable
		try {
			out = new PrintWriter(outFilePath);
		} catch (Exception e) {
			System.out.println("Can't write file.");
			System.exit(3);
		}

		String jsonString = null; // Check if input file is readable
		try {
			jsonString = new String(Files.readAllBytes(Paths.get(inFilePath))); // Reading JSON file to string
		} catch (Exception e) { // If input file can't be found - exit
			System.out.println("Can't find or access: " + inFilePath);
			System.exit(2);
		}

		ArrayList<Artist> goodArtists = new ArrayList<Artist>();
		ArrayList<Artist> badArtists = new ArrayList<Artist>();

		JSONObject joFile = null;
		JSONArray jaArtists = null;
		try { // Check for JSON syntax errors
			joFile = new JSONObject(jsonString); // Converting string to JSONObject
			jaArtists = joFile.getJSONArray("artists"); // Getting array of artists
		} catch (JSONException e) {
			System.out.println("There is a syntax error in the JSON or a duplicated key.");
			System.exit(4);
		}

		for (int i = 0; i < jaArtists.length(); i++) {
			String name = null;
			try { // Check for any issues during Artist creation
				ArrayList<String> alGenres = new ArrayList<String>(); // Genres filtration
				JSONObject joArtist = jaArtists.getJSONObject(i); // Getting artists one-by-one
				JSONArray jaGenres = joArtist.getJSONArray("genres");
				for (int j = 0; j < jaGenres.length(); j++) {
					try {
						String genre = jaGenres.getString(j);
						alGenres.add(genre);
					} catch (JSONException e) {
					}
				}

				if (!alGenres.contains("POP"))
					continue; // Continues to next iteration, if artist has no "POP" in genres

				name = joArtist.getString("name");

				try { // Trying to get rank
					int rank = joArtist.getInt("rank");
					Artist A1 = new Artist(name, rank);
					goodArtists.add(A1);
				} catch (JSONException e) { // In case there is no rank specified in JSON, add Artist to bad_artists
					int rank = 0;
					Artist A1 = new Artist(name, rank);
					badArtists.add(A1);
				}
			} catch (JSONException e) {
				System.out.println("Exception in processing artist" + name);
			}
		}

		goodArtists.sort(null); // Sort with help of Comparable interface

		for (int i = 0; i < goodArtists.size(); i++) { // Output to file
			String text = goodArtists.get(i).name + "   Rank: " + goodArtists.get(i).rank;
			out.println(text);
		}
		if (badArtists.size() > 0) {
			out.println();
			out.println("Following artists have no defined rank:");
			for (int i = 0; i < badArtists.size(); i++) {
				String text = badArtists.get(i).name;
				out.println(text);
			}
		}

		out.close();
	}
}