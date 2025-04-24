package ak.accounts;

public class CheckingAccount extends Account {
    private double overdraftLimit = 500;

    public CheckingAccount(String customerId, String accountHolderName, double initialBalance, double overdraftLimit,
            boolean activated) {
        super(customerId, accountHolderName, initialBalance, activated);
        this.overdraftLimit = overdraftLimit;
    }

    public CheckingAccount(String customerId, String accountHolderName, double initialBalance, double overdraftLimit,
            String accountNumber, boolean activated) {
        super(customerId, accountHolderName, initialBalance, accountNumber, activated);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (amount > balance + overdraftLimit) {
            throw new IllegalArgumentException("Withdrawal amount exceeds available balance and overdraft limit");
        }
        balance -= amount;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }
}