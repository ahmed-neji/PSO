package org.hana.pso;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class Mapping_Task   implements PSOConstants {

	static int n;
    static int swarm_size;
	static double[][] TC_VM = new double[n][m]; //cout total de VM
	double[] lag_vm;
	double[][] ET;
	static double[][] DT;
//	double[] VM_Cost = {1.5,2.5,4}; // cout de chaque VM
	private static Location loc;
	static double[] SE;  //--- SE: start execution de tâche
	static double[] FE;  //--- FE: finish execution de tâche
	static double [] Location_TET = new double[n];
	static double [] Location_FET =  new double [n];
	static int [] Location_SET = new int [n];
	static int max=0;

	

	
//----------------------- Calculer le Temps d'exécution totale : TET-------------------// Utilisée


public static  double[][] calcul_Total_ex_time(double[][] DT, double[][] ET,double[] lag_vm, int n, int m) {
	 
	  double[] DTT=new double[DT.length]; //DTT: un tableau contient le temps de transfert de chaque tâche
	  									//----(en calculant le max de tous les temps de transfert s'il y'en a plusieurs)
System.out.println("longeur de DT est :" +DT.length);
	  double[][] TET=new double[n][m];//TET: un tableau contient le temps d'ex total des tâches sur tous les types de VMs
	  								
	  for (int i = 0; i < DT.length ; i++) {
		 double transfer_data =0;
		     for (int j=0; j<DT.length ; j++) {
		    	if(transfer_data< DT[j][i])
		    		{  
		    		transfer_data = DT[j][i];
 		    	 
 		    	}
		     }
		     DTT[i]=transfer_data;
		
	  }   
	  
	  
	  for ( int i=0 ; i < n; i++) {
	        for (int j=0; j< 3 ; j++) {
	        	TET[i][j]= ET[i][j]+ DTT[i] + lag_vm[j];
	        	
	        }
	  }
    
    
            
		   
	      for ( int q=0 ; q < n ; q++) {
	        TET[q][3]=ET[q][3];
	        TET[q][4]=ET[q][4];
	        
	        }
  
	return TET;	
}


//----------------------- Calculer le nb d'heure (ou de VM) pour une tâche ayant une durée d'ex 'ex_time'----------------//Utilisée


public static  int calcul_Nb_VM(double ex_time) {
	  int nb_vm = 0;
	  if(ex_time % 60 ==0)
	      nb_vm = (int) (ex_time/60);
	  else if(ex_time/60<1)
		  nb_vm=1;
	  else
		  nb_vm = (int) (ex_time/60+1);
	  
	  return nb_vm;

}


//-----------Méthode retourne le type de VM associé à la tâche i -------------------------//Non-utlisée

public static  int chercher_VM_Type(double[] xx ,int m) {
	  int i=0; 
	  boolean existe= false;
	  int type_vm = 0;
	   while(i<m &&  existe== false)  {
		     if(xx[i] != 0.0)
		     { type_vm = i;
		         
		       existe = true;
		     }
		     else
		    	 i++;
	 	}
	   return type_vm;
} 


//----- Chercher le dernier élèment d'un WF (çàd qui n'a pas de successeur)----------// Utilisée
public static int chercher_dernier_elet_WF(double[][] DT)
 {     int n= DT.length;
	   boolean trouve=false;
	   int i=0;
	   int indice_fin = 0;
			 while( trouve== false && i<n)  {
				
			 		 int nb_succ=0; int j=1; boolean trouve_1=false;// trouve_1=true çàd j'ai trouvé un élèment égale à 1
			 		 while( trouve_1 == false && j<n) { // ------ parcourir les colonnes de la ligne i
	  	        	//	  System.out.println("--------------DT["+i+"]["+j+"]--------------"+ DT[i][j]);
	  	      			     if(DT[i][j] == 0.0)  ///----çàd j est un successeur de i 

	  	      			     {
	  	      			    	nb_succ++;
	  	      			     }
	  	      			     else
	  	      			     {	trouve_1=true;  }
	  	      			    
	  	      			     j++;
	  	        	   }    	 

			 		 if (nb_succ== n-1)
			 			{
			 			 indice_fin= i; //jusqu'à i=3 (dans notre cas)
			 		     trouve=true;
			 		     }
			 		 else 
			 			 i++;
	          	}
			 	 //  System.out.println("--------------Indice de Fin--------------"+ indice_fin);
		return indice_fin;
 }

//----- Chercher le premier élèment d'un WF (çàd qui n'a pas de predecesseur)----------//Utilisée
public static int chercher_premier_elet_WF(double[][] DT) {
    
	int n= DT.length;
     System.out.println("n_mapping" +n);

	   boolean trouve=false;
	   int i=1;
	   int indice_start = 0;
			 while( trouve== false && i<n)  {
				
				// System.out.println("n_mapping_task=="+n);
				 
			 		 int nb_pred=0; int j=0; boolean trouve_1=false;// trouve_1=true çàd j'ai trouvé un élèment égale à 1
			 		 while( trouve_1 == false && j<n) { // ------ parcourir les colonnes de la ligne i
	  	        		     if(DT[j][i] != 0.0)  ///----çàd j est un successeur de i 
	  	      			     {
	  	      			    	nb_pred++;
	  	      			     }
	  	      			     else
	  	      			     {	
	  	      			    	 trouve_1=true;  }
	  	      			    
	  	      			     i++;
	  	        	   }    	 

			 		 if (nb_pred== n-1)
			 			{
			 			indice_start= j; //jusqu'à i=3 (dans notre cas)
			 		     trouve=true;
			 		     }
			 		 else 
			 			 j++;
	          	}
			 	  // System.out.println("--------------Indice_Start--------------"+ indice_start);
		return indice_start;
}


//----------------------- Contrainte de deadline------------------//Non_utilisée
public static void RespectD(double D,double [][] TET,double [][] ET) {

	double [] Tot_Exc = new double[n];
	double result = 0;
	boolean r=false;
	
	for (int i=0 ; i<n ; i++){
    Tot_Exc[i] = TET[0][loc.getLoc()[i]]+ET[i][loc.getLoc()[i]];
	
	   System.out.println("Tot_Exc["+i+"] :"+Tot_Exc[i]);

	   
result = result + Tot_Exc[i];
	}
	 if( result <=D){
	 	   System.out.println("--------------Deadline respecté--------------");
		
	 }else{
	 	   System.out.println("--------------Deadline Non respecté--------------");
		}
	}

//-------------------Calcul de SE (Start execution) et FE (Finish execution)-------------------------// Non-utilisée
	//----------- pour la tâche T0 -------------------   	
public static double calcul_SE_FE(double[] vm,double [][] TET) {

	double result =0;
    SE[0]=0; 
	    int k=0;
	    boolean calcul=false;
	    while (k<m && calcul==false)
	    {  
	     System.out.println(" *------------------* "); 
	    	if(vm[k] != 0.0) 
	    	{
	    		FE[0]=SE[0]+TET[0][k];
	    		calcul=true;
	    		result = result+ FE[0];   	  
	    	  System.out.println(""+ TET[0][k]+" ***____SE[0]== "+ SE[0]+"----FE[0]="+ FE[0]);
	    
	    	}  
	    	else {
	    		k++;
	    		
	    }
}
	    System.out.println("res"+result);
		  return result;
}

  	 
//-----------Méthode retourne la liste des prédecesseurs d'une tâche-----------//Utilisée
public static List<List<Integer>> pred_all_tasks_Workflow(int n,double[][] DT)
{
  //List<Integer> Pred_T0= new Vector<Integer>(); 
  List<Integer> Pred_T0= new ArrayList<Integer>(); 
  List<List<Integer>> Pred_all_tasks_wf = new ArrayList<List<Integer>>(); 
  //	 Location_SET[0]=0;	


  Pred_T0.add(-1); //---- çàd la tâche 0 n'a pas de prédecesseur //hana: changer -1 par 0
          Pred_all_tasks_wf.add(Pred_T0);
    
          for (int j=1; j<n ; j++) {
        	 List<Integer> Pred_1_task= new ArrayList<Integer>(); 
	        	  for (int i = 0; i < n ; i++) {  // ------ parcourir les lignes de chaque colonne j
	        		     if(DT[i][j] != 0.0)  ///----çàd i est un successeur de j
	      			     {

	      			    	
	      			    	 Pred_1_task.add(i); //j'ai essayée avec new Integer(i)
	      			    	
	      			    	 
	      			  
		 	      			     
	      			     }
	      			   
	      		
	        	   }    	 
	        	Pred_all_tasks_wf.add(Pred_1_task);
	        	
	        	

        	}
       
		
	    
	  return Pred_all_tasks_wf;        
}



//-----------Méthode retourne la liste des Location_FET du dernier elem de chaque particule------// Non-utilisée
public static List<Integer> Loc_FET_last_elem_partic (double [] Location_FET ){
	///double [] Location_FET;
	List<Integer> Loc_FET_Part = new ArrayList<Integer>(); 

    for (int i=0; i<swarm_size ; i++) {
    	Location_FET[i]= Location_FET[chercher_dernier_elet_WF(DT)];
    	Loc_FET_Part.add((int)Location_FET[i]);
    }

	return Loc_FET_Part;
}

//----- Méthode cherche le sous indice de elet ds VM_Tache2----------------------------

int chercher_indice_s_elet(Vector<Integer> v,int elet)
{
	   boolean trouve=false;int k=0;
			 while(k<v.size() &&  trouve== false)  
			   { if (v.get(k)==elet)
				  trouve = true;
				 else
					 k++;
			   }
	   return k;
}

//----- Méthode cherche l'indice de elet ds VM_Tache2----------------------------//Utilisée
int chercher_indice_elet(	Vector<Vector<Integer>> VM_Tache2,int elet)
{
	   boolean trouve=false;int j=0;
	   while(j<VM_Tache2.size() &&  trouve== false)  
		   {  Vector<Integer> v = new Vector<Integer>();
			  v = VM_Tache2.elementAt(j);
			  int k= 0;
			 while(k<v.size() &&  trouve== false)  
			   { 
				 if (v.get(k)==elet)
				  trouve = true;
				 else
					 k++;
			   }
			 if(trouve== false)
				 j++;
		   } 
	   return j;
}

//---------------------- Méthode retourne les successeurs d'une tâche --------------------------//Utilisée

static Vector<Integer>  Succ_1tache_de_Wf(int indice_t,double[][] DT)
{  /// System.out.println("é~~é~~é~~é~~é~~é~~ é~~é~~é~~  n ======" + n);
	  Vector<Integer> succ_task= new Vector<Integer>();
	  for (int j=1; j<DT.length ; j++) {
		   if(DT[indice_t][j] !=0)
			   succ_task.add(j);
	  }
	  //..	 System.out.println("é~~é~~é~~ Les succeurs de ::"+ indice_t + "::: sont:::::" + succ_task);
    return  succ_task;
}
}

