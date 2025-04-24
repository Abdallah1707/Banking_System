package ak;

import ak.accounts.*;
import ak.customer.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer("123", "Jane Doe", "jane.doe@example.com", "1234567890");
    }

    /*
     * -------------------------------------------------
     * 1. Customer creation (constructor has no guards)
     * -------------------------------------------------
     */
    @Test
    void validCustomerCreation() {
        assertEquals("123", customer.getCustomerId());
        assertEquals("Jane Doe", customer.getName());
        assertNotNull(customer.getAccounts());
        assertTrue(customer.getAccounts().isEmpty());
    }

    @Test
    void allowsNullNameAndEmptyIdAccordingToCurrentCode() {
        // Constructor no longer throws; verify object still created
        Customer c1 = new Customer("456", null, "x@y.com", "000");
        assertNull(c1.getName());

        Customer c2 = new Customer("", "John", "j@d.com", "111");
        assertEquals("", c2.getCustomerId());
    }

    /*
     * -------------------------------------------------
     * 2. Account association
     * -------------------------------------------------
     */
    @Test
    void addSingleAccount() {
        Account acc = new SavingsAccount("123", "Jane Doe", 1_000, 2.5, true);
        customer.addAccount(acc);

        List<Account> accounts = customer.getAccounts();
        assertEquals(1, accounts.size());
        assertSame(acc, accounts.get(0));
    }

    @Test
    void addMultipleAccounts() {
        Account a1 = new CheckingAccount("123", "Jane", 500, 500, "ACC124", true);
        Account a2 = new SavingsAccount("123", "Jane", 1_500, 2.5);

        customer.addAccount(a1);
        customer.addAccount(a2);

        List<Account> list = customer.getAccounts();
        assertEquals(2, list.size());
        assertTrue(list.containsAll(List.of(a1, a2)));
    }

    /*
     * -------------------------------------------------
     * 3. Guard: adding null account
     * -------------------------------------------------
     */
    @Test
    void addNullAccountThrows() {
        Customer customer = new Customer("C1", "John", "j@d.com", "123", "u", "p");
        assertThrows(NullPointerException.class, () -> customer.addAccount(null));
    }
}