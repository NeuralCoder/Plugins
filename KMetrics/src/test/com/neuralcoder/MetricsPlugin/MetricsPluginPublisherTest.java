package com.neuralcoder.MetricsPlugin;

import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.FreeStyleProject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.TestBuilder;

public class MetricsPluginPublisherTest {
	private static final String TEST_XML_NOT_EXIST = "D:\test.xml";
	private static final String TEST_XML_EXIST = "D:\\CI\\MetricsPlugin\\src\\main\\resources\\META-INF\\projectMetrics.xml";
	@Rule
	public JenkinsRule jenkinsRule = new JenkinsRule();
	private FreeStyleProject project;

	@Before
	public void setUp() throws Exception {
		project = jenkinsRule.createFreeStyleProject();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPerformAbstractBuildLauncherBuildListenerWhenFileDoesNotExists() {

		final boolean[] perform = { false };

		project.getBuildersList().add(new TestBuilder() {
			@Override
			public boolean perform(AbstractBuild<?, ?> build,
					Launcher launcher, BuildListener listener)
					throws InterruptedException, IOException {
				MetricsPluginPublisher metricsPublisher = new MetricsPluginPublisher(
						TEST_XML_NOT_EXIST);
				perform[0] = metricsPublisher
						.perform(build, launcher, listener);

				return perform[0];
			}
		});
		try {
			project.scheduleBuild2(0).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		Assert.assertFalse(perform[0]);
	}

	@Test
	public void testPerformAbstractBuildLauncherBuildListenerWhenFileDoesExists() {

		final boolean[] perform = { false };

		project.getBuildersList().add(new TestBuilder() {
			@Override
			public boolean perform(AbstractBuild<?, ?> build,
					Launcher launcher, BuildListener listener)
					throws InterruptedException, IOException {
				MetricsPluginPublisher metricsPublisher = new MetricsPluginPublisher(
						TEST_XML_EXIST);
				perform[0] = metricsPublisher
						.perform(build, launcher, listener);

				return perform[0];
			}
		});
		try {
			project.scheduleBuild2(0).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		Assert.assertFalse(perform[0]);
	}

}
