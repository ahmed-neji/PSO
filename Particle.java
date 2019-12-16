package org.hana.pso;


//Classe particule , contient des caractéristiques: Velocity & Location
public class Particle {
	private double fitnessValue;//valeur de la fonction objective
	private Velocity velocity;
	private Location location;
	
	public Particle() {
		super();
	}

	

	public Particle(double fitnessValue, Velocity velocity, Location location) {
		super();
		this.fitnessValue = fitnessValue;
		this.velocity = velocity;
		this.location = location;
	
	}

	public Velocity getVelocity() {
		return velocity;
	}

	public void setVelocity(Velocity velocity) {
		this.velocity = velocity;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public double getFitnessValue(double [][] TET,int n,double[][] DT,double[] UC_VM) { //ajouter TET en entrée
		
		fitnessValue = ProblemSet.evaluate(location,n,TET,DT,UC_VM);
		return fitnessValue;
	}
	
	
	
	
	
	
}
