package com.neuralcoder.metrics;

import java.util.HashMap;
import java.util.Map;

public class MetricsModel {
	
	private Map<EMetricsModelType, IMetricsGeneric> metricsSet = new HashMap<EMetricsModelType, IMetricsGeneric>();
	
	public MetricsModel() {}
	
	public void addMetrics(EMetricsModelType metricsModelType, IMetricsGeneric metricsGeneric) {
		getMetricsSet().put(metricsModelType, metricsGeneric);
	}
	
	public void initializeModel() {
		for (EMetricsModelType type : EMetricsModelType.values()) {
			getMetricsSet().put(type, new MetricsGeneric());
		}
	}
	
	public void addMetrics(EMetricsModelType metricsModelType, int buildNumber, double... ds) {
		getMetricsSet().get(metricsModelType).add(buildNumber, ds);
	}
	
	public IMetricsGeneric getMetricsSet(EMetricsModelType type) {
		return metricsSet.get(type);
	}
	
	public Map<EMetricsModelType, IMetricsGeneric> getMetricsSet() {
		return metricsSet;
	}
	
	public void setMetricsSet(Map<EMetricsModelType, IMetricsGeneric> metricsSet) {
		this.metricsSet = metricsSet;
	}
	
	public void deleteBuild(int number) {
		
		for (EMetricsModelType modelType : EMetricsModelType.values()) {
			IMetricsGeneric metricsGenericSet = metricsSet.get(modelType);
			metricsGenericSet.remove(number);
		}
	}
}
