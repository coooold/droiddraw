/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: org/droiddraw/android/NetworkInterface.aidl
 */
package org.droiddraw.android;
import java.lang.String;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Binder;
import android.os.Parcel;
public interface NetworkInterface extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.droiddraw.android.NetworkInterface
{
private static final java.lang.String DESCRIPTOR = "org.droiddraw.android.NetworkInterface";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an NetworkInterface interface,
 * generating a proxy if needed.
 */
public static org.droiddraw.android.NetworkInterface asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
org.droiddraw.android.NetworkInterface in = (org.droiddraw.android.NetworkInterface)obj.queryLocalInterface(DESCRIPTOR);
if ((in!=null)) {
return in;
}
return new org.droiddraw.android.NetworkInterface.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags)
{
try {
switch (code)
{
case TRANSACTION_addMessageListener:
{
org.droiddraw.android.MessageListener _arg0;
_arg0 = org.droiddraw.android.MessageListener.Stub.asInterface(data.readStrongBinder());
this.addMessageListener(_arg0);
return true;
}
case TRANSACTION_acknowledgeRequest:
{
boolean _arg0;
_arg0 = (0!=data.readInt());
this.acknowledgeRequest(_arg0);
return true;
}
}
}
catch (android.os.DeadObjectException e) {
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.droiddraw.android.NetworkInterface
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public void addMessageListener(org.droiddraw.android.MessageListener listener) throws android.os.DeadObjectException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_addMessageListener, _data, null, 0);
}
finally {
_data.recycle();
}
}
public void acknowledgeRequest(boolean accept) throws android.os.DeadObjectException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInt(((accept)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_acknowledgeRequest, _data, null, 0);
}
finally {
_data.recycle();
}
}
}
static final int TRANSACTION_addMessageListener = (IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_acknowledgeRequest = (IBinder.FIRST_CALL_TRANSACTION + 1);
}
public void addMessageListener(org.droiddraw.android.MessageListener listener) throws android.os.DeadObjectException;
public void acknowledgeRequest(boolean accept) throws android.os.DeadObjectException;
}
