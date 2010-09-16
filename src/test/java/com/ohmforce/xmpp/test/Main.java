/*
 * Created on 7 sept. 07
 *
 */
package com.ohmforce.xmpp.test;

import java.util.Properties;

import org.jivesoftware.smack.packet.Message;


import com.ohmforce.xmpp.XMPPConnectionPool;
import com.ohmforce.xmpp.XMPPPooledConnection;

public class Main {

	public static void main(String arg[])
	{
		Properties p = new Properties();
		p.put("username", "webserver");
		p.put("password", "encore");
		p.put("host", "localhost");
		p.put("domain", "localhost");
		
		try {
			XMPPConnectionPool pool = XMPPConnectionPool.getInstance(p);
			sendMessage("cstar@localhost", "Salut", pool);
			sendMessage("cstar@localhost", "Salut2", pool);
			sendMessage("cstar@localhost", "Salut3", pool);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static void sendMessage(String to, String message,  XMPPConnectionPool pool) throws Exception{
		XMPPPooledConnection con = pool.getConnection();
		Message m = new Message(to);
		m.setBody(message);
		con.sendPacket(m);
		con.disconnect();
		//Thread.sleep(1000);
	}
}
