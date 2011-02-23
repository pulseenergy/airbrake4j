package com.pulseenergy.oss.hoptoad;

import nu.xom.Attribute;
import nu.xom.Element;

public class HoptoadXmlSerializer {
	public String serialize(final HoptoadNotification notification) {
		final Element root = new Element("notification");
		root.addAttribute(new Attribute("version", notification.getVersion()));
		return root.toXML();
	}
}
