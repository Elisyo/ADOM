package src;

import java.util.ArrayList;

public class MultiData {

	ArrayList<Integer> cities;
	int costA;
	int costB;
	int costPonderate;
	
	public MultiData(ArrayList<Integer> cities) {
		this.cities = cities;
	}

	@Override
	public String toString() {
		return "Coût pondéré entre A et B : "+ costPonderate +" (avec A :" + getCostA()+" et B :" + getCostB()+")";
	}
	
	public ArrayList<Integer> getCities() {
		return cities;
	}

	public void setCities(ArrayList<Integer> cities) {
		this.cities = cities;
	}

	public int getCostA() {
		return costA;
	}

	public void setCostA(int costA) {
		this.costA = costA;
	}

	public int getCostB() {
		return costB;
	}

	public void setCostB(int costB) {
		this.costB = costB;
	}

	public int getCostPonderate() {
		return costPonderate;
	}

	public void setCostPonderate(int costPonderate) {
		this.costPonderate = costPonderate;
	}
	
	
	
}
