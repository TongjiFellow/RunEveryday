package edu.tj.sse.runeveryday.utils.plangen;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import edu.tj.sse.runeveryday.database.business.PlanBase;
import edu.tj.sse.runeveryday.database.business.UserBase;
import edu.tj.sse.runeveryday.database.entity.Plan;
import edu.tj.sse.runeveryday.database.entity.Training;
import edu.tj.sse.runeveryday.database.entity.User;


public class PlanGenerator {
	
	private Context context;
	private int defaultPlan=1;
	private int primaryPlan=2;
	private int seniorPlan=3;
	private PlanBase planBase;
	public PlanGenerator(Context context){
		this.context=context;
		planBase=new PlanBase(this.context);
	}
	public List<Training> RecommendByWeight(){
		User user=new UserBase(context).getUser();
		int planID=1;
		if(user!=null){
			int standard=StandardWeight.computeStandard(user.isGender(), user.getAge(), user.getHeight());
			float diff=Math.abs(standard-user.getWeight());
			if(diff<1)
				planID=seniorPlan;
			else if(diff<3)
				planID=primaryPlan;
			else 
				planID=defaultPlan;
		}
		if(!planBase.isCurrentPlan(planID)){
			planBase.setCurrentPlan(planID);
		}
		Plan plan=planBase.getPlanByID(planID);
		List<Training> trainlist=new ArrayList<Training>();
		trainlist.addAll(plan.getTrainings());
		return trainlist;
	}
}
