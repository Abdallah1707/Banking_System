package ak;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import ak.loans.LoanRequestManager;
import ak.loans.LoanRequest;
import ak.loans.LoanManager;
import ak.loans.LoanDetails;
import ak.accounts.AccountManager;
import ak.accounts.CheckingAccount;
import ak.accounts.Account;
import ak.customer.Customer;
import ak.customer.CustomerManager;

public class LoanApprovalIntegration_TC3_2Test {

    String holderName = "user5";
    double overdraft_limit = 1000;
    double initialDeposit = 0;

    @Test
    @DisplayName("TC3.2 - Submit Loan Request → Approve → Generate Loan")
    void testLoanApprovalFlow() {
        // LoanRequest request = new LoanRequest(
        //     "LR1001",
        //     "ACC1001",
        //     10000.0,
        //     "Home Renovation",
        //     "Pending"
        // );

        // LoanRequestManager requestManager = new LoanRequestManager();
        
        // requestManager.submitLoanRequest(null, request); // Assume null or a stub customer
        
        Customer customer = new Customer("Ali Test", "ali@example.com", "01012345678");
        AccountManager accountManager = new AccountManager();
        Account account = accountManager.createCheckingAccount(customer.getCustomerId(), holderName, initialDeposit, overdraft_limit);
        
        // Create loan request
        LoanRequest request = new LoanRequest(
            "LR2001",
            account.getAccountNumber(),
            10000.0,
            "Business Expansion",
            "Pending"
            );
            
            // Submit loan request
            
            LoanRequestManager requestManager = new LoanRequestManager();
            boolean submitted = requestManager.submitLoanRequest(request.getAccountNumber(),request.getLoanAmount(),request.getLoanReason() );
            
            LoanManager loanManager = new LoanManager();
            LoanDetails details = loanManager.createLoan();
            
            assertNotNull(details);
        assertEquals("LR1001", details.getRequestId());
        assertEquals(10000.0, details.getPrincipal(), 0.001);
    }
}
