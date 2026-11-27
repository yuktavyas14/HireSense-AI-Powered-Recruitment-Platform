package com.hiresense;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

/**
 * Checks the Flyway migrations on the test database and the privileges around Flyway's history table.
 * Nothing here writes to flyway_schema_history: privileges are only asked about (has_table_privilege)
 * and the only real statement against the table is a SELECT.
 */
@SpringBootTest
class FlywayMigrationTest {

    private static final String HISTORY = "public.flyway_schema_history";
    private static final String APP_ROLE = "hiresense_app";
    private static final String OWNER_ROLE = "hiresense_owner";
    private static final String TEST_DB = "hiresense_test";

    @Autowired
    Flyway flyway; // migrates as hiresense_owner

    @Autowired
    DataSource appDataSource; // connection pool as hiresense_app

    @Value("${spring.flyway.url}")
    String ownerUrl;

    @Value("${spring.flyway.user}")
    String ownerUser;

    @Test
    void migrationsOneAndTwoAreAppliedSuccessfully() {
        MigrationInfo[] applied = flyway.info().applied();
        assertThat(applied).extracting(m -> m.getVersion().getVersion()).containsExactly("1", "2", "3", "4", "5", "6", "7", "8", "9");
        assertThat(applied).allMatch(m -> m.getState() == MigrationState.SUCCESS);
    }

    @Test
    void noMigrationIsPending() {
        assertThat(flyway.info().pending()).isEmpty();
    }

    @Test
    void noMigrationHasFailed() {
        assertThat(flyway.info().all()).noneMatch(m -> m.getState().isFailed());
    }

    @Test
    void ownerCanReadMigrationHistory() throws Exception {
        try (Connection c = ownerDataSource().getConnection()) {
            assertIdentity(c, OWNER_ROLE);
            try (Statement s = c.createStatement();
                    ResultSet r = s.executeQuery("SELECT count(*) FROM " + HISTORY)) {
                r.next();
                assertThat(r.getInt(1)).isGreaterThanOrEqualTo(2);
            }
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"SELECT", "INSERT", "UPDATE", "DELETE"})
    void ownerHasPrivilegeOnMigrationHistory(String privilege) throws Exception {
        try (Connection c = ownerDataSource().getConnection()) {
            assertIdentity(c, OWNER_ROLE);
            assertThat(hasPrivilege(c, OWNER_ROLE, privilege)).as("owner %s", privilege).isTrue();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"SELECT", "INSERT", "UPDATE", "DELETE"})
    void appRoleLacksPrivilegeOnMigrationHistory(String privilege) throws Exception {
        try (Connection c = appDataSource.getConnection()) {
            assertIdentity(c, APP_ROLE);
            assertThat(hasPrivilege(c, APP_ROLE, privilege)).as("app %s", privilege).isFalse();
        }
    }

    @Test
    void appRoleSelectOnMigrationHistoryFailsWithInsufficientPrivilege() throws Exception {
        try (Connection c = appDataSource.getConnection()) {
            assertIdentity(c, APP_ROLE);
            try (Statement s = c.createStatement()) {
                s.executeQuery("SELECT 1 FROM " + HISTORY); // the only real statement; a read
                fail("the application role must not be able to read " + HISTORY);
            } catch (SQLException e) {
                assertThat(e.getSQLState()).isEqualTo("42501"); // insufficient_privilege
            }
        }
    }

    /** User name and NO password: the PostgreSQL JDBC driver finds it in pgpass.conf. */
    private DataSource ownerDataSource() {
        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriverClass(org.postgresql.Driver.class);
        ds.setUrl(ownerUrl);
        ds.setUsername(ownerUser);
        return ds;
    }

    private static void assertIdentity(Connection c, String expectedRole) throws SQLException {
        try (Statement s = c.createStatement();
                ResultSet r = s.executeQuery("SELECT current_user, current_database()")) {
            r.next();
            assertThat(r.getString(1)).as("connected role").isEqualTo(expectedRole);
            assertThat(r.getString(2)).as("connected database").isEqualTo(TEST_DB);
        }
    }

    private static boolean hasPrivilege(Connection c, String role, String privilege) throws SQLException {
        try (PreparedStatement p = c.prepareStatement("SELECT has_table_privilege(?, ?, ?)")) {
            p.setString(1, role);
            p.setString(2, HISTORY);
            p.setString(3, privilege);
            try (ResultSet r = p.executeQuery()) {
                r.next();
                return r.getBoolean(1);
            }
        }
    }
}
