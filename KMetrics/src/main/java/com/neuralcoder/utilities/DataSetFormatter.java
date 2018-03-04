package com.neuralcoder.utilities;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;

import java.util.Set;
import java.util.TreeSet;

import com.neuralcoder.metrics.EMetricsModelType;
import com.neuralcoder.metrics.MetricsModel;

public class DataSetFormatter {
	
	public static String getFormatedDataSet(MetricsModel metricsModel, EMetricsModelType modelType, String categoryName, int index) {
		StringBuffer stringBuffer = new StringBuffer("['" + categoryName + "', ");
		
		Set<Integer> keySet = new TreeSet<Integer>(metricsModel.getMetricsSet(modelType).getIncluded().keySet());
		
		for (Integer key : keySet) {
			
			double[] dsNew = new double[10];
			
			double[] ds = metricsModel.getMetricsSet(modelType).getIncluded().get(key);
			int length = ds.length;
			// new item added in projectMetrics.xml(moRam). need to create a
			// new
			// array with 3 elements(the 3rd is for index "2")
			if (("RAM".equals(modelType.toString()) || "ROM".equals(modelType.toString()) || "EEPROM".equals(modelType.toString())) && (length == 2)) {
				dsNew[0] = ds[0];
				dsNew[2] = ds[1];
				dsNew[1] = 0;
			} else {
				dsNew = ds;
			}
			stringBuffer.append(dsNew[index] + ", ");
		}
		
		stringBuffer.replace(stringBuffer.length() - 2, stringBuffer.length(), "]");
		
		return stringBuffer.toString();
	}
	
	public static String getFormatedBuildsSet(MetricsModel metricsModel, EMetricsModelType modelType, String categoryName,
			AbstractProject<?, ?> project) {
		
		StringBuffer sb = new StringBuffer("['" + categoryName + "', ");
		
		Set<Integer> buildNumberSet = new TreeSet<Integer>(metricsModel.getMetricsSet(modelType).getIncluded().keySet());
		
		for (Integer buildNumber : buildNumberSet) {
			AbstractBuild<?,?> buildByNumber = project.getBuildByNumber(buildNumber);
			if (buildByNumber != null) {
				sb.append("'");
				sb.append(buildByNumber.getDisplayName());
				sb.append("', ");
			}
			
		}
		
		sb.replace(sb.length() - 2, sb.length(), "]");
		
		return sb.toString();
		
	}
	
}
