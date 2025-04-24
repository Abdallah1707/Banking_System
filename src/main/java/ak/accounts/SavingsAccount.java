package ak.accounts;

public class SavingsAccount extends Account {
    private double interestRate = 2.5;

    public SavingsAccount(String customerId, String accountHolderName, double initialBalance, double interestRate,
            boolean activated) {
        super(customerId, accountHolderName, initialBalance, activated);
        this.interestRate = interestRate;
    }

    public SavingsAccount(String customerId, String accountHolderName, double initialBalance, double interestRate) {
        this(customerId, accountHolderName, initialBalance, interestRate, true);
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient funds in Savings Account");
        }
        balance -= amount;
    }

    public void addInterest() {
        double interest = getBalance() * (interestRate / 100);
        deposit(interest);
    }

    public double getInterestRate() {
        return interestRate;
    }
}