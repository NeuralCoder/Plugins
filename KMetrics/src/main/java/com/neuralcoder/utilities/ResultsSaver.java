package com.neuralcoder.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import hudson.EnvVars;
import hudson.FilePath;
import hudson.model.AbstractBuild;
import hudson.model.Computer;
import hudson.model.Run;

public class ResultsSaver {
	private AbstractBuild<?, ?> build;
	private static final Logger LOGGER = Logger.getLogger(ResultsSaver.class.getName());

	public ResultsSaver(AbstractBuild<?, ?> build) {
		this.build = build;
	}

	public EStatus saveFileExecutor(String sourcePath, PrintStream logger) {

		String name = getSourceFileName(sourcePath);
		FilePath source = new FilePath(Computer.currentComputer().getChannel(), sourcePath);

		File destinationPath = new File(build.getRootDir(), GlobalProperties.K_FOLDER);
		destinationPath.mkdirs();
		destinationPath.setWritable(true);

		FilePath destination = createNewDestination(destinationPath, name);
		try {

			EStatus e = (saveFile(source, destination, logger) == EStatus.SUCCESS) ? EStatus.SUCCESS : EStatus.FAILURE;
			return e;

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		return EStatus.FAILURE;
	}

	public void createFile(File destinationPath, String fileName, String value) {
		destinationPath.mkdirs();
		destinationPath.setWritable(true);

		File file = new File(destinationPath, fileName);
		try {
			if (file.createNewFile()) {
				file.setWritable(true);
				MetricsLogs.consoleLogs("\n" + fileName + " is created!");
				System.out.println(fileName + " is created!");
			} else {
				MetricsLogs.consoleLogs("\n" + fileName + " already exists!");
				System.out.println(fileName + " already exists!");
			}
		}

		catch (IOException e1) {
			e1.printStackTrace();
		}

		Writer writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
			writer.write(value);
		} catch (IOException e) {
			MetricsLogs.consoleLogs("K1 - Can not create K1 file");
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {/* ignore */
			}
		}
	}

	public void deleteInputFiles(PrintStream logger) {
		File destinationPath = new File(build.getRootDir(), GlobalProperties.K_FOLDER);
		File fileSW = new File(destinationPath, GlobalProperties.SW_DASHBOARD_FILE);
		File fileJU = new File(destinationPath, GlobalProperties.SW_JUNIT_FILE);
		File fileFP = new File(destinationPath, GlobalProperties.FILE_PROPERTIES);
		boolean success;

		ArrayList<File> files = new ArrayList<>();
		files.add(fileSW);
		files.add(fileJU);
		files.add(fileFP);

		for (int i = 0; i < files.size(); i++) {
			if (files.get(i).exists()) {
				success = files.get(i).delete();
				if (success) {
					String fileName = getSourceFileName(files.get(i).toString());
					// metricsLogs.consoleLogs("File " + fileName + " has been successfully deleted
					// from master machine.");
					LOGGER.log(Level.INFO, "The file " + fileName + " has been successfully deleted");
					System.out.println("The file " + fileName + " has been successfully deleted");
					// logger.format("The file has"+files.get(i)+"been successfully deleted from
					// master machine, build"+
					// build.getNumber());
				}
			}
		}
	}

	// split the path by "/". return the last element of the path, which is the
	// SourceFileName
	public String getSourceFileName(String sourcePath) {
		String[] sourcePathSplit = sourcePath.split("\\\\");
		int NrOfwords = sourcePathSplit.length;
		String name = sourcePathSplit[NrOfwords - 1];

		return name;
	}

	private FilePath createNewDestination(File destinationPath, String fileName) {
		File pathFile = new File(destinationPath, fileName);
		FilePath destination = new FilePath(pathFile.getAbsoluteFile());

		return destination;
	}

	private EStatus saveFile(FilePath sourceFile, FilePath destFile, PrintStream logger)
			throws IOException, InterruptedException {
		logger.println("\nStart copying source file: " + sourceFile.getBaseName() + " to " + getJenkinsMasterName()
				+ " Master PC...");
		if (!sourceFile.exists()) {
			logger.format("\nFile %s doesn't exist...", sourceFile.getBaseName());
			return EStatus.FAILURE;
		}

		sourceFile.copyTo(destFile);
		logger.format("Done saving %s!\n", sourceFile.getBaseName());

		return EStatus.SUCCESS;

	}

	private String getJenkinsMasterName() {

		Run<?, ?> lastBuid = Computer.currentComputer().getBuilds().getLastBuild();
		try {
			EnvVars envVars = lastBuid.getEnvironment(null);
			Map<String, String> map = envVars.descendingMap();

			return map.get("COMPUTERNAME");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

}
