package src;

public class Data {

	private int city;
	private int coord_X;
	private int coord_Y;
	
	public Data() {
		
	}
	
	public Data(int city, int coord_X, int coord_Y) {
		this.city=city;
		this.coord_X=coord_X;
		this.coord_Y=coord_Y;
	}

	public String toString() {
		return city + " : " + coord_X + ", " + coord_Y;
	}
	
	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public int getCoord_X() {
		return coord_X;
	}

	public void setCoord_X(int coord_X) {
		this.coord_X = coord_X;
	}

	public int getCoord_Y() {
		return coord_Y;
	}

	public void setCoord_Y(int coord_Y) {
		this.coord_Y = coord_Y;
	}
}
