package edu.tj.sse.runeveryday.utils;


/**
 * This class is a utility about the calculation of running.
 * 
 * @author Haijie Hong
 * @version 1.0
 */
public class CalcUtil {
	private double distance;
	private V3 speed, lastAcceleration, g;
	private long lastTime, startTime;
	private double calories;
	
	public CalcUtil() {
		reset();
	}

	/**
	 * This method is used to set the current acceleration.
	 * 
	 * @param acc
	 *            The value of current acceleration(m/s).
	 * @return None.
	 */
	public void setAcceleration(V3 acc) {
		if (g == null) {
			g = acc;
			return;
		}
		acc = acc.substract(g);
		long curTime = System.currentTimeMillis();
		double timeEscape = (curTime - lastTime) / 1000.0;
		V3 avgAcc = lastAcceleration.add(acc).multiply(0.5);
		
		V3 disp = speed.multiply(timeEscape).add(avgAcc.multiply(timeEscape * timeEscape * 0.5));
		double curDis = Math.sqrt(disp.x * disp.x + disp.y * disp.y);
		
		double avgSpd = 0;
		if (timeEscape != 0) avgSpd = curDis / timeEscape;
		if (avgSpd > 0.1)
			distance += curDis;
		if (avgSpd != 0) {
			double k = 400 / avgSpd / 60;
			calories += timeEscape / 3600 * k;
		}
		
		speed = speed.add(avgAcc.multiply(timeEscape));
		lastAcceleration = acc;
		
		lastTime = curTime;
	}

	/**
	 * This method is used to get the speed of the time.
	 * 
	 * @param time
	 *            The millisecond from 1970.1.1, current time is System.currentTimeMillis();
	 * @return speed(m/s).
	 */
	public V3 getSpeed(long time) {
		return speed;
	}

	/**
	 * This method is used to get the current distance.
	 * 
	 * @return distance(meter).
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * This method is used to get the total step from the instance is created or reset.
	 * 
	 * @return step count.
	 */
	public int getSteps() {
		return 0;
	}

	/**
	 * This method is used to get the total calories from the instance is created or reset.
	 * 
	 * @param weight
	 *            The weight of the person(Kg)
	 * @return calories.
	 */
	public double getCalories(double weight) {
		return weight * calories;
//		
//		long totTime = System.currentTimeMillis() - startTime;
//		
//		double avgSpeed = distance / totTime;
//		if (avgSpeed == 0)
//			return 0;
//		double k = 400 / avgSpeed / 60;
//		return weight * totTime / 3600 * k;
	}

	/**
	 * This method reset the counter.
	 * 
	 * @return None.
	 */
	public void reset() {
		distance = 0;
		calories = 0;
		this.startTime = this.lastTime = System.currentTimeMillis();
		speed = new V3();
		lastAcceleration = new V3();
	}
}