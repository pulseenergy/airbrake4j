package com.pulseenergy.oss.hoptoad;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class HoptoadXmlSerializerTest {
	private static final String EXPECTED_MESSAGE = "This is the message";
	private static final String EXPECTED_API_KEY = "lkjhfslkjhfljhfljkh";
	private final HoptoadXmlSerializer serializer = new HoptoadXmlSerializer();

	@Test
	public void serialize() {
		final Hoptoad4jNotice notification = new Hoptoad4jNotice();
		notification.populateThrowable(new RuntimeException(EXPECTED_MESSAGE));
		notification.setApiKey(EXPECTED_API_KEY);
		final String xml = serializer.serialize(notification);
		System.out.println(xml);
		assertThat(StringUtils.isEmpty(xml), is(false));
	}

	@Test
	public void serializeNested() {
		final Hoptoad4jNotice notification = new Hoptoad4jNotice();
		notification.populateThrowable(new RuntimeException(EXPECTED_MESSAGE, new IllegalArgumentException(EXPECTED_MESSAGE)));
		notification.setApiKey(EXPECTED_API_KEY);
		final String xml = serializer.serialize(notification);
		System.out.println(xml);
		assertThat(StringUtils.isEmpty(xml), is(false));
	}
}
