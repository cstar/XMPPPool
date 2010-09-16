/**
 * Created on 7 sept. 07
 * @author Eric Cestari <eric@ohmforce.com>
 * Singleton providing XMPP connections.
 */
package com.ohmforce.xmpp;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.pool.impl.GenericObjectPool;

public class XMPPConnectionPool extends GenericObjectPool {

	private static XMPPConnectionPool singleton=null;
	/**
	 * Returns the singleton.
	 */
	public static synchronized XMPPConnectionPool getInstance(Properties props) throws IllegalAccessException, InvocationTargetException{
		if(singleton == null){
			Config c = new Config();
			BeanUtils.populate(c, props);
			XMPPConnectionFactory xcf = new  XMPPConnectionFactory(props);
			singleton = new XMPPConnectionPool(xcf, c);
		}
		return singleton;
	}
	

	
	public Object clone() throws CloneNotSupportedException {
    	throw new CloneNotSupportedException(); 
	}
	/**
	 * Fetches a connection from the pool.
	 * @see XMPPPooledConnection
	 * 
	 */
	public XMPPPooledConnection getConnection() throws Exception{
		XMPPPooledConnection con = (XMPPPooledConnection)borrowObject();
		con.setPool(this);
		return con;
	}
	

	private XMPPConnectionPool(XMPPConnectionFactory arg0, Config arg1) {
		super(arg0, arg1);
		
	}




	
	
	

}
