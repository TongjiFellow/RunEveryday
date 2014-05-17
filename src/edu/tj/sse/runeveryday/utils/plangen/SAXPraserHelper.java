package edu.tj.sse.runeveryday.utils.plangen;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import edu.tj.sse.runeveryday.database.entity.Training;
import android.util.Log;

public class SAXPraserHelper extends DefaultHandler{
	
	private String Tag="SAXPraserHelper";
	private List<Training> trainings;
	private Training training;
	
	private String currentTag;
	private String currentValue;
	
	public List<Training> getTrainings(){
		return trainings;
	}
	
	//当解析到文档开始时的回调方法
	@Override
    public void startDocument() throws SAXException
    {
		trainings = new ArrayList<Training>();
    }
	
	//  当解析到xml的标签时的回调方法
    @Override
	public void startElement(String uri, String localName, String qName,
	         Attributes attributes) throws SAXException
	{
	      if("train".equals(localName))
	      {
	          training = new Training();
	      }
	      //    设置当前的标签名
	      currentTag = localName;
	 }
    
	  //  当解析到xml的文本内容时的回调方法
	  @Override
	  public void characters(char[] ch, int start, int length)
	          throws SAXException
	  {
	      //    得到当前的文本内容
	      currentValue = new String(ch,start, length);
	      //    当currentValue不为null、""以及换行时
	      if(currentValue != null && !"".equals(currentValue) && !"\n".equals(currentValue))
	      {
	          //    判断当前的currentTag是哪个标签
	          if("week".equals(currentTag))
	          {
	              training.setWeek(currentValue);
	          }
	          else if("day".equals(currentTag))
	          {
	        	  training.setDay(currentValue);
	          }
	          else if("work".equals(currentTag))
	          {
	        	  training.setWork(currentValue);
	          }
	      }
	      //    清空currentTag和currentValue
	      currentTag = null;
	      currentValue = null;
	  }
	  
	//    当解析到标签的结束时的回调方法
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException
    {
        if("train".equals(localName))
        {
            trainings.add(training);
            training = null;
        }
    }
}

