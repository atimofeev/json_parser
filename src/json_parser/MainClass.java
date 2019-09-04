package json_parser;

import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class MainClass{
	public static void main(String[] args) throws Exception{
		ArrayList<Artist> good_artists = new ArrayList<Artist>();
		ArrayList<Artist> bad_artists = new ArrayList<Artist>();
    
		String in_file_path = args[0]; //Passing file paths as arguments
		String out_file_path = args[1];
		
		String filestring = new String();
		try{
			filestring = Files.readString(Path.of(in_file_path)); //Reading JSON file to string
		}
		
		catch(java.nio.file.NoSuchFileException e) { //If input file can't be found - exit
			System.out.println("Can't find or access: " + in_file_path);
			System.exit(0);
		}
		
		JSONObject jo_file = new JSONObject(filestring); //Converting string to JSONObject
		JSONArray ja_artists = jo_file.getJSONArray("artists"); //Getting array of artists
		
		for (int i = 0; i < ja_artists.length(); i++) { 
			JSONObject jo_artist = ja_artists.getJSONObject(i); //Getting artists one-by-one
            
			ArrayList<String> al_genres = new ArrayList<String>(); //Genres filtration
            JSONArray ja_genres = jo_artist.getJSONArray("genres");
            for (int j = 0; j < ja_genres.length(); j++) {
            	String genre = ja_genres.getString(j);
            	al_genres.add(genre);
            }
      
            if (!al_genres.contains("POP")) continue; //Continues to next iteration, if artist has no "POP" in genres
      
            String name = jo_artist.getString("name");
      
            try{ 								//Trying to get rank
            	int rank = jo_artist.getInt("rank");     
            	Artist A1 = new Artist(name, rank);
            	good_artists.add(A1);
            }
        
            catch(JSONException e){ 			//In case there is no rank specified in JSON, add Artist to bad_artists
            	int rank = 0;
            	Artist A1 = new Artist(name, rank);
            	bad_artists.add(A1);
            }
		}
    
		good_artists.sort(null);  //Sort with help of Comparable interface
    
		PrintWriter out = new PrintWriter(out_file_path); //Output to file
     
		for (int i = 0; i < good_artists.size(); i++) { 
			String text = "Name: " + good_artists.get(i).name + " Rank: " + good_artists.get(i).rank;
			out.println(text);
		}
    
		for (int i = 0; i < bad_artists.size(); i++) { 
			String text = "Name: " + bad_artists.get(i).name;
			out.println(text);
		}
    
		out.close();
	}
}