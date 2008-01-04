package net.sf.infrared.collector.impl.persistence;

import java.sql.SQLException;
import java.util.Hashtable;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import net.sf.infrared.base.util.LoggingFactory;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.DelegatingDataSource;

public class CollectorDataSource extends DelegatingDataSource {

	private String jndiName;
	
	private DataSource defaultDataSource;
	
	private static final Logger log = LoggingFactory.getLogger(CollectorDataSource.class);	

	public DataSource getDefaultDataSource() {
		return defaultDataSource;
	}

	public void setDefaultDataSource(DataSource defaultDataSource) {
		this.defaultDataSource = defaultDataSource;
	}

	public String getJndiName() {
		return jndiName;
	}

	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}
	
	public void afterPropertiesSet() {		
		try {
			InitialContext ctx = new InitialContext(new Hashtable() );
			Object obj = ctx.lookup(jndiName);
			
			setTargetDataSource((DataSource) obj);
			log.debug("Datasource configured for the iRED jndi name. Using the user specified datasource.");
		} catch (NamingException e) {									
			log.debug("The JNDI name " + jndiName + 
					" is not bound to a valid database. Starting the default hypersonic database.");
			setTargetDataSource(defaultDataSource);			
		}		
		super.afterPropertiesSet();
	}

	public boolean isWrapperFor(Class clazz) throws SQLException {
		return false;
	}

	public Object unwrap(Class clazz) throws SQLException {
		return null;
	}	
}
