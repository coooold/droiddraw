package org.droiddraw.android;

import java.util.Hashtable;
import java.util.Stack;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class AnDroidDraw extends Activity implements ServiceConnection, View.OnClickListener {
	Stack<ViewGroup> layoutStack;
	Hashtable<String, Integer> ids;
	NetworkInterface service;
	CheckBox cb;
	TextView lt;
	Button preview;
	Handler h;

	String layout;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		this.setContentView(R.layout.main);
		this.cb = (CheckBox)this.findViewById(R.id.connect);
		this.lt = (TextView)this.findViewById(R.id.text);
		this.preview = (Button)this.findViewById(R.id.layout);
		this.preview.setOnClickListener(this);

		Intent netService = new Intent(this, NetworkService.class);
		this.startService(netService);
		this.bindService(netService, this, Context.BIND_AUTO_CREATE);

		//this.registerReceiver(new NetworkReceiver(this), new IntentFilter("layout"));
		this.h = new Handler();
	}

	public void updateLayout() {
		lt.setText(layout);
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public void onServiceConnected(ComponentName name, IBinder srv) {
		this.service = NetworkInterface.Stub.asInterface(srv);
		Toast.makeText(this, "Layout Server Started", Toast.LENGTH_SHORT).show();	

		try {
			this.service.addMessageListener(new MessageListener.Stub() {
				public void receivedRequest(String msg)
				throws RemoteException {
					//Toast.makeText(AnDroidDraw.this,
					//		cb.isChecked()?"Accepting Connection":"Rejecting Connection",
					//				Toast.LENGTH_LONG).show();
					service.acknowledgeRequest(cb.isChecked());
				}

				public void newLayout(String layout) throws DeadObjectException {
					setLayout(layout);
					h.post(new Runnable() {
						public void run() {
							updateLayout();
						//	Toast.makeText(AnDroidDraw.this,
						//			"Layout Received",
						//			Toast.LENGTH_SHORT).show();
						}
					});
				}
			});
		}
		catch (RemoteException ex) {
			ex.printStackTrace();
		}
	}

	public void onServiceDisconnected(ComponentName arg0) {
		this.service = null;
	}

	public void onClick(View v) {
		if (v == preview) {
			Intent i = new Intent(this, ParseActivity.class);
			String data;
			if (lt.getText().length() > 0) {
				data = lt.getText().toString();
			}
			else {
				data = layout;
			}
			i.putExtra(ParseActivity.DATA, data);
			startActivity(i);
		}
	}
}