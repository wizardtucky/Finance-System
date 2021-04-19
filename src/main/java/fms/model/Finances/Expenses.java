package fms.model.Finances;

import fms.model.Categories.Category;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Expenses extends Finance {
    public Expenses(Double money, String name) {
        super(money, name);
    }

    public Expenses() {}
}
