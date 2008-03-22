package org.droiddraw.android;

interface MessageListener {
	  void receivedRequest(in String msg);
	  void newLayout(in String layout);
}
