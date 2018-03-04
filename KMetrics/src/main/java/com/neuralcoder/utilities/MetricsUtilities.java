package com.neuralcoder.utilities;

import hudson.model.AbstractBuild;
import hudson.model.Run;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import com.neuralcoder.metrics.MetricsModel;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class MetricsUtilities {
	
	public static EStatus saveMetricsToJSONData(File file, MetricsModel metricsModel) {
		
		try {
			
			JSONSerializer jsonSerializer = new JSONSerializer();
			
			String deepSerialize = jsonSerializer.deepSerialize(metricsModel);
			
			Files.write(file.toPath(), deepSerialize.getBytes(), StandardOpenOption.CREATE);
			
		} catch (IOException e) {
			
			e.printStackTrace();
			return EStatus.FAILURE;
			
		}
		
		return EStatus.SUCCESS;
		
	}
	
	public static MetricsModel loadMetricsFromJSONData(File fromFile) {
		
		/*
		 * We need to change the class loader because of different context of
		 * execution used my Jenkins (jelly/Java/etc).
		 */
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(MetricsUtilities.class.getClassLoader());
		
		try {
			
			String fileContent = new String(Files.readAllBytes(fromFile.toPath()));
			
			JSONDeserializer<MetricsModel> jsonDeserializer = new JSONDeserializer<MetricsModel>();
			
			MetricsModel metricsModel = jsonDeserializer.deserialize(fileContent);
			
			return metricsModel;
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
		finally {
			
			Thread.currentThread().setContextClassLoader(contextClassLoader);
			
		}
		
		return null;
		
	}
	
	/**
	 * @param build of type Run
	 * @return  -  KPI folder as a File object
	 */
	public static File getKPIFolder(Run<?, ?> build) {
		File buildPath = build.getRootDir().getParentFile().getParentFile();
		System.out.println(buildPath);
		return new File(buildPath, GlobalProperties.KPI_FOLDER);
	}
	
	/**
	 * @param build of type AbstractBuild
	 * @return  -  KPI folder as a File object
	 */
	public static File getKPIFolder(AbstractBuild<?, ?> build) {
		File buildPath = build.getRootDir().getParentFile().getParentFile();
		System.out.println(buildPath);
		return new File(buildPath, GlobalProperties.KPI_FOLDER);
	}
	
	/**
	 * @param build 
	 * @return KPI folder Path, but also creates "KPI" folder at the same lavel with "builds" folder
	 */
	public static File createKPIFolder(Run<?, ?> build) {
		File destinationPath = new File(build.getRootDir().getParentFile().getParentFile(),GlobalProperties.KPI_FOLDER);
		if (!destinationPath.exists()) {
			destinationPath.mkdirs();
			destinationPath.setWritable(true);
		}
		return destinationPath;
	}
	
	/**
	 * @param source  - source file path
	 * @param destination - destination file path 
	 * @param fileName - file name of the file to be copied
	 * @return TRUE, if the file can be copied from source to destination(override the existing one), FALSE otherwise 
	 */
	public static boolean copyFile(String source, String destination, String fileName) {
		
		File f1 = new File(source, fileName);
		File f2 = new File(destination, fileName);
		boolean ok = true;
		try {
			Files.copy(f1.toPath(), f2.toPath(), StandardCopyOption.REPLACE_EXISTING);
			System.out.println("File copied to destination.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ok = false;
		}
		return ok;
	}
	
	/**
	 * @param fileName
	 * @return an ArrayList- each element represents a line text
	 */
	public static ArrayList<String> getValueFromTxtFile(File fileName) {
		ArrayList<String> fileValues = new ArrayList<String>();
		try (BufferedReader fileValue = new BufferedReader(new FileReader(fileName))){
			String line = null;
			while ((line = fileValue.readLine()) != null) {
				fileValues.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return fileValues;
	}
}
