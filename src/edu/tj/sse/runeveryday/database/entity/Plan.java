package edu.tj.sse.runeveryday.database.entity;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "plan")
public class Plan {
	
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private String planName;
	
	@ForeignCollectionField
	private ForeignCollection<Training> trainings;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public ForeignCollection<Training> getTrainings() {
		return trainings;
	}

	public void setTrainings(ForeignCollection<Training> trainings) {
		this.trainings = trainings;
	}
	
	
}
