package edu.tj.sse.runeveryday.database.business;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import edu.tj.sse.runeveryday.database.DatabaseHelper;
import edu.tj.sse.runeveryday.database.entity.Plan;
import edu.tj.sse.runeveryday.database.entity.Training;
import edu.tj.sse.runeveryday.utils.plangen.SAXPraserHelper;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class PlanBase {

	private SAXParserFactory factory=SAXParserFactory.newInstance();
	private SAXParser parser;
	private SAXPraserHelper helperHandler=new SAXPraserHelper();
	private Context context;
	
	private DatabaseHelper dbhDatabaseHelper;
	private RuntimeExceptionDao<Plan,Integer> plandao;
	private RuntimeExceptionDao<Training,Integer> trainingdao;
	private SharedPreferences settings;
	
	public PlanBase(Context context){
		this.context=context;
		dbhDatabaseHelper = new DatabaseHelper(context);
		plandao=dbhDatabaseHelper.getPlanDataDao();
		trainingdao=dbhDatabaseHelper.getTrainingRuntimeDao();
		settings = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	/*
	 * @return:返回默认计划
	 */
	public List<Training> getDefaultPlan(){
		List<Training> trainlist=new ArrayList<Training>();
		Plan defaultplan=plandao.queryForId(1);
		trainlist.addAll(defaultplan.getTrainings());
		return trainlist;
	}
	
	/*
	 * @return:返回当前计划
	 */
	public List<Training> getCurrentPlan(){
		List<Training> trainlist=new ArrayList<Training>();
		int currentPlanID=settings.getInt("currentPlanID", 1);
		Plan currentPlan=plandao.queryForId(currentPlanID);
		trainlist.addAll(currentPlan.getTrainings());
		return trainlist;
	}
	
	/*
	 * 根据ID得到计划
	 */
	public Plan getPlanByID(int planID){
		return plandao.queryForId(planID);
	}
	/*
	 * 返回当前需进行的训练
	 * @return: null所有训练已经完成
	 */
	public Training getCurrentTraining(){
		Map<String,Object> queryValues=new HashMap<String,Object>();
		queryValues.put("plan_id", 1);
		queryValues.put("isdone", false);
		List<Training> result=trainingdao.queryForFieldValues(queryValues);
		if(result.size()>0){
			int i=0,minOrder=Integer.MAX_VALUE,target=0;
			for(;i<result.size();i++){
				if(result.get(i).getOrder()<minOrder){
					minOrder=result.get(i).getOrder();
					target=i;
				}
			}
			return result.get(target);
		}else{
			return null;
		}
	}
	
	/*
	 * 设置训练完成
	 */
	public void setTrainingDone(Training training){
		training.setIsdone(true);
		trainingdao.update(training);
	}
	
	/*
	 * 设置执行某套计划
	 * @currentPlanID:计划ID
	 */
	public void setCurrentPlan(int currentPlanID){
		clearTrainingTagInfo(currentPlanID);
		
		SharedPreferences.Editor editor=settings.edit();
		editor.putInt("currentPlanID", currentPlanID);
		editor.commit();
	}
	
	/*
	 * 清除训练完成标记
	 */
	public void clearTrainingTagInfo(int planID){
		String clearString="update training set isdone=0 where plan_id="+planID;
		trainingdao.executeRawNoArgs(clearString);
	}

	public boolean isCurrentPlan(int planID){
		int currentPlanID=settings.getInt("currentPlanID", 1);
		return currentPlanID==planID;
	}
	@Deprecated
	public List<Training> getDefaultPlanFromXML(){
		try {
			parser=factory.newSAXParser();
			XMLReader xmlReader=parser.getXMLReader();
			xmlReader.setContentHandler(helperHandler);
			
			InputStream stream=context.getResources().getAssets().open("defaultplan.xml");
			InputSource is=new InputSource(stream);
			xmlReader.parse(is);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return helperHandler.getTrainings();
	}
	
}
