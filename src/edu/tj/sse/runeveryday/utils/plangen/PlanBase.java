package edu.tj.sse.runeveryday.utils.plangen;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.content.Context;


public class PlanBase {

	private SAXParserFactory factory=SAXParserFactory.newInstance();
	private SAXParser parser;
	private SAXPraserHelper helperHandler=new SAXPraserHelper();
	private Context context;
	
	public PlanBase(Context context){
		this.context=context;
	}
	
	public List<Training> getDefaultPlan(){
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
