
package com.neuralcoder.metrics;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletException;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import com.neuralcoder.utilities.EStatus;
import com.neuralcoder.utilities.GlobalProperties;
import com.neuralcoder.utilities.MetricsLogs;
import com.neuralcoder.utilities.MetricsUtilities;
import com.neuralcoder.utilities.ResultsSaver;

import hudson.EnvVars;
import hudson.Extension;
import hudson.Launcher;
import hudson.PluginWrapper;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.Computer;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.util.FormValidation;
import jenkins.model.Jenkins;
import net.sf.json.JSONObject;

// TODO: Auto-generated Javadoc
/**
 * The Class MetricsPluginPublisher.
 */
public class MetricsPluginPublisher extends Recorder {

	/** The SW path. */
	public final String SWPath;

	/** The log. */
	public TaskListener log;

	/** The JU path. */
	public final String JUPath;

	/** The FP path. */
	public final String FPPath;

	/** The logger. */
	public static PrintStream logger;

	/** The metrics logs. */
	public MetricsLogs metricsLogs;

	/**
	 * Instantiates a new metrics plugin publisher.
	 *
	 * @param SWPath
	 *            the SW Software dashboard path
	 * @param JUPath
	 *            the JU junit_log path
	 * @param FPPath
	 *            the FP file_properties path
	 */
	@DataBoundConstructor
	public MetricsPluginPublisher(String SWPath, String JUPath, String FPPath) {

		this.SWPath = SWPath;
		this.JUPath = JUPath;
		this.FPPath = FPPath;

	}

	/**
	 * Gets the environment of the job configuration.
	 *
	 * @param build
	 *            the build of the job
	 * @return the map that contains environmental variables to be used for
	 *         launching processes for this build.
	 */
	public String getEnv(AbstractBuild<?, ?> build, Object key, BuildListener listener, String fileType) {

		String envVar = null;

		logger = listener.getLogger();
		try {
			envVar = key.toString().substring(1, key.toString().indexOf("%", 1));
		}

		catch (StringIndexOutOfBoundsException e) {
			logger.println("\nThe introduced file path of " + fileType + " is not correct!");
			return null;
		}

		TaskListener log = null;

		EnvVars jobEnv = null;

		try {
			jobEnv = build.getEnvironment(log);
		}

		catch (IOException | InterruptedException e) {
			e.getStackTrace();
			logger.println("The job configuration environment could not be retrieved!");
			return null;
		}

		return jobEnv.get(envVar);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hudson.tasks.BuildStepCompatibilityLayer#getProjectActions(hudson.model.
	 * AbstractProject)
	 */
	@Override
	public Collection<? extends Action> getProjectActions(AbstractProject<?, ?> project) {
		ArrayList<Action> metrics = new ArrayList<>();

		File fromFile = new File(project.getRootDir(), "resultsK1lite.json");

		if (fromFile.exists()) {

			MetricsModel loadMetricsFromJSONData = MetricsUtilities.loadMetricsFromJSONData(fromFile);
			if (null != loadMetricsFromJSONData.getMetricsSet(EMetricsModelType.K1)) {
				metrics.add(new MetricsAction(project, loadMetricsFromJSONData));
			} else {
				metrics.add(new MetricsAction(project, null));
			}
		} else {
			metrics.add(new MetricsAction(project, null));
		}
		return metrics;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hudson.tasks.BuildStepCompatibilityLayer#perform(hudson.model.
	 * AbstractBuild, hudson.Launcher, hudson.model.BuildListener)
	 */
	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) {

		logger = listener.getLogger();
		PluginWrapper whichPlugin = Jenkins.getInstance().getPluginManager().whichPlugin(MetricsPluginPublisher.class);
		logger.println("Build started with " + whichPlugin.getDisplayName() + "\t version " + whichPlugin.getVersion());
		logger.println(Computer.currentComputer().getChannel() + "");

		ResultsSaver saver = new ResultsSaver(build);
		GlobalProperties.SW_DASHBOARD_FILE = saver.getSourceFileName(SWPath);
		GlobalProperties.SW_JUNIT_FILE = saver.getSourceFileName(JUPath);
		GlobalProperties.FILE_PROPERTIES = saver.getSourceFileName(FPPath);

		if (GlobalProperties.SW_DASHBOARD_FILE.length() > 0) {

			File configSWDashboard = new File(SWPath);

			if (configSWDashboard.isAbsolute()) {

				GlobalProperties.SWPath = configSWDashboard.getPath();
				if (saver.saveFileExecutor(GlobalProperties.SWPath, logger) == EStatus.FAILURE) {
					return false;
				}
			}

			else {
				String env = getEnv(build, SWPath, listener, "SW_DASHBOARD");

				if (env != null) {
					env += configSWDashboard.getPath().substring(configSWDashboard.getPath().indexOf("%", 1) + 1);
				} else {
					logger.println("\nThe introduced file path of SW_DASHBOARD is not correct!");
					return false;
				}

				GlobalProperties.SWPath = env;
				if (saver.saveFileExecutor(GlobalProperties.SWPath, logger) == EStatus.FAILURE) {
					return false;
				}
			}
		}

		else {
			logger.println("\nK1 - SW_DASHBOARD.XML file is not provided! Compute only K1 lite!");
		}

		if (GlobalProperties.SW_JUNIT_FILE.length() > 0) {

			File configJunitLog = new File(JUPath);

			if (configJunitLog.isAbsolute()) {
				GlobalProperties.JUPath = configJunitLog.getPath();
				if (saver.saveFileExecutor(configJunitLog.getPath(), logger) == EStatus.FAILURE) {
					return false;
				}
			}

			else {
				String env = getEnv(build, JUPath, listener, "junit_log");
				if (env != null) {
					env += configJunitLog.getPath().substring(configJunitLog.getPath().indexOf("%", 1) + 1);
				} else {
					logger.println("\nThe introduced file path of junit_log is not correct!");
					return false;
				}

				GlobalProperties.JUPath = env;
				if (saver.saveFileExecutor(GlobalProperties.JUPath, logger) == EStatus.FAILURE) {
					return false;
				}
			}
		}

		else {
			logger.println("\nK1 - junit_log.xml file is not provided! Build will be set on FAILURE!");
			return false;
		}

		if (GlobalProperties.FILE_PROPERTIES.length() > 0) {

			File cnfFileProperties = new File(FPPath);

			if (cnfFileProperties.isAbsolute()) {
				GlobalProperties.FPPath = cnfFileProperties.getPath();
				if (saver.saveFileExecutor(GlobalProperties.FPPath, logger) == EStatus.FAILURE) {
					return false;
				}
			}

			else {
				String env = getEnv(build, FPPath, listener, "files_properties");
				if (env != null) {
					env += cnfFileProperties.getPath().substring(cnfFileProperties.getPath().indexOf("%", 1) + 1);
				} else {
					logger.println("\nThe introduced file path of files_properties is not correct!");
					return false;
				}

				GlobalProperties.FPPath = env;
				if (saver.saveFileExecutor(GlobalProperties.FPPath, logger) == EStatus.FAILURE) {
					return false;
				}
			}
		}

		else {
			logger.println("\nK1 - file_properties.xml file is not provided! Build will be set on FAILURE!");
			return false;
		}

		MetricsAction action = build.getProject().getAction(MetricsAction.class);

		MetricsLogs.logger = logger;
		action.load(build);

		return true;
	}

	/**
	 * Overrides {@link getDescriptor } (non-Javadoc)
	 * 
	 * @see hudson.tasks.Recorder#getDescriptor()
	 */
	@Override
	public DescriptorImpl getDescriptor() {
		return (DescriptorImpl) super.getDescriptor();
	}

	/**
	 * The Class DescriptorImpl.
	 */
	@Extension
	public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {

		/**
		 * Instantiates a new descriptor impl.
		 */
		public DescriptorImpl() {
			load();
		}

		/**
		 * Do check SW path.
		 *
		 * @param value
		 *            the value
		 * @return the form validation
		 * @throws IOException
		 *             Signals that an I/O exception has occurred.
		 * @throws ServletException
		 *             the servlet exception
		 */
		public FormValidation doCheckSWPath(@QueryParameter String value) throws IOException, ServletException {
			if (value.length() > 0 && value.length() < 3) {
				return FormValidation.error("Insert a valid Path for SW_DASHBOARD.XML ");
			}
			return FormValidation.ok();
		}

		/**
		 * Do check JU path.
		 *
		 * @param value
		 *            the value
		 * @return the form validation
		 * @throws IOException
		 *             Signals that an I/O exception has occurred.
		 * @throws ServletException
		 *             the servlet exception
		 */
		public FormValidation doCheckJUPath(@QueryParameter String value) throws IOException, ServletException {
			if (value.length() < 3) {
				return FormValidation.error("Path to short.Please insert a valid path");
			}
			if (value.isEmpty()) {
				return FormValidation.error("Path must be set");
			}
			return FormValidation.ok();
		}

		/**
		 * Do check FP path.
		 *
		 * @param value
		 *            the value
		 * @return the form validation
		 * @throws IOException
		 *             Signals that an I/O exception has occurred.
		 * @throws ServletException
		 *             the servlet exception
		 */
		public FormValidation doCheckFPPath(@QueryParameter String value) throws IOException, ServletException {
			if (value.length() < 3) {
				return FormValidation.error("Path to short.Please insert a valid path");
			}
			if (value.isEmpty()) {
				return FormValidation.error("Path must be set");
			}
			return FormValidation.ok();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see hudson.tasks.BuildStepDescriptor#isApplicable(java.lang.Class)
		 */
		@Override
		public boolean isApplicable(Class<? extends AbstractProject> aClass) {
			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see hudson.model.Descriptor#getDisplayName()
		 */
		@Override
		public String getDisplayName() {
			return GlobalProperties.PLUGIN_NAME;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see hudson.model.Descriptor#configure(org.kohsuke.stapler.StaplerRequest,
		 * net.sf.json.JSONObject)
		 */
		@Override
		public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {

			// To persist global configuration information,
			// set that to properties and call save().

			// ^Can also use req.bindJSON(this, formData);
			// (easier when there are many fields; need set* methods for this,
			// like setUseFrench)
			save();
			return super.configure(req, formData);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hudson.tasks.BuildStep#getRequiredMonitorService()
	 */
	@Override
	public BuildStepMonitor getRequiredMonitorService() {

		return BuildStepMonitor.BUILD;

	}

}
