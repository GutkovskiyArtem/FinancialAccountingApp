package artem.gutkovskiy.FinancialAccounting;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExpensesController {

    @GetMapping("/expenses-by-category")
    public String getExpensesByCategory(
            @RequestParam("year") int year,
            @RequestParam("month") int month
    ) {
        // Жестко заданный результат для примера
        String result = "{ \"year\": " + year + ", \"month\": " + month + ", \"expenses\": ["
                + "{ \"category\": \"Food\", \"amount\": 500 },"
                + "{ \"category\": \"Transportation\", \"amount\": 200 },"
                + "{ \"category\": \"Entertainment\", \"amount\": 300 }"
                + "] }";
        return result;
    }
}
