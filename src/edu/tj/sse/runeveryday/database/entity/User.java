package edu.tj.sse.runeveryday.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "user")
public class User {
	 
    @DatabaseField(id = true)
	private String name;
	private boolean gender;
	private int age;
	private int height;
	private float weight;
	private String medhistory;
	
	public User(){}
	
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public boolean isGender() {
		return gender;
	}


	public void setGender(boolean gender) {
		this.gender = gender;
	}


	public int getAge() {
		return age;
	}


	public void setAge(int age) {
		this.age = age;
	}


	public int getHeight() {
		return height;
	}


	public void setHeight(int height) {
		this.height = height;
	}


	public float getWeight() {
		return weight;
	}


	public void setWeight(float weight) {
		this.weight = weight;
	}


	public String getMedhistory() {
		return medhistory;
	}


	public void setMedhistory(String medhistory) {
		this.medhistory = medhistory;
	}


	@Override
	public String toString(){
		StringBuilder sb=new StringBuilder();
		sb.append(name+",");
		sb.append(gender?"ÄÐ":"Å®"+",");
		sb.append(age+",");
		sb.append(height+",");
		sb.append(weight+",");
		sb.append(medhistory+".");
		return sb.toString();
	}
}
