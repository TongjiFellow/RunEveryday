package edu.tj.sse.runeveryday.utils;

//��ά����
public class V3
{
	public double x = 0, y = 0, z = 0;
	public V3() {}
	public V3(double _x, double _y, double _z) 
	{
		x = _x;  y = _y; z = _z;
	}
	public double len() 
	{
		return Math.sqrt(x * x + y * y + z * z);
	}
	
}