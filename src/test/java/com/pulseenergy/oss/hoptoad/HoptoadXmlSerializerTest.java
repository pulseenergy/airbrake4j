package com.pulseenergy.oss.hoptoad;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.xml.sax.SAXException;

public class HoptoadXmlSerializerTest {
	private static final String HOPTOAD_NOTICE_SCHEMA = "/hoptoad-notice_2_0.xsd";
	private static final String EXPECTED_MESSAGE = "This is the message";
	private static final String EXPECTED_API_KEY = "lkjhfslkjhfljhfljkh";
	private final HoptoadXmlSerializer serializer = new HoptoadXmlSerializer();

	@Test
	public void serialize() throws Exception {
		final Hoptoad4jNotice notification = new Hoptoad4jNotice();
		notification.populateThrowable(new RuntimeException(EXPECTED_MESSAGE));
		notification.setApiKey(EXPECTED_API_KEY);
		final String xml = serializer.serialize(notification);
		System.out.println(xml);
		assertThat(StringUtils.isEmpty(xml), is(false));
		validateXml(xml);
	}

	@Test
	public void serializeNested() throws Exception {
		final Hoptoad4jNotice notification = new Hoptoad4jNotice();
		notification.populateThrowable(new RuntimeException(EXPECTED_MESSAGE, new IllegalArgumentException("This is the nested exception")));
		notification.setApiKey(EXPECTED_API_KEY);
		final String xml = serializer.serialize(notification);
		assertThat(StringUtils.isEmpty(xml), is(false));
		validateXml(xml);
	}

	private void validateXml(final String xml) throws SAXException, IOException {
		final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		final URL resourceURL = getClass().getResource(HOPTOAD_NOTICE_SCHEMA);
		assertThat(resourceURL, notNullValue());
		final Schema schema = schemaFactory.newSchema(resourceURL);
		final Validator validator = schema.newValidator();
		validator.validate(new StreamSource(new StringReader(xml)));
	}

}
