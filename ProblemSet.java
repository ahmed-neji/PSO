package org.hana.pso;

import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.ArrayList;

  
public class ProblemSet implements PSOConstants{

	
	public static final double VEL_LOW =1;
	public static final double VEL_HIGH =2; 
	
	public static final double ERR_TOLERANCE = 1E-20; // the smaller the tolerance, the more accurate the result, 
	                                                  // but the number of iteration is increased
	

	static double [] FET_Part = new double [SWARM_SIZE];
	
	static double [] Location_FET ;
	
	
	@SuppressWarnings("static-access")
	
	
	
	public static double evaluate(Location location,int n,double [][] TET,double[][] DT,double[] UC_VM) {
		double result = 0;
	
	//	double[][] TC_VM = new double[n][m]; 
		double[][] ET = new double [n][m+f]; 	
		double [] Location_TET = new double[n];
	    double [] Location_Cost = new double[n];
	    double [] Location_SET = new double [n];
        Location_FET =  new double [n];
        double max;

		Mapping_Task mt = new Mapping_Task();
		
	

		
		for (int i=0 ; i<n ; i++){	
			 
			//--------------------Step 1 :Test pour que indice de VM ne dépasse pas m ne sera pas < 0-----------------------------//			

			if(location.getLoc()[i]>=m+f || location.getLoc()[i]<0 )
			{
			    //générer pour cette position (i) une valeur entre 0 et m-1 (comme dans l'initialisation) 
				int xx = (int)(Math.random() * (m+f) );
				while ((Test_main_2.tasks_run[i][xx]==0) &&  TET[i][xx]<temps_execute[i])
					 xx = (int)(Math.random() * (m+f) );
				location.getLoc()[i] =xx;
						
			}
			//-------------------Step 2 : Donner Cost de particule-------------------------------//			
			
	 	
			    Location_TET[i] = TET[i][location.getLoc()[i]]; //enlever casting (int)

			    System.out.println("Localité de T["+i+"] : "+ (location.getLoc()[i]));
			    System.out.println("Location_TET["+i+"] : "+ Location_TET[i]);

		   //FF   Location_Cost[i] = mt.calcul_Nb_VM(Location_TET[i]) * mt.VM_Cost[ location.getLoc()[i]];
		       Location_Cost[i] = mt.calcul_Nb_VM(Location_TET[i]) * UC_VM[ location.getLoc()[i]];
			    
				   		
		    result = result + Location_Cost[i];  // Résultat final avant consolidation
		} // fin boucle "i"

		     //-------------------Step 3 : Calcul des SET et FET des tâches de chaque particule-------------------------------//			

		
		
         	 
          	 Location_SET[0] =0;
        	 Location_FET[0]=Location_SET[0]+Location_TET[0];
        	 System.out.println("Location_FET[0]==="+Location_FET[0]);
        	 
         
		
		  
     //*****************Chercher les prédecesseurs des tâches du WK et faire ses SET & FET**************//

		     
		   for (int k=1; k< mt.pred_all_tasks_Workflow(n,DT).size()  ; k++) {
				  //System.out.println("11 mt.pred_all_tasks_Workflow(n,DT).size()==="+ mt.pred_all_tasks_Workflow(n,DT).size());
				 // System.out.println( "ahmed test : "+ Location_FET[mt.pred_all_tasks_Workflow(n,DT).get(5).get(0)]);
		  max=  Location_FET[mt.pred_all_tasks_Workflow(n,DT).get(k).get(0)];
			//   max= Location_FET[0];
		  //System.out.println("max = "+max );
		  
		// System.out.println("22 t.pred_all_tasks_Workflow(n,DT).get("+k+")==="+ mt.pred_all_tasks_Workflow(n,DT).get(k));
		  
		  	for (int j=1; j< mt.pred_all_tasks_Workflow(n,DT).get(k).size(); j++) {
		  		
	 		       System.out.println("Pred_T["+ k +"] ==  "+ mt.pred_all_tasks_Workflow(n,DT).get(k).get(j));
		 		   		if( Location_FET[mt.pred_all_tasks_Workflow(n,DT).get(k).get(j)]>= max) //doit être >= max pour donner de vrais Res
		 				{
		 		   		max=  Location_FET[mt.pred_all_tasks_Workflow(n,DT).get(k).get(j)];
		 				}
		 		   		System.out.println("Le max :"+max);
		 		   		}//fin boucle "j"
		  	
		  	 Location_SET[k]= max;
		 	 System.out.println("Location_SET["+k+"]= " +Location_SET[k]);

		     Location_FET[k]=  Location_TET[k] + Location_SET[k];
		     System.out.println("Location_FET["+k+"]= " +Location_FET[k]); 
		

	       
		   } // fin for boucle "k"
		  // }// fin "if"
			
		   
	 
		System.out.println("------------Total Exectution Cost TCT :=========="+ result);
		return result;

	}// fin evaluate
	public static double[] getLocation_FET() {
		return Location_FET;
	}
	public static void setLocation_FET(double[] location_FET) {
		Location_FET = location_FET;
	}
	
	
} // fin problemSet
