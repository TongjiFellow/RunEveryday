package edu.tj.sse.runeveryday.utils;

public interface CalcInterface
{
	//���õ�ǰ���ٶȣ����ٶ�Ϊ��ά����
	void setAcceleration(V3 acc);      
	
	//�õ�ʱ��Ϊtimeʱ�ٶ�, ��λΪ���� 
	//System.currentTimeMillis();
	V3 getSpeed(long time);			
	
	//�õ�ʱ��Ϊtimeʱλ��
	V3 getDisplacement(long time);		 
	
	//�õ�ʱ��Ϊtimeʱ����
	int getSteps(long time);	
	
	//ÿ����Ҫ���¼���ǰ���
	void clear();				  
}