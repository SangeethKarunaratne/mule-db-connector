/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.db.internal.domain.connection.sqlserver;

import static org.apache.commons.codec.binary.Hex.encodeHexString;

import org.mule.extension.db.internal.domain.type.AbstractStructuredDbType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * //TODO
 * This is done because SQLServer when inserting a varbinary type and the given value is a String, this should be
 * the HEX representation of the real value.
 */
public class SqlServerVarBinaryDbType extends AbstractStructuredDbType {

  public SqlServerVarBinaryDbType() {
    super(-3, "varbinary");
  }

  @Override
  public Object getParameterValue(CallableStatement statement, int index) throws SQLException {
    return super.getParameterValue(statement, index);
  }

  @Override
  public void setParameterValue(PreparedStatement statement, int index, Object value) throws SQLException {
    if (value instanceof String) {
      String stringValue = (String) value;
      value = encodeHexString(stringValue.getBytes());
    }
    super.setParameterValue(statement, index, value);
  }
}
