package com.pulseenergy.oss.hoptoad;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class HoptoadXmlSerializerTest {
	private final HoptoadXmlSerializer serializer = new HoptoadXmlSerializer();

	@Test
	public void serialize() {
		final HoptoadNotification notification = new HoptoadNotification();
		final String xml = serializer.serialize(notification);
		assertThat(StringUtils.isEmpty(xml), is(false));
	}
}
