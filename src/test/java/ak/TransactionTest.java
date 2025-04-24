package ak;

import ak.accounts.*;
import ak.transactions.Transaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    /* -------------------------------------------------
       1. Constructor happy‑path
       ------------------------------------------------- */
    @Test
    void validTransactionCreation() {
        Account src  = new SavingsAccount("C1","John",1_000,2.5);
        Account dest = new SavingsAccount("C2","Jane",  500,2.5);

        // supply timestamp string so we can assert format
        String ts = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                     .format(LocalDateTime.now());

        Transaction tr = new Transaction(200,"Transfer",
                                         src.getAccountNumber(),
                                         dest.getAccountNumber(),
                                         ts);

        assertAll(
            () -> assertEquals(200, tr.getAmount(), 0.0001),
            () -> assertEquals("Transfer", tr.getType()),
            () -> assertEquals(src.getAccountNumber(),  tr.getFromAccount()),
            () -> assertEquals(dest.getAccountNumber(), tr.getToAccount()),
            () -> assertEquals(ts, tr.getTimestamp())
        );
    }

    /* -------------------------------------------------
       2. Constructor allows negative / zero amounts etc.
          Validation belongs to TransactionManager, so
          we simply assert no exception is thrown.
       ------------------------------------------------- */
    @Test
    void constructorDoesNotValidateAmountTypeOrAccounts() {
        assertDoesNotThrow(() -> new Transaction(
            -50, "", null, null, null));
    }
}
