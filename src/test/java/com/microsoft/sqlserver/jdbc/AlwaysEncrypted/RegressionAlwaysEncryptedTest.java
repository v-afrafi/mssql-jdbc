/*
 * Microsoft JDBC Driver for SQL Server Copyright(c) Microsoft Corporation All rights reserved. This program is made
 * available under the terms of the MIT License. See the LICENSE file in the project root for more information.
 */
package com.microsoft.sqlserver.jdbc.AlwaysEncrypted;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.microsoft.sqlserver.jdbc.TestUtils;
import com.microsoft.sqlserver.testframework.AbstractSQLGenerator;
import com.microsoft.sqlserver.testframework.Constants;
import com.microsoft.sqlserver.testframework.PrepUtil;


@RunWith(JUnitPlatform.class)
@Tag(Constants.xSQLv12)
@Tag(Constants.xAzureSQLDW)
@Tag(Constants.xAzureSQLDB)
public class RegressionAlwaysEncryptedTest extends AESetup {

    @Test
    public void alwaysEncrypted1() throws SQLException {
        try (Connection connection = PrepUtil.getConnection(
                connectionString + ";trustservercertificate=true;columnEncryptionSetting=enabled;", AEInfo);
                Statement stmt = connection.createStatement()) {
            dropTables(stmt);

            createNumericTable(stmt);
            populateNumericTable(connection);
            verifyNumericTable(connection, false);

            dropTables(stmt);
            createDateTable(stmt);
            populateDateTable(connection);
            verifyDateTable(connection);

            dropTables(stmt);
            createNumericTable(stmt);
            populateNumericTableWithNull(connection);
            verifyNumericTable(connection, true);

            dropTables(stmt);
        }
    }

    @Test
    public void alwaysEncrypted2() throws SQLException {
        try (Connection connection = PrepUtil.getConnection(
                connectionString + ";trustservercertificate=true;columnEncryptionSetting=enabled;", AEInfo);
                Statement stmt = connection.createStatement()) {
            dropTables(stmt);

            createCharTable(stmt);
            populateCharTable(connection);
            verifyCharTable(connection);

            dropTables(stmt);
            createDateTable(stmt);
            populateDateTable(connection);
            verifyDateTable(connection);

            dropTables(stmt);
            createNumericTable(stmt);
            populateNumericTableSpecificSetter(connection);
            verifyNumericTable(connection, false);

            dropTables(stmt);
        }
    }

    private void populateDateTable(Connection connection) throws SQLException {
        String sql = "insert into " + AbstractSQLGenerator.escapeIdentifier(Constants.DATE_TABLE_AE) + " values( " + "?"
                + ")";
        try (PreparedStatement sqlPstmt = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY, connection.getHoldability())) {
            sqlPstmt.setObject(1, Constants.DATE);
            sqlPstmt.executeUpdate();
        }
    }

    private void populateCharTable(Connection connection) throws SQLException {
        String sql = "insert into " + AbstractSQLGenerator.escapeIdentifier(Constants.CHAR_TABLE_AE) + " values( "
                + "?,?,?,?,?,?" + ")";
        try (PreparedStatement sqlPstmt = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY, connection.getHoldability())) {
            sqlPstmt.setObject(1, "hi");
            sqlPstmt.setObject(2, "sample");
            sqlPstmt.setObject(3, "hey");
            sqlPstmt.setObject(4, "test");
            sqlPstmt.setObject(5, "hello");
            sqlPstmt.setObject(6, "caching");
            sqlPstmt.executeUpdate();
        }
    }

    private void populateNumericTable(Connection connection) throws SQLException {
        String sql = "insert into " + AbstractSQLGenerator.escapeIdentifier(Constants.NUMERIC_TABLE_AE)
                + " values(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement sqlPstmt = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY, connection.getHoldability())) {
            sqlPstmt.setObject(1, true);
            sqlPstmt.setObject(2, false);
            sqlPstmt.setObject(3, true);
            Integer value = 255;
            sqlPstmt.setObject(4, value.shortValue(), JDBCType.TINYINT);
            sqlPstmt.setObject(5, value.shortValue(), JDBCType.TINYINT);
            sqlPstmt.setObject(6, value.shortValue(), JDBCType.TINYINT);
            sqlPstmt.setObject(7, Short.valueOf("127"), JDBCType.SMALLINT);
            sqlPstmt.setObject(8, Short.valueOf("127"), JDBCType.SMALLINT);
            sqlPstmt.setObject(9, Short.valueOf("127"), JDBCType.SMALLINT);
            sqlPstmt.executeUpdate();
        }
    }

    private void populateNumericTableSpecificSetter(Connection connection) throws SQLException {
        String sql = "insert into " + AbstractSQLGenerator.escapeIdentifier(Constants.NUMERIC_TABLE_AE) + " values( "
                + "?,?,?,?,?,?,?,?,?" + ")";
        try (PreparedStatement sqlPstmt = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY, connection.getHoldability())) {
            sqlPstmt.setBoolean(1, true);
            sqlPstmt.setBoolean(2, false);
            sqlPstmt.setBoolean(3, true);
            Integer value = 255;
            sqlPstmt.setShort(4, value.shortValue());
            sqlPstmt.setShort(5, value.shortValue());
            sqlPstmt.setShort(6, value.shortValue());
            sqlPstmt.setByte(7, Byte.valueOf("127"));
            sqlPstmt.setByte(8, Byte.valueOf("127"));
            sqlPstmt.setByte(9, Byte.valueOf("127"));
            sqlPstmt.executeUpdate();
        }
    }

    private void populateNumericTableWithNull(Connection connection) throws SQLException {
        String sql = "insert into " + AbstractSQLGenerator.escapeIdentifier(Constants.NUMERIC_TABLE_AE) + " values( "
                + "?,?,?" + ",?,?,?" + ",?,?,?" + ")";
        try (PreparedStatement sqlPstmt = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY, connection.getHoldability())) {
            sqlPstmt.setObject(1, null, java.sql.Types.BIT);
            sqlPstmt.setObject(2, null, java.sql.Types.BIT);
            sqlPstmt.setObject(3, null, java.sql.Types.BIT);
            sqlPstmt.setObject(4, null, java.sql.Types.TINYINT);
            sqlPstmt.setObject(5, null, java.sql.Types.TINYINT);
            sqlPstmt.setObject(6, null, java.sql.Types.TINYINT);
            sqlPstmt.setObject(7, null, java.sql.Types.SMALLINT);
            sqlPstmt.setObject(8, null, java.sql.Types.SMALLINT);
            sqlPstmt.setObject(9, null, java.sql.Types.SMALLINT);
            sqlPstmt.executeUpdate();
        }
    }

    private void verifyDateTable(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = stmt.executeQuery(
                        "select * from " + AbstractSQLGenerator.escapeIdentifier(Constants.DATE_TABLE_AE))) {
            while (rs.next()) {
                // VSTS BUG 5268
                // assertEquals(date.getTime(), ((Date) rs.getObject(1)).getTime());
            }
        }
    }

    private void verifyCharTable(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = stmt.executeQuery(
                        "select * from " + AbstractSQLGenerator.escapeIdentifier(Constants.CHAR_TABLE_AE))) {
            while (rs.next()) {
                assertEquals("hi                  ", rs.getObject(1));
                assertEquals("sample              ", rs.getObject(2));
                assertEquals("hey                 ", rs.getObject(3));
                assertEquals("test", rs.getObject(4));
                assertEquals("hello", rs.getObject(5));
                assertEquals("caching", rs.getObject(6));
            }
        }
    }

    private void verifyNumericTable(Connection connection, boolean isNull) throws SQLException {
        try (Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = stmt.executeQuery(
                        "select * from " + AbstractSQLGenerator.escapeIdentifier(Constants.NUMERIC_TABLE_AE))) {
            while (rs.next()) {
                if (isNull) {
                    assertEquals(null, rs.getObject(1));
                    assertEquals(null, rs.getObject(2));
                    assertEquals(null, rs.getObject(3));
                    assertEquals(null, rs.getObject(4));
                    assertEquals(null, rs.getObject(5));
                    assertEquals(null, rs.getObject(6));
                    assertEquals(null, rs.getObject(7));
                    assertEquals(null, rs.getObject(8));
                    assertEquals(null, rs.getObject(9));
                } else {
                    assertEquals(true, rs.getObject(1));
                    assertEquals(false, rs.getObject(2));
                    assertEquals(true, rs.getObject(3));
                    assertEquals((short) 255, rs.getObject(4));
                    assertEquals((short) 255, rs.getObject(5));
                    assertEquals((short) 255, rs.getObject(6));
                    assertEquals((short) 127, rs.getObject(7));
                    assertEquals((short) 127, rs.getObject(8));
                    assertEquals((short) 127, rs.getObject(9));
                }
            }
        }
    }

    private void createDateTable(Statement stmt) throws SQLException {
        String sql = "create table " + AbstractSQLGenerator.escapeIdentifier(Constants.DATE_TABLE_AE) + " ("
                + "RandomizedDate date ENCRYPTED WITH (ENCRYPTION_TYPE = RANDOMIZED, ALGORITHM = 'AEAD_AES_256_CBC_HMAC_SHA_256', COLUMN_ENCRYPTION_KEY = "
                + Constants.CEK_NAME + ") NULL," + ");";
        stmt.execute(sql);
    }

    private void createCharTable(Statement stmt) throws SQLException {
        String sql = "create table " + AbstractSQLGenerator.escapeIdentifier(Constants.CHAR_TABLE_AE) + " ("
                + "PlainChar char(20) null,"
                + "RandomizedChar char(20) COLLATE Latin1_General_BIN2 ENCRYPTED WITH (ENCRYPTION_TYPE = RANDOMIZED, ALGORITHM = 'AEAD_AES_256_CBC_HMAC_SHA_256', COLUMN_ENCRYPTION_KEY = "
                + Constants.CEK_NAME + ") NULL,"
                + "DeterministicChar char(20) COLLATE Latin1_General_BIN2 ENCRYPTED WITH (ENCRYPTION_TYPE = DETERMINISTIC, ALGORITHM = 'AEAD_AES_256_CBC_HMAC_SHA_256', COLUMN_ENCRYPTION_KEY = "
                + Constants.CEK_NAME + ") NULL," + "PlainVarchar varchar(50) null,"
                + "RandomizedVarchar varchar(50) COLLATE Latin1_General_BIN2 ENCRYPTED WITH (ENCRYPTION_TYPE = RANDOMIZED, ALGORITHM = 'AEAD_AES_256_CBC_HMAC_SHA_256', COLUMN_ENCRYPTION_KEY = "
                + Constants.CEK_NAME + ") NULL,"
                + "DeterministicVarchar varchar(50) COLLATE Latin1_General_BIN2 ENCRYPTED WITH (ENCRYPTION_TYPE = DETERMINISTIC, ALGORITHM = 'AEAD_AES_256_CBC_HMAC_SHA_256', COLUMN_ENCRYPTION_KEY = "
                + Constants.CEK_NAME + ") NULL," + ");";
        stmt.execute(sql);
    }

    private void createNumericTable(Statement stmt) throws SQLException {
        String sql = "create table " + AbstractSQLGenerator.escapeIdentifier(Constants.NUMERIC_TABLE_AE) + " ("
                + "PlainBit bit null,"
                + "RandomizedBit bit ENCRYPTED WITH (ENCRYPTION_TYPE = RANDOMIZED, ALGORITHM = 'AEAD_AES_256_CBC_HMAC_SHA_256', COLUMN_ENCRYPTION_KEY = "
                + Constants.CEK_NAME + ") NULL,"
                + "DeterministicBit bit ENCRYPTED WITH (ENCRYPTION_TYPE = DETERMINISTIC, ALGORITHM = 'AEAD_AES_256_CBC_HMAC_SHA_256', COLUMN_ENCRYPTION_KEY = "
                + Constants.CEK_NAME + ") NULL," + "PlainTinyint tinyint null,"
                + "RandomizedTinyint tinyint ENCRYPTED WITH (ENCRYPTION_TYPE = RANDOMIZED, ALGORITHM = 'AEAD_AES_256_CBC_HMAC_SHA_256', COLUMN_ENCRYPTION_KEY = "
                + Constants.CEK_NAME + ") NULL,"
                + "DeterministicTinyint tinyint ENCRYPTED WITH (ENCRYPTION_TYPE = DETERMINISTIC, ALGORITHM = 'AEAD_AES_256_CBC_HMAC_SHA_256', COLUMN_ENCRYPTION_KEY = "
                + Constants.CEK_NAME + ") NULL," + "PlainSmallint smallint null,"
                + "RandomizedSmallint smallint ENCRYPTED WITH (ENCRYPTION_TYPE = RANDOMIZED, ALGORITHM = 'AEAD_AES_256_CBC_HMAC_SHA_256', COLUMN_ENCRYPTION_KEY = "
                + Constants.CEK_NAME + ") NULL,"
                + "DeterministicSmallint smallint ENCRYPTED WITH (ENCRYPTION_TYPE = DETERMINISTIC, ALGORITHM = 'AEAD_AES_256_CBC_HMAC_SHA_256', COLUMN_ENCRYPTION_KEY = "
                + Constants.CEK_NAME + ") NULL," + ");";
        stmt.execute(sql);
    }

    public static void dropTables(Statement stmt) throws SQLException {
        TestUtils.dropTableIfExists(AbstractSQLGenerator.escapeIdentifier(Constants.DATE_TABLE_AE), stmt);
        TestUtils.dropTableIfExists(AbstractSQLGenerator.escapeIdentifier(Constants.CHAR_TABLE_AE), stmt);
        TestUtils.dropTableIfExists(AbstractSQLGenerator.escapeIdentifier(Constants.NUMERIC_TABLE_AE), stmt);
    }
}