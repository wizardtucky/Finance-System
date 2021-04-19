package fms.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    protected String name;
    protected String password;
    protected int accessLevel;

    public FinanceManagementSystem getFms() {
        return fms;
    }

    public void setFms(FinanceManagementSystem fms) {
        this.fms = fms;
    }

    @JsonIgnore
    @ManyToOne
    protected FinanceManagementSystem fms;

    public User(String name, String password, int accessLevel) {
        this.name = name;
        this.password = password;
        this.accessLevel = accessLevel;
    }

    public User() {}

    public void setId(int id) {this.id = id;}

    public int getId() {return id;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }

}

