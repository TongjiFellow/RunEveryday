package edu.tj.sse.runeveryday.database.entity;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "plan")
public class Plan {
	
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private int distance;
	@DatabaseField
	private int usetime;
	@DatabaseField
	private int clorie;
	@DatabaseField
	private Date date;
	@DatabaseField
	private int days;
	
	public Plan(){}
	
	
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


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public int getDays() {
		return days;
	}


	public void setDays(int days) {
		this.days = days;
	}


	@Override
	public String toString(){
		StringBuilder sb=new StringBuilder();
		sb.append(id+",");
		sb.append(distance+",");
		sb.append(usetime+",");
		sb.append(clorie+",");
		sb.append(date+",");
		sb.append(days+".");
		return sb.toString();
	}
}
