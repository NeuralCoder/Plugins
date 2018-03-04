package com.neuralcoder;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.neuralcoder.MetricsPlugin.MetricsPluginPublisherTest;
import com.neuralcoder.utilities.PropertiesReaderTest;

@RunWith(Suite.class)
@SuiteClasses({ MetricsPluginPublisherTest.class, PropertiesReaderTest.class })
public class AllTests {

}
