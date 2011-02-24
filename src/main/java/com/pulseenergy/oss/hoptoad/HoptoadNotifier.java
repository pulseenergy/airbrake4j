package com.pulseenergy.oss.hoptoad;

import java.io.IOException;


public interface HoptoadNotifier {

	void send(final Hoptoad4jNotice notification) throws IOException;

}