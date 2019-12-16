package org.hana.pso;




import hana.Interface_WK;

import java.util.Random;
import java.util.Vector;

public class PSOProcess implements PSOConstants {
	
	
	private Vector<Particle> swarm = new Vector<Particle>();// vector contenant les particules
	private double[] pBest = new double[SWARM_SIZE];
	private Vector<Location> pBestLocation = new Vector<Location>();
	private double gBest;
	private Location gBestLocation;
	private int gBestLoc;
	private Location location;
	private double[] fitnessValueList = new double[SWARM_SIZE];//tableau contenant les valeurs de fonct objectif pour chaque particule
	static double [] FET_Part = new double [SWARM_SIZE];//Temps de fin d'execution des particules
	static double[] Location_TET;
	double max;
	double min_FET;
	double min_cost;
	private Location loc;
	String Log_map="Localities of Tasks's Best Solution :";
	Random generator = new Random();
;
	
	public void execute(double [][] TET,int n,double[][] DT , double [][] ET,double[] UC_VM, double Deadline,int [][] tasks_run,int[] temps_execute) {    //ajouter TET comme param d'entr�e pour pouvoir l'ajouter dans evaluate/updateFitnessList
		

     	initializeSwarm(n,tasks_run,ET,temps_execute);
		updateFitnessList(TET, n, DT, UC_VM, Deadline); 

		
		Mapping_Task mt =new Mapping_Task();

		for(int i=0; i<SWARM_SIZE; i++) { 
			
			pBest[i] = fitnessValueList[i];
			pBestLocation.add(swarm.get(i).getLocation());
		}

		
		int t = 0;
	//	double w;
		double err = 9999;
		
		
		
		while(t < MAX_ITERATION && err > ProblemSet.ERR_TOLERANCE) {
			

//--------------------------------------------------step 1 : Update pBest--------------------------------------------------//	
			for(int i=0; i<SWARM_SIZE; i++) {        
			
				if(fitnessValueList[i] < pBest[i]) {
					pBest[i] = fitnessValueList[i];
					pBestLocation.set(i, swarm.get(i).getLocation());

				}

				System.out.println("........pBest["+i+"]===== " + pBest[i]);
			}

//--------------------------------------------------step 2-0 : Deadline constraint--------------------------------------------------//	
		
			int nb_sol_feas=0;
			int bestParticleIndex=0;
			//FET: temps de fin d'executionnde Wf
			//**********C'est un bloc d'affichage pour verifier si FET < D et FET > D**********//
						
						//**********Debut Affichage**********//
		   	 for (int k=0; k< FET_Part.length ; k++) {
			    	if (FET_Part[k] > Deadline ){  // affichage pour cas > D
				    System.out.println("FET_Part["+k+"] : "+FET_Part[k]);
			    	System.out.println("FET_Part["+k+"] > "+Deadline);
		    	} // fin "if" (1)
			   	else{
			    		if (FET_Part[k] < Deadline){ // affichage pour cas < D
						System.out.println("FET_Part["+k+"] : "+FET_Part[k]);
					   	System.out.println("FET_Part["+k+"] < "+Deadline);
			    		}// fin  "if" (2)
			   	}// fin else
		   	 }
				        //**********Fin Affichage**********//


		   	 for (int k=0; k< FET_Part.length ; k++) {
//**********Chercher les sol faisables : C'est leurs FET < D; s'il ya une solution faisable on compare son cost avec min_cost**********//

		    	 if (FET_Part[k] < Deadline){ //Cas de FET_Part <D : sol faisable
		    		 nb_sol_feas++; // nb_sol faisable s'incr�mente
		    	
		// sinon on a trouv� au moins une sol faisable, elle prend min_FET et min_cost et on compare avec elle

		    		      if (nb_sol_feas ==1){ // si on a une sol feas elle sera best particule et puis on compare les autres avec cette sol jusqu'� trouver plus meilleure
		    		    	  min_FET= FET_Part[k];
		    		    	   min_cost=pBest[k];
					    	   bestParticleIndex = k;

		    		      }
		    	                      
		    			 if ( pBest[k] < min_cost) { // on prend min_cost en cas des sol fais
		    		         min_cost=pBest[k];
				         bestParticleIndex =k;
				  		}//fin "if"
		    			 else {
		    				 if (FET_Part[k] == min_FET){ // cas ou il ya 2 sol feas (c�d 2 particules qui ont meme FET et sont < D) on prend celle qui a min cost
		    					 if (pBest[k] < min_cost) {
		    						 	min_cost = pBest[k];
								    	   bestParticleIndex = k;

		    					 }//fin "if"
						    		  
					       } //fin "if"
		    				 }// fin "else"
		    			 
		    	 }// fin "if" < D 
		    		      else {
	 //**********S'il y a pas une solution faisable (c�d leurs FET > D) on compare leurs FET et on prend min ,en cas d'�galit� de FET on prend celle qui a min_cost**********//

		    		    	  if (nb_sol_feas == 0 ){ // dans ce cas on compare avec la 1�re particule
		    		    			min_cost = pBest[0];
		    		    			min_FET = FET_Part[0];
		    		    	  }
		    		    	  
		    		 		       if (FET_Part[k] < min_FET){ // si FET de particule k < min_FET (au debut c'est FET de 1�re particule) on la prend comme min_FET
		    		 		    	   min_FET = FET_Part[k] ;
		    		 		    	 bestParticleIndex = k;
		    		 		    	   
		    		 		       }//fin "if" 
		    		 		       else{
		    		 				       if (FET_Part[k] == min_FET){ // en cas d'agalit� de FET on compare leurs cost et on prend min
		    		 				    	   if (pBest[k] < min_cost) {// si cost du particule k < min_cost (au debut c'est cost de 1�re particule) on la prend comme min_cost
		    		 				    		   	min_cost = pBest[k];
		    		 				    	   bestParticleIndex = k;
		    		 				    	   }//fin "if"

		    		 			       } //fin "if"

		    		 		       }// fin "else"
		    		 		     
		    		 		   
		    		      }// fin "else"

		    	 
	  	 }// fin "for" 
		  	 
		  	
//--------------------------------------------------step 2-1 : Update gBest--------------------------------------------------//	

		        gBest = pBest[bestParticleIndex];
	    	    gBestLocation = swarm.get(bestParticleIndex).getLocation();
				System.out.println("........gBest===== " + gBest);	  
			
			
		//	w = W_UPPERBOUND - (((double) t) / MAX_ITERATION) * (W_UPPERBOUND - W_LOWERBOUND);
			System.out.println("...................w===== " + w);	
			
			for(int i=0; i<SWARM_SIZE; i++) {
				double r1 = generator.nextDouble();
				double r2 = generator.nextDouble();
				
				
				Particle p = swarm.get(i);
//--------------------------------------------------step 3 : Update Velocity--------------------------------------------------//	

				double[] newVel = new double[n];
				for (int j=0; j<n ; j++){  //faire boucle pour faire tous les cas "nb.illimit� de t�ches"
				newVel[j] = (w * p.getVelocity().getPos()[j]) + 
							(r1 * C1) * (pBestLocation.get(i).getLoc()[j] - p.getLocation().getLoc()[j]) +
							(r2 * C2) * (gBestLocation.getLoc()[j] - p.getLocation().getLoc()[j]);
				System.out.println( "new vel de j= "+j+" est �gale �  "+ newVel[j]);
			}
				Velocity vel = new Velocity(newVel);
				p.setVelocity(vel);
				
//--------------------------------------------------step 4 : Update Location--------------------------------------------------//	
		
				int[] newLoc = new int[n];
				for (int k=0; k<n ; k++){ //faire boucle pour faire tous les cas "nb.illimit� de t�ches"
				newLoc[k] = (int) (p.getLocation().getLoc()[k] + newVel[k]);
				System.out.println( " ********* ahmed : "+n+"   " +newLoc[k]);
				
				}
				Location loc = new Location(newLoc);
				p.setLocation(loc);
			}
		
			err = ProblemSet.evaluate(gBestLocation,n,TET,DT,UC_VM) - 0; // minimizing the functions means it's getting closer to 0
			
			//******************Affichage des it�rations de chaque particule*********************//
			System.out.println("*****ITERATION N�:::::" + t + ": "); 
			for (int k=0; k<n ; k++){ 
			System.out.println("     Best T"+k+" : " + (gBestLocation.getLoc()[k])); //enlever casting (int)
			} //fin for
			System.out.println("     Final Value: " +gBest);
			
			t++; // t incr�mente 	

			if (t < MAX_ITERATION){ //condition pour ne pas d�passer le nb it�rations demand�s
		    System.out.println("-----------The new Localities :-----------  ");
			updateFitnessList(TET,n,DT,UC_VM,Deadline);// il fait update de la fc fitness que si il respecte le nb_it�ration c�d t < MAX_ITERATION
 
			}// fin "if"
			
		}// fin while
		

		
		//******************Affichage de la meilleure solution (it�ration)*********************//
		System.out.println("\nSolution found at iteration " + (t-1 ) + ", the solutions is:");
		for (int k2=0; k2<n ; k2++){ 
			System.out.println("     Best T"+k2+": " + (gBestLocation.getLoc()[k2])); //enlever casting (int)
			Log_map= Log_map+" T"+k2+"--->" + (gBestLocation.getLoc()[k2]);
			
			
			//gBestLoc = gBestLocation.getLoc()[k2];
			//System.out.println("Log_map"+Log_map);
		}
	//	System.out.println("****************R�sultat affectation T�che-> Type de Ressource***********************");
	 
	}// fin execute
	
	 
	public int getgBestLoc() {
		return gBestLoc;
	}

	public void setgBestLoc(int gBestLoc) {
		this.gBestLoc = gBestLoc;
	}
 	
//--------------------------------------------------step 5 : M�thode d'initialiser Swarm--------------------------------------------------//	

	
	public static double[] getLocation_TET() {
		return Location_TET;
	}

	public static void setLocation_TET(double[] location_TET) {
		Location_TET = location_TET;
	}

	
	//*************************modifier====( *m ==> *(m+f) )*************************
	public void initializeSwarm(int n,int [][] tasks_run, double [][] ET,int[] temps_execute) {
		Particle p;
		System.out.println("************Affectaion al�atoires*************** ");
		
		for(int i=0; i<SWARM_SIZE; i++) { //SWARM_SIZE=taille du swarm=nbre de particule
			p = new Particle();
			
			int x=0;
			int[] loc = new int[n];
			System.out.println("************Particule n�*************** "+ i);
			
			for (int j=0; j<n ; j++)
			{
				 x=(int)(Math.random() * (m+f) );
				//if(x==3||x==4) 
				//{
					//while( x>=3 && CloudSim_generer_estimation.FogList.get((x-3)).getTacheExecute()[j]==0 )// pour v�rifier si cette machie execute la tache num�ro j 
					//{
						//x=(int)(Math.random() * (m+f) );
						
					//}
				 while((Test_main_2.tasks_run[j][x]==0) &&  ET[j][x]>temps_execute[j] )   // 
				 {
					 x=(int)(Math.random() * (m+f) );
				 }
					
				
			loc[j] = x ;  // pour affecter les VM al�atoirement aux t�ches (1...m) 
			System.out.println("**********Localit� de T["+(j+1)+"] :************** "+ loc[j] );
			 
			}
			
			Location location = new Location(loc);
		
			double[] vel = new double[n];
			for (int j=0; j<n ; j++){ 
			vel[j] = 0;

			}
			Velocity velocity = new Velocity(vel);
			
			p.setLocation(location);
			p.setVelocity(velocity);
			swarm.add(p);
			
		}
		
	}
	
//--------------------------------------------------step 6 : M�thode pour changer Fonction objective (liste de fitness value)--------------------------------------------------//	

	public void updateFitnessList(double [][] TET,int n, double[][] DT,double[] UC_VM, double Deadline) {  //ajouter TET comme param entr�e
	
		Mapping_Task mt =new Mapping_Task();

		for(int i=0; i<SWARM_SIZE; i++) {
			System.out.println("************Localities of Tasks:*************** ");
			
			fitnessValueList[i] = swarm.get(i).getFitnessValue(TET,n,DT,UC_VM); //utiliser TET dans getFitnessValue de la classe "Particle"
			System.out.println("Fitness Value ["+i+"] : "+fitnessValueList[i] );
			
			ProblemSet p = new ProblemSet();
			 double[] Location_FET = new double [n];
			 Location_FET = p.getLocation_FET();
	 		FET_Part[i]=Location_FET[mt.chercher_dernier_elet_WF(DT)];
           System.out.println("FET_Part["+i+"]: " +FET_Part[i]);
           
}
		
	}

	public static double[] getFET_Part() {
		return FET_Part;
	}

	public static void setFET_Part(double[] fET_Part) {
		FET_Part = fET_Part;
	}

	public String getLog_map() {
		return Log_map;
	}

	public void setLog_map(String log_map) {
		Log_map = log_map;
	}

	
	
	

}
