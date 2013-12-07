import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by artem on 06.12.13.
 */
public class Account extends ReentrantLock{
    private int balance;
    private Lock lock;
    /*
    use AtomicInteger for making increment atomic operation(thread safe with use volatile)
     */
    private AtomicInteger failCounter = new AtomicInteger(0);

    public Lock getLock() {
        return lock;
    }

    public Account(int InitialBalance) {
        this.balance = InitialBalance;
        lock = new ReentrantLock();
    }

    public void withdraw(int amount) {
        balance -= amount;
    }

    public void deposit(int amount) {
        balance += amount;
    }

    public int getBalance() {
        return balance;
    }

    public void incFailedTransferCount() {
        failCounter.incrementAndGet();
    }

    public AtomicInteger getFailcounter() {
        return failCounter;
    }
}
