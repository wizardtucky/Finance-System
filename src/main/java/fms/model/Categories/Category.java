package fms.model.Categories;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fms.model.Finances.Expenses;
import fms.model.Finances.Income;
import fms.HibernateControl.CategoriesHibernateControl;
import fms.HibernateControl.ExpenseHibernateControl;
import fms.HibernateControl.IncomeHibernateControl;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    protected int id;

    protected String name;
    protected String description;

    @JsonIgnore
    @OneToMany(mappedBy = "parentCategory", cascade= CascadeType.ALL, orphanRemoval=true)
    @OrderBy("id ASC")
    @LazyCollection(LazyCollectionOption.FALSE)
    List<subCategory> subCategories = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "category", cascade= CascadeType.ALL, orphanRemoval=true)
    @OrderBy("id ASC")
    @LazyCollection(LazyCollectionOption.FALSE)
    List<Income> Income = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "category", cascade= CascadeType.ALL, orphanRemoval=true)
    @OrderBy("id ASC")
    @LazyCollection(LazyCollectionOption.FALSE)
    List<Expenses> Expenses = new ArrayList<>();

    static EntityManagerFactory factory = Persistence.createEntityManagerFactory("FMSHibernate");
    static CategoriesHibernateControl categoriesHibernateControl = new CategoriesHibernateControl(factory);
    static IncomeHibernateControl incomeHibernateControl = new IncomeHibernateControl(factory);
    static ExpenseHibernateControl expenseHibernateControl = new ExpenseHibernateControl(factory);

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Category() {}

    public void setId(int id) {this.id = id;}

    public int getId() {
        return id;
    }

    public int getCatId(){
        return id;
    }
    public List<subCategory> getSubCategories() {
        return subCategories;
    }

    public void removeSubCategory(Category subCat) {
        this.subCategories.remove(subCat);
    }
    public void removeIncomeFromList(fms.model.Finances.Income income) {
        this.Income.remove(income);
    }

    public void removeExpenseFromList(fms.model.Finances.Expenses expense) {
        this.Expenses.remove(expense);
    }

    public void setExpensesFromDb(){
        this.Expenses = expenseHibernateControl.findExpenseEntities();
    }

    public void setIncomeFromDb(){
        this.Income = incomeHibernateControl.findIncomeEntities();
    }

//    @JsonIgnore
    public List<Income> getThisIncomeList() {
        return Income;
    }
//    @JsonIgnore
    public List<Expenses> getThisExpenseList() {
        return Expenses;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory(String ID, Category category) {
        for (subCategory cat : subCategories) { // else continuing recursion
            if(ID.equals(String.valueOf(cat.getId()))) {
                category = cat;
                return category;
            }
            else
                category = cat.getCategory(ID, category);
        }
        return category;
    }
    public List<Income> getIncomeListById(String ID, List<Income> income) {
        for (subCategory cat : subCategories) { // else continuing recursion
            if(ID.equals(String.valueOf(cat.getCatId()))) {
                income = cat.Income;
                return income;
            }
            else
                income = cat.getIncomeListById(ID, income);
        }
        return income;
    }
    public List<Expenses> getExpenseListById(String ID, List<Expenses> expense) {
        for (subCategory cat : subCategories) { // else continuing recursion
            if(ID.equals(String.valueOf(cat.getCatId()))) {
                expense = cat.Expenses;
                return expense;
            }
            else
                expense = cat.getExpenseListById(ID, expense);
        }
        return expense;
    }

    public void addSubCategory(String id, subCategory newSubCategory, Category parent) throws Exception {
        if (String.valueOf(this.id).equals(id)) {
            newSubCategory.setParentCategory(this); // set parent category
            subCategories.add(newSubCategory);
            categoriesHibernateControl.createTopCategory(newSubCategory);
            System.out.println("New Category added successfully");
        } else {
            for (subCategory cat : subCategories) { // else continuing recursion
                cat.addSubCategory(id, newSubCategory, cat);
            }
        }
    }

    public void editNameOfSelCatSub(String id, String newName) throws Exception {
        if(id.equals(String.valueOf(getId()))){
            name = newName;
            return;
        } else  {
            for (subCategory cat : subCategories){
                if (id.equals(String.valueOf(cat.getId()))){//
                    cat.setName(newName);
                    categoriesHibernateControl.editTopCategory(cat);
                    break;
                } else {
                    cat.editNameOfSelCatSub(id, newName);
                }
            }
        }
    }

    public void editDescriptionOfSelCatSub(String id, String newDis) throws Exception {
        for (subCategory cat : subCategories){
            if (id.equals(String.valueOf(cat.getId()))){
                cat.setDescription(newDis);
                categoriesHibernateControl.editTopCategory(cat);
                break;
            } else {
                cat.editNameOfSelCatSub(id, newDis);
            }
        }
    }

    public void removeCategory(String id) throws Exception {
        for (subCategory cat : subCategories) {
            if (id.equals(String.valueOf(cat.getCatId()))) {
                categoriesHibernateControl.removeSubCategory(this, cat);
                break;
            } else {
                cat.removeCategory(id);
            }
        }
    }

    public boolean chekIfIdExistsSub(String id, boolean existance) {
        if (existance)
            return true;
        for (subCategory cat : subCategories) {
            if (id.equals(String.valueOf(cat.getId()))) {
                return true;
            } else {
                existance = cat.chekIfIdExistsSub(id, existance);
                if (existance)
                    return true;
            }
        }
        return false;
    }
    // SELECTED CAT FUNCTIONS
    public void addIncome(Income newIncome, String id) throws Exception {
        if (id.equals(String.valueOf(getId()))){
            newIncome.setCategory(this);
            Income.add(newIncome);
            incomeHibernateControl.create(newIncome);
        } else {
            for (subCategory cat : subCategories){
                cat.addIncome(newIncome, id);
            }
        }
    }
    public void addExpense(Expenses newExp, String id) throws Exception {
        if (id.equals(String.valueOf(getId()))){
            newExp.setCategory(this);
            Expenses.add(newExp);
            expenseHibernateControl.create(newExp);
        } else {
            for (subCategory cat : subCategories){
                cat.addExpense(newExp, id);
            }
        }
    }

    public void editNameOfExpense(String catId, String expenseId, String newName) throws Exception {
        String tempId = String.valueOf(id);
        if (tempId.equals(catId)) {
            for (Expenses statement : Expenses) {
                tempId = String.valueOf(statement.getId());
                if (tempId.equals(expenseId) && !newName.isEmpty()) {
                    statement.setName(newName);
                    expenseHibernateControl.edit(statement);
                    break;
                }
            }
        } else {
            for (subCategory cat : subCategories)
                cat.editNameOfIncome(catId, expenseId, newName);
        }
    }
    public void editAmountOfExpense(String catId, String expenseId, String amount) throws Exception {
        String tempId = String.valueOf(id);
        if (tempId.equals(catId)) {
            for (Expenses statement : Expenses) {
                tempId = String.valueOf(statement.getId());
                if (tempId.equals(expenseId) && !amount.isEmpty()) {
                    double tempAmount = Double.parseDouble(amount);
                    statement.setMoney(tempAmount);
                    expenseHibernateControl.edit(statement);
                    break;
                }
            }
        } else {
            for (subCategory cat : subCategories)
                cat.editNameOfIncome(catId, expenseId, amount);
        }
    }
    public void editNameOfIncome(String catId, String incomeId, String newName) throws Exception {
        String tempId = String.valueOf(id);
        if (tempId.equals(catId)) {
            for (Income statement : Income) {
                tempId = String.valueOf(statement.getId());
                if (tempId.equals(incomeId) && !newName.isEmpty()) {
                    statement.setName(newName);
                    incomeHibernateControl.edit(statement);
                    break;
                }
            }
        } else {
            for (subCategory cat : subCategories)
                cat.editNameOfIncome(catId, incomeId, newName);
        }
    }
    public void editAmountOfIncome(String catId, String incomeId, String amount) throws Exception {
        String tempId = String.valueOf(id);
        if (tempId.equals(catId)) {
            for (Income statement : Income) {
                tempId = String.valueOf(statement.getId());
                if (tempId.equals(incomeId) && !amount.isEmpty()) {
                    double tempAmount = Double.parseDouble(amount);
                    statement.setMoney(tempAmount);
                    incomeHibernateControl.edit(statement);
                    break;
                }
            }
        }
         else {
            for (subCategory cat : subCategories)
                cat.editNameOfIncome(catId, incomeId, amount);
        }
    }

    public void removeIncome(String catId, String incId) throws Exception {
        if (catId.equals(String.valueOf(getId()))){
            for (Income fin : Income) {
                if (incId.equals(String.valueOf(fin.getId()))) {
                    Income.remove(fin);
                    categoriesHibernateControl.removeIncomeFromCat(this, fin);
                    break;
                }
            }
        } else {
            for (subCategory cat : subCategories) {
                cat.removeIncome(incId, catId);
            }
        }
        setIncomeFromDb();
    }
    public void removeExpense(String delEXP, String id) throws Exception {
        if (delEXP.equals(String.valueOf(getId()))){
            for (Expenses fin : Expenses) {
                if (id.equals(String.valueOf(fin.getId()))) {
                    Expenses.remove(fin);
                    categoriesHibernateControl.removeExpenseFromCat(this, fin);
                    break;
                }
            }
        } else {
            for (subCategory cat : subCategories) {
                cat.removeExpense(id, delEXP);
            }
        }
        setExpensesFromDb();
    }

    public double countTotalIncome(double amount, String id){
        if (!Income.isEmpty()) {
            for (fms.model.Finances.Income fin : Income){
                amount += fin.getMoney();
            }
        }

        for (subCategory cat : subCategories){
                amount += cat.countTotalIncome(amount, id);
        }
        return amount;
    }
    public double countTotalExpenses(double amount, String id){
        if (!Expenses.isEmpty()) {
            for (fms.model.Finances.Expenses fin : Expenses){
                amount += fin.getMoney();
            }
        }

        for (subCategory cat : subCategories){
                amount += cat.countTotalExpenses(amount, id);
            }
        return amount;
    }
}

