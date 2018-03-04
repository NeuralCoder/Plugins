/*
 * 
 */
package com.neuralcoder.pcc.xmlparse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// TODO: Auto-generated Javadoc
/**
 * The Class Component.
 */
class GdComponent{
	
	/** The status. */
	private String status;
	
	/** The date. */
	private LocalDate date;
	
	/**
	 * Instantiates a new component.
	 */
	public GdComponent()
	{
		super();
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setStatus(String status)
	{
		if ((!status.isEmpty()) && (!" ".equals(status)) && (null != status))
		{
			this.status = status.trim();
		}
		else
		{
			this.status = "null";
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
	public String getStatus()
	{
		return status;
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
