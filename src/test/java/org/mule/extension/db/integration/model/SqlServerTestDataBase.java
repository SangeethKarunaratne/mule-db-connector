/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.db.integration.model;

import org.mule.extension.db.integration.DbTestUtil;
import org.mule.metadata.api.model.MetadataType;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;

//TODO - Complete, there are missing Store Procedures
public class SqlServerTestDataBase extends AbstractTestDatabase {

  @Override
  public DbTestUtil.DbType getDbType() {
    return DbTestUtil.DbType.SQLSERVER;
  }

  public static String SQL_CREATE_SP_UPDATE_TEST_TYPE_1 =
      "CREATE PROCEDURE updateTestType1\n" +
          "AS\n" +
          "BEGIN\n" +
          "    UPDATE PLANET SET NAME='Mercury' WHERE POSITION=4;\n" +
          "END";

  public static String SQL_CREATE_SP_GET_RECORDS =
      "CREATE PROCEDURE getTestRecords\n" +
          "AS\n" +
          "BEGIN\n" +
          "    SELECT * FROM PLANET;\n" +
          "END";

  public static String SQL_CREATE_SP_GET_SPLIT_RECORDS =
      "CREATE PROCEDURE getSplitTestRecords\n" +
          "AS\n" +
          "BEGIN\n" +
          "    SELECT * FROM PLANET WHERE POSITION <= 2; \n" +
          "    SELECT * FROM PLANET WHERE POSITION > 2;\n" +
          "END";

  @Override
  public void createPlanetTable(Connection connection) throws SQLException {
    executeDdl(connection,
               "CREATE TABLE PLANET(\n" +
                   "ID INTEGER NOT NULL IDENTITY (1,1),\n" +
                   "POSITION INTEGER,\n" +
                   "NAME VARCHAR(255), \n" +
                   "PICTURE varbinary(8000), \n" +
                   "DESCRIPTION NTEXT, \n" +
                   "PRIMARY KEY (ID))");
  }

  @Override
  public void createSpaceshipTable(Connection connection) throws SQLException {
    executeDdl(connection,
               "CREATE TABLE SPACESHIP(\n" +
                   "ID INTEGER NOT NULL IDENTITY (1,1),\n" +
                   "MODEL VARCHAR(255), \n" +
                   "MANUFACTURER VARCHAR(255), \n" +
                   "PRIMARY KEY (ID))");
  }

  @Override
  protected String getInsertPlanetSql(String name, int position) {
    return "INSERT INTO PLANET(POSITION, NAME) VALUES (" + position + ", '" + name + "')";
  }

  @Override
  public void createStoredProcedureGetRecords(DataSource dataSource) throws SQLException {
    executeDdl(dataSource, "DROP PROCEDURE IF EXISTS getTestRecords;\n");
    createStoredProcedure(dataSource, SQL_CREATE_SP_GET_RECORDS);
  }

  @Override
  public void createStoredProcedureUpdateTestType1(DataSource dataSource) throws SQLException {
    executeDdl(dataSource, "DROP PROCEDURE IF EXISTS updateTestType1;\n");
    createStoredProcedure(dataSource, SQL_CREATE_SP_UPDATE_TEST_TYPE_1);
  }

  @Override
  public void createStoredProcedureParameterizedUpdatePlanetDescription(DataSource dataSource) throws SQLException {
    executeDdl(dataSource, "DROP PROCEDURE IF EXISTS updatePlanetDescription;\n");

    final String sql =
        "CREATE PROCEDURE updatePlanetDescription(@pName VARCHAR(50),@pDescription NTEXT)\n" +
            "AS\n" +
            "BEGIN\n" +
            "    update Planet SET DESCRIPTION=@pDescription where NAME = @pName;\n" +
            "END";

    createStoredProcedure(dataSource, sql);
  }

  @Override
  public void createStoredProcedureParameterizedUpdateTestType1(DataSource dataSource) throws SQLException {
    executeDdl(dataSource, "DROP PROCEDURE IF EXISTS updateParamTestType1;\n");

    final String sql = "CREATE PROCEDURE updateParamTestType1(@pName VARCHAR(50))\n" +
        "AS\n" +
        "BEGIN\n" +
        "    UPDATE PLANET SET NAME=@pName WHERE POSITION=4;\n" +
        "END";

    createStoredProcedure(dataSource, sql);
  }

  @Override
  public void createStoredProcedureCountRecords(DataSource dataSource) throws SQLException {
    executeDdl(dataSource, "DROP PROCEDURE IF EXISTS countTestRecords;\n");

    final String sql = "CREATE PROCEDURE countTestRecords(@pResult INT)\n" +
        "AS\n" +
        "BEGIN\n" +
        "    set @pResult = (select count(*) from PLANET)\n" +
        "END";

    createStoredProcedure(dataSource, sql);
  }

  @Override
  public void createStoredProcedureGetSplitRecords(DataSource dataSource) throws SQLException {
    executeDdl(dataSource, "DROP PROCEDURE IF EXISTS getSplitTestRecords;\n");
    createStoredProcedure(dataSource, SQL_CREATE_SP_GET_SPLIT_RECORDS);

  }

  @Override
  public void createStoredProcedureDoubleMyInt(DataSource dataSource) throws SQLException {
    executeDdl(dataSource, "DROP PROCEDURE IF EXISTS doubleMyInt;\n");

    final String sql =
        "CREATE PROCEDURE doubleMyInt(@pInt INT OUTPUT)\n" +
            "AS\n" +
            "BEGIN\n" +
            "    SET @pInt = @pInt * 2\n" +
            "END";

    createStoredProcedure(dataSource, sql);
  }

  @Override
  public void createStoredProcedureMultiplyInts(DataSource dataSource) throws SQLException {
    executeDdl(dataSource, "DROP PROCEDURE IF EXISTS multiplyInts;\n");

    final String sql =
        "CREATE DEFINER=CURRENT_USER PROCEDURE multiplyInts(IN pInt1 INT, IN pInt2 INT, OUT pResult1 INT, IN pInt3 INT, OUT pResult2 INT)\n"
            + "BEGIN\n" + "SET pResult1 := pInt1 * pInt2;\n" + "SET pResult2 := pInt1 * pInt2 * pInt3;\n" + "END";
    createStoredProcedure(dataSource, sql);
  }

  @Override
  public void createStoredProcedureConcatenateStrings(DataSource dataSource) throws SQLException {
    executeDdl(dataSource, "DROP PROCEDURE IF EXISTS concatenateStrings;\n");

    final String sql =
        "CREATE DEFINER=CURRENT_USER PROCEDURE concatenateStrings(IN pString1 VARCHAR(50), IN pString2 VARCHAR(50), OUT pResult VARCHAR(100))\n"
            + "BEGIN\n" + "SET pResult := CONCAT(pString1, pString2);\n" + "END";

    createStoredProcedure(dataSource, sql);
  }

  @Override
  public void createDelayFunction(DataSource dataSource) throws SQLException {
    //SQL Server doesn't support delays inside functions
  }

  @Override
  public MetadataType getDescriptionFieldMetaDataType() {
    // TODO(pablo.kraan): DB - what type must be used here?
    return super.getDescriptionFieldMetaDataType();
  }
}
