package com.neuralcoder.utilities;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.util.RunList;

import static com.neuralcoder.metrics.EMetricsModelType.K1;
import static com.neuralcoder.metrics.EMetricsModelType.K1Lite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.neuralcoder.metrics.MetricsModel;
import com.neuralcoder.xml_parser.K1parser;

public class ProjectMetricsJobIterator {
	
	private static final int THREADS_NUMBER = 8;
	private AbstractProject<?, ?> project;
	private MetricsModel metricsModel;
	private AbstractBuild<?, ?> build;
	
	public ProjectMetricsJobIterator(AbstractProject<?, ?> project, MetricsModel metricsModel) {
		this.metricsModel = metricsModel;
		this.project = project;
	}
	
	public ProjectMetricsJobIterator(AbstractBuild<?, ?> build, MetricsModel metricsModel) {
		this.metricsModel = metricsModel;
		this.build = build;
	}
	
	private File createNewFile(File oldFile, String newFileName) {
		File pathFile = new File(oldFile, GlobalProperties.K_FOLDER);
		File newFile = new File(pathFile, newFileName);
		return newFile;
	}
	
	public void addBuildToModel() {
		
		PropertiesReader propertiesReader = new PropertiesReader();
		ArrayList<File> projectFiles = new ArrayList<File>();
		
		File projectMetricsFile = build.getRootDir();
		// projectMetricsFile = new File(projectMetricsFile,
		// GlobalProperties.K_FOLDER);
		projectFiles.add(createNewFile(projectMetricsFile, GlobalProperties.SW_DASHBOARD_FILE));
		projectFiles.add(createNewFile(projectMetricsFile, GlobalProperties.SW_JUNIT_FILE));
		projectFiles.add(createNewFile(projectMetricsFile, GlobalProperties.FILE_PROPERTIES));
		
		// projectFiles.add(createNewFilePath(GlobalProperties.SWPath));
		// projectFiles.add(createNewFilePath(GlobalProperties.JUPath));
		// projectFiles.add(createNewFilePath(GlobalProperties.FPPath));
		
		startThreads(projectFiles, propertiesReader, build.getNumber(), build);
	}
	
	public void iterateOnBuildsAndStartParser() {
		
		File projectMetricsFile;
		RunList<?> buildsList = project.getBuilds();
		ListIterator<?> listIterator = buildsList.listIterator();
		ArrayList<File> projectFiles = new ArrayList<>();
		
		while (listIterator.hasNext()) {
			
			AbstractBuild<?, ?> nextBuild = (AbstractBuild<?, ?>) listIterator.next();
			
			projectMetricsFile = nextBuild.getRootDir();
			projectMetricsFile = new File(projectMetricsFile, GlobalProperties.K_FOLDER);
			projectFiles.add(createNewFile(projectMetricsFile, GlobalProperties.SW_DASHBOARD_FILE));
			projectFiles.add(createNewFile(projectMetricsFile, GlobalProperties.SW_JUNIT_FILE));
			projectFiles.add(createNewFile(projectMetricsFile, GlobalProperties.FILE_PROPERTIES));
			
			if (!projectFiles.isEmpty()) {
				continue;
			}
			
			// startThreads(projectFiles, propertiesReader,
			// nextBuild.getNumber());
			
		}
		
	}
	
	public void iterateOnBuildsAndStartK1Parser() {
		
		File projectMetricsFile;
		double k1Value;
		double k1LiteValue;

		RunList<?> buildsList = project.getBuilds();
		ListIterator<?> listIterator = buildsList.listIterator();
		ArrayList<File> projectFiles = new ArrayList<>();
		
		while (listIterator.hasNext()) {
			
			AbstractBuild<?, ?> nextBuild = (AbstractBuild<?, ?>) listIterator.next();
			projectMetricsFile = nextBuild.getRootDir();
			File K1LiteFile = createNewFile(projectMetricsFile, "K1lite.txt");
			File K1File = createNewFile(projectMetricsFile, "K1.txt");
			
			if (K1LiteFile.exists()) {
				projectFiles.add(K1LiteFile);
				k1LiteValue = getValueFromFile(K1LiteFile);
				metricsModel.addMetrics(K1Lite, nextBuild.getNumber(), k1LiteValue);
			}
			
			if (K1File.exists()) {
				projectFiles.add(K1File);
				k1Value = getValueFromFile(K1File);
				metricsModel.addMetrics(K1, nextBuild.getNumber(), k1Value);
			}
			
			if (!projectFiles.isEmpty()) {
				continue;
			}
		}
	}
	
	public double getValueFromFile(File fileName) {
		String text = "";
		BufferedReader fileValue = null;
		try {
			fileValue = new BufferedReader(new FileReader(fileName));
			text = fileValue.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try{
				if (fileValue != null){
				}
					fileValue.close();
			}
			catch (IOException e){
				e.printStackTrace();
			}
		}
		return Double.parseDouble(text);
	}
	
	private void startThreads(ArrayList<File> projectFiles, PropertiesReader propertiesReader, int buildNumber, AbstractBuild<?,?> build) {
		
		ListeningExecutorService threadPool = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(THREADS_NUMBER));
		List<Callable<Void>> lista = new ArrayList<Callable<Void>>();
		lista.add(new K1parser(projectFiles, propertiesReader, buildNumber, metricsModel, build));
		
		try {
			List<Future<Void>> invokeAll = threadPool.invokeAll(lista);
			for (Future<Void> future : invokeAll) {
				try {
					future.get();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
			
		}
		
		threadPool.shutdownNow();
		
	}
}
