/**
 * Created on 7 sept. 07
 * @author Eric Cestari <eric@ohmforce.com>
 * 
 * Extends org.jivesoftware.smack.XMPPConnection, only to override the disconnect() method, 
 * which actually does not disconnect, but returns the connection in the pool
 */
package com.ohmforce.xmpp;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;

public class XMPPPooledConnection extends XMPPConnection {
	XMPPConnectionPool pool;
	XMPPPooledConnection(ConnectionConfiguration arg0) {
		super(arg0);
	}
	
	public void disconnect(){
		try {
			pool.returnObject(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close()
	{
		disconnect();
	}
	public void disconnect(Presence p){
		super.sendPacket(p);
		try {
			pool.returnObject(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	XMPPConnectionPool getPool() {
		return pool;
	}

	void setPool(XMPPConnectionPool pool) {
		this.pool = pool;
	}
	
	void reallyDisconnect()
	{
		super.disconnect();
	}
	
}
