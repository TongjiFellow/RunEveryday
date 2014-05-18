package edu.tj.sse.runeveryday.database.entity;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "v3data")
public class V3Data {

	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private double x;
	@DatabaseField
	private double y;
	@DatabaseField
	private double z;
	@DatabaseField
	private long time=new Date().getTime(); //milliseconds,default value is current time
	@DatabaseField(foreign = true,foreignAutoRefresh = true)
	private RunData rundata;
	
	public V3Data(){
		
	}
	
	public V3Data(double x, double y, double z){
		this.x=x;
		this.y=y;
		this.z=z;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getZ() {
		return z;
	}
	public void setZ(double z) {
		this.z = z;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public RunData getRundata() {
		return rundata;
	}
	public void setRundata(RunData rundata) {
		this.rundata = rundata;
	}
}
