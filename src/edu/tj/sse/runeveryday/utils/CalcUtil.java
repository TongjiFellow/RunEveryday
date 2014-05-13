package edu.tj.sse.runeveryday.utils;
import V3;

import java.util.Map.Entry;
import java.util.TreeMap;


/**
* This class is a utility about the calculation of running.
* @author Haijie Hong
* @version 1.0
*/

public class CalcUtil
{
	private double distance;
	V3 speed, lastAcceleration;
	long weight, lastTime, startTime;
	
	
	public CalcUtil() {
		reset();
	}
	
	/**
	* This method is used to set the current acceleration.
	* @param acc The value of current acceleration(m/s).
	* @return None.
	*/
	void setAcceleration(V3 acc) {
		long curTime = System.currentTimeMillis();
		V3 speedDelta = lastAcceleration.add(acc).multiply(0.5 * (curTime - lastTime));
		speed = speed.add(speedDelta);
		lastAcceleration = acc;
		distance += speedDelta.len();
	};
	
	/**
	* This method is used to get the speed of the time.
	* @param time The millisecond from 1970.1.1, current time is System.currentTimeMillis();
	* @return speed(m/s).
	*/
	public V3 getSpeed(long time) {
		return speed;
	};
	
	/**
	* This method is used to get the current distance.
	* @return distance(meter).
	*/
	public double getDistance() {
		return distance;
	};
	
	/**
	* This method is used to get the total step from the instance is created or reset.
	* @return step count.
	*/
	public int getSteps() {
		return 0;
	};
	
	/**
	* This method is used to get the total calories from the instance is created or reset.
	* @param weight The weight of the person(Kg)
	* @return calories.
	*/
	public double getCalories(double weight) {
		long totTime = System.currentTimeMillis() - startTime;
		double avgSpeed = distance / totTime;
		if (avgSpeed == 0) return 0;
		double k = 400 / avgSpeed / 60;
		return weight * totTime / 3600 * k;
	};
	
	/**
	* This method reset the counter.
	* @return None.
	*/
	public void reset() {
		distance = 0;
		this.startTime = this.lastTime = System.currentTimeMillis();
		speed = new V3();
		lastAcceleration = new V3();
	};				  
}