package artem.gutkovskiy.financialaccounting.dto;

public class RequestObject {
    private String description;
    private double amount;
    private Long userId;

    public RequestObject(String description, double amount, Long userId) {
        this.description = description;
        this.amount = amount;
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
