package ak;

import ak.customer.Customer;
import ak.loans.Loan;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoanTest {

    /* -------------------------------------------------
       1. Happy‑path construction
       ------------------------------------------------- */
    @Test
    void validLoanCreation() {
        Customer cust = new Customer("123", "Nour", "nour@example.com", "1234567890");
        Loan loan = new Loan("LOAN123", cust.getCustomerId(),
                             "ACC123", 10_000, 0.05, 12);

        assertAll(
            () -> assertEquals(cust.getCustomerId(), loan.getCustomerId()),
            () -> assertEquals(10_000, loan.getLoanAmount(), 0.01),
            () -> assertEquals(0.05,   loan.getInterestRate(), 0.00001),
            () -> assertEquals(12,     loan.getDurationInMonths())
        );
    }

    /* -------------------------------------------------
       2. Validation failures in constructor
       ------------------------------------------------- */
    @Test
    void negativeAmountThrowsIllegalArgument() {
        Customer cust = new Customer("123", "Nour", "nour@example.com", "1234567890");

        assertThrows(IllegalArgumentException.class,
            () -> new Loan("LOAN123", cust.getCustomerId(),
                           "ACC123", -5_000, 0.05, 12));
    }

    @Test
    void nullCustomerIdThrowsIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
            () -> new Loan("LOAN123", null,
                           "ACC123", 5_000, 0.05, 12));
    }
}
