package org.hana.pso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import org.hana.pso.Fog;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.MyVm;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
public class CloudSim_generer_estimation implements PSOConstants {




	/** The cloudlet list. */
	private static List<Cloudlet> cloudletList;
    /** The vmlist. */
	static List<MyVm> vmlist;
 	static double [][] dependance_task;
	static int [][] caracters_task;
	public static ArrayList<Cloudlet> list = new ArrayList<Cloudlet>();

	 static double[] c_1_vm;           //   tableau contient le coût par heure de chaque type de VM                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
	 List<Cloudlet> newList;
	 
	 //fog déclaration
	 static List<Fog> FogList; // liste des devices fog
	 static Double[] c_1_fd;// le cout par heure ed chaque type de fog
	 
	 //--------------constructeur fog ------------------------------
	//-----------------Méthode crée une liste de fog devices-------------------
	 
	 public static List<Fog> createFD(int userId, int fds) {
			
	 	 	//Creates a container to store VMs. This list is passed to the broker later
			LinkedList<Fog> list_2 = new LinkedList<Fog>();
			
			//------------------------------ Les caractèristiques de VMs ---------------------
			//fog Parameters
			long size = 10000; //image size (MB)
			int ram = 2048; //vm memory (MB)
			int mips = 3000;
			long up_bw = 1000;
			long down_bw = 1100;
		    int pesNumber = 4; //number of cpus
			String f_name = "Fog"; //VMM name
			double cost_hour1=0.030;
			double cost_hour2=0.050;
			float rate_mips=100;
			int[] tachef1 = new int[5];
			int[] tachef2=new int[5];
			tachef1[0]=1;tachef1[1]=1;tachef1[2]=1;tachef1[3]=0;tachef1[4]=0;
			tachef2[0]=0;tachef2[1]=0;tachef2[2]=0;tachef2[3]=1;tachef2[4]=1;
			//create FDs
			
			Fog[] fd = new Fog[fds];
			//c_1_fd[0]=cost_hour1;
			//c_1_fd[1]=cost_hour2;
		
			
			fd[0] = new Fog(0, f_name+'1',size,  up_bw, down_bw,mips, ram,rate_mips,cost_hour1,pesNumber,tachef1);
			list_2.add(fd[0]);

	 		fd[1] = new Fog(1, f_name+'1',size,up_bw+100,down_bw-200, mips+1000, ram*3,rate_mips+50,cost_hour2,pesNumber+1,tachef2);
			list_2.add(fd[1]);

			
	 		return list_2;
		}

	 
	 
	//------------- Constructeur ----------------------- 
	public CloudSim_generer_estimation(double[][] dependance_tache,
			int[][] caracters_task) {
		super();
		dependance_task = dependance_tache;
		this.caracters_task = caracters_task;
				 
	}
	
	//-----------------Méthode crée une liste de VM-------------------
	public static List<MyVm> createVM(int userId, int vms) {
	
 	 	//Creates a container to store VMs. This list is passed to the broker later
		LinkedList<MyVm> list = new LinkedList<MyVm>();
		
		//------------------------------ Les caractèristiques de VMs ---------------------
		//VM Parameters
		long size = 10000; //image size (MB)
		int ram = 2048; //vm memory (MB)
		int mips = 1500;
		long bw = 1000;
	    int pesNumber = 4; //number of cpus
		String vmm = "Xen"; //VMM name
		double cost_hour1=0.036;
		double cost_hour2=0.072;
		double cost_hour3=0.252;
		String name_c= "VM" ;
		//create VMs
		MyVm[] vm = new MyVm[vms];
		
		vm[0] = new MyVm(0, name_c+1,userId, mips+300*0, pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared(),cost_hour1);
		list.add(vm[0]);

 		vm[1] = new MyVm(1, name_c+2,userId, mips+300*1, pesNumber,  ram*3, bw, size, vmm, new CloudletSchedulerSpaceShared(),cost_hour2);
		list.add(vm[1]);

		vm[2] = new MyVm(2,name_c+3, userId, mips+300*2, pesNumber*2, ram*4, bw, size, vmm, new CloudletSchedulerSpaceShared(),cost_hour3);
		list.add(vm[2]);
		
		
 		return list;
	}
public void remplir_temps(int[]temps_execute) {	
	temps_execute[0]= 30;
	temps_execute[1]= 15;
	temps_execute[2]= 40;
	temps_execute[3]= 30;
	temps_execute[4]= 25;
	temps_execute[5]= 5;
	temps_execute[6]= 10;
	temps_execute[7]= 25;
	temps_execute[8]= 60;
	
}
	 public  int[][] remplir_task_run()
	 {
		 int[][] task_run=new int[9][5];
		 
	/* t0 */ 	 task_run[0][0]=1;   task_run[0][1]=1;   task_run[0][2]=1;   task_run[0][3]=1;   task_run[0][4]=1;
		 
		 
	/* t1 */	 task_run[1][0]=1;  task_run[1][1]=1;  task_run[1][2]=1;  task_run[1][3]=1;  task_run[1][4]=1;
		 
		 
	/* t2 */	 task_run[2][0]=1;  task_run[2][1]=1;  task_run[2][2]=1;  task_run[2][3]=1;  task_run[2][4]=1;
		 
		 
	/* t3 */	 task_run[3][0]=1;  task_run[3][1]=1;  task_run[3][2]=0;  task_run[3][3]=1;  task_run[3][4]=0;
		 
		 
	/* t4 */	 task_run[4][0]=1  ;task_run[4][1]=0;  task_run[4][2]=1;  task_run[4][3]=1;  task_run[4][4]=1;
		 
		 
	/* t5 */	 task_run[5][0]=1;  task_run[5][1]=1;  task_run[5][2]=1;  task_run[5][3]=1;  task_run[5][4]=1;
		 
		 
	/* t6 */	 task_run[6][0]=1;  task_run[6][1]=1;  task_run[6][2]=0;  task_run[6][3]=1;  task_run[6][4]=1;
		 
		 
	/* t7 */	 task_run[7][0]=0;  task_run[7][1]=0;  task_run[7][2]=0;  task_run[7][3]=1;  task_run[7][4]=1;
		 
		 
	/* t8 */	 task_run[8][0]=1;  task_run[8][1]=1;  task_run[8][2]=1;  task_run[8][3]=1;  task_run[8][4]=1;
		 return task_run;
	 }

	//-----------------Méthode crée une liste de Cloudlets-------------------

	private static List<Cloudlet> createCloudlet(int userId, int nb_cloudlets, int[][] caract_cloudlet){
		// Creates a container to store Cloudlets
		
		 
		//cloudlet parameters
		long length ;
		long fileSize;
		long outputSize ;
		int pesNumber;
		
		
		//UtilizationModel utilizationModel = new UtilizationModelFull();

		Cloudlet[] cloudlet = new Cloudlet[nb_cloudlets];
	//--	Créer n cloudlets -----------------------------
	 	
		for(int i=0;i<nb_cloudlets;i++)
			{   
				length = caract_cloudlet[i][0];
				fileSize = caract_cloudlet[i][1];
				outputSize = caract_cloudlet[i][2];
				pesNumber = caract_cloudlet[i][3];
				
				
				UtilizationModel utilizationModel = new UtilizationModelFull();

				cloudlet[i] = new Cloudlet(i, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
				cloudlet[i].setUserId(userId);
				list.add(cloudlet[i]);
			}

		return list;
	}



	private static Datacenter createDatacenter(String name){

		// Here are the steps needed to create a PowerDatacenter:
		// 1. We need to create a list to store one or more
		//    Machines
		List<Host> hostList = new ArrayList<Host>();
		// 2. A Machine contains one or more PEs or CPUs/Cores. Therefore, should
		//    create a list to store these PEs before creating
		//    a Machine.
		List<Pe> peList1 = new ArrayList<Pe>();

		//--FF  int mips = 1000;
		   int mips = 1000000;   //--FF 
		// 3. Create PEs and add these into the list.
		//for a quad-core machine, a list of 4 PEs is required:
		peList1.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
		peList1.add(new Pe(1, new PeProvisionerSimple(mips)));
		peList1.add(new Pe(2, new PeProvisionerSimple(mips)));
		peList1.add(new Pe(3, new PeProvisionerSimple(mips)));

		//Another list, for a dual-core machine
		List<Pe> peList2 = new ArrayList<Pe>();

		peList2.add(new Pe(0, new PeProvisionerSimple(mips)));
		peList2.add(new Pe(1, new PeProvisionerSimple(mips)));

		//4. Create Hosts with its id and list of PEs and add them to the list of machines
		int hostId=0;
		int ram = 204800; //host memory (MB)
		long storage = 10000000; //host storage
		int bw = 100000;
		
		hostList.add(
    			new Host(
    				hostId,
    				new RamProvisionerSimple(ram),
    				new BwProvisionerSimple(bw),
    				storage,
    				peList1,
    				new VmSchedulerTimeShared(peList1)
    			)
    		); // This is our first machine

		hostId++;

		hostList.add(
    			new Host(
    				hostId,
    				new RamProvisionerSimple(ram),
    				new BwProvisionerSimple(bw),
    				storage,
    				peList2,
    				new VmSchedulerTimeShared(peList2)
    			)
    		); // Second machine

		// 5. Create a DatacenterCharacteristics object that stores the
		//    properties of a data center: architecture, OS, list of
		//    Machines, allocation policy: time- or space-shared, time zone
		//    and its price (G$/Pe time unit).
		String arch = "x86";      // system architecture
		String os = "Linux";          // operating system
		String vmm = "Xen";
		double time_zone = 10.0;         // time zone this resource located
		double cost = 3.0;              // the cost of using processing in this resource
		double costPerMem = 0.05;		// the cost of using memory in this resource
		double costPerStorage = 0.1;	// the cost of using storage in this resource
		double costPerBw = 0.1;			// the cost of using bw in this resource
		LinkedList<Storage> storageList = new LinkedList<Storage>();	//we are not adding SAN devices by now

		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);


		// 6. Finally, we need to create a PowerDatacenter object.
		Datacenter datacenter = null;
		try {
			datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
		} catch (Exception e) {
			e.printStackTrace();
		} 

		return datacenter;
	}

	//We strongly encourage users to develop their own broker policies, to submit vms and cloudlets according
	//to the specific rules of the simulated scenario
	private static DatacenterBroker createBroker(){

		DatacenterBroker broker = null;
		try {
			broker = new DatacenterBroker("Broker");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}
	
	//--FF Calcul de temps d'exécution des tâches sur les # types de VMs ------------------
 	public static double[][] Calcul_ET(int n,int m, ArrayList<Cloudlet> list,List<Fog> list2,List<MyVm> liste1,int[][] task_run) {
 		double[][] ET = new double[n][m]; 
 		DecimalFormat dft = new DecimalFormat("0.00");
 		
 		for (int i=0; i<9; i++) { // for (int i=0; i<list.size(); i++)
 			for (int j=0;j<3;j++)
 			{
 			//int cloudlet_id=list.get(i).getCloudletId();
	 	 	
			//int vm_id=list.get(i).getVmId();
		if(task_run[i][j]==1) // on a chager vm_id par boucle de j et [cloudlet_id][j] par [i][j]
				
				ET[i][j] = list.get(i).getCloudletLength()/liste1.get(j).getMips()/60;//list.get(i).getActualCPUTime()/60; // ET[cloudlet_id][j] 
			
			else
				ET[i][j] = -1; //ET[cloudlet_id][j] 
 			}
			
			
		}	
 		//-------------------------------------------------------
 		
 		for(int i=0;i<9; i++)// {
 			if (task_run[i][3]==1)
 				ET[i][3]=list.get(i).getCloudletLength()/ list2.get(0).getMIPS() /60;
 			else
 				ET[i][3]=-1;
 		//}
 		for(int i=0;i<9; i++) //{
 	 		
 			
 			if (task_run[i][4]==1)
 				ET[i][4]=list.get(i).getCloudletLength()/ list2.get(1).getMIPS() /60;
 			else
 				ET[i][4]=-1;
 	//	}
 		
 		// modifier par le tableau de task_run
 			/*boolean test1=false;
 			boolean test2=false;
 			int[]l1=list2.get(0).getTacheExecute();
 			int[]l2=list2.get(1).getTacheExecute();
 			
 				if (l1[cloudlet_id]==1)
 					test1=true;
 			if (test1==true) 
 			ET[cloudlet_id][3]=list.get(i).getCloudletLength()/ list2.get(0).getMIPS() /60;
 			else
 				ET[cloudlet_id][3]=-1;
 			
 			
 				if (l2[cloudlet_id]==1)
 					test2=true;
 			if (test2==true) 
 			ET[cloudlet_id][4]=(list.get(i).getCloudletLength()/( list2.get(1).getMIPS()))/60;	
 			else
 				ET[cloudlet_id][4]=-1;
 		}*/
 			
 		//-------------------------------------------------------
 		
 		System.out.println(" ****** --------------- Calcul_ET ----------------***** ");
     	for (int i=0; i<n ; i++) {
	    	for (int j=0; j<m; j++) {
    	 		System.out.print(dft.format(ET[i][j])+"  ");
    		}
	    	
    		System.out.println();
     	}
    		
		return ET;
		
		 
 }
 	
 	//--FF Calcul de temps de transfert entre les tâches : la matrice DT  ------------------
 	static  double[][]  Calcul_DT(int n, List<Cloudlet> list, List<MyVm> vmlist, double[][] dependance_task) {
 		double [][] DT = new double[n][n]; 
 	//	DecimalFormat dft = new DecimalFormat("0.00");
 	
    	for (int i=0; i<n ; i++) {
	    	for (int j=0; j<n; j++) {
	    		if( dependance_task[i][j]!= 0 ) //---Il faut calculer le temps de transfert entre i et j
	    		{	
	    			DT[i][j] = (double)(caracters_task[i][2]/60)/(vmlist.get(0).getBw());//--- (double) pour afficher les chiffres
	    			//--- pour récupérer la valeur de bandwith, j'ai pris la valeur du 1er élèment de vmlist puisque la valeur de bw des VMs sont égaux
	    		}
    	 	}
	   }
 		System.out.println(" ** --------------- Calcul_DT ---------------- ** ");
 		for (int i=0; i<n ; i++) {
	    	for (int j=0; j<n; j++) {
    	 		System.out.print(DT[i][j]+"    ");
    		}
    		System.out.println();
	     }  
 		
 		return DT;
 	}
 	
 		
 	/**
	 * Prints the Cloudlet objects
	 * @param list  list of Cloudlets
	 */
	public static void printCloudletList(List<Cloudlet> list) {	
		int size = list.size();
		Cloudlet cloudlet;

		String indent = "    ";
		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +
				"Data center ID" + indent + "VM ID" + indent + indent + "Time" + indent + "Start Time" + indent + "Finish Time");

		DecimalFormat dft = new DecimalFormat("###.##");
		
		for (int i = 0; i < size; i++) {
			cloudlet = list.get(i);
			Log.print(indent + cloudlet.getCloudletId() + indent + indent);

			if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS){
				Log.print("SUCCESS");

				Log.printLine( indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet.getVmId() +
						indent + indent + indent + dft.format(cloudlet.getActualCPUTime()/60) +
						indent + indent + dft.format(cloudlet.getExecStartTime()/60)+ indent + indent + indent + dft.format(cloudlet.getFinishTime()/60));
			}
		}
	 
  }
	//--------------
	  public void generer_estimation(int n,int m,int[][] caracters_task, double[][] dependance_task){
			
		
			try {
				// First step: Initialize the CloudSim package. It should be called
				// before creating any entities.
				int num_user = 1;   // number of grid users
				Calendar calendar = Calendar.getInstance();
				boolean trace_flag = false;  // mean trace events

				// Initialize the CloudSim library
				CloudSim.init(num_user, calendar, trace_flag);

				// Second step: Create Datacenters
				//Datacenters are the resource providers in CloudSim. We need at list one of them to run a CloudSim simulation
				@SuppressWarnings("unused")
				Datacenter datacenter0 = createDatacenter("Datacenter_0");
				
				//Third step: Create Broker
				DatacenterBroker broker = createBroker();
			
				int brokerId = broker.getId();
				System.out.println("=======brokerId ============= "+ brokerId);	
				//Fourth step: Create VMs and Cloudlets and send them to broker
				
				vmlist = createVM(brokerId,m); //creating m vms
				FogList=createFD(brokerId,PSOConstants.f);
				 //---------- Remplir le tableau c_1_vm de coût par heure de chaque type de VM -----------
				double[] c_1_vm = new double[5];
				for(int i=0;i<vmlist.size();i++)
					{
					    UC_VM[i]= vmlist.get(i).getCost_hour();
					}
				UC_VM[3]=FogList.get(0).getCost();
				UC_VM[4]=FogList.get(1).getCost();
				for(int k=0;k<5;k++)
					System.out.println(UC_VM[k]);
				
				
				
				//---
				
				
				
				broker.submitVmList(vmlist);

         		System.out.println("Nb_VMs : "+ m);
				System.out.println("Nb_Tasks : "+ n);
				cloudletList = createCloudlet(brokerId,n,caracters_task);
				 broker.submitCloudletList(cloudletList);
				 newList=cloudletList;
			for(int k=0;k<m;k++)
		   	{    // creating  cloudletList à n elets
			     //-- Le nbre de soumission de cloudletList est le nbre de type de VMs
			
				for(int i=0;i<cloudletList.size();i++)
				{	
				    cloudletList.get(i).setVmId(vmlist.get(k).getId()); //--- J'utilise le type 'k' de VM
			     	}
		   	}
			
				// Fifth step: Starts the simulation
				CloudSim.startSimulation();

				newList = broker.getCloudletReceivedList();
				
	            CloudSim.stopSimulation();
	        	printCloudletList(newList); //--- Tout l'affichage sera dans la même liste (newList):pour les m=7 types de VM
	        	
	         	

     	
	    		
			}
			catch (Exception e)
			{
				e.printStackTrace();
				Log.printLine("The simulation has been terminated due to an unexpected error");
			} 
			
		}
	public static double[] getC_1_vm() {
		return c_1_vm;
	}
	public void setC_1_vm(double[] c_1_vm) {
		this.c_1_vm = c_1_vm;
	}  
	
	
}
