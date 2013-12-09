import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by artem on 09.12.13.
 */
public class Operations2 {

    public static void main(String args[]){

        Random rnd = new Random();

        ExecutorService service = Executors.newFixedThreadPool(4);

        for(int i = 0; i < 10; i++) {
            service.submit(
                new Transfer(new Account(1000), new Account(2000), rnd.nextInt(400))
            );
        }
        service.shutdown();
    }
}
