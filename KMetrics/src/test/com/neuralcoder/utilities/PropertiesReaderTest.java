package com.neuralcoder.utilities;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PropertiesReaderTest {

	private PropertiesReader properties;

	@Before
	public void setUp() throws Exception {
		properties = new PropertiesReader();
	}

	@After
	public void tearDown() throws Exception {
		properties = null;
	}

	@Test
	public void testReadFromPropertiesWithEmptyString() {
		String readFromProperties = properties.readFromProperties("");
		Assert.assertEquals("Expected empty string here.", "",
				readFromProperties);
	}

	@Test
	public void testReadFromPropertiesWithNullString() {
		String readFromProperties = properties.readFromProperties(null);
		Assert.assertEquals("Expected empty string here.", "",
				readFromProperties);
	}

	@Test
	public void testReadFromPropertiesWithExistingKey() {
		String readFromProperties = properties
				.readFromProperties("available_rom");
		Assert.assertEquals(
				"Expected existing value from xml_paths.properties.",
				"/project/Memory_size/ROM/avROM/text()", readFromProperties);
	}

	@Test
	public void testReadFromPropertiesWithNonExistingKey() {
		String readFromProperties = properties.readFromProperties("TEST");
		Assert.assertEquals("Expected empty from xml_paths.properties.", "",
				readFromProperties);
	}

}
