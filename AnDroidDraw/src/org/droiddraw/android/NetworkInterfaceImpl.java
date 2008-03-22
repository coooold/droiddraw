package org.droiddraw.android;

import org.droiddraw.android.NetworkInterface.Stub;

import android.os.DeadObjectException;

public class NetworkInterfaceImpl extends Stub {
	NetworkService ns;
	
	public NetworkInterfaceImpl(NetworkService ns) {
		this.ns = ns;
	}
	
	public void acknowledgeRequest(boolean accept) throws DeadObjectException {
		ns.acknowledgeRequest(accept);
	}

	public void addMessageListener(MessageListener listener)
			throws DeadObjectException {
		ns.addMessageListener(listener);
	}

}
