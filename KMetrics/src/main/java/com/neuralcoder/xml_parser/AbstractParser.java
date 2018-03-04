package com.neuralcoder.xml_parser;

import hudson.model.AbstractBuild;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import com.neuralcoder.metrics.MetricsModel;
import com.neuralcoder.utilities.PropertiesReader;
import com.neuralcoder.utilities.XPathParser;

public abstract class AbstractParser implements Callable<Void> {

	protected ArrayList<File> projectMetricsFile;

	protected PropertiesReader propertiesReader;

	protected int buildNumber;
	
	protected AbstractBuild<?,?> abstractbuild;

	protected XPathParser parser = new XPathParser();

	protected MetricsModel metricsModel;

	public AbstractParser(ArrayList<File> projectMetricsFile, PropertiesReader propertiesReader, int buildNumber, MetricsModel metricsModel, AbstractBuild<?,?> build) {

		super();

		this.projectMetricsFile = projectMetricsFile;

		this.propertiesReader = propertiesReader;

		this.buildNumber = buildNumber;

		this.metricsModel = metricsModel;
		
		this.abstractbuild=build;

	}

	@Override
	public Void call() {

		computeKMetrics();

		return null;

	}

	protected abstract void computeKMetrics();
 
}
