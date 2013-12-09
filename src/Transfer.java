import javax.naming.InsufficientResourcesException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by artem on 09.12.13.
 */
public class Transfer implements Callable<Boolean> {

    private static final int WAIT_SEC = 1000; //timeout for thread
    private Account acc1, acc2;
    private int amount;

    public Transfer(Account acc1,  Account acc2, int amount) {
        this.acc1 = acc1;
        this.acc2 = acc2;
        this.amount = amount;

    }

    @Override
    public Boolean call() throws Exception  {

        if (acc1.getLock().tryLock(WAIT_SEC, TimeUnit.SECONDS)) {
            System.out.println("In block IF: acc1.getLock()" + "Thread id: " + Thread.currentThread().getId()); //debug
            try {
                if (acc1.getBalance() < amount)
                    throw new InsufficientResourcesException();
                if (acc2.getLock().tryLock(WAIT_SEC, TimeUnit.SECONDS)) {
                    System.out.println("In block IF: acc2.getLock()" + "Thread id: " + Thread.currentThread().getId()); //debug
                    try {
                        acc1.withdraw(1000);
                        System.out.println("acc1.withdraw" + "Thread id: " + Thread.currentThread().getId()); //debug
                        acc2.deposit(2000);
                        System.out.println("acc2.deposit" + "Thread id: " + Thread.currentThread().getId()); //debug
                    } finally {
                        //TODO
                    }
                }
            } finally {
                acc1.getLock().unlock();
                System.out.println("acc1.unlock" + "Thread id: " + Thread.currentThread().getId()); //debug
                acc2.getLock().unlock();
                System.out.println("acc2.unlock" + "Thread id: " + Thread.currentThread().getId()); //debug
                return true;
            }
        }else {
            acc1.incFailedTransferCount();
            acc2.incFailedTransferCount();
            return false;
        }
    }
}
