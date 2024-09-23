package artem.gutkovskiy.financialaccounting.counter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/counter")
public class CounterController {

    private final RequestCounterService requestCounterService;

    @Autowired
    public CounterController(RequestCounterService requestCounterService) {
        this.requestCounterService = requestCounterService;
    }

    @GetMapping
    public ResponseEntity<Long> getCounter() {
        long count = requestCounterService.getCounter();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
