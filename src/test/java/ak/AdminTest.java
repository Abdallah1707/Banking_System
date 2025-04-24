package ak;

import ak.admins.Admin;
import ak.customer.Customer;
import ak.accounts.Account;
import ak.accounts.AccountManager;
import ak.customer.CustomerManager;
import ak.database.DBconnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class AdminTest {
    private Admin admin;
    private CustomerManager customerManager;
    private AccountManager accountManager;
    private Connection testConnection;

    @BeforeEach
    public void setUp() throws SQLException {
        // Set up test database connection
        testConnection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        DBconnection.setTestConnection(testConnection);

        // Create necessary tables
        try (Statement stmt = testConnection.createStatement()) {
            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS customers (
                            customer_id VARCHAR(50) PRIMARY KEY,
                            name VARCHAR(100),
                            email VARCHAR(100),
                            phone_number VARCHAR(20),
                            username VARCHAR(50),
                            password_hash VARCHAR(100)
                        )
                    """);
            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS accounts (
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

        DBconnection.clearDatabase(); // Clear the database before each test

        customerManager = new CustomerManager();
        accountManager = new AccountManager();
        admin = new Admin("adminId", "Admin Name", "adminUsername", "passwordHash");
        admin.setCustomerManager(customerManager);
        admin.setAccountManager(accountManager);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (testConnection != null) {
            testConnection.close();
        }
        DBconnection.setTestConnection(null);
    }

    @Test
    public void testGetAdminId() {
        assertEquals("adminId", admin.getAdminId());
    }

    @Test
    public void testGetName() {
        assertEquals("Admin Name", admin.getName());
    }

    @Test
    public void testGetUsername() {
        assertEquals("adminUsername", admin.getUsername());
    }

    @Test
    public void testGetPasswordHash() {
        assertEquals("passwordHash", admin.getPasswordHash());
    }

    @Test
    public void testGetAllCustomers() {
        List<Customer> customers = admin.getAllCustomers();
        assertNotNull(customers);
    }

    @Test
    public void testGetAllAccounts() {
        List<Account> accounts = admin.getAllAccounts();
        assertNotNull(accounts);
    }

    @Test
    public void testDeleteCustomer() {
        boolean result = admin.deleteCustomer("customerId");
        assertTrue(result);
    }

    @Test
    public void testDeleteAccount() {
        boolean result = admin.deleteAccount("accountNumber");
        assertTrue(result);
    }

    @Test
    public void testAddCustomer() {
        Customer customer = admin.addCustomer("Name", "email@example.com", "1234567890", "username", "passwordHash");
        assertNotNull(customer);
    }

    @Test
    public void testAddSavingsAccount() {
        Account account = admin.addSavingsAccount("customerId", "holderName", 1000.0, 0.05);
        assertNotNull(account);
    }

    @Test
    public void testAddCheckingAccount() {
        Account account = admin.addCheckingAccount("customerId", "holderName", 500.0, 100.0);
        assertNotNull(account);
    }
}