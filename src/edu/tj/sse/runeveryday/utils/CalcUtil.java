package edu.tj.sse.runeveryday.utils;
import java.util.Map.Entry;
import java.util.TreeMap;


/**
* This class is a utility about the calculation of running.
* @author Haijie Hong
* @version 1.0
*/

public class CalcUtil
{
	class Event {
		V3 ac, speed, disp;
	}
	TreeMap<Long, Event> c = new TreeMap<Long, Event>();
	
	public CalcUtil() {
	}
	
	/**
	* This method is used to set the current acceleration.
	* @param acc The value of current acceleration(m/s).
	* @return None.
	*/
	void setAcceleration(V3 acc) {
		long time = System.currentTimeMillis();
		Event e = new Event();
		e.ac = new V3(acc);
		if (c.size() == 0) {
			e.speed = new V3();
			e.disp = new V3();
		} else {
			Entry<Long, Event> last = c.lastEntry();
			e.speed = acc.add(last.getValue().ac).multiply(0.5 * (time - last.getKey()) * 0.001);
			e.disp = e.speed.add(last.getValue().speed).multiply(0.5 * (time - last.getKey()) * 0.001);
		}
		c.put(time, e);
	};
	
	/**
	* This method is used to get the speed of the time.
	* @param time The millisecond from 1970.1.1, current time is System.currentTimeMillis();;
	* @return speed(m/s).
	*/
	public V3 getSpeed(long time) {
		Entry<Long, Event> fst = c.lowerEntry(time);
		Entry<Long, Event> snd = c.ceilingEntry(time);
		if (fst == null) return snd.getValue().speed;
		else return fst.getValue().speed;
	};
	
	/**
	* This method is used to get the displacement of the time.
	* @param time The millisecond from 1970.1.1, current time is System.currentTimeMillis();;
	* @return speed(meter).
	*/
	public V3 getDisplacement(long time) {
		Entry<Long, Event> fst = c.lowerEntry(time);
		Entry<Long, Event> snd = c.ceilingEntry(time);
		if (fst == null) return snd.getValue().disp;
		else return fst.getValue().disp;
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
	* @param weight The weight(Kg) of person who is running.
	* @return calories.
	*/
	public double getCalories(long weight) {
		if (c.size() <= 1) return 0;
		double totTime = (c.lastKey() - c.firstKey()) * 0.001;
		double avgSpeed = c.lastEntry().getValue().disp.len() / totTime;
		if (avgSpeed == 0) return 0;
		double k = 400 / avgSpeed / 60;
		return weight * totTime / 3600 * k;
	};
	
	/**
	* This method reset the counter.
	* @return None.
	*/
	public void reset() {
		c.clear();
	};				  
}