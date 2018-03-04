package com.neuralcoder.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;

import com.google.common.base.Strings;

public class PropertiesReader {
	
	public String readFromProperties(String propertiesKey) {
		
		if (Strings.isNullOrEmpty(propertiesKey)) {
			
			return "";
			
		}
		
		Properties properties = new Properties();
		
		try (InputStream resourceStream = this.getClass().getClassLoader().getResourceAsStream(GlobalProperties.XML_PARSING_PATHS_PROPERTIES);) {
			
			properties.load(resourceStream);
			
		} catch (IOException e) {
			
			GlobalProperties.logger.log(Level.WARNING, e.getMessage());
			
		}
		
		String propertyValues = properties.getProperty(propertiesKey);
		return (propertyValues == null) ? "" : propertyValues;
		
	}
}
