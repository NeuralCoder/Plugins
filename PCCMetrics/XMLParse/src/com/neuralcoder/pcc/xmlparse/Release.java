/*
 * 
 */
package com.neuralcoder.pcc.xmlparse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// TODO: Auto-generated Javadoc
/**
 * The Class Release.
 */
public class Release{
	
	/** The name. */
	private String name;
	
	/** The date. */
	private LocalDate date;
	
	/**
	 * Instantiates a new release.
	 *
	 * @param name the name
	 * @param date the date
	 */
	public Release(String name,LocalDate date)
	{
		 this.name = name;
	     this.date = date;
		
	}
	
	/**
	 * Instantiates a new release.
	 */
	public Release()
	{
		super();
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
	 * Sets the date.
	 *
	 * @param date the new date
	 */
	public void setDate(String date)
	{
		 if ( (!date.isEmpty()) &&  (!" ".equals(date)) && (date != null) )
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
	 * Gets the date.
	 *
	 * @return the date
	 */
	public LocalDate getDate()
	{
		return date;
	}
}