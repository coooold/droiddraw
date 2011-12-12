package org.droiddraw.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class NetworkService extends Service implements Runnable {
	boolean running;
	int id;
	Thread listener;
	Vector<MessageListener> listeners;
	boolean accept;
	NetworkInterfaceImpl impl;
	
	public void onCreate() {
		this.listener = new Thread(this);
		this.listener.start();
		this.listeners = new Vector<MessageListener>();
		this.impl = new NetworkInterfaceImpl(this);
		super.onCreate();
	}
	
	public void onDestroy() {
		running = false;
		try {
			listener.join();
		}
		catch (InterruptedException ex) {}
	}

	public void run() {
		running = true;
		try {
			ServerSocket ss = new ServerSocket(7100);
			while (running) {
				try {
					Socket s = ss.accept();
					OutputStream os = s.getOutputStream();
					synchronized (this) {
						if (listeners != null) {
							for (MessageListener l : listeners) {
								try {
									l.receivedRequest("Connect");
									throw new IllegalStateException();
								}
								catch (Exception ex) {
									ex.printStackTrace();
								}
								//System.out.println(l);
							}
							try {
								this.wait(2000);
							}
							catch (InterruptedException ex) {}
						}
					}
					if (accept) {
						os.write("Accepted.".getBytes());
						os.write("\r\n".getBytes());
						os.flush();
						InputStream is = s.getInputStream();
						BufferedReader in = new BufferedReader(new InputStreamReader(is));
						String layout = "";
						for (String l=in.readLine();l!=null;l=in.readLine()) {
							layout += l;
							layout += " ";
						}
						for (MessageListener l : listeners) {
							try {
								l.newLayout(layout);
							}
							catch (RemoteException ex) {
								ex.printStackTrace();
							}
						}
					}
					else {
						os.write(("Denied."+(listeners != null?listeners.size():-1)).getBytes());
						os.write("\r\n".getBytes());
						os.flush();
					}
					s.close();
				}
				catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public void acknowledgeRequest(boolean accept) {
		this.accept = accept;
		synchronized (this) {
			this.notify();
		}
	}
	
	public void addMessageListener(MessageListener l) {
		listeners.add(l);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return impl;
	}
}
