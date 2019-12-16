package org.hana.pso;


import hana.Interface_WK;
import hana.Read_XML_Matrices;

import org.cloudbus.cloudsim.MyVm;
import org.hana.pso.CloudSim_generer_estimation;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.text.StyledEditorKit.ForegroundAction;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;



public class Test_main_2 implements PSOConstants {
	

	    static int n = 9;//le nbre de tache
	    static double[] lag_vm = {1,1,1,0,0} ;//le temps de démarrage de VM, j'ai mis '1' pour chaque type
	    public static double[][] ET = new double [n][m+f];//Et: contient les temps d'execution générés par Cloudsim//m= le nombre de type de VMs
		public static double[][] TET = new double [n][m+f];//TET: contient les temps d'execution totales des tâches sur les types de VM
		public static double[][] DT = new double [n][n];//DT: contient le temps de transfert de données généré par Cloudsim
	 	static double[] vm = new double [m];
	 	static int[] loc = new int[n];
	 	static int [][] tasks_run = new int[9][5];

		//public static double[] UC_VM;	
	 
		static double [][] dependance_task = new double [n][n];// dependances entre les tâches (les lines)
		static int[][] caracters_task = new int [n][c];// caractéristque des tâches  (length,filesize,outputsize, nb of pes)
		private static List<Fog> fogDevices ;
		private static List<MyVm> CloudDevices ;
	 
		
 	
	 
	 public static void main(String args[]) throws SAXException, IOException, ParserConfigurationException {
		 String name_WF_file = "Workflow.xml";
		 
        System.out.println("**********Affichage de caractéristque Task***************");
		
		int[][] caracters_task_ = new int [n][c];

		Read_XML_Matrices r = new Read_XML_Matrices();
    	caracters_task_=	r.caracteristique_task(name_WF_file);
 
    	
    	//-----------------------------Affichage de la matrice : dependance_task--------------------//
    	System.out.print("***********Affichage de dependance_Task****************\n");

    	double[][] dependance_task_ = new double [n][n];

	    	Read_XML_Matrices r1 = new Read_XML_Matrices();
	    	dependance_task_=	r1.dependance_task(name_WF_file);

	    //------------------------------------------------------------------------------------------	
	    
	    //------------------------------------------------------------------------------------------
	    	
	    	
		//---------Générer les estimations de DT et ET --------------------------//
		CloudSim_generer_estimation c1= new CloudSim_generer_estimation(dependance_task_, caracters_task_);
		c1.generer_estimation(n,m, caracters_task_, dependance_task_);
		
	
		int xs=1;
		tasks_run=c1.remplir_task_run();
		c1.remplir_temps(temps_execute);
		
		CloudDevices= c1.createVM(xs, m);
		ET = c1.Calcul_ET(n,m+f,c1.list,c1.FogList,CloudDevices, tasks_run);//--  Calcul de temps d'exécution des tâches sur les # types de VMs
		DT = c1.Calcul_DT(n,c1.newList,c1.vmlist, dependance_task_ ); //--  Calcul de temps de transfert entre les tâches  
		
		
		//System.out.println("cost de machine 2="+UC_VM[2]);
		//---------------------------------------------------------------------	
		
		
		Mapping_Task mt = new Mapping_Task();
		Location location= new Location(loc);
		
		//******************Affichage de la matrice de TET*****************//
		 TET = mt.calcul_Total_ex_time(DT, ET, lag_vm, n, m+f);

		 
		 System.out.println("***********Affichage de TET**********************");
         for (int i1=0; i1<n; i1++) {
		         for (int j=0; j<m+f; j++) {

				System.out.println("--------TET["+i1+"]["+j+"]------------ :"+TET[i1][j] +"  ");//affichage de TET
				if(TET[i1][j]<0)
					System.out.println("Nombre de VM de TET["+ i1+"]["+ j+"] ======= 0");
				else
					System.out.println("Nombre de VM de TET["+ i1+"]["+ j+"] ======= "+ mt.calcul_Nb_VM(TET[i1][j]));  
					
		        }//fin "j"
		         System.out.println();
		  
		   } // fin for boucle "i"
         
        //******************************************Affectation : appel de PSO***************************// 
        
       PSOProcess p = new PSOProcess();
       p.execute(TET,n, DT, ET,UC_VM,Deadline,tasks_run,temps_execute);
 

 
		} 


 

	
} 
