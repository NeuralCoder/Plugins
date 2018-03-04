package com.neuralcoder.metrics;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MetricsGeneric implements IMetricsGeneric {

	private int arrayLength = 0;
	private Map<Integer, double[]> included = new HashMap<Integer, double[]>();

	public MetricsGeneric() {

		super();

	}

	@Override
	public void add(int buildNumber, double... values) {

		this.arrayLength = values.length;
		included.put(buildNumber, values);
	}

	@Override
	public void remove(int buildNumber) {
		included.remove(buildNumber);
	}

	@Override
	public Map<Integer, double[]> getIncluded() {
		return new TreeMap<Integer, double[]>(included);
	}

	@Override
	public void setIncluded(Map<Integer, double[]> included) {
		this.included = included;
	}

	@Override
	public int getArrayLength() {
		return arrayLength;
	}

	@Override
	public void setArrayLength(int length) {
		this.arrayLength = length;
	}
}
