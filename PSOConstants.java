package org.hana.pso;



public interface PSOConstants { //cette classe contient les valeurs des constantes utilisées
	
	//Les paramètres de PSO
	int SWARM_SIZE = 3;//nombre de particule=taille de swarm
	int MAX_ITERATION =3;//nombre d'iteration
	double C1 = 2.0;
	double C2 = 2.0;
	double w=0.5;
	static double[] UC_VM=new double[5];
	
	//-----------------------------------------
	static int m =3; //Nbre de type de VMs
 	double Deadline=200; // deadline
	static int c = 4; //caractéristiques de task
	
	static int f= 2; // nombre de fog devise 
	static  int[] temps_execute = new int[9];
	static int [][] tasks_run_g = new int [9][5];


	
	}
	
	
	
