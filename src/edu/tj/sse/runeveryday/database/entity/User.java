package edu.tj.sse.runeveryday.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "user")
public class User {
	 
    @DatabaseField(id = true)
    private int id;
    @DatabaseField
	private String name;
    @DatabaseField
	private boolean gender; //false:female,true:male
    @DatabaseField
    private int age;
    @DatabaseField
    private int height;
    @DatabaseField
	private float weight;
    @DatabaseField
	private String medhistory;
	
	public User(){}
	
	
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
		sb.append(gender?"��":"Ů"+",");
		sb.append(age+",");
		sb.append(height+",");
		sb.append(weight+",");
		sb.append(medhistory+".");
		return sb.toString();
	}
}
