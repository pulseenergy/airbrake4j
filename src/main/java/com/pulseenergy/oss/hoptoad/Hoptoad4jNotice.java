package com.pulseenergy.oss.hoptoad;

import java.io.IOException;
import java.util.Properties;

public class Hoptoad4jNotice {
	private static String notifierName;
	private static String notifierVersion;
	private static String notifierUrl;

	static {
		final Properties hoptoadProperties = new Properties();
		try {
			hoptoadProperties.load(Hoptoad4jNotice.class.getResourceAsStream("/META-INF/MANIFEST.MF"));
		} catch (final IOException e) {
			throw new IllegalStateException("Unable to load manifest file", e);
		}
		notifierName = String.valueOf(hoptoadProperties.get("hoptoad4j-name"));
		notifierVersion = String.valueOf(hoptoadProperties.get("hoptoad4j-version"));
		notifierUrl = String.valueOf(hoptoadProperties.get("hoptoad4j-url"));
	}
	private final String version = "2.0"; // Required. The version of the API being used. Should be set to "2.0"

	private String apiKey; // Required. The API key for the project that this error belongs to. The API key can be found by viewing the edit project form on the
	                       // Hoptoad site.

	private String errorClass; // Required. The class name or type of error that occurred.

	private String errorMessage; // Optional. A short message describing the error that occurred.

	private String projectRoot; // Optional. The path to the project in which the error occurred, such as RAILS_ROOT or DOCUMENT_ROOT.

	private String environmentName; // Required. The name of the server environment in which the error occurred, such as "staging" or "production."
	private Throwable throwable;
	private ThrowableData throwableData;
	private String nodeName;
	private String componentName;

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(final String apiKey) {
		this.apiKey = apiKey;
	}

	public String getNotifierName() {
		return notifierName;
	}

	public String getNotifierVersion() {
		return notifierVersion;
	}

	public String getNotifierUrl() {
		return notifierUrl;
	}

	public String getErrorClass() {
		return errorClass;
	}

	public void setErrorClass(final String errorClass) {
		this.errorClass = errorClass;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(final String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getProjectRoot() {
		return projectRoot;
	}

	public void setProjectRoot(final String projectRoot) {
		this.projectRoot = projectRoot;
	}

	public String getEnvironmentName() {
		return environmentName;
	}

	public void setEnvironmentName(final String environmentName) {
		this.environmentName = environmentName;
	}

	public String getVersion() {
		return version;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(final String nodeName) {
		this.nodeName = nodeName;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(final String appName) {
		this.componentName = appName;
	}

	public void setThrowableData(final ThrowableData throwableData) {
		this.throwableData = throwableData;
	}

	public ThrowableData getThrowableData() {
		return throwableData;
	}


}
