package ak;

import ak.admins.Admin;
import ak.admins.AdminManager;
import ak.database.DBconnection;
import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class AdminManagerTest {

    private static Connection h2;
    private AdminManager adminMgr;

    @BeforeAll
    static void initDb() throws Exception {
        // Set up H2 in-memory database
        h2 = DriverManager.getConnection("jdbc:h2:mem:admindb;MODE=PostgreSQL");
        DBconnection.setTestConnection(h2); // Use H2 for testing

        try (Statement st = h2.createStatement()) {
            st.execute("""
                        CREATE TABLE IF NOT EXISTS admins(
                          admin_id      VARCHAR(50) PRIMARY KEY,
                          name          VARCHAR(50),
                          username      VARCHAR(50) UNIQUE,
                          password_hash VARCHAR(100)
                        )
                    """);
            st.execute("""
                        CREATE TABLE IF NOT EXISTS customers(
                          customer_id VARCHAR(50) PRIMARY KEY,
                          name VARCHAR(100),
                          email VARCHAR(100),
                          phone_number VARCHAR(20),
                          username VARCHAR(50),
                          password_hash VARCHAR(100)
                        )
                    """);
            st.execute("""
                        CREATE TABLE IF NOT EXISTS accounts(
                          account_number VARCHAR(50) PRIMARY KEY,
                          customer_id VARCHAR(50),
                          holder_name VARCHAR(100),
                          balance DECIMAL(15,2),
                          account_type VARCHAR(20),
                          interest_rate DECIMAL(5,2),
                          overdraft_limit DECIMAL(15,2),
                          is_active BOOLEAN
                        )
                    """);
        }
    }

    @AfterAll
    static void close() throws Exception {
        if (h2 != null && !h2.isClosed()) {
            h2.close();
        }
    }

    @BeforeEach
    void setUp() throws SQLException {
        DBconnection.clearDatabase(); // Clear database before each test
        adminMgr = new AdminManager();
        adminMgr.addAdmin("Test Admin", "testAdminUsername", "testPasswordHash");
    }

    @AfterEach
    void clean() throws SQLException {
        DBconnection.clearDatabase(); // Clear database after each test
    }

    @Test
    void authenticateAdmin() {
        Admin a = adminMgr.authenticateAdmin("testAdminUsername", "testPasswordHash");
        assertNotNull(a);
    }

    @Test
    void addAdmin() {
        assertTrue(adminMgr.addAdmin("New", "newU", "newP"));
    }

    @Test
    void getAdminByUsername() {
        assertNotNull(adminMgr.getAdminByUsername("testAdminUsername"));
    }
}
