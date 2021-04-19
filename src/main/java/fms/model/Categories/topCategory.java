package fms.model.Categories;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fms.model.FinanceManagementSystem;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
public class topCategory extends Category implements Serializable {

    public topCategory(String name, String description) {
        super(name, description);
    }

    public topCategory() {}

    @JsonIgnore
    @ManyToOne
    private FinanceManagementSystem fms;

    public FinanceManagementSystem getFms() {
        return fms;
    }
    public void setFms(FinanceManagementSystem fms) {
        this.fms = fms;
    }
}
