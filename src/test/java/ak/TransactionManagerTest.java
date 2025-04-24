package ak;

import ak.accounts.*;
import ak.customer.Customer;
import ak.customer.CustomerManager;
import ak.database.DBconnection;
import ak.transactions.Transaction;
import ak.transactions.TransactionManager;
import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class TransactionManagerTest {

    private static Connection h2;
    private AccountManager accountMgr;
    private TransactionManager txMgr;
    private Account src, dst;

    @BeforeAll
    static void initDb() throws Exception {
        h2 = DriverManager.getConnection("jdbc:h2:mem:txdb;MODE=PostgreSQL");
        DBconnection.setTestConnection(h2);
        try (Statement st = h2.createStatement()) {
            st.execute("""
                        CREATE TABLE customers(
                          customer_id VARCHAR(20) PRIMARY KEY,
                          name VARCHAR(100),
                          email VARCHAR(100),
                          phone_number VARCHAR(20),
                          username VARCHAR(50),
                          password_hash VARCHAR(100)
                        );
                    """);
            st.execute("""
                        CREATE TABLE accounts(
                          account_number VARCHAR(20) PRIMARY KEY,
                          customer_id VARCHAR(20),
                          account_holder_name VARCHAR(100),
                          balance DECIMAL(15,2),
                          account_type VARCHAR(20),
                          interest_rate DECIMAL(5,2),
                          overdraft_limit DECIMAL(15,2),
                          activated BOOLEAN
                        );
                    """);
            st.execute("""
                        CREATE TABLE transactions(
                          transaction_id VARCHAR(20) PRIMARY KEY,
                          amount DECIMAL(15,2),
                          type VARCHAR(50),
                          from_account VARCHAR(20),
                          to_account VARCHAR(20),
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                        );
                    """);
        }
    }

    @AfterAll
    static void close() throws Exception {
        h2.close();
    }

    @BeforeEach
    void setUp() throws SQLException {
        DBconnection.clearDatabase();
        accountMgr = new AccountManager();
        txMgr = new TransactionManager(accountMgr);

        CustomerManager customerManager = new CustomerManager();
        Customer c = customerManager.addCustomer("John", "j@d.com", "123", "u", "p");
        src = new SavingsAccount(c.getCustomerId(), "John", 1_000, 2.5, true);
        dst = new SavingsAccount(c.getCustomerId(), "Jane", 500, 2.5, true);
        accountMgr.createAccount(src);
        accountMgr.createAccount(dst);
    }

    @AfterEach
    void clear() throws Exception {
        try (Statement st = h2.createStatement()) {
            st.execute("TRUNCATE TABLE accounts");
            st.execute("TRUNCATE TABLE transactions");
        }
    }

    @Test
    void validTransaction() {
        Transaction t = txMgr.createTransaction(200, "TRANSFER", src.getAccountNumber(), dst.getAccountNumber());
        assertNotNull(t);
        assertEquals(800, src.getBalance(), 0.0001);
        assertEquals(700, dst.getBalance(), 0.0001);
    }

    @Test
    void insufficientFunds() {
        assertThrows(IllegalArgumentException.class,
                () -> txMgr.createTransaction(2_000, "TRANSFER", src.getAccountNumber(), dst.getAccountNumber()));
    }

    @Test
    void negativeAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> txMgr.createTransaction(-1, "TRANSFER", src.getAccountNumber(), dst.getAccountNumber()));
    }
}