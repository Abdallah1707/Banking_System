package ak;

import ak.accounts.*;
import ak.transactions.Transaction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionWhiteBoxTest {

    /* -------------------------------------------------
       1. Transaction ID format
       ------------------------------------------------- */
    @Test
    void generateTransactionId_hasCorrectPrefixAndLength() {
        Transaction tx = new Transaction(1,"X","A","B",null);

        assertTrue(tx.getTransactionId().startsWith("TXN-"));
        assertEquals(12, tx.getTransactionId().length()); // "TXN-" + 8 chars
    }

    /* -------------------------------------------------
       2. Timestamp remains null when constructor arg is null
       ------------------------------------------------- */
    @Test
    void timestampRemainsNullIfNotProvided() {
        Transaction tx = new Transaction(1,"X","A","B",null);
        assertNull(tx.getTimestamp());
    }
}
