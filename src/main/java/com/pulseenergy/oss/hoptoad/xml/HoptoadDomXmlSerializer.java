package com.pulseenergy.oss.hoptoad.xml;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.pulseenergy.oss.hoptoad.Hoptoad4jNotice;

public class HoptoadDomXmlSerializer {
	private static final String INDENT = "'      ";

	public String serialize(final Hoptoad4jNotice notification) {
		final Document document = createDocument();
		final Element notice = document.createElement("notice");
		document.appendChild(notice);
		addVersion(notice, notification);
		addApiKey(document, notice, notification);
		addNotifierInfo(document, notice, notification);
		addErrorInfo(document, notice, notification);
		addServerEnvironment(document, notice, notification);
		return generateXml(document);
	}

	private Document createDocument() {
		try {
			return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (final ParserConfigurationException e) {
			throw new IllegalStateException("Could not create a DOM document", e);
		}
	}

	private String generateXml(final Document document) {
		try {
			final TransformerFactory transformerFactory = TransformerFactory.newInstance();
			final Transformer transformer = transformerFactory.newTransformer();
			final StringWriter stringWriter = new StringWriter();
			final StreamResult result = new StreamResult(stringWriter);
			transformer.transform(new DOMSource(document), result);
			return stringWriter.toString();
		} catch (final TransformerConfigurationException e) {
			throw new IllegalStateException("Could not create an XML Transformer", e);
		} catch (final TransformerException e) {
			throw new RuntimeException("Could not convert DOM document to String result", e);
		}
	}

	private void addApiKey(final Document document, final Element notice, final Hoptoad4jNotice notification) {
		final Element apiKey = buildTextElement(document, "api-key", notification.getApiKey());
		notice.appendChild(apiKey);
	}

	private void addVersion(final Element root, final Hoptoad4jNotice notification) {
		root.setAttribute("version", notification.getVersion());
	}

	private void addErrorInfo(final Document document, final Element noticeElement, final Hoptoad4jNotice notification) {
		final Element error = document.createElement("error");
		error.appendChild(buildTextElement(document, "message", notification.getErrorMessage()));
		error.appendChild(buildTextElement(document, "class", notification.getErrorClass()));
		final Element backtrace = document.createElement("backtrace");
		final Throwable throwable = notification.getThrowable();
		if (throwable == null) {
			backtrace.appendChild(buildTextElement(document, "line", "NO STACK TRACE PROVIDED"));
		} else {
			appendBacktraceLines(document, backtrace, throwable);
		}
		error.appendChild(backtrace);
		noticeElement.appendChild(error);
	}

	private void appendBacktraceLines(final Document document, final Element backtrace, final Throwable throwable) {
		final Element titleLine = document.createElement("line");
		titleLine.setAttribute("file", throwable.getMessage());
		titleLine.setAttribute("number", "");
		backtrace.appendChild(titleLine);
		for (final StackTraceElement element : throwable.getStackTrace()) {
			final Element line = document.createElement("line");
			line.setAttribute("file", INDENT + element.getFileName());
			line.setAttribute("number", String.valueOf(element.getLineNumber()));
			line.setAttribute("method", element.getMethodName());
			backtrace.appendChild(line);
		}
		if (throwable.getCause() != null) {
			appendBacktraceLines(document, backtrace, throwable.getCause());
		}
	}

	private void addNotifierInfo(final Document document, final Element notice, final Hoptoad4jNotice notification) {
		final Element notifier = document.createElement("notifier");
		notifier.appendChild(buildTextElement(document, "name", notification.getNotifierName()));
		notifier.appendChild(buildTextElement(document, "version", notification.getNotifierVersion()));
		notifier.appendChild(buildTextElement(document, "url", notification.getNotifierUrl()));
		notice.appendChild(notifier);
	}

	private void addServerEnvironment(final Document document, final Element noticeElement, final Hoptoad4jNotice notification) {
		final Element serverEnvironment = document.createElement("server-environment");
		serverEnvironment.appendChild(buildTextElement(document, "environment-name", notification.getEnvironmentName()));
		noticeElement.appendChild(serverEnvironment);
	}

	private static Element buildTextElement(final Document document, final String elementName, final String text) {
		final Element textElement = document.createElement(elementName);
		textElement.appendChild(document.createTextNode(StringUtils.trimToEmpty(text)));
		return textElement;
	}
}
