package org.hana.pso;

public class Fog {
	private int id;
	private String name;
	private long size ;
	private long Uplink_BW;
	private long DownLink_BW;
	private double MIPS;
	private double RAM;
	private double Rate_MIPS;
	private double cost;
	private long bw;
	public long getBw() {
		return bw;
	}
	public void setBw(long bw) {
		this.bw = bw;
	}


	private int pesNumber;
	private int[] tacheExecute;
	public Fog(int id, String name,long size,  long uplink_BW, long downLink_BW, double mIPS, double rAM,
			double rate_MIPS, double cost,int pesNumber,int[] tacheExecute) {
		super();
		this.id = id;
		this.name = name;
		this.size=size;
		Uplink_BW = uplink_BW;
		DownLink_BW = downLink_BW;
		MIPS = mIPS;
		RAM = rAM;
		Rate_MIPS = rate_MIPS;
		this.cost=cost;
		this.pesNumber=pesNumber;
		this.tacheExecute=tacheExecute;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public long getUplink_BW() {
		return Uplink_BW;
	}
	public void setUplink_BW(long uplink_BW) {
		Uplink_BW = uplink_BW;
	}
	public long getDownLink_BW() {
		return DownLink_BW;
	}
	public void setDownLink_BW(long downLink_BW) {
		DownLink_BW = downLink_BW;
	}
	public double getMIPS() {
		return MIPS;
	}
	public void setMIPS(double mIPS) {
		MIPS = mIPS;
	}
	public double getRAM() {
		return RAM;
	}
	public void setRAM(double rAM) {
		RAM = rAM;
	}
	public double getRate_MIPS() {
		return Rate_MIPS;
	}
	public void setRate_MIPS(double rate_MIPS) {
		Rate_MIPS = rate_MIPS;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public int getPesNumber() {
		return pesNumber;
	}
	public void setPesNumber(int pesNumber) {
		this.pesNumber = pesNumber;
	}
	public int[] getTacheExecute() {
		return tacheExecute;
	}
	public void setTacheExecute(int[] tacheExecute) {
		this.tacheExecute = tacheExecute;
	}
	
	
	public Fog(int id, String name,long size,  long bw, double mIPS, double rAM,
			 double cost,int pesNumber) {
		super();
		this.id = id;
		this.name = name;
		this.size=size;
		this.bw=bw;
		MIPS = mIPS;
		RAM = rAM;
		
		this.cost=cost;
		this.pesNumber=pesNumber;
		
	}
	

}
