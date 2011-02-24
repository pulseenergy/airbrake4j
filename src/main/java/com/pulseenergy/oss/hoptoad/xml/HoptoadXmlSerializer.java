package com.pulseenergy.oss.hoptoad.xml;

import com.pulseenergy.oss.hoptoad.Hoptoad4jNotice;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;

public class HoptoadXmlSerializer {
	private static final String INDENT = "'      ";

	public String serialize(final Hoptoad4jNotice notification) {
		final Element root = new Element("notice");
		addVersion(root, notification);
		addApiKey(root, notification);
		addNotifierInfo(root, notification);
		addErrorInfo(root, notification);
		addServerEnvironment(root, notification);
		return new Document(root).toXML();
	}

	private void addApiKey(final Element root, final Hoptoad4jNotice notification) {
		final Element apiKey = buildStringElement("api-key", notification.getApiKey());
		root.appendChild(apiKey);
	}

	private void addVersion(final Element root, final Hoptoad4jNotice notification) {
		root.addAttribute(new Attribute("version", notification.getVersion()));
	}

	private void addErrorInfo(final Element root, final Hoptoad4jNotice notification) {
		final Element error = new Element("error");
		error.appendChild(buildStringElement("message", notification.getErrorMessage()));
		error.appendChild(buildStringElement("class", notification.getErrorClass()));
		final Element backtrace = new Element("backtrace");
		final Throwable throwable = notification.getThrowable();
		if (throwable == null) {
			backtrace.appendChild(buildStringElement("line", "NO STACK TRACE PROVIDED"));
		} else {
			appendBacktraceLines(backtrace, throwable);
		}
		error.appendChild(backtrace);
		root.appendChild(error);
	}

	private void appendBacktraceLines(final Element backtrace, final Throwable throwable) {
		final Element titleLine = new Element("line");
		titleLine.addAttribute(new Attribute("file", throwable.getMessage()));
		titleLine.addAttribute(new Attribute("number", ""));
		backtrace.appendChild(titleLine);
		for (final StackTraceElement element : throwable.getStackTrace()) {
			final Element line = new Element("line");
			line.addAttribute(new Attribute("file", INDENT + element.getFileName()));
			line.addAttribute(new Attribute("number", String.valueOf(element.getLineNumber())));
			line.addAttribute(new Attribute("method", element.getMethodName()));
			backtrace.appendChild(line);
		}
		if (throwable.getCause() != null) {
			appendBacktraceLines(backtrace, throwable.getCause());
		}
	}

	private void addNotifierInfo(final Element root, final Hoptoad4jNotice notification) {
		final Element notifier = new Element("notifier");
		notifier.appendChild(buildStringElement("name", notification.getNotifierName()));
		notifier.appendChild(buildStringElement("version", notification.getNotifierVersion()));
		notifier.appendChild(buildStringElement("url", notification.getNotifierUrl()));
		root.appendChild(notifier);
	}

	private void addServerEnvironment(final Element root, final Hoptoad4jNotice notification) {
		final Element serverEnvironment = new Element("server-environment");
		serverEnvironment.appendChild(buildStringElement("environment-name", notification.getEnvironmentName()));
		root.appendChild(serverEnvironment);
	}

	private static Element buildStringElement(final String elementName, final String text) {
		final Element errorMessage = new Element(elementName);
		errorMessage.appendChild(text);
		return errorMessage;
	}
}
