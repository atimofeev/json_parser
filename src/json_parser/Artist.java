package json_parser;

public class Artist implements Comparable<Artist> {
    public String name;
    public int rank;

	public Artist() {
		}
	
  	public Artist(String name, int rank) {
    	this.name = name; 
    	this.rank = rank;
	}
  
	@Override
	public int compareTo(Artist o) {
		if (this.rank != o.rank){ //Sort by rank
			return this.rank - o.rank;
		}
		else { //If rank is identical, sort by name
			return this.name.compareTo(o.name);
      }
    }
}