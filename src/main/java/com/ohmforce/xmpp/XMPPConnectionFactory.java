/**
 * Created on 7 sept. 07
 * @author Eric Cestari <eric@ohmforce.com>
 * XMPP Connection Factory.
 * Creates the connections to be pooled.
 */
package com.ohmforce.xmpp;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.pool.PoolableObjectFactory;
import org.jivesoftware.smack.ConnectionConfiguration;

class XMPPConnectionFactory implements PoolableObjectFactory{

	static int DEFAULT_PORT = 5222;
	ConnectionConfiguration config;
	private Logger log = Logger.getLogger("XMPPPooledConnectionFactory");
	private String username;
	private String password;
	/**
	 * Takes Properties which should at least include the following keys :
	 * - username, password, service.
	 * Optionnally, host and port can be specified.
	 * Default values for port is 5222 and host is set to service
	 */
	public XMPPConnectionFactory(Properties props) throws IllegalAccessException, InvocationTargetException
	{
		super();
		config = new ConnectionConfiguration(props.getProperty("service"));
		this.password = props.getProperty("password");
		this.username = props.getProperty("username");
		BeanUtils.populate(config, props);
		log.info(this.toString());
	}
	
	public String toString()
	{
		return "Connected as "+username+"@"+config.getServiceName()+" on "+config.getHost()+":"+config.getPort(); 
	}


	public void activateObject(Object o) throws Exception {
		XMPPPooledConnection con = (XMPPPooledConnection)o;
		log.finest("Connection "+con+" used");
		
	}

	public void destroyObject(Object o) throws Exception {
		XMPPPooledConnection con = (XMPPPooledConnection)o;
		con.reallyDisconnect();
		log.finest("Connection "+con+" destroyed");
	}

	public Object makeObject() throws Exception { 
		XMPPPooledConnection con =  new XMPPPooledConnection(config);
		con.connect();
		con.login(username, password, con.toString().split("@")[1]);
		log.finest("Connection "+con+" created");
		return con;
	}

	public void passivateObject(Object o) throws Exception {
		XMPPPooledConnection con = (XMPPPooledConnection)o;
		log.finest("Connection "+con+" returned");
		
	}

	public boolean validateObject(Object o) {
		return true;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	


}
