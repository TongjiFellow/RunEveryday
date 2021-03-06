package edu.tj.sse.runeveryday.utils.plangen;

public class StandardWeight {

	static private int[] y_age=new int[]{19,21,23,25,27,29,31,33,35,37,39,41,43,45,47,49,51,53,55,57,59,61,63,65,67,69};
	
	static private int[] f_x_height=new int[]{152,156,160,162,164,166,168,170,172,176};
	static private int[][] f_v=new int[][]{{46,46,46,46,47,47,48,48,49,49,50,51,51,52,52,52,52,53,53,53,53,53,52,52,52,52},
								   {47,47,47,48,48,49,49,50,50,51,52,52,53,53,53,53,54,54,54,55,55,54,54,54,54,54},
								   {49,49,49,49,50,51,51,51,52,53,53,54,55,55,57,56,56,56,56,56,56,56,55,55,55,55},
								   {50,50,50,50,51,52,52,52,52,53,53,54,55,55,57,56,56,56,57,57,57,56,56,56,56,56},
								   {51,51,51,51,52,53,53,53,53,54,55,55,56,57,57,57,57,58,58,58,58,57,57,57,57,57},
								   {52,52,52,53,53,54,54,55,55,56,57,57,58,58,58,59,59,59,60,60,60,59,59,59,59,59},
								   {54,54,54,55,55,56,56,57,57,59,59,59,60,60,60,60,61,61,61,61,61,61,61,61,61,61},
								   {56,56,56,56,56,58,58,58,59,60,60,61,62,62,62,62,62,62,63,63,63,63,62,62,62,62},
								   {57,57,57,57,58,59,59,59,60,61,61,62,63,63,63,63,63,64,64,64,64,64,63,63,63,63},
								   {60,60,60,61,61,62,62,63,63,64,65,65,66,66,67,67,67,67,67,68,68,67,67,66,66,66}};
	static private int[] m_x_height=new int[]{152,156,160,164,168,172,176,180,184,188};
	static private int[][] m_v=new int[][]{{50,51,52,52,52,53,53,54,54,55,55,55,56,56,56,56,57,57,56,56,56,56,56,56,56,56},
								   {52,53,53,54,54,55,55,56,56,56,57,57,57,57,58,58,58,58,58,57,57,57,57,57,57,57},
								   {52,54,55,55,55,56,56,57,57,58,58,58,58,59,59,59,59,59,59,59,58,58,58,58,58,58},
								   {54,55,56,57,57,57,58,58,59,59,60,60,60,60,61,61,61,61,61,60,60,60,60,60,60,60},
								   {56,57,58,59,59,59,60,60,61,61,61,62,62,62,63,63,63,63,63,62,62,62,62,62,62,62},
								   {58,60,60,61,61,61,62,63,63,63,64,64,64,64,65,65,65,65,65,65,64,64,64,64,64,64},
								   {61,62,63,63,64,64,65,65,66,66,66,67,67,67,67,68,68,68,68,67,67,67,67,67,67,67},
								   {64,65,66,67,67,67,68,68,69,69,70,70,70,70,71,71,71,71,71,70,70,70,70,70,70,70},
								   {67,69,70,71,71,71,72,72,73,73,74,74,74,74,75,75,75,75,75,74,74,74,74,74,74,74},
								   {70,72,73,74,74,74,75,75,76,76,77,77,77,77,78,78,78,78,78,77,77,77,77,77,77,77}};
	
	static public int computeStandard(boolean gender,int age,int height){
		int[] x_height=null;
		int[][] v=null;
		if(gender){
			x_height=f_x_height;
			v=f_v;
		}
		else{
			x_height=m_x_height;
			v=m_v;
		}
		int h_index=0,a_index=0;
		for(;a_index<y_age.length;a_index++){
			if(age<y_age[a_index])
				break;
		}
		for(;h_index<x_height.length;h_index++){
			if(height<x_height[h_index])
				break;
		}
		a_index=(a_index==0)?0:a_index-1;
		h_index=(h_index==0)?0:h_index-1;
		return v[h_index][a_index];
	}
}
