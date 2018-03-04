/*
 * Copyright 2016(C) < S.C. neuralcoder Automotive Romania S.R.L >
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// TODO: Auto-generated Javadoc
/**
 * The Class Component.
 */
public class SwComponent{
	
	/** The name. */
	private String name;
	
	/** The target. */
	private String target;
	
	/** The justification. */
	private String justification;
	
	/** The date. */
	private LocalDate date;
	
	/** The is name. */
	boolean isName;
	
	/** The is target. */
	boolean isTarget;
	
	/** The is justification. */
	boolean isJustification;
	
	/** The is date. */
	boolean isDate;
	
	/**
	 * Instantiates a new component.
	 *
	 * @param name the name
	 * @param target the target
	 * @param justification the justification
	 * @param date the date
	 */
	public SwComponent(String name,String target,String justification,LocalDate date)
	{
		this.name = name;
		this.target = target;
		this.justification = justification;
		this.date = date; 
	}
	
	/**
	 * Instantiates a new component.
	 */
	public SwComponent()
	{
		super();
	}
	
	/**
	 * Checks if is component consistent.
	 *
	 * @return true, if is component consistent
	 */
	public boolean isComponentConsistent()
	{	
		return (this.isName == true && this.isTarget == true && this.isJustification ==true && this.isDate ==true) ? true : false;
	}
	
	/**
	 * Reset bool values.
	 */
	public void resetBoolValues()
	{
		this.isJustification = false;
		this.isTarget = false;
		this.isName = false;
		this.isDate = false;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name)
	{
		if ((!name.isEmpty()) && (!" ".equals(name)) && (null != name))
		{
			this.name = name.trim();
		}
		else
		{
			this.name = "null";
		}
		
	}
	
	/**
	 * Sets the target.
	 *
	 * @param target the new target
	 */
	public void setTarget(String target)
	{
		if ((!target.isEmpty()) && (!" ".equals(target)) && (null != target))
		{
			this.target = target.trim();
		}
		else
		{
			this.target = "null";
		}
	}
	
	/**
	 * Sets the justification.
	 *
	 * @param justification the new justification
	 */
	public void setJustification(String justification)
	{
		if ((!justification.isEmpty()) && (!" ".equals(justification)) && (null != justification))
		{
			this.justification = justification.trim();
		}
		else
		{
			this.justification = "null";
		}
	}
	
	/**
	 * Sets the date.
	 *
	 * @param date the new date
	 */
	public void setDate(String date)
	{	
		 if ((date != null) && (!date.isEmpty()) &&  (!" ".equals(date)) )
		 {
			 this.date = LocalDate.parse(date.trim(),DateTimeFormatter.ofPattern("yyyy-M-d")); 
		 }
		 else 
		{
			 this.date = LocalDate.MIN; 
		}
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Gets the target.
	 *
	 * @return the target
	 */
	public String getTarget()
	{
		return target;
	}
	
	/**
	 * Gets the justification.
	 *
	 * @return the justification
	 */
	public String getJustification()
	{
		return justification;
	}
	
	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public LocalDate getDate()
	{
		return date;
	}
}
