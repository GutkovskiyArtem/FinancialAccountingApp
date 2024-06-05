package artem.gutkovskiy.financialaccounting.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private double amount;
    private String userName;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private artem.gutkovskiy.financialaccounting.entity.user user;

    // Constructors, getters, and setters
    public expense() {}

    public expense(String description, double amount, artem.gutkovskiy.financialaccounting.entity.user user) {
        this.description = description;
        this.amount = amount;
        this.user = user;
        this.userName = user.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public artem.gutkovskiy.financialaccounting.entity.user getUser() {
        return user;
    }

    public void setUser(artem.gutkovskiy.financialaccounting.entity.user user) {
        this.user = user;
        if (user != null) {
            this.userName = user.getName();
        }
    }

}
