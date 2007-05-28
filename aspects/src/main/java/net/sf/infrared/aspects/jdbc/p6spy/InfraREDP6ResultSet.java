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
 * Original Author:  kamal.govindraj (Tavant Technologies)
 * Contributor(s):   -;
 *
 */
package net.sf.infrared.aspects.jdbc.p6spy;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;

import net.sf.infrared.agent.MonitorFactory;
import net.sf.infrared.aspects.api.ApiContext;
import net.sf.infrared.base.model.ExecutionTimer;

import com.p6spy.engine.spy.P6Statement;

/**
 * Wrapper over {@link java.sql.ResultSet}. Intercepts java.sql.ResultSet
 * methods for monitoring.
 * 
 * @author kamal.govindraj
 */
public class InfraREDP6ResultSet implements ResultSet {
    protected ResultSet passthru;

    protected P6Statement statement;

    protected String query;

    protected String preparedQuery;

    protected InfraREDP6Factory factory;

    long noOfRowsFetched = 0;

    long timeTakenForFetch = 0;

    public InfraREDP6ResultSet(InfraREDP6Factory factory, ResultSet resultSet, P6Statement statement,
            String preparedQuery, String query) {
        this.factory = factory;
        this.passthru = resultSet;
        this.statement = statement;
        this.query = query;
        this.preparedQuery = preparedQuery;
    }

    public boolean next() throws SQLException {
        boolean returnValue = false;
        if (factory.isJDBCMonitoringEnabled()) {
            final String methodName = "next";
            ApiContext apiCtx = new ApiContext(ResultSet.class.getName(), methodName, "JDBC");
            ExecutionTimer timer = new ExecutionTimer(apiCtx);
        
            try {
                MonitorFactory.getFacade().recordExecutionBegin(timer);
                returnValue = passthru.next();
            } finally {
                MonitorFactory.getFacade().recordExecutionEnd(timer);
            }
        } else {
            returnValue = passthru.next();
        }
        return returnValue;
    }

    public int getRow() throws SQLException {
        return passthru.getRow();
    }

    public byte[] getBytes(String p0) throws SQLException {
        return passthru.getBytes(p0);
    }

    public byte[] getBytes(int p0) throws SQLException {
        return passthru.getBytes(p0);
    }

    public boolean getBoolean(int p0) throws SQLException {
        return passthru.getBoolean(p0);
    }

    public boolean getBoolean(String p0) throws SQLException {
        return passthru.getBoolean(p0);
    }

    public int getType() throws SQLException {
        return passthru.getType();
    }

    public long getLong(int p0) throws SQLException {
        return passthru.getLong(p0);
    }

    public long getLong(String p0) throws SQLException {
        return passthru.getLong(p0);
    }

    public boolean previous() throws SQLException {
        return passthru.previous();
    }

    public void close() throws SQLException {
        passthru.close();
    }

    public Object getObject(String p0, java.util.Map p1) throws SQLException {
        return passthru.getObject(p0, p1);
    }

    public Object getObject(int p0) throws SQLException {
        return passthru.getObject(p0);
    }

    public Object getObject(String p0) throws SQLException {
        return passthru.getObject(p0);
    }

    public Object getObject(int p0, java.util.Map p1) throws SQLException {
        return passthru.getObject(p0, p1);
    }

    public Ref getRef(String p0) throws SQLException {
        return passthru.getRef(p0);
    }

    public Ref getRef(int p0) throws SQLException {
        return passthru.getRef(p0);
    }

    public Time getTime(int p0, java.util.Calendar p1) throws SQLException {
        return passthru.getTime(p0, p1);
    }

    public Time getTime(String p0, java.util.Calendar p1) throws SQLException {
        return passthru.getTime(p0, p1);
    }

    public Time getTime(String p0) throws SQLException {
        return passthru.getTime(p0);
    }

    public Time getTime(int p0) throws SQLException {
        return passthru.getTime(p0);
    }

    public java.sql.Date getDate(int p0) throws SQLException {
        return passthru.getDate(p0);
    }

    public java.sql.Date getDate(String p0, java.util.Calendar p1) throws SQLException {
        return passthru.getDate(p0);
    }

    public java.sql.Date getDate(String p0) throws SQLException {
        return passthru.getDate(p0);
    }

    public java.sql.Date getDate(int p0, java.util.Calendar p1) throws SQLException {
        return passthru.getDate(p0, p1);
    }

    public boolean wasNull() throws SQLException {
        return passthru.wasNull();
    }

    public String getString(String p0) throws SQLException {
        String result = passthru.getString(p0);
        return result;
    }

    public String getString(int p0) throws SQLException {
        return passthru.getString(p0);
    }

    public byte getByte(String p0) throws SQLException {
        return passthru.getByte(p0);
    }

    public byte getByte(int p0) throws SQLException {
        return passthru.getByte(p0);
    }

    public short getShort(String p0) throws SQLException {
        short result = passthru.getShort(p0);
        return result;
    }

    public short getShort(int p0) throws SQLException {
        return passthru.getShort(p0);
    }

    public int getInt(int p0) throws SQLException {
        return passthru.getInt(p0);
    }

    public int getInt(String p0) throws SQLException {
        int result = passthru.getInt(p0);
        return result;
    }

    public float getFloat(String p0) throws SQLException {
        return passthru.getFloat(p0);
    }

    public float getFloat(int p0) throws SQLException {
        return passthru.getFloat(p0);
    }

    public double getDouble(int p0) throws SQLException {
        return passthru.getFloat(p0);
    }

    public double getDouble(String p0) throws SQLException {
        return passthru.getDouble(p0);
    }

    public BigDecimal getBigDecimal(String p0) throws SQLException {
        return passthru.getBigDecimal(p0);
    }

    public BigDecimal getBigDecimal(int p0) throws SQLException {
        return passthru.getBigDecimal(p0);
    }

    public BigDecimal getBigDecimal(int p0, int p1) throws SQLException {
        return passthru.getBigDecimal(p0, p1);
    }

    public BigDecimal getBigDecimal(String p0, int p1) throws SQLException {
        return passthru.getBigDecimal(p0, p1);
    }

    public Timestamp getTimestamp(String p0) throws SQLException {
        return passthru.getTimestamp(p0);
    }

    public Timestamp getTimestamp(String p0, java.util.Calendar p1) throws SQLException {
        return passthru.getTimestamp(p0, p1);
    }

    public Timestamp getTimestamp(int p0) throws SQLException {
        return passthru.getTimestamp(p0);
    }

    public Timestamp getTimestamp(int p0, java.util.Calendar p1) throws SQLException {
        return passthru.getTimestamp(p0, p1);
    }

    public InputStream getAsciiStream(String p0) throws SQLException {
        return passthru.getAsciiStream(p0);
    }

    public InputStream getAsciiStream(int p0) throws SQLException {
        return passthru.getAsciiStream(p0);
    }

    public InputStream getUnicodeStream(int p0) throws SQLException {
        return passthru.getUnicodeStream(p0);
    }

    public InputStream getUnicodeStream(String p0) throws SQLException {
        return passthru.getUnicodeStream(p0);
    }

    public InputStream getBinaryStream(int p0) throws SQLException {
        return passthru.getBinaryStream(p0);
    }

    public InputStream getBinaryStream(String p0) throws SQLException {
        return passthru.getBinaryStream(p0);
    }

    public SQLWarning getWarnings() throws SQLException {
        return passthru.getWarnings();
    }

    public void clearWarnings() throws SQLException {
        passthru.clearWarnings();
    }

    public String getCursorName() throws SQLException {
        return passthru.getCursorName();
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        return factory.getResultSetMetaData(passthru.getMetaData());
    }

    public int findColumn(String p0) throws SQLException {
        return passthru.findColumn(p0);
    }

    public Reader getCharacterStream(String p0) throws SQLException {
        return passthru.getCharacterStream(p0);
    }

    public Reader getCharacterStream(int p0) throws SQLException {
        return passthru.getCharacterStream(p0);
    }

    public boolean isBeforeFirst() throws SQLException {
        return passthru.isBeforeFirst();
    }

    public boolean isAfterLast() throws SQLException {
        return passthru.isAfterLast();
    }

    public boolean isFirst() throws SQLException {
        return passthru.isFirst();
    }

    public boolean isLast() throws SQLException {
        return passthru.isLast();
    }

    public void beforeFirst() throws SQLException {
        passthru.beforeFirst();
    }

    public void afterLast() throws SQLException {
        passthru.afterLast();
    }

    public boolean first() throws SQLException {
        return passthru.first();
    }

    public boolean last() throws SQLException {
        return passthru.last();
    }

    public boolean absolute(int p0) throws SQLException {
        return passthru.absolute(p0);
    }

    public boolean relative(int p0) throws SQLException {
        return passthru.relative(p0);
    }

    public void setFetchDirection(int p0) throws SQLException {
        passthru.setFetchDirection(p0);
    }

    public int getFetchDirection() throws SQLException {
        return passthru.getFetchDirection();
    }

    public void setFetchSize(int p0) throws SQLException {
        passthru.setFetchSize(p0);
    }

    public int getFetchSize() throws SQLException {
        return passthru.getFetchSize();
    }

    public int getConcurrency() throws SQLException {
        return passthru.getConcurrency();
    }

    public boolean rowUpdated() throws SQLException {
        return passthru.rowUpdated();
    }

    public boolean rowInserted() throws SQLException {
        return passthru.rowInserted();
    }

    public boolean rowDeleted() throws SQLException {
        return passthru.rowDeleted();
    }

    public void updateNull(int p0) throws SQLException {
        passthru.updateNull(p0);
    }

    public void updateNull(String p0) throws SQLException {
        passthru.updateNull(p0);
    }

    public void updateBoolean(int p0, boolean p1) throws SQLException {
        passthru.updateBoolean(p0, p1);
    }

    public void updateBoolean(String p0, boolean p1) throws SQLException {
        passthru.updateBoolean(p0, p1);
    }

    public void updateByte(String p0, byte p1) throws SQLException {
        passthru.updateByte(p0, p1);
    }

    public void updateByte(int p0, byte p1) throws SQLException {
        passthru.updateByte(p0, p1);
    }

    public void updateShort(int p0, short p1) throws SQLException {
        passthru.updateShort(p0, p1);
    }

    public void updateShort(String p0, short p1) throws SQLException {
        passthru.updateShort(p0, p1);
    }

    public void updateInt(int p0, int p1) throws SQLException {
        passthru.updateInt(p0, p1);
    }

    public void updateInt(String p0, int p1) throws SQLException {
        passthru.updateInt(p0, p1);
    }

    public void updateLong(int p0, long p1) throws SQLException {
        passthru.updateLong(p0, p1);
    }

    public void updateLong(String p0, long p1) throws SQLException {
        passthru.updateLong(p0, p1);
    }

    public void updateFloat(String p0, float p1) throws SQLException {
        passthru.updateFloat(p0, p1);
    }

    public void updateFloat(int p0, float p1) throws SQLException {
        passthru.updateFloat(p0, p1);
    }

    public void updateDouble(int p0, double p1) throws SQLException {
        passthru.updateDouble(p0, p1);
    }

    public void updateDouble(String p0, double p1) throws SQLException {
        passthru.updateDouble(p0, p1);
    }

    public void updateBigDecimal(String p0, BigDecimal p1) throws SQLException {
        passthru.updateBigDecimal(p0, p1);
    }

    public void updateBigDecimal(int p0, BigDecimal p1) throws SQLException {
        passthru.updateBigDecimal(p0, p1);
    }

    public void updateString(String p0, String p1) throws SQLException {
        passthru.updateString(p0, p1);
    }

    public void updateString(int p0, String p1) throws SQLException {
        passthru.updateString(p0, p1);
    }

    public void updateBytes(int p0, byte[] p1) throws SQLException {
        passthru.updateBytes(p0, p1);
    }

    public void updateBytes(String p0, byte[] p1) throws SQLException {
        passthru.updateBytes(p0, p1);
    }

    public void updateDate(int p0, java.sql.Date p1) throws SQLException {
        passthru.updateDate(p0, p1);
    }

    public void updateDate(String p0, java.sql.Date p1) throws SQLException {
        passthru.updateDate(p0, p1);
    }

    public void updateTime(String p0, Time p1) throws SQLException {
        passthru.updateTime(p0, p1);
    }

    public void updateTime(int p0, Time p1) throws SQLException {
        passthru.updateTime(p0, p1);
    }

    public void updateTimestamp(int p0, Timestamp p1) throws SQLException {
        passthru.updateTimestamp(p0, p1);
    }

    public void updateTimestamp(String p0, Timestamp p1) throws SQLException {
        passthru.updateTimestamp(p0, p1);
    }

    public void updateAsciiStream(int p0, InputStream p1, int p2) throws SQLException {
        passthru.updateAsciiStream(p0, p1, p2);
    }

    public void updateAsciiStream(String p0, InputStream p1, int p2) throws SQLException {
        passthru.updateAsciiStream(p0, p1, p2);
    }

    public void updateBinaryStream(int p0, InputStream p1, int p2) throws SQLException {
        passthru.updateBinaryStream(p0, p1, p2);
    }

    public void updateBinaryStream(String p0, InputStream p1, int p2) throws SQLException {
        passthru.updateBinaryStream(p0, p1, p2);
    }

    public void updateCharacterStream(int p0, Reader p1, int p2) throws SQLException {
        passthru.updateCharacterStream(p0, p1, p2);
    }

    public void updateCharacterStream(String p0, Reader p1, int p2) throws SQLException {
        passthru.updateCharacterStream(p0, p1, p2);
    }

    public void updateObject(int p0, Object p1) throws SQLException {
        passthru.updateObject(p0, p1);
    }

    public void updateObject(int p0, Object p1, int p2) throws SQLException {
        passthru.updateObject(p0, p1, p2);
    }

    public void updateObject(String p0, Object p1) throws SQLException {
        passthru.updateObject(p0, p1);
    }

    public void updateObject(String p0, Object p1, int p2) throws SQLException {
        passthru.updateObject(p0, p1, p2);
    }

    public void insertRow() throws SQLException {
        passthru.insertRow();
    }

    public void updateRow() throws SQLException {
        passthru.updateRow();
    }

    public void deleteRow() throws SQLException {
        passthru.deleteRow();
    }

    public void refreshRow() throws SQLException {
        passthru.refreshRow();
    }

    public void cancelRowUpdates() throws SQLException {
        passthru.cancelRowUpdates();
    }

    public void moveToInsertRow() throws SQLException {
        passthru.moveToInsertRow();
    }

    public void moveToCurrentRow() throws SQLException {
        passthru.moveToCurrentRow();
    }

    public Statement getStatement() throws SQLException {
        return this.statement;
    }

    public Blob getBlob(int p0) throws SQLException {
        return passthru.getBlob(p0);
    }

    public Blob getBlob(String p0) throws SQLException {
        return passthru.getBlob(p0);
    }

    public Clob getClob(String p0) throws SQLException {
        return passthru.getClob(p0);
    }

    public Clob getClob(int p0) throws SQLException {
        return passthru.getClob(p0);
    }

    public Array getArray(int p0) throws SQLException {
        return factory.getArray(passthru.getArray(p0), statement, preparedQuery, query);
    }

    public Array getArray(String p0) throws SQLException {
        return factory.getArray(passthru.getArray(p0), statement, preparedQuery, query);
    }

    // Since JDK 1.4
    public java.net.URL getURL(int p0) throws SQLException {
        return passthru.getURL(p0);
    }

    // Since JDK 1.4
    public java.net.URL getURL(String p0) throws SQLException {
        return passthru.getURL(p0);
    }

    // Since JDK 1.4
    public void updateRef(int p0, Ref p1) throws SQLException {
        passthru.updateRef(p0, p1);
    }

    // Since JDK 1.4
    public void updateRef(String p0, Ref p1) throws SQLException {
        passthru.updateRef(p0, p1);
    }

    // Since JDK 1.4
    public void updateBlob(int p0, Blob p1) throws SQLException {
        passthru.updateBlob(p0, p1);
    }

    // Since JDK 1.4
    public void updateBlob(String p0, Blob p1) throws SQLException {
        passthru.updateBlob(p0, p1);
    }

    // Since JDK 1.4
    public void updateClob(int p0, Clob p1) throws SQLException {
        passthru.updateClob(p0, p1);
    }

    // Since JDK 1.4
    public void updateClob(String p0, Clob p1) throws SQLException {
        passthru.updateClob(p0, p1);
    }

    // Since JDK 1.4
    public void updateArray(int p0, Array p1) throws SQLException {
        passthru.updateArray(p0, p1);
    }

    // Since JDK 1.4
    public void updateArray(String p0, Array p1) throws SQLException {
        passthru.updateArray(p0, p1);
    }
}
