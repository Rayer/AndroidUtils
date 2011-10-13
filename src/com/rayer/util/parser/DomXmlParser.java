package com.rayer.util.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DomXmlParser {
	
	String                 xmlString = null;
	ByteArrayInputStream   xml       = null;
	Document               dom       = null;
	DocumentBuilder        db        = null;
	DocumentBuilderFactory dbf       = DocumentBuilderFactory.newInstance();
	Element                docEle    = null;
	NodeList               nl        = null;
	
	//  初始化 nl 物件以供往後的 parsing化
	public DomXmlParser(String xmlStr, String tag) {
		xmlString  = xmlStr;
		xml        = new ByteArrayInputStream(xmlString.getBytes());
		
		//Log.d("hamibookdbg", "start parsing : " + xmlStr + "with root tag = " + tag);
		try {
			db     = dbf.newDocumentBuilder();
			dom    = db.parse(xml);
			docEle = dom.getDocumentElement();
			nl = dom.getElementsByTagName(tag);
			//nl     = docEle.getElementsByTagName(tag);
			
			//Log.d("hamibookdbg", "parsing components : " + db + " " + dom + " " + docEle + " " + nl);
		} catch(ParserConfigurationException pce) { pce.printStackTrace();//Util.alert(pce);
		} catch(SAXException se)                  { se.printStackTrace();//Util.alert(se);
		} catch(IOException ioe)                  { ioe.printStackTrace();//Util.alert(ioe);
		} catch(Exception e)                      { e.printStackTrace();}//Util.alert(e); }
	}
	
	public int getEntries() {
		if(nl != null) { return nl.getLength(); }
		else return 0;
	}
	
	public String getTextValue(int position, String tagName) {
		//Log.d("hamibookdbg", "position : " + position + " tagname : " + tagName);
		String  textVal = null;
		if(nl != null) {
			Element el = (Element)nl.item(position);
			//Log.d("hamibookdbg", "length : " + nl.getLength());
			if(el != null) {
				NodeList nodeList = el.getElementsByTagName(tagName); 
				if(nodeList != null && nodeList.getLength() > 0) {
					Element element = (Element)nodeList.item(0);
					try {
						textVal = element.getFirstChild().getNodeValue();
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
			else {
				//Log.d("hamibookdbg", "no such attr");
				new RuntimeException("no such attr");
			}
		}
		return textVal;
	}
		
	public Integer getIntValue(int position, String tagName) {
		Integer intValue = null;
		if(nl != null) { 
			String textValue = getTextValue(position,tagName);
			if(textValue != null) {
				try { intValue = new Integer(Integer.parseInt(textValue)); }
				catch(Exception e) { e.printStackTrace(); }
			} 
		}
		return intValue;
	}
	
	public String getAttrTextValue(int position, String tagName) {
		String  textVal = null;
		if(nl != null) {
			Element el = (Element)nl.item(position);
			if(el != null) {
				textVal = el.getAttribute(tagName);
			}
			else {
				new RuntimeException("no such attr");
			}
		}
		return textVal;
	}
	
	public Integer getAttrIntValue(int position, String tagName) {
		Integer intValue = null;
		if(nl != null) { 
			String textValue = getAttrTextValue(position,tagName);
			if(textValue != null) {
				try { intValue = new Integer(Integer.parseInt(textValue)); }
				catch(Exception e) { e.printStackTrace(); }
			} 
		}
		return intValue;
	}
}
