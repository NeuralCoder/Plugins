package com.neuralcoder.utilities;

import com.google.common.base.Strings;

public class SwattFile{
	private String fileName;
	private String coverageFile;
	
	public SwattFile(String fileName,String coverageFile){
		
		if ((!Strings.isNullOrEmpty(fileName)) && (!" ".equals(fileName)))
		{
			this.fileName = fileName;
		}
		else
		{
			this.fileName = "null";
		}
		
		if ((!Strings.isNullOrEmpty(coverageFile)) && (!" ".equals(coverageFile)))
		{
			this.coverageFile = coverageFile;
		}
		else
		{
			this.coverageFile = "0";
		}
	}
	
	public SwattFile(){
		super();
	}
	
	public void setName(String name){
		if ((!Strings.isNullOrEmpty(name)) && (!" ".equals(name)))
		{
			this.fileName = name;
		}
		else
		{
			this.fileName = "null";
		}
		
	}
	
	public void setCoverageType(String name){
		if ((!Strings.isNullOrEmpty(name)) && (!" ".equals(name)))
		{
			this.coverageFile = name;
		}
		else
		{
			this.coverageFile = "0";
		}
		
	}
	
	public String getName(){
		return fileName;
	}
	
	public String getCoverageType(){
		return coverageFile;
	}
}