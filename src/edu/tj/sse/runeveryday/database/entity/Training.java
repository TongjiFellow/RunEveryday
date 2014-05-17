package edu.tj.sse.runeveryday.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "training")
public class Training {

	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private String week;
	@DatabaseField
	private String day;
	@DatabaseField
	private String work;
	@DatabaseField
	private int order;  //start from 1
	@DatabaseField(defaultValue="false")
	private boolean isdone;
	@DatabaseField(foreign = true,foreignAutoRefresh = true)
	private Plan plan;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public boolean isIsdone() {
		return isdone;
	}
	public void setIsdone(boolean isdone) {
		this.isdone = isdone;
	}
	public Plan getPlan() {
		return plan;
	}
	public void setPlan(Plan plan) {
		this.plan = plan;
	}
}
