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
import java.util.List;
import java.util.stream.Collectors;

public class IntegerArray extends AbstractStructuredDbType {

  public static final String INT_4 = "_int4";
  private Connection connection;

  IntegerArray(Connection connection) {
    super(2003, INT_4);
    this.connection = connection;
  }

  @Override
  public Object getParameterValue(CallableStatement statement, int index) throws SQLException {
    return super.getParameterValue(statement, index);
  }

  @Override
  public void setParameterValue(PreparedStatement statement, int index, Object value) throws SQLException {
    //    Object[] array;
    //    if (value instanceof Collection) {
    //      array = ((Collection) value).toArray();
    //    } else if (value instanceof Iterator) {
    //      array = Iterators.toArray((Iterator) value, Object.class);
    //    } else {
    //      array = new Object[] {};
    //    }
    Object collect = ((List) value).stream().map(Object::toString).collect(Collectors.joining(",", "{", "}"));
    super.setParameterValue(statement, index, collect);
  }
}
