package artem.gutkovskiy.financialaccounting.counter;

import org.springframework.stereotype.Service;

@Service
public class RequestCounterService {
    public long getCounter() {
        return RequestCounter.getInstance().getCount();
    }
}
