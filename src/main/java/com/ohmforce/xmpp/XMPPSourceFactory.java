/**
 * Created on 7 sept. 07
 * @author Eric Cestari <eric@ohmforce.com>
 * This implementation of ObjectFactory will return an XMPPConnectionPool.
 * 
 * It must be configured in tomcat's $TOMCAT_HOME/conf/server.xml in a Context or the GlobalNaming section of the file
 * Example configuration : 
 * <Resource auth="Container" maxActive="10" maxIdle="5" maxWait="10000" 
 * factory="com.ohmforce.xmpp.XMPPSourceFactory" type="com.ohmforce.xmpp.XMPPConnectionPool"  name="xmpp/pool"
 * username="webserver" password="pass" service="localhost" />
 * 
 * The Jar must be copied in $TOMCAT_HOME/common/lib with dependencies.
 * 
 */
package com.ohmforce.xmpp;

import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.naming.spi.ObjectFactory;

public class XMPPSourceFactory extends Reference implements ObjectFactory {

	private static final long serialVersionUID = -797907503267399463L;
	public XMPPSourceFactory(){
		super ("com.ohmforce.xmppXMPPConnectionPool", XMPPSourceFactory.class.getName(), null); 
	}
	
	public Object getObjectInstance(final Object refObj, final Name name, final Context nameCtx,
			final Hashtable environment) throws Exception {
		final Reference ref = (Reference) refObj;

		final Properties props = new Properties();
		final Enumeration attrs = ref.getAll();
        while (attrs.hasMoreElements()) {
                    final RefAddr attr = (RefAddr) attrs.nextElement();
                    if ("factory".equals(attr.getType())) {
                        continue;
                    }

                    props.put(attr.getType(), (String) attr.getContent());
                }
	           // Create and return the new Session object
	           XMPPPooledConnection connection;
			try {
				connection = XMPPConnectionPool.getInstance(props).getConnection();
				return (connection);
			} catch (final IllegalAccessException e) {
				e.printStackTrace();
				return null;
			} catch (final InvocationTargetException e) {
				e.printStackTrace();
				return null;
			}
	            
	}
	public void setProperties (final Properties properties)
    {
        final Iterator entries = properties.entrySet().iterator();
        while (entries.hasNext())
        {
            final Map.Entry e = (Map.Entry)entries.next();
            final StringRefAddr sref = (StringRefAddr)get((String)e.getKey());
            if (sref != null)
                throw new RuntimeException ("property "+e.getKey()+" already set on reference, can't be changed");
            add(new StringRefAddr((String)e.getKey(), (String)e.getValue()));
        }
    }


}
