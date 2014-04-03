package utils;

public interface CalcInterface
{
	//设置当前加速度，加速度为三维向量
	void setAcceleration(V3 acc);      
	
	//得到时间为time时速度, 单位为毫秒 
	//System.currentTimeMillis();
	V3 getSpeed(long time);			
	
	//得到时间为time时位移
	V3 getDisplacement(long time);		 
	
	//得到时间为time时步数
	int getSteps(long time);	
	
	//每次需要重新计数前清空
	void clear();				  
}