import javax.naming.InsufficientResourcesException;
import java.util.concurrent.TimeUnit;

/**
 * Created by artem on 06.12.13.
 */
public class Operations{

    private static final int WAIT_SEC = 1000; //timeout for thread

    public static void main(String args[]) throws InsufficientResourcesException {
        final Account a = new Account(1000);
        final Account b = new Account(2000);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    transfer(a, b, 500);
                    System.out.println("Transfer a -> successful"); //debug out
                } catch (InsufficientResourcesException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        try {
            transfer(b, a, 300);
            System.out.println("Transfer b -> successful"); //debug out
        } catch (InsufficientResourcesException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void transfer(Account acc1, Account acc2, int amount)
            throws InsufficientResourcesException, InterruptedException {

        if (acc1.getLock().tryLock(WAIT_SEC, TimeUnit.SECONDS)) {
            try {

                if (acc1.getBalance() < amount)
                    throw new InsufficientResourcesException();
                if (acc2.getLock().tryLock(WAIT_SEC, TimeUnit.SECONDS)) {
                    try {
                        acc1.withdraw(1000);
                        acc2.deposit(2000);
                    } finally {
                        //TODO
                    }
                }
            } finally {
                acc1.getLock().unlock();
                acc2.getLock().unlock();
            }
        }else {
            acc1.incFailedTransferCount();
            acc2.incFailedTransferCount();
        }
    }
}
