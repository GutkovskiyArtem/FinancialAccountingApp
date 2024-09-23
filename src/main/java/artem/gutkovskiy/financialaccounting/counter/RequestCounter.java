package artem.gutkovskiy.financialaccounting.counter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestCounter {
    private static final Logger logger = LoggerFactory.getLogger(
            RequestCounter.class);
    private static RequestCounter instance;
    private long count;

    private RequestCounter() {
        this.count = 0;
    }

    public static synchronized RequestCounter getInstance() {
        if (instance == null) {
            instance = new RequestCounter();
        }
        return instance;
    }

    public synchronized void increment() {
        count++;
        logger.info("Request count incremented: {}", count);
    }

    public synchronized long getCount() {
        return count;
    }
}
