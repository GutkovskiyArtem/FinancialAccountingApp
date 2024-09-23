package artem.gutkovskiy.financialaccounting.controllers;

import artem.gutkovskiy.financialaccounting.counter.RequestCounter;
import artem.gutkovskiy.financialaccounting.dto.RequestObject;
import artem.gutkovskiy.financialaccounting.dto.ResponseObject;
import artem.gutkovskiy.financialaccounting.service.BulkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bulk")
public class BulkController {

    private final BulkService bulkService;

    @Autowired
    public BulkController(BulkService bulkService) {
        this.bulkService = bulkService;
    }

    @PostMapping("/expenses")
    public ResponseEntity<List<ResponseObject>> createBulkExpenses(
            @RequestBody List<RequestObject> requestObjects) {
        List<ResponseObject> responseObjects = bulkService.processExpenses(
                requestObjects);
        RequestCounter.getInstance().increment();
        return ResponseEntity.ok(responseObjects);
    }
}
