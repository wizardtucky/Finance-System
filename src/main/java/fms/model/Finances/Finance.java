package fms.model.Finances;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import fms.model.Categories.Category;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Finance implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    protected int id;

    @JsonIgnore
    @ManyToOne
    protected Category category;

    private double Money;
    private String name;
    protected Date dateCreated = null;

    public Finance(Double money, String name) {
        Money = money;
        this.name = name;
        dateCreated = new Date(System.currentTimeMillis());
    }

    public Finance() {}

    public Date getDateCreated() {
        return dateCreated;
    }
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public double getMoney() {
        return Money;
    }
    public void setMoney(double money) {
        Money = money;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Category getCategory() {return category;}

    public void setCategory(Category category) {this.category = category;}
  }
