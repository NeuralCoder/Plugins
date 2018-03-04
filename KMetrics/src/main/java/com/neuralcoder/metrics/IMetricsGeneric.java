package com.neuralcoder.metrics;

import java.util.Map;

public interface IMetricsGeneric {

	void add(int buildNumber, double... ds);

	void remove(int buildNumber);

	Map<Integer, double[]> getIncluded();

	int getArrayLength();

	void setArrayLength(int length);

	void setIncluded(Map<Integer, double[]> included);

}
