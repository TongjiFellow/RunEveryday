package edu.tj.sse.runeveryday.database.entity;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "rundata")
public class RunData {
	
	@DatabaseField(generatedId = true)
	private int id;
	private int distance;
	private int usetime;
	private int clorie;
	private Date datetime;
	
	public RunData(){}
	
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getDistance() {
		return distance;
	}


	public void setDistance(int distance) {
		this.distance = distance;
	}


	public int getUsetime() {
		return usetime;
	}


	public void setUsetime(int usetime) {
		this.usetime = usetime;
	}


	public int getClorie() {
		return clorie;
	}


	public void setClorie(int clorie) {
		this.clorie = clorie;
	}


	public Date getDatetime() {
		return datetime;
	}


	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}


	@Override
	public String toString(){
		StringBuilder sb=new StringBuilder();
		sb.append(id+",");
		sb.append(distance+",");
		sb.append(usetime+",");
		sb.append(clorie+",");
		sb.append(datetime+".");
		return sb.toString();
	}
}