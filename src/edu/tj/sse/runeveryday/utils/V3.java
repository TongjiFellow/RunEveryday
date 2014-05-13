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
	public V3(V3 a) {
		x = a.x; y = a.y; z = a.z;
	}
	public double len() 
	{
		return Math.sqrt(x * x + y * y + z * z);
	}
	public V3 add(V3 b) {
		return new V3(x + b.x, y + b.y, z + b.z);
	}
	public V3 substract(V3 b) {
		return new V3(x - b.x, y - b.y, z - b.z);
	}
	public V3 multiply(double k) {
		return new V3(k * x, k * y, k * z);
	}
}