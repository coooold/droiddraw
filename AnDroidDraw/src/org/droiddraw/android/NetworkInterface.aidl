package org.droiddraw.android;

import org.droiddraw.android.MessageListener;

interface NetworkInterface {
	void addMessageListener(in MessageListener listener);
	void acknowledgeRequest(in boolean accept);

}
