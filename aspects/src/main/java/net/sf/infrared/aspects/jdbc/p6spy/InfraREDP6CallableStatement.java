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
 * Original Author:  prashant.nair (Tavant Technologies)
 * Contributor(s):   binil.thomas;
 *
 */
package net.sf.infrared.aspects.jdbc.p6spy;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import java.sql.NClob;
import java.sql.RowId;
import java.sql.SQLXML;

import net.sf.infrared.agent.MonitorFactory;
import net.sf.infrared.base.model.ExecutionContext;
import net.sf.infrared.base.model.ExecutionTimer;

import com.p6spy.engine.spy.P6Connection;

/**
 * 
 * @author prashant.nair
 * @author binil.thomas
 */
public class InfraREDP6CallableStatement 
        extends InfraREDP6PreparedStatement implements CallableStatement {

    private CallableStatement passThru;

    private ExecutionContext ctx;

    public InfraREDP6CallableStatement(InfraREDP6Factory infraP6Factory, 
            CallableStatement real, P6Connection conn, String sql) {
        super(infraP6Factory, real, conn, sql);
        this.passThru = real;
        this.factory = infraP6Factory;
        this.conn = conn;
        this.ctx = infraP6Factory.getExecuteContext(sql);
    }

    public boolean execute() throws SQLException {
        if (factory.isJDBCMonitoringEnabled()) {
            ExecutionTimer timer = new ExecutionTimer(ctx);
            MonitorFactory.getFacade().recordExecutionBegin(timer);
            try {
                return passThru.execute();
            } finally {
                MonitorFactory.getFacade().recordExecutionEnd(timer);
            }
        } else {
            return passThru.execute();
        }
    }

    public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
        passThru.registerOutParameter(parameterIndex, sqlType);
    }

    public void registerOutParameter(int parameterIndex, int sqlType, int scale)
            throws SQLException {
        passThru.registerOutParameter(parameterIndex, sqlType, scale);
    }

    public boolean wasNull() throws SQLException {
        return passThru.wasNull();
    }

    public String getString(int parameterIndex) throws SQLException {
        return passThru.getString(parameterIndex);
    }

    public boolean getBoolean(int parameterIndex) throws SQLException {
        return passThru.getBoolean(parameterIndex);
    }

    public byte getByte(int parameterIndex) throws SQLException {
        return passThru.getByte(parameterIndex);
    }

    public short getShort(int parameterIndex) throws SQLException {
        return passThru.getShort(parameterIndex);
    }

    public int getInt(int parameterIndex) throws SQLException {
        return passThru.getInt(parameterIndex);
    }

    public long getLong(int parameterIndex) throws SQLException {
        return passThru.getLong(parameterIndex);
    }

    public float getFloat(int parameterIndex) throws SQLException {
        return passThru.getFloat(parameterIndex);
    }

    public double getDouble(int parameterIndex) throws SQLException {
        return passThru.getDouble(parameterIndex);
    }

    public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
        return passThru.getBigDecimal(parameterIndex, scale);
    }

    public byte[] getBytes(int parameterIndex) throws SQLException {
        return passThru.getBytes(parameterIndex);
    }

    public Date getDate(int parameterIndex) throws SQLException {
        return passThru.getDate(parameterIndex);
    }

    public Time getTime(int parameterIndex) throws SQLException {
        return passThru.getTime(parameterIndex);
    }

    public Timestamp getTimestamp(int parameterIndex) throws SQLException {
        return passThru.getTimestamp(parameterIndex);
    }

    public Object getObject(int parameterIndex) throws SQLException {
        return passThru.getObject(parameterIndex);
    }

    public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
        return passThru.getBigDecimal(parameterIndex);
    }

    public Object getObject(int i, Map map) throws SQLException {
        return passThru.getObject(i, map);
    }

    public Ref getRef(int i) throws SQLException {
        return passThru.getRef(i);
    }

    public Blob getBlob(int i) throws SQLException {
        return passThru.getBlob(i);
    }

    public Clob getClob(int i) throws SQLException {
        return passThru.getClob(i);
    }

    public Array getArray(int i) throws SQLException {
        return passThru.getArray(i);
    }

    public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
        return passThru.getDate(parameterIndex, cal);
    }

    public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
        return passThru.getTime(parameterIndex, cal);
    }

    public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
        return passThru.getTimestamp(parameterIndex, cal);
    }

    public void registerOutParameter(int paramIndex, int sqlType, String typeName)
            throws SQLException {
        passThru.registerOutParameter(paramIndex, sqlType, typeName);
    }

    public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
        passThru.registerOutParameter(parameterName, sqlType);
    }

    public void registerOutParameter(String parameterName, int sqlType, int scale)
            throws SQLException {
        passThru.registerOutParameter(parameterName, sqlType, scale);
    }

    public void registerOutParameter(String parameterName, int sqlType, String typeName)
            throws SQLException {
        passThru.registerOutParameter(parameterName, sqlType, typeName);
    }

    public URL getURL(int parameterIndex) throws SQLException {
        return passThru.getURL(parameterIndex);
    }

    public void setURL(String parameterName, URL val) throws SQLException {
        passThru.setURL(parameterName, val);
    }

    public void setNull(String parameterName, int sqlType) throws SQLException {
        passThru.setNull(parameterName, sqlType);
    }

    public void setBoolean(String parameterName, boolean x) throws SQLException {
        passThru.setBoolean(parameterName, x);
    }

    public void setByte(String parameterName, byte x) throws SQLException {
        passThru.setByte(parameterName, x);
    }

    public void setShort(String parameterName, short x) throws SQLException {
        passThru.setShort(parameterName, x);
    }

    public void setInt(String parameterName, int x) throws SQLException {
        passThru.setInt(parameterName, x);
    }

    public void setLong(String parameterName, long x) throws SQLException {
        passThru.setLong(parameterName, x);
    }

    public void setFloat(String parameterName, float x) throws SQLException {
        passThru.setFloat(parameterName, x);
    }

    public void setDouble(String parameterName, double x) throws SQLException {
        passThru.setDouble(parameterName, x);
    }

    public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
        passThru.setBigDecimal(parameterName, x);
    }

    public void setString(String parameterName, String x) throws SQLException {
        passThru.setString(parameterName, x);
    }

    public void setBytes(String parameterName, byte x[]) throws SQLException {
        passThru.setBytes(parameterName, x);
    }

    public void setDate(String parameterName, Date x) throws SQLException {
        passThru.setDate(parameterName, x);
    }

    public void setTime(String parameterName, Time x) throws SQLException {
        passThru.setTime(parameterName, x);
    }

    public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
        passThru.setTimestamp(parameterName, x);
    }

    public void setAsciiStream(String parameterName, 
            InputStream x, int length) throws SQLException {
        passThru.setAsciiStream(parameterName, x, length);
    }

    public void setBinaryStream(String parameterName, InputStream x, int length)
            throws SQLException {
        passThru.setBinaryStream(parameterName, x, length);
    }

    public void setObject(String parameterName, Object x, int targetSqlType, int scale)
            throws SQLException {
        passThru.setObject(parameterName, x, targetSqlType, scale);
    }

    public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
        passThru.setObject(parameterName, x, targetSqlType);
    }

    public void setObject(String parameterName, Object x) throws SQLException {
        passThru.setObject(parameterName, x);
    }

    public void setCharacterStream(String parameterName, Reader reader, int length)
            throws SQLException {
        passThru.setCharacterStream(parameterName, reader, length);
    }

    public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
        passThru.setDate(parameterName, x, cal);
    }

    public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
        passThru.setTime(parameterName, x, cal);
    }

    public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
        passThru.setTimestamp(parameterName, x, cal);
    }

    public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
        passThru.setNull(parameterName, sqlType, typeName);
    }

    public String getString(String parameterName) throws SQLException {
        return passThru.getString(parameterName);
    }

    public boolean getBoolean(String parameterName) throws SQLException {
        return passThru.getBoolean(parameterName);
    }

    public byte getByte(String parameterName) throws SQLException {
        return passThru.getByte(parameterName);
    }

    public short getShort(String parameterName) throws SQLException {
        return passThru.getShort(parameterName);
    }

    public int getInt(String parameterName) throws SQLException {
        return passThru.getInt(parameterName);
    }

    public long getLong(String parameterName) throws SQLException {
        return passThru.getLong(parameterName);
    }

    public float getFloat(String parameterName) throws SQLException {
        return passThru.getFloat(parameterName);
    }

    public double getDouble(String parameterName) throws SQLException {
        return passThru.getDouble(parameterName);
    }

    public byte[] getBytes(String parameterName) throws SQLException {
        return passThru.getBytes(parameterName);
    }

    public Date getDate(String parameterName) throws SQLException {
        return passThru.getDate(parameterName);
    }

    public Time getTime(String parameterName) throws SQLException {
        return passThru.getTime(parameterName);
    }

    public Timestamp getTimestamp(String parameterName) throws SQLException {
        return passThru.getTimestamp(parameterName);
    }

    public Object getObject(String parameterName) throws SQLException {
        return passThru.getObject(parameterName);
    }

    public BigDecimal getBigDecimal(String parameterName) throws SQLException {
        return passThru.getBigDecimal(parameterName);
    }

    public Object getObject(String parameterName, Map map) throws SQLException {
        return passThru.getObject(parameterName, map);
    }

    public Ref getRef(String parameterName) throws SQLException {
        return passThru.getRef(parameterName);
    }

    public Blob getBlob(String parameterName) throws SQLException {
        return passThru.getBlob(parameterName);
    }

    public Clob getClob(String parameterName) throws SQLException {
        return passThru.getClob(parameterName);
    }

    public Array getArray(String parameterName) throws SQLException {
        return passThru.getArray(parameterName);
    }

    public Date getDate(String parameterName, Calendar cal) throws SQLException {
        return passThru.getDate(parameterName, cal);
    }

    public Time getTime(String parameterName, Calendar cal) throws SQLException {
        return passThru.getTime(parameterName, cal);
    }

    public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
        return passThru.getTimestamp(parameterName, cal);
    }

    public URL getURL(String parameterName) throws SQLException {
        return passThru.getURL(parameterName);
    }

	public Reader getCharacterStream(int parameterIndex) throws SQLException {
		return passThru.getCharacterStream(parameterIndex);
	}

	public Reader getCharacterStream(String parameterName) throws SQLException {
		return passThru.getCharacterStream(parameterName);
	}

	public Reader getNCharacterStream(int parameterIndex) throws SQLException {
		return passThru.getNCharacterStream(parameterIndex);
	}

	public Reader getNCharacterStream(String parameterName) throws SQLException {
		return passThru.getNCharacterStream(parameterName);
	}

	public NClob getNClob(int parameterIndex) throws SQLException {
		return passThru.getNClob(parameterIndex);
	}

	public NClob getNClob(String parameterName) throws SQLException {
		return passThru.getNClob(parameterName);
	}

	public String getNString(int parameterIndex) throws SQLException {
		return passThru.getNString(parameterIndex);
	}

	public String getNString(String parameterName) throws SQLException {
		return passThru.getNString(parameterName);
	}

	public RowId getRowId(int parameterIndex) throws SQLException {
		return passThru.getRowId(parameterIndex);
	}

	public RowId getRowId(String parameterName) throws SQLException {
		return passThru.getRowId(parameterName);
	}

	public SQLXML getSQLXML(int parameterIndex) throws SQLException {
		return passThru.getSQLXML(parameterIndex);
	}

	public SQLXML getSQLXML(String parameterName) throws SQLException {
		return passThru.getSQLXML(parameterName);
	}

	public void setAsciiStream(String parameterName, InputStream x)
			throws SQLException {
		passThru.setAsciiStream(parameterName, x);
	}

	public void setAsciiStream(String parameterName, InputStream x, long length)
			throws SQLException {
		passThru.setAsciiStream(parameterName, x, length);
	}

	public void setBinaryStream(String parameterName, InputStream x)
			throws SQLException {
		passThru.setBinaryStream(parameterName, x);
	}

	public void setBinaryStream(String parameterName, InputStream x, long length)
			throws SQLException {
		passThru.setBinaryStream(parameterName, x, length);
	}

	public void setBlob(String parameterName, Blob x) throws SQLException {
		passThru.setBlob(parameterName, x);
	}

	public void setBlob(String parameterName, InputStream inputStream)
			throws SQLException {
		passThru.setBlob(parameterName, inputStream);
	}

	public void setBlob(String parameterName, InputStream inputStream,
			long length) throws SQLException {
		passThru.setBlob(parameterName, inputStream, length);
	}

	public void setCharacterStream(String parameterName, Reader reader)
			throws SQLException {
		passThru.setCharacterStream(parameterName, reader);
	}

	public void setCharacterStream(String parameterName, Reader reader,
			long length) throws SQLException {
		passThru.setCharacterStream(parameterName, reader, length);
	}

	public void setClob(String parameterName, Clob x) throws SQLException {
		passThru.setClob(parameterName, x);
	}

	public void setClob(String parameterName, Reader reader)
			throws SQLException {
		passThru.setClob(parameterName, reader);
	}

	public void setClob(String parameterName, Reader reader, long length)
			throws SQLException {
		passThru.setClob(parameterName, reader, length);
	}

	public void setNCharacterStream(String parameterName, Reader value)
			throws SQLException {
		passThru.setNCharacterStream(parameterName, value);
	}

	public void setNCharacterStream(String parameterName, Reader value,
			long length) throws SQLException {
		passThru.setNCharacterStream(parameterName, value, length);
	}

	public void setNClob(String parameterName, NClob value) throws SQLException {
		passThru.setNClob(parameterName, value);
	}

	public void setNClob(String parameterName, Reader reader)
			throws SQLException {
		passThru.setNClob(parameterName, reader);
	}

	public void setNClob(String parameterName, Reader reader, long length)
			throws SQLException {
		passThru.setNClob(parameterName, reader, length);
	}

	public void setNString(String parameterName, String value)
			throws SQLException {
		passThru.setNString(parameterName, value);
	}

	public void setRowId(String parameterName, RowId x) throws SQLException {
		passThru.setRowId(parameterName, x);
	}

	public void setSQLXML(String parameterName, SQLXML xmlObject)
			throws SQLException {
		passThru.setSQLXML(parameterName, xmlObject);
	}
}
