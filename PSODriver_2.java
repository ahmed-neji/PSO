package org.hana.pso;


import hana.Interface_WK;
import hana.Read_XML_Matrices;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


public class PSODriver_2 implements PSOConstants {
	
	    static int n = 5;
	    static double[] lag_vm = {1,1,1} ;
	    public static double[][] ET = new double [n][m];;//Le temps d'execution  généré par Cloudsim
		public static double[][] TET = new double [n][m];
		public static double[][] DT = new double [n][n];//Le temps de transfert  généré par Cloudsim
		static double [] Final;
		static double r;
		static double[] vm = new double [m];
		static int max=0;
		static int[] loc = new int[n];
		static double [] SET;
		static double [] FET;
		static double [] Location = {1,0,2,2};
		static double[] UC_VM;	
		static double[] CT_vm;
		
		static double [][] dependance_task = new double [n][n];
		static int[][] caracters_task = new int [n][c];
		static String Log_map;
		static String  Log_map_reaff;
		static double result_total_cost;
	    static double  cout_total_Tache_execute;
	    static double  cout_total_Tache_restante;
	    static double  cout_total_après_reaffectation;
		static String task_consolid;
		static double result_final;
	    
	    
		static  int[][] Caract_task_Ap_Ajout;
		static double[][] Dependance_task_Ap_Ajout;
		static double [][] Dependance_task_restante;
		static int [][] Carcters_task_restante;
		static double[][] ET2 ; //--FF matrice qui contient le temps d'ex sur les # types de VMs
	 	static double[][] DT2 ; //--FF matrice qui contient le temps de transfert entre les # tâches
	     static Vector<Integer> Tache_restante = new Vector<Integer>();
	     static	Vector<Integer> Tache_execute = new Vector<Integer>();


	 	static int nb_ap_ajout;
	 	static double new_D;
	 	static int tache_res;
	 	static int tache_res_1;
	 	static int tache_res_2;
	 	static int tache_res_3;
	 	static int nb_tache_res;
	 	 static int task_adding ; 
	 	
	 	static double[] SE_actuel ;// pour récupérer les SE des tâches exécutées
	 	static String SE_actuel__ ="SBT Of executed Tasks :\n";
		static double [] FE_actuel;// pour récupérer les FE des tâches exécutées
	 	static String FE_actuel__ ="FBT Of executed Tasks :\n";

	 	static double []new_SBT ;// pour récupérer les SE des tâches reaffectées(pour tous Workflow)
	 	static String new_SBT__ ="SBT Of reaffected Tasks :\n";
	 	static double[] new_FBT ;// pour récupérer les FE des tâches reaffectées(pour tous Workflow)
	 	static String new_FBT__ ="FBT Of reaffected Tasks :\n";
	 	
	 	

		public static void Driver_method (String name_WF_file,String name_rule_File, int D) throws SAXException, IOException, ParserConfigurationException{

        System.out.println("**********Affichage de caractéristque Task***************");
		
		int[][] caracters_task_ = new int [n][c];

		Read_XML_Matrices r = new Read_XML_Matrices();
    	caracters_task_=	r.caracteristique_task(name_WF_file);
 
    	
    	//-----------------------------Affichage de la matrice : dependance_task--------------------//
    	System.out.print("***********Affichage de dependance_Task****************\n");

    	double[][] dependance_task_ = new double [n][n];

	    	Read_XML_Matrices r1 = new Read_XML_Matrices();
	    	dependance_task_=	r1.dependance_task(name_WF_file);

	    	System.out.println("****************Affectation des Tasks before Add**************");

		
		//---------Générer estimations de DT et ET --------------------------//
		CloudSim_generer_estimation c1= new CloudSim_generer_estimation(dependance_task_, caracters_task_);
		c1.generer_estimation(n,m, caracters_task_, dependance_task_);
		//ET = c1.Calcul_ET(n,m,c1.newList);//--FF  Calcul de temps d'exécution des tâches sur les # types de VMs
		DT = c1.Calcul_DT(n,c1.newList,c1.vmlist, dependance_task_ );
		UC_VM= c1.c_1_vm;
		//---------------------------------------------------------------------	
		
		
		Mapping_Task mt = new Mapping_Task();
		Location location= new Location(loc);
		
		//******************Affichage de la matrice de TET*****************//
		 TET = mt.calcul_Total_ex_time(DT, ET, lag_vm, n, m);

		 
		 System.out.println("***********Affichage de TET**********************");
         for (int i1=0; i1<n; i1++) {
		         for (int j=0; j<m; j++) {

				System.out.println("--------TET["+i1+"]["+j+"]------------ :"+TET[i1][j] +"  ");//affichage de TET
					System.out.println("Nombre de VM de TET["+ i1+"]["+ j+"] ======= "+ mt.calcul_Nb_VM(TET[i1][j]));  
					
		        }//fin "j"
		         System.out.println();
		  
		   } // fin for boucle "i"
         
        //******************************************Affectation sans exception***************************// 
        
       PSOProcess p = new PSOProcess();
     // p.execute(TET,n, DT, ET,UC_VM,D);
 
       Log_map = p.getLog_map(); // pour avoir les best localités des tasks de la meilleure solution

       
   
       
       System.out.println("Log_map"+Log_map);




	} // fin "driver_method"	


		public static double getCout_total_Tache_execute() {
			return cout_total_Tache_execute;
		}

		public static void setCout_total_Tache_execute(double cout_total_Tache_execute) {
			PSODriver_2.cout_total_Tache_execute = cout_total_Tache_execute;
		}
		
		
		public static String getSE_actuel__() {
			return SE_actuel__;
		}




		public static void setSE_actuel__(String sE_actuel__) {
			SE_actuel__ = sE_actuel__;
		}

		public static double getResult_total_cost() {
			return result_total_cost;
		}





		public static String getFE_actuel__() {
			return FE_actuel__;
		}




		public static void setFE_actuel__(String fE_actuel__) {
			FE_actuel__ = fE_actuel__;
		}
		
		public static String getLog_map() {
			return Log_map;
		}
} // fin driver
