package edu.tj.sse.runeveryday.utils;

public interface CalcUtil
{
	void setAcceleration(V3 acc);      
	
	V3 getSpeed(long time);			
	
	V3 getDisplacement(long time);		 
	
	int getSteps(long time);	
	
	int getCalories();
	
	void clear();				  
}