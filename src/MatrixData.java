package src;

public class MatrixData {

	private double distance;
	private boolean ancestor;
	
	public MatrixData() {
		this.distance=0;
	}
	
	public MatrixData(double distance) {
		this.distance=distance;
		this.ancestor=false;
	}
	
	public String toString() {
		return distance+"";
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public boolean isAncestor() {
		return ancestor;
	}

	public void setAncestor(boolean ancestor) {
		this.ancestor = ancestor;
	}
}
