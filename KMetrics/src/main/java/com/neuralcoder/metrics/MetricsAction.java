
package com.neuralcoder.metrics;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import com.neuralcoder.utilities.DataSetFormatter;
import com.neuralcoder.utilities.GlobalProperties;
import com.neuralcoder.utilities.MetricsUtilities;
import com.neuralcoder.utilities.ProjectMetricsJobIterator;
import com.neuralcoder.utilities.socketio.SocketIOService;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.Job;
import hudson.model.Run;
import hudson.model.listeners.RunListener;

//import javax.swing.Action;

import net.sf.json.JSONObject;

// TODO: Auto-generated Javadoc
/**
 * The Class K1MetricsAction.
 */
public class MetricsAction implements Action {

	/** The project. */
	private AbstractProject<?, ?> project;

	/** The metrics model. */
	private MetricsModel metricsModel;

	/**
	 * Instantiates a new k 1 metrics action.
	 *
	 * @param project
	 *            the project
	 * @param metricsModel
	 *            the metrics model
	 */
	public MetricsAction(AbstractProject<?, ?> project, MetricsModel metricsModel) {

		this.project = project;

		if (metricsModel == null) {

			this.metricsModel = new MetricsModel();
			this.metricsModel.initializeModel();

			buildJSONFile();

		} else {

			this.metricsModel = metricsModel;

		}

	}

	/**
	 * The listener interface for receiving myRun events. The class that is
	 * interested in processing a myRun event implements this interface, and the
	 * object created with that class is registered with a component using the
	 * component's <code>addMyRunListener<code> method. When the myRun event
	 * occurs, that object's appropriate method is invoked.
	 *
	 * @see MyRunEvent
	 */
	@Extension
	public static final class MyRunListener extends RunListener<Run<?, ?>> {

		/**
		 * On finalized.
		 *
		 * @param r
		 *            the r
		 */
		@Override
		public void onFinalized(Run<?, ?> r) {

			MetricsAction action = r.getParent().getAction(MetricsAction.class);

			if (action == null) {
				return;
			}

			JSONObject json = new JSONObject().element("projectName", r.getParent().getName())
					.element("buildDisplayNumber", new Integer(r.getNumber()))
					.element("buildStatus", r.getResult().toString());
			SocketIOService.send(json.toString());
		}

		/**
		 * On deleted.
		 *
		 * @param r
		 *            the r
		 */
		@Override
		public void onDeleted(Run<?, ?> r) {
			Job<?, ?> job = r.getParent();
			if (!shouldTreatJob(job)) {
				return;
			}

			/*
			 * build number that is to be deleted
			 */
			int number = r.getNumber();
			MetricsAction action = job.getAction(MetricsAction.class);
			MetricsModel metricsModel = action.getMetricsModel();

			metricsModel.deleteBuild(number);
			File file = new File(job.getRootDir(), "resultsK1lite.json");
			MetricsUtilities.saveMetricsToJSONData(file, metricsModel);

		}

		/**
		 * Should treat job.
		 *
		 * @param job
		 *            the job
		 * @return true, if successful
		 */
		private boolean shouldTreatJob(Job<?, ?> job) {
			return (job.getAction(MetricsAction.class) != null);
		}

	}

	/**
	 * Gets the icon file name.
	 *
	 * @return the icon file name
	 */
	@Override
	public String getIconFileName() {
		//
		return GlobalProperties.PLUGIN_ICON_K1;
	}

	/**
	 * Gets the display name.
	 *
	 * @return the display name
	 */
	@Override
	public String getDisplayName() {
		return GlobalProperties.K1_LINK_NAME.toString();
	}

	/**
	 * Gets the url name.
	 *
	 * @return the url name
	 */
	@Override
	public String getUrlName() {
		return GlobalProperties.PLUGIN_URL_K1;
	}

	/**
	 * Builds the JSON file.
	 */
	private void buildJSONFile() {
		MetricsModel metricsModel = new MetricsModel();
		metricsModel.initializeModel();

		ProjectMetricsJobIterator xmlParsers = new ProjectMetricsJobIterator(project, metricsModel);
		xmlParsers.iterateOnBuildsAndStartK1Parser();
		File file = new File(project.getRootDir(), "resultsK1lite.json");
		MetricsUtilities.saveMetricsToJSONData(file, metricsModel);

		this.metricsModel = metricsModel;
	}

	/**
	 * Gets the k 1 metrics.
	 *
	 * @return the k 1 metrics
	 */
	public String getK1Metrics() {
		StringBuffer result = new StringBuffer("[");
		String BuildsK1Set = DataSetFormatter.getFormatedBuildsSet(metricsModel, EMetricsModelType.K1, "BUILD",
				project);
		String k1Total = DataSetFormatter.getFormatedDataSet(metricsModel, EMetricsModelType.K1, "K1 formula", 0);

		result.append(BuildsK1Set);
		result.append(",");
		result.append(k1Total);
		result.append("]");

		return result.toString();
	}

	/**
	 * Gets the k 1 lite metrics.
	 *
	 * @return the k 1 lite metrics
	 */
	public String getK1LiteMetrics() {
		StringBuffer result = new StringBuffer("[");
		String BuildsK1Set = DataSetFormatter.getFormatedBuildsSet(metricsModel, EMetricsModelType.K1Lite, "BUILD",
				project);
		String k1LiteTotal = DataSetFormatter.getFormatedDataSet(metricsModel, EMetricsModelType.K1Lite,
				"K1 lite formula", 0);

		result.append(BuildsK1Set);
		result.append(",");
		result.append(k1LiteTotal);
		result.append("]");

		return result.toString();
	}

	/**
	 * Gets the k 1 log.
	 *
	 * @return the k 1 log
	 */
	public String getK1Log() {
		String result = "";

		File fileName = new File(project.getBuilds().getLastBuild().getRootDir(), "KReport/K1LogFile.txt");
		ArrayList<String> logK1 = MetricsUtilities.getValueFromTxtFile(fileName);
		Iterator<String> logK1Iterator = logK1.iterator();
		while (logK1Iterator.hasNext()) {
			result = result + logK1Iterator.next();
		}
		return result;
	}

	/**
	 * Gets the k 1 lite log.
	 *
	 * @return the k 1 lite log
	 */
	public String getK1LiteLog() {
		String result = "";

		File fileName = new File(project.getBuilds().getLastBuild().getRootDir(), "KReport/K1LiteLogFile.txt");
		ArrayList<String> logK1Lite = MetricsUtilities.getValueFromTxtFile(fileName);
		Iterator<String> logK1LiteIterator = logK1Lite.iterator();
		while (logK1LiteIterator.hasNext()) {
			result = result + logK1LiteIterator.next();
		}
		return result;
	}

	/**
	 * Load.
	 *
	 * @param build
	 *            the build
	 */
	public void load(AbstractBuild<?, ?> build) {
		ProjectMetricsJobIterator projectMetricsJob = new ProjectMetricsJobIterator(build, metricsModel);
		projectMetricsJob.addBuildToModel();

		File file = new File(build.getProject().getRootDir(), "resultsK1lite.json");
		MetricsUtilities.saveMetricsToJSONData(file, metricsModel);
	}

	/**
	 * Gets the metrics model.
	 *
	 * @return the metrics model
	 */
	public MetricsModel getMetricsModel() {
		return metricsModel;
	}

	/**
	 * Gets the project.
	 *
	 * @return the project
	 */
	public AbstractProject<?, ?> getProject() {
		return project;
	}

}
