/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.db.internal.domain.connection.postgres;

import org.mule.extension.db.internal.domain.connection.DefaultDbConnection;
import org.mule.extension.db.internal.domain.type.DbType;
import org.mule.runtime.api.metadata.DataType;
import org.mule.runtime.api.metadata.MediaType;
import org.mule.runtime.api.metadata.TypedValue;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class PostgreSQLConnection extends DefaultDbConnection {

  private static final String MONEY_DATA_TYPE = "money";
  private static final String JSON_DATA_TYPE = "json";
  private static final String JSONB_DATA_TYPE = "jsonb";
  private final Connection connection;

  PostgreSQLConnection(Connection connection, List<DbType> dbTypes) {
    super(connection, dbTypes);
    this.connection = connection;
  }

  @Override
  public List<DbType> getCustomDataTypes() {
    ArrayList<DbType> dbTypes = new ArrayList<>();
    dbTypes.add(new TextArray(connection));
    dbTypes.add(new IntegerArray(connection));
    return dbTypes;
  }

  @Override
  public Object getObject(ResultSet resultSet, int i) throws SQLException {
    String columnTypeName = resultSet.getMetaData().getColumnTypeName(i);
    switch (columnTypeName) {
      case MONEY_DATA_TYPE:
        return parseMoneyType(resultSet.getString(i));
      case JSON_DATA_TYPE:
        return parseJsonType(resultSet.getBinaryStream(i));
      case JSONB_DATA_TYPE:
        return parseJsonType(resultSet.getBinaryStream(i));
      default:
        return resultSet.getObject(i);
    }
  }

  private Object parseJsonType(InputStream bytes) {
    return new TypedValue<>(bytes, DataType.builder().type(InputStream.class).mediaType(MediaType.APPLICATION_JSON).build());
  }

  private Object parseMoneyType(String string) {
    return new BigDecimal(string.substring(1).replace(",", ""));
  }
}
