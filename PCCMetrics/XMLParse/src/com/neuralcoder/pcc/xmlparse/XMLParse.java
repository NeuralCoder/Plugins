/*
 * Copyright 2016(C) < Neural Coder  >
 * 
 * Created on : 31-10-2016
 * Author     : Vlad Oparlescu
 *
 *-----------------------------------------------------------------------------
 * Revision History (Release 1.0)
 *-----------------------------------------------------------------------------
 * VERSION         AUTHOR/          DESCRIPTION OF CHANGE
 * OLD/NEW         DATE                        RFC NO
 *-----------------------------------------------------------------------------
 * 1.0/1.0 | Vlad Oparlescu       | Initial Revision.
 *         | 31-10-2016           |
 *---------|----------------------|--------------------------------------------
 *         | author               | Defect ID 1/Description
 *         | dd-mm-yy             | 
 *---------|----------------------|--------------------------------------------
 */
package com.neuralcoder.pcc.xmlparse;

import com.neuralcoder.pcc.xmlparse.SwComponent;
import com.neuralcoder.pcc.xmlparse.Release;
import com.neuralcoder.pcc.xmlparse.GdComponent;

import java.util.ArrayList;
import java.lang.String;

import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;


/**
*
* <h1>Parse XML</h1>
* The {@code XMLParse} class implements an application that
* parses an XML and stores the XML elements in private containers.
* <p>
* <b>Note:</b> The class provides public Getters and Setters for every 
* type of stored XML object
* 
*
* @author  Vlad Oparlescu
* @version 1.0
* @since   2016-10-31
*  
*/

public class XMLParse {
	
   /** The previous release XML object. */
   private static Release previousRelease = new Release();
   
   /** The current release XML object. */
   private static Release currentRelease = new Release();
   
   private static GdComponent workproductArch = new GdComponent();
   
   /** The aux sdd object for temporary storage. */
   private static SwComponent auxSdd = new SwComponent();
   
   /** The aux srs object for temporary storage. */
   private static SwComponent auxSrs = new SwComponent();
   
   /** The aux review object for temporary storage. */
   private static SwComponent auxReview = new SwComponent();
   
   /** The aux mts object for temporary storage. */
   private static SwComponent auxMts = new SwComponent();
   
   /** The aux its object for temporary storage. */
   private static SwComponent auxIts = new SwComponent();
   
   /** The aux rts object for temporary storage. */
   private static SwComponent auxRts = new SwComponent();
   
   /** The XML elements list of old release dates. */
   private static ArrayList<Release> oldRelease = new ArrayList<>();
   
   /** The XML elements list of next release dates. */
   private static ArrayList<Release> nextRelease = new ArrayList<>();
   
   /** The XML elements list containing sdd(sw design document) indicators. */
   private static ArrayList<SwComponent> indicatorSdd = new ArrayList<>();
   
   /** The XML elements list containing srs(sw requirements specification) indicators. */
   private static ArrayList<SwComponent> indicatorSrs = new ArrayList<>();
   
   /** The XML elements list containing review indicators. */
   private static ArrayList<SwComponent> indicatorReview = new ArrayList<>();
   
   /** The XML elements list containing mts(module test specification) indicators. */
   private static ArrayList<SwComponent> indicatorMts = new ArrayList<>();
   
   /** The XML elements list containing its(integration test specification) indicators. */
   private static ArrayList<SwComponent> indicatorIts = new ArrayList<>();
   
   /** The XML elements list containing rts(requirements test specification) indicators. */
   private static ArrayList<SwComponent> indicatorRts = new ArrayList<>();
   
   /**
    * This method is used as a getter for the XML elements component list.
    * The method receives an argument and it returns a list of XML elements.
    * Based on the argument sent,it will return the desired indicators list.
    * If the argument is different from the indicators that can be provided,
    * {@code null} will be returned.
    *
    * @param metricsType representing the type of desired list
    * @return the desired indicator component list
    */
   public static ArrayList<SwComponent> getComponentList(String metricsType)
   {
		switch (metricsType) {

		case "Sdd":
			return indicatorSdd;

		case "Srs":
			return indicatorSrs;

		case "Review":
			return indicatorReview;

		case "Mts":
			return indicatorMts;

		case "Its":
			return indicatorIts;

		case "Rts":
			return indicatorRts;

		default:
			//TODO implement null pattern
			return null;
	   }
   }
   
   /**
    * This method is used as a getter for the XML elements component list.
    * The method returns an GD XML element list.More exactly the XML child 
    * of SW Architecture parent. 
    *
    * @return the XML element child under SW Architecture parent
    */
   public static GdComponent getGdComponent()
   {
	   return workproductArch;
   }
   
   /**
    * This method is used as a getter for the XML elements release list.
    * The method receives an argument and it returns a list of XML elements.
    * Based on the argument sent,it will return the desired release list.
    * If the argument is different from the releases that can be provided,
    * {@code null} will be returned.
    *
    * @param dateType represents the type of the release,old or next
    * @return the desired release list
    */
   public static ArrayList<Release> getReleaseList(String dateType)
   {
		if (("old".equals(dateType) && !oldRelease.isEmpty())) {
			return oldRelease;
		}

		else if ("next".equals(dateType) && !nextRelease.isEmpty()) {
			return nextRelease;
		}

		else{
			//TODO implement null pattern
			return null;
		}
   }
   
   /**
    * This method is used as a getter for the XML stored releases.
    * The method receives an argument and it returns a release object.
    * Based on the argument sent,it will return the desired release.
    * If the argument is different from the releases that can be provided,
    * {@code null} will be returned.
    *
    * @param dateType represents the type of the release,previous or current
    * @return the desired release
    */
   public static Release getRelease(String dateType)
   {
		if ("previous".equals(dateType)) {
			return previousRelease;
		}

		else if ("current".equals(dateType)) {
			return currentRelease;
		}

		else {
			//TODO implement null pattern
			return null;
		}
   }
   
   /**
    * This method is general purpose developed.It is used for storing the 
    * indicators and releases retrieved from XML elements during the parsing.
    * The values retrieved are sent as arguments and put in different containters,
    * based on their type.This is done by using several arguments for the value,
    * date,type and factor.
    *
    * @param value the value that XML element has as an attribute
    * @param date the date that XML element has as an attribute
    * @param factor the factor is used only for Component objects,for having
    * 		 a generic case(dissociated by the caller,not computed by the method)
    * @param type the type of the object,component or release object
    */
   private static void storeValues(String value,String date,SwComponent factor,String type)
   {
	   Release auxObj = new Release();
	   
	   switch(type)
	   {
	   
	   case "WorkStatus":
		   workproductArch.setStatus(value);
		
		   break;
		   
	   case "WorkDate":
		   workproductArch.setDate(date);
		
		   break;
	   
	   case "Old":
		   auxObj.setName(value);
		   auxObj.setDate(date);
		   
		   oldRelease.add(new Release(auxObj.getName(),auxObj.getDate()));
		   
		   break;
		   
	   case "Previous":
		   auxObj.setName(value);
		   auxObj.setDate(date);

		   previousRelease = new Release(auxObj.getName(),auxObj.getDate());
			
		   break;
		   
	   case "Current":
		   	auxObj.setName(value);
		   	auxObj.setDate(date);
			
		   	currentRelease = new Release(auxObj.getName(),auxObj.getDate());
		   	
		   	break;
		   
	   case "Next":
		   	auxObj.setName(value);
		   	auxObj.setDate(date);
			
		   	nextRelease.add(new Release(auxObj.getName(),auxObj.getDate()));
		   	break;	   
	   
	   case "Name":
		   	factor.setName(value);
		 
		   	factor.isName = true;
		   	
		   	break;   
		   
	   case "Target":
		   	factor.setTarget(value);
			factor.setDate(date);
			
			factor.isTarget = true;
			factor.isDate = true;
			
			break;
		   
	   case "Justification":
		   
		   	factor.setJustification(value);
		   
		   	factor.isJustification = true;
		   
		   	break;
	   }
	 
   }
   
   /**
    * Parses the sw dashboard.
    *
    * @param filePath the file path
    */
   public static final void parseSWDashboard(String filePath)
   {
	  boolean fileExists = false;
	  boolean isArchitecture = false;
	  
      try 
      {
         XMLInputFactory factory = XMLInputFactory.newInstance();
         XMLStreamReader streamReader = 
         factory.createXMLStreamReader(new FileReader(filePath));

            while(streamReader.hasNext()){
               int event = streamReader.next();
               switch(event){
                  case XMLStreamConstants.START_ELEMENT:
                	  
                	  switch(streamReader.getLocalName())
                	  {
                	  	  case Utils.GD:
                	  		  if ("SW Architecture".equals(streamReader.getAttributeValue(0).trim())){
                	  			isArchitecture = true;
                	  		  }
                	  		  
                	  		  if ((isArchitecture) && streamReader.getAttributeValue(1) != null){
                	  			if ("Workproduct status".equals(streamReader.getAttributeValue(1).trim())){
                    	  			storeValues(streamReader.getAttributeValue(0),null,null,"WorkStatus");
                    	  		  }
                    	  		  
                    	  		 if ("Workproduct Target date".equals(streamReader.getAttributeValue(1).trim()) && streamReader.getAttributeValue(2)!= null){
                     	  			storeValues(null,streamReader.getAttributeValue(2),null,"WorkDate");
                     	  		  }    
                	  		  }
                	  		  break;
                	  		  
                		  case Utils.OLD:
                			  storeValues(streamReader.getAttributeValue(0),streamReader.getAttributeValue(1),null,"Old");
                			  break;
                			  
                		  case Utils.PREVIOUS:
                			  storeValues(streamReader.getAttributeValue(0),streamReader.getAttributeValue(1),null,"Previous");
                			  break;
                			  
                		  case Utils.CURRENT:
                			  storeValues(streamReader.getAttributeValue(0),streamReader.getAttributeValue(1),null,"Current");
                			  break;
                			  
                		  case Utils.NEXT:
                			  storeValues(streamReader.getAttributeValue(0),streamReader.getAttributeValue(1),null,"Next");
                			  break;
                			  
                		  case Utils.SW:
                			  if((!fileExists) && streamReader.getAttributeValue(1)!= null)
                			  {
                				  if(streamReader.getAttributeValue(1).trim().equals("FILE_NAME"))
                				  {
                					  if (!streamReader.getAttributeValue(0).trim().equalsIgnoreCase("N/A") && 
                						  !streamReader.getAttributeValue(0).equals(" ") && 
                					      !streamReader.getAttributeValue(0).trim().isEmpty() && 
                					       streamReader.getAttributeValue(0) != null )
                					  {  
                						  fileExists = true;
                					  }
                				  }
                			  }
                			  
                			  if (fileExists)
                			  {
                				  String value = streamReader.getAttributeValue(1).trim();
                			
                				  switch(value)
                				  {
         
                				  	case Utils.SDD:
                				  		storeValues(streamReader.getAttributeValue(0),null,auxSdd,"Name");
                				  		break;
                    		 	 
                				  	case Utils.SDD_TARGET:
                				  		storeValues(streamReader.getAttributeValue(0),streamReader.getAttributeValue(2),auxSdd,"Target");
                				  		break;
                    			 
                				  	case Utils.SDD_JUSTIFICATION:
                				  		storeValues(streamReader.getAttributeValue(0),null,auxSdd,"Justification");
                				  		break;
                    			 
                				  	case Utils.SRS:
                				  		storeValues(streamReader.getAttributeValue(0),null,auxSrs,"Name");
                				  		break;
                    			  
                				  	case Utils.SRS_TARGET:
                				  		storeValues(streamReader.getAttributeValue(0),streamReader.getAttributeValue(2),auxSrs,"Target");
                				  		break;
                    			 
                				  	case Utils.SRS_JUSTIFICATION:
                				  		storeValues(streamReader.getAttributeValue(0),null,auxSrs,"Justification");
                				  		break;
                    			 
                				  	case Utils.REVIEW:
                				  		storeValues(streamReader.getAttributeValue(0),null,auxReview,"Name");
                				  		break;
                    			 
                				  	case Utils.REVIEW_TARGET:
                				  		storeValues(streamReader.getAttributeValue(0),streamReader.getAttributeValue(2),auxReview,"Target");
                				  		break;
                    			 
                				  	case Utils.REVIEW_JUSTIFICATION:
                				  		storeValues(streamReader.getAttributeValue(0),null,auxReview,"Justification");
                				  		break;
                    			 
                				  	case Utils.MTS:
                				  		storeValues(streamReader.getAttributeValue(0),null,auxMts,"Name");
                				  		break;
                    			 
                				  	case Utils.MTS_TARGET:
                				  		storeValues(streamReader.getAttributeValue(0),streamReader.getAttributeValue(2),auxMts,"Target");
                				  		break;
                    			 
                				  	case Utils.MTS_JUSTIFICATION:
                				  		storeValues(streamReader.getAttributeValue(0),null,auxMts,"Justification");
                				  		break;
                    			 
                				  	case Utils.ITS:
                				  		storeValues(streamReader.getAttributeValue(0),null,auxIts,"Name");
                				  		break;
                    			 
                				  	case Utils.ITS_TARGET:
                				  		storeValues(streamReader.getAttributeValue(0),streamReader.getAttributeValue(2),auxIts,"Target");
                				  		break;
                    			 
                				  	case Utils.ITS_JUSTIFICATION:
                				  		storeValues(streamReader.getAttributeValue(0),null,auxIts,"Justification");
                				  		break;
                    			 
                				  	case Utils.REQUIREMENTS:
                				  		storeValues(streamReader.getAttributeValue(0),null,auxRts,"Name");
                				  		break;
                    			 
                				  	case Utils.RTS_TARGET:
                				  		storeValues(streamReader.getAttributeValue(0),streamReader.getAttributeValue(2),auxRts,"Target");
                				  		break;
                    			 
                				  	case Utils.RTS_JUSTIFICATION:
                				  		storeValues(streamReader.getAttributeValue(0),null,auxRts,"Justification");
                				  		break;
                    		 }
                    	 }
                	}
                	        
                     break;

                  case  XMLStreamConstants.END_ELEMENT:
                	  
                	  if("GDComponents".equals(streamReader.getLocalName())){
                		  isArchitecture = false;
                	  }
                	  
                	  if("Decomposition".equals(streamReader.getLocalName()))
                	  {
                		  if(auxSdd.isComponentConsistent())
                		  {
                			  indicatorSdd.add(new SwComponent(auxSdd.getName(),auxSdd.getTarget(),auxSdd.getJustification(),auxSdd.getDate()));
                			  auxSdd.resetBoolValues();
                		  }
                		  
                		  if(auxSrs.isComponentConsistent())
                		  {
                			  indicatorSrs.add(new SwComponent(auxSrs.getName(),auxSrs.getTarget(),auxSrs.getJustification(),auxSrs.getDate()));
                			  auxSrs.resetBoolValues();
                		  }
                		  
                		  if(auxReview.isComponentConsistent())
                		  {
                			  indicatorReview.add(new SwComponent(auxReview.getName(),auxReview.getTarget(),auxReview.getJustification(),auxReview.getDate()));
                			  auxReview.resetBoolValues();
                		  }
                		  
                		  if(auxMts.isComponentConsistent());
                		  {
                			  indicatorMts.add(new SwComponent(auxMts.getName(),auxMts.getTarget(),auxMts.getJustification(),auxMts.getDate()));
                			  auxMts.resetBoolValues();
                		  }
                		  
                		  if(auxIts.isComponentConsistent())
                		  {
                			  indicatorIts.add(new SwComponent(auxIts.getName(),auxIts.getTarget(),auxIts.getJustification(),auxIts.getDate()));
                			  auxIts.resetBoolValues();
                		  }
                		  
                		  if(auxRts.isComponentConsistent())
                		  {
                			  indicatorRts.add(new SwComponent(auxRts.getName(),auxRts.getTarget(),auxRts.getJustification(),auxRts.getDate()));
                			  auxRts.resetBoolValues();
                		  }
                	
                		  fileExists = false;
                	  }
                       
                     break;
               }		    
            }  
      }
      catch (FileNotFoundException e){
    	  e.printStackTrace();
      } 
      catch (XMLStreamException e) {
            e.printStackTrace();
      }
   }
}



