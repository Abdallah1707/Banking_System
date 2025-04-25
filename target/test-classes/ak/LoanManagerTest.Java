package ak;

import ak.database.DBconnection;
import ak.loans.Loan;
import ak.loans.LoanManager;
import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class LoanManagerTest {

    private static Connection h2;
    private LoanManager loanMgr;

    @BeforeAll
    static void init() throws Exception {
        h2 = DriverManager.getConnection("jdbc:h2:mem:loans;MODE=PostgreSQL");
        DBconnection.setTestConnection(h2);
        try (Statement st = h2.createStatement()) {
            st.execute("""
                CREATE TABLE loans(
                  loan_id VARCHAR(20) PRIMARY KEY,
                  customer_id VARCHAR(20),
                  account_number VARCHAR(20),
                  loan_amount DECIMAL(15,2),
                  interest_rate DECIMAL(5,2),
                  duration_months INT,
                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);
        }
    }

    @AfterAll
    static void close() throws Exception { h2.close(); }

    @BeforeEach
    void setUp() { loanMgr = new LoanManager(); }

    @AfterEach
    void clear() throws Exception {
        try (Statement st = h2.createStatement()) { st.execute("TRUNCATE TABLE loans"); }
    }

    @Test
    void createLoan_persists() {
        Loan l = loanMgr.createLoan("C1","A1",1_000,5,12);
        assertNotNull(l);
        try (ResultSet rs = h2.createStatement()
                              .executeQuery("SELECT COUNT(*) FROM loans")) {
            rs.next(); assertEquals(1, rs.getInt(1));
        }
    }

    @Test
    void validationFails() {
        assertThrows(IllegalArgumentException.class,
            () -> loanMgr.createLoan("C1","A1",-1,5,12));
    }

    @Test
    void removeLoan() {
        Loan l = loanMgr.createLoan("C9","A9",500,4,6);
        assertTrue(loanMgr.removeLoan(l.getLoanId()));
        assertNull(loanMgr.getLoanById(l.getLoanId()));
    }
}
