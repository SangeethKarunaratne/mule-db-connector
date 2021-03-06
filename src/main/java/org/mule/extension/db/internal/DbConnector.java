/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.db.internal;

import org.mule.extension.db.api.exception.connection.ConnectionCreationException;
import org.mule.extension.db.api.exception.connection.DbError;
import org.mule.extension.db.api.logger.LoggerApiPackage;
import org.mule.extension.db.api.param.BulkQueryDefinition;
import org.mule.extension.db.api.param.JdbcType;
import org.mule.extension.db.api.param.QueryDefinition;
import org.mule.extension.db.api.param.StoredProcedureCall;
import org.mule.extension.db.internal.domain.connection.datasource.DataSourceReferenceConnectionProvider;
import org.mule.extension.db.internal.domain.connection.derby.DerbyConnectionProvider;
import org.mule.extension.db.internal.domain.connection.generic.GenericConnectionProvider;
import org.mule.extension.db.internal.domain.connection.mysql.MySqlConnectionProvider;
import org.mule.extension.db.internal.domain.connection.oracle.OracleDbConnectionProvider;
import org.mule.extension.db.internal.domain.connection.sqlserver.SqlServerConnectionProvider;
import org.mule.extension.db.internal.domain.type.CompositeDbTypeManager;
import org.mule.extension.db.internal.domain.type.DbTypeManager;
import org.mule.extension.db.internal.domain.type.MetadataDbTypeManager;
import org.mule.extension.db.internal.domain.type.StaticDbTypeManager;
import org.mule.extension.db.internal.exception.DbExceptionHandler;
import org.mule.extension.db.internal.operation.BulkOperations;
import org.mule.extension.db.internal.operation.DdlOperations;
import org.mule.extension.db.internal.operation.DmlOperations;
import org.mule.extension.db.internal.source.RowListener;
import org.mule.runtime.api.lifecycle.Initialisable;
import org.mule.runtime.api.lifecycle.InitialisationException;
import org.mule.runtime.extension.api.annotation.Export;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.OnException;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.Sources;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;
import org.mule.runtime.extension.api.annotation.error.ErrorTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * Connector for connecting to relation Databases through the JDBC API
 *
 * @since 1.0
 */
@Extension(name = "Database")
@Operations({DmlOperations.class, DdlOperations.class, BulkOperations.class})
@Sources(RowListener.class)
@ConnectionProviders({DataSourceReferenceConnectionProvider.class, GenericConnectionProvider.class, DerbyConnectionProvider.class,
    MySqlConnectionProvider.class, OracleDbConnectionProvider.class, SqlServerConnectionProvider.class})
@Xml(prefix = "db")
@Export(
    classes = {QueryDefinition.class, StoredProcedureCall.class, BulkQueryDefinition.class, ConnectionCreationException.class,
        LoggerApiPackage.class})
@ErrorTypes(DbError.class)
@OnException(DbExceptionHandler.class)
public class DbConnector implements Initialisable {

  private DbTypeManager typeManager;

  @Override
  public void initialise() throws InitialisationException {
    typeManager = createBaseTypeManager();
  }

  public DbTypeManager getTypeManager() {
    return typeManager;
  }

  private DbTypeManager createBaseTypeManager() {
    List<DbTypeManager> typeManagers = new ArrayList<>();
    typeManagers.add(new MetadataDbTypeManager());
    typeManagers.add(new StaticDbTypeManager(JdbcType.getAllTypes()));

    return new CompositeDbTypeManager(typeManagers);
  }
}
