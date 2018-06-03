/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.db.internal.domain.connection.postgres;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.mule.extension.db.internal.domain.connection.DbConnectionProvider.DRIVER_FILE_NAME_PATTERN;
import static org.mule.runtime.api.meta.ExternalLibraryType.JAR;
import static org.mule.runtime.extension.api.annotation.param.ParameterGroup.CONNECTION;

import org.mule.extension.db.internal.domain.connection.DataSourceConfig;
import org.mule.extension.db.internal.domain.connection.DbConnection;
import org.mule.extension.db.internal.domain.connection.DbConnectionProvider;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.ExternalLib;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

import java.sql.Connection;
import java.util.Optional;

import javax.sql.DataSource;

/**
 * {@link DbConnectionProvider} implementation for Microsoft SQL Server Databases
 *
 * @since 1.1.0
 */
@DisplayName("PostgreSQL Connection")
@Alias("postgresql")
@ExternalLib(name = "PostgreSQL Driver",
    description = "A JDBC driver which supports connecting to an PostgreSQL Database",
    requiredClassName = PostgreSQLConnectionProvider.POSTGRESQL_DRIVER, type = JAR,
    coordinates = "org.postgresql:postgresql:42.2.2",
    nameRegexpMatcher = DRIVER_FILE_NAME_PATTERN)
public class PostgreSQLConnectionProvider extends DbConnectionProvider {

  static final String POSTGRESQL_DRIVER = "org.postgresql.Driver";

  @ParameterGroup(name = CONNECTION)
  private PostgreSQLConnectionParameters connectionParameters;

  @Override
  protected DbConnection createDbConnection(Connection connection) throws Exception {
    return new PostgreSQLConnection(connection, super.resolveCustomTypes());
  }

  @Override
  public Optional<DataSource> getDataSource() {
    return empty();
  }

  @Override
  public Optional<DataSourceConfig> getDataSourceConfig() {
    return ofNullable(connectionParameters);
  }
}

