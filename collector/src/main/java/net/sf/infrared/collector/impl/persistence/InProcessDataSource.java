/* 
 * Copyright 2005 Tavant Technologies and Contributors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 *
 *
 * Original Author:  binil.thomas (Tavant Technologies)
 * Contributor(s):   subin.p;
 *
 */
package net.sf.infrared.collector.impl.persistence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import net.sf.infrared.base.util.LoggingFactory;

import org.apache.log4j.Logger;
import org.hsqldb.jdbc.jdbcDataSource;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class InProcessDataSource implements DataSource, InitializingBean, DisposableBean {
    private static final String DB_SCHEMA_FILE = "net/sf/infrared/collector/impl/persistence/hsqldb-schema.script";
    private static final String DB_PROPS_FILE = "net/sf/infrared/collector/impl/persistence/hsqldb.properties";

    public static final String DEFAULT_DB_PATH = System.getProperty("user.home") 
        + File.separator + ".infrared";
    
    public static final String DB_NAME = "infrared-db";
    
    private static final Logger log = LoggingFactory.getLogger(InProcessDataSource.class);

    private jdbcDataSource ds = null;

    private String dbPath = DEFAULT_DB_PATH;
    
    //private File dest = null;

    public InProcessDataSource() {        
    }    
    
    public int getLoginTimeout() throws SQLException {
        return ds.getLoginTimeout();
    }

    public void setLoginTimeout(int seconds) throws SQLException {
        ds.setLoginTimeout(seconds);
    }

    public PrintWriter getLogWriter() throws SQLException {
        return ds.getLogWriter();
    }

    public void setLogWriter(PrintWriter out) throws SQLException {
        setLogWriter(out);
    }

    public Connection getConnection() throws SQLException {
        log.debug("Getting Connection from InProcessDataSource");
        return ds.getConnection();
    }

    public Connection getConnection(String username, String password) throws SQLException {        
        return ds.getConnection(username, password);
    }

    public void afterPropertiesSet() throws Exception {        
        createSchemaIfNewDb();
        
        ds = new jdbcDataSource();
        ds.setDatabase("jdbc:hsqldb:file:" + getDbPath() + "/" + DB_NAME);
        ds.setUser("sa");
        ds.setPassword("");
        
        if (log.isDebugEnabled()) {
            log.debug("Created in-process hsql database");
        }        
    }

    public void destroy() throws Exception {        
        Connection con = null;
        Statement stmt = null;
        try {
            con = ds.getConnection();
            stmt = con.createStatement();
            stmt.execute("SHUTDOWN");
        } catch (Throwable th) {
            // @TODO log this!
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    // @TODO log this
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    // @TODO log this
                }
            }
        }
    }
    
    boolean createSchemaIfNewDb() {
        File script = new File(getDbPath() + File.separator + DB_NAME + ".script");
        boolean ret = copy(DB_SCHEMA_FILE, script);
        File props = new File(getDbPath() + File.separator + DB_NAME + ".properties");
        ret = copy(DB_PROPS_FILE, props) & ret;
        
        return ret;
    }

    boolean copy(String src, File dest) {
        // @TODO replace this with Commons FileUtils
        if (dest.exists()) {
            if (log.isDebugEnabled()) {
                log.debug(dest.getAbsoluteFile() + " exists");
            }
            return false;
        } else {
            if (log.isDebugEnabled()) {
                log.debug(dest.getAbsoluteFile() + " doesn't exists");
            }
        }
        
        URL srcUrl = null;
        try {
            srcUrl = Thread.currentThread().getContextClassLoader().getResource(src);
        } catch (Throwable th) {
            log.error("Failed to find default in-process DB schema", th);
            return false;
        }
        
        if (srcUrl == null) {
            log.error("Failed to find default in-process DB schema");
            return false;
        }

        InputStream srcStream = null;
        byte[] bytes = null;
        try {
            srcStream = srcUrl.openStream();
            bytes = new byte[srcStream.available()];
            srcStream.read(bytes);
        } catch (IOException e) {
            log.fatal("Failed to read in-process DB schema from " + srcUrl, e);
            return false;
        } finally {
            try {
                if (srcStream != null) {
                    srcStream.close(); 
                }
            } catch (IOException ignored) { 
            }
        }

        FileOutputStream destStream = null;
        try {
            if (! dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            dest.createNewFile();
            destStream = new FileOutputStream(dest);
            destStream.write(bytes);
            if (log.isDebugEnabled()) {
                log.debug("Create new in-process database at " + getDbPath());
            }
            return true;
        } catch (IOException e) {
            log.fatal("Failed to create file " + dest, e);
            return false;
        } finally {
            try {
                if (destStream != null) {
                    destStream.close(); 
                }
            } catch (IOException ignored) { 
            }
        }
    }

    public void setDbPath(String path) {
        dbPath = path;
    }

    public String getDbPath() {
        return dbPath;
    }

	public boolean isWrapperFor(Class arg0) throws SQLException {
		return false;
	}

	public Object unwrap(Class arg0) throws SQLException {
		return null;
	}
}
