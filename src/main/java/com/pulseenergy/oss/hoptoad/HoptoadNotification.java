package com.pulseenergy.oss.hoptoad;

import java.util.List;

public class HoptoadNotification {
	private final String version = "2.0"; // Required. The version of the API being used. Should be set to "2.0"

	private String apiKey; // Required. The API key for the project that this error belongs to. The API key can be found by viewing the edit project form on the
	                       // Hoptoad site.

	private String notifierName; // Required. The name of the notifier client submitting the request, such as "hoptoad4j" or "rack-hoptoad."

	private String notifierVersion; // Required. The version number of the notifier client submitting the request.

	private String notifierUrl; // Required. A URL at which more information can be obtained concerning the notifier client.

	private String errorClass; // Required. The class name or type of error that occurred.

	private String errorMessage; // Optional. A short message describing the error that occurred.

	private List<String> errorBacktraceLines; // Required. This element can occur more than once. Each line element describes one code location or frame in the
	                                          // backtrace when the error occurred, and requires @file and @number attributes. If the location includes a method
	                                          // or function, the @method attribute should be used.
	private String projectRoot; // Optional. The path to the project in which the error occurred, such as RAILS_ROOT or DOCUMENT_ROOT.

	private String environmentName; // Required. The name of the server environment in which the error occurred, such as "staging" or "production."

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(final String apiKey) {
		this.apiKey = apiKey;
	}

	public String getNotifierName() {
		return notifierName;
	}

	public void setNotifierName(final String notifierName) {
		this.notifierName = notifierName;
	}

	public String getNotifierVersion() {
		return notifierVersion;
	}

	public void setNotifierVersion(final String notifierVersion) {
		this.notifierVersion = notifierVersion;
	}

	public String getNotifierUrl() {
		return notifierUrl;
	}

	public void setNotifierUrl(final String notifierUrl) {
		this.notifierUrl = notifierUrl;
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

	public List<String> getErrorBacktraceLines() {
		return errorBacktraceLines;
	}

	public void setErrorBacktraceLines(final List<String> errorBacktraceLines) {
		this.errorBacktraceLines = errorBacktraceLines;
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

	/*
	 * private String request ; // Optional. If this error occurred during an HTTP request, the children of this element can be used to describe the request that
	 * caused the error.
	 * 
	 * private String request/url Required only if there is a request element. The URL at which the error occurred.
	 * 
	 * private String request/component Required only if there is a request element. The component in which the error occurred. In model-view-controller
	 * frameworks like Rails, this should be set to the controller. Otherwise, this can be set to a route or other request category.
	 * 
	 * private String request/action ; // Optional. The action in which the error occurred. If each request is routed to a controller action, this should be set
	 * here. Otherwise, this can be set to a method or other request subcategory.
	 * 
	 * private String request/params/var ; // Optional. A list of var elements describing request parameters from the query string, POST body, routing, and other
	 * inputs. See the section on var elements below.
	 * 
	 * private String request/session/var ; // Optional. A list of var elements describing session variables from the request. See the section on var elements
	 * below.
	 * 
	 * private String request/cgi-data/var ; // Optional. A list of var elements describing CGI variables from the request, such as SERVER_NAME and REQUEST_URI.
	 * See the section on var elements below.
	 */

}
