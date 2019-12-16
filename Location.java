package org.hana.pso;

 
//Class Location pour représenter location de tâche sur VM

public class Location {
	// store the Location in an array to accommodate multi-dimensional problem space
	private int[] loc;

	public Location(int[] newLoc) {
		super();
		this.loc = newLoc;
	}

	public int[] getLoc() {
		return loc;
	}

	public void setLoc(int[] loc) {
		this.loc = loc;
	}
	
}
