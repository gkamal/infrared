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
 * Contributor(s):   prashant.nair, binil.thomas;
 *
 */
package net.sf.infrared.aspects.jdbc.p6spy;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import net.sf.infrared.agent.MonitorFactory;
import net.sf.infrared.agent.StatisticsCollector;
import net.sf.infrared.base.model.ExecutionContext;
import net.sf.infrared.base.model.ExecutionTimer;

import com.p6spy.engine.spy.P6Connection;
import com.p6spy.engine.spy.P6PreparedStatement;
import com.p6spy.engine.spy.P6Statement;

/**
 * This class is used by the P6Spy driver as a wrapper for PreparedStatement Not
 * inheriting from P6PreparedStatement as its implementation add overhead by
 * collecting bind values etc.
 * 
 * @author kamal.govindraj
 * @author prashant.nair
 * @author binil.thomas
 */
public class InfraREDP6PreparedStatement extends InfraREDP6Statement implements PreparedStatement {
    private PreparedStatement passThru;

    private String preparedQuery;

    private ExecutionContext executeCtx;

    public InfraREDP6PreparedStatement(InfraREDP6Factory infraP6Factory, PreparedStatement real,
            P6Connection conn, String sql) {

        super(infraP6Factory, real, conn);
        this.passThru = real;
        this.factory = infraP6Factory;
        this.preparedQuery = sql;
        this.conn = conn;
        executeCtx = infraP6Factory.getExecuteContext(sql);
    }

    public boolean execute() throws SQLException {
        if (factory.isJDBCMonitoringEnabled()) {
            ExecutionTimer timer = new ExecutionTimer(executeCtx);
            StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
            try {
                return passThru.execute();
            } finally {
                MonitorFactory.getFacade().recordExecutionEnd(timer, col);
            }       
        } else {
            return passThru.execute();
        }
    }

    public void addBatch() throws SQLException {
        passThru.addBatch();
    }

    public void setCharacterStream(int parameterIndex, Reader reader, int length)
            throws SQLException {
        passThru.setCharacterStream(parameterIndex, reader, length);
    }

    public void setRef(int i, Ref x) throws SQLException {
        passThru.setRef(i, x);
    }

    public void setBlob(int i, Blob x) throws SQLException {
        passThru.setBlob(i, x);
    }

    public void setClob(int i, Clob x) throws SQLException {
        passThru.setClob(i, x);
    }

    public void setArray(int i, Array x) throws SQLException {
        passThru.setArray(i, x);
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        return factory.getResultSetMetaData(passThru.getMetaData());
    }

    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        passThru.setDate(parameterIndex, x, cal);
    }

    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        passThru.setTime(parameterIndex, x, cal);
    }

    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        passThru.setTimestamp(parameterIndex, x, cal);
    }

    public void setNull(int paramIndex, int sqlType, String typeName) throws SQLException {
        passThru.setNull(paramIndex, sqlType, typeName);
    }

    public void setURL(int parameterIndex, URL x) throws SQLException {
        passThru.setURL(parameterIndex, x);
    }

    public ParameterMetaData getParameterMetaData() throws SQLException {
        return passThru.getParameterMetaData();
    }

    public ResultSet executeQuery() throws SQLException {
        ResultSet resultSet = null;
        if (factory.isJDBCMonitoringEnabled()) {
            ExecutionTimer timer = new ExecutionTimer(executeCtx);
            StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
            try {
                resultSet = passThru.executeQuery();
            } finally {
                MonitorFactory.getFacade().recordExecutionEnd(timer, col);
            }
        } else {
            resultSet = passThru.executeQuery();
        }
        return factory.getResultSet(resultSet, getP6Statement(), preparedQuery, query);
    }

    public int executeUpdate() throws SQLException {
        int returnValue;
        
        if (factory.isJDBCMonitoringEnabled()) {
            ExecutionTimer timer = new ExecutionTimer(executeCtx);
            StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
            try {
                returnValue = passThru.executeUpdate();
            } finally {
                MonitorFactory.getFacade().recordExecutionEnd(timer, col);
            }
        } else {
            returnValue = passThru.executeUpdate();
        }

        return returnValue;
    }

    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        passThru.setNull(parameterIndex, sqlType);
    }

    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        passThru.setBoolean(parameterIndex, x);
    }

    public void setByte(int parameterIndex, byte x) throws SQLException {
        passThru.setByte(parameterIndex, x);
    }

    public void setShort(int parameterIndex, short x) throws SQLException {
        passThru.setShort(parameterIndex, x);
    }

    public void setInt(int parameterIndex, int x) throws SQLException {
        passThru.setInt(parameterIndex, x);
    }

    public void setLong(int parameterIndex, long x) throws SQLException {
        passThru.setLong(parameterIndex, x);
    }

    public void setFloat(int parameterIndex, float x) throws SQLException {
        passThru.setFloat(parameterIndex, x);
    }

    public void setDouble(int parameterIndex, double x) throws SQLException {
        passThru.setDouble(parameterIndex, x);
    }

    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        passThru.setBigDecimal(parameterIndex, x);
    }

    public void setString(int parameterIndex, String x) throws SQLException {
        passThru.setString(parameterIndex, x);
    }

    public void setBytes(int parameterIndex, byte x[]) throws SQLException {
        passThru.setBytes(parameterIndex, x);
    }

    public void setDate(int parameterIndex, Date x) throws SQLException {
        passThru.setDate(parameterIndex, x);
    }

    public void setTime(int parameterIndex, Time x) throws SQLException {
        passThru.setTime(parameterIndex, x);
    }

    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        passThru.setTimestamp(parameterIndex, x);
    }

    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        passThru.setAsciiStream(parameterIndex, x, length);
    }

    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        passThru.setUnicodeStream(parameterIndex, x, length);
    }

    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        passThru.setBinaryStream(parameterIndex, x, length);
    }

    public void clearParameters() throws SQLException {
        passThru.clearParameters();
    }

    public void setObject(int parameterIndex, Object x, int targetSqlType, int scale)
            throws SQLException {
        passThru.setObject(parameterIndex, x, targetSqlType, scale);
    }

    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        passThru.setObject(parameterIndex, x, targetSqlType);
    }

    public void setObject(int parameterIndex, Object x) throws SQLException {
        passThru.setObject(parameterIndex, x);
    }

    protected P6Statement getP6Statement() {
        if (p6Statement == null) {
            p6Statement = new P6PreparedStatement(factory, this, conn, preparedQuery);
        }
        return p6Statement;
    }

	public void setAsciiStream(int parameterIndex, InputStream x)
			throws SQLException {
		passThru.setAsciiStream(parameterIndex, x);
	}

	public void setAsciiStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		passThru.setAsciiStream(parameterIndex, x, length);
	}

	public void setBinaryStream(int parameterIndex, InputStream x)
			throws SQLException {
		passThru.setBinaryStream(parameterIndex, x);
	}

	public void setBinaryStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		passThru.setBinaryStream(parameterIndex, x, length);
	}

	public void setBlob(int parameterIndex, InputStream inputStream)
			throws SQLException {
		passThru.setBlob(parameterIndex, inputStream);
	}

	public void setBlob(int parameterIndex, InputStream inputStream, long length)
			throws SQLException {
		passThru.setBlob(parameterIndex, inputStream, length);
	}

	public void setCharacterStream(int parameterIndex, Reader reader)
			throws SQLException {
		passThru.setCharacterStream(parameterIndex, reader);
	}

	public void setCharacterStream(int parameterIndex, Reader reader,
			long length) throws SQLException {
		passThru.setCharacterStream(parameterIndex, reader, length);	
	}

	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		passThru.setClob(parameterIndex, reader);
	}

	public void setClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		passThru.setClob(parameterIndex, reader, length);
	}

	public void setNCharacterStream(int parameterIndex, Reader value)
			throws SQLException {
		passThru.setNCharacterStream(parameterIndex, value);
	}

	public void setNCharacterStream(int parameterIndex, Reader value,
			long length) throws SQLException {
		passThru.setNCharacterStream(parameterIndex, value, length);
	}

	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		passThru.setNClob(parameterIndex, value);
	}

	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		passThru.setNClob(parameterIndex, reader);
	}

	public void setNClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		passThru.setNClob(parameterIndex, reader, length);
	}

	public void setNString(int parameterIndex, String value)
			throws SQLException {
		passThru.setNString(parameterIndex, value);
	}

	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		passThru.setRowId(parameterIndex, x);
	}

	public void setSQLXML(int parameterIndex, SQLXML xmlObject)
			throws SQLException {
		passThru.setSQLXML(parameterIndex, xmlObject);
	}
}
