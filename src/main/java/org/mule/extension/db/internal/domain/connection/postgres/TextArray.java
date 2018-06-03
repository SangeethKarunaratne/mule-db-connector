/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.db.internal.domain.connection.postgres;

import org.mule.extension.db.internal.domain.type.AbstractStructuredDbType;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TextArray extends AbstractStructuredDbType {

  TextArray(Connection connection) {
    super(2003, "_text");
  }

  @Override
  public Object getParameterValue(CallableStatement statement, int index) throws SQLException {
    return super.getParameterValue(statement, index);
  }

  @Override
  public void setParameterValue(PreparedStatement statement, int index, Object value) throws SQLException {
    super.setParameterValue(statement, index, value.toString());
  }
}
