package fms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fms.model.Categories.Category;
import fms.model.Categories.subCategory;
import fms.model.Categories.topCategory;
import fms.model.Finances.Expenses;
import fms.model.Finances.Income;
import fms.HibernateControl.CategoriesHibernateControl;
import fms.HibernateControl.FMSHibernateControl;
import fms.HibernateControl.UserHibernateControl;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class FinanceManagementSystem implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    private String currentVersion;

    private static int regualUser = 1, manager = 2, admin = 3;

    @JsonIgnore
    @Transient
    User activeUser;

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @OneToMany(mappedBy = "fms", cascade= CascadeType.ALL, orphanRemoval=true)
    @OrderBy("id ASC")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<User> users = new ArrayList<>();

    public List<topCategory> getTopCategories() {
        return topCategories;
    }

    public void setTopCategories(List<topCategory> topCategories) {
        this.topCategories = topCategories;
    }

    @OneToMany(mappedBy = "fms", cascade= CascadeType.ALL, orphanRemoval=true)
    @OrderBy("id ASC")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<topCategory> topCategories = new ArrayList<>();

    static EntityManagerFactory factory = Persistence.createEntityManagerFactory("FMSHibernate");
    static FMSHibernateControl FMSHibernateControl = new FMSHibernateControl(factory);
    static UserHibernateControl userHibernateControl = new UserHibernateControl(factory);
    static CategoriesHibernateControl categoriesHibernateControl = new CategoriesHibernateControl(factory);

    public FinanceManagementSystem() {
        if(users.isEmpty()) {
            User user = new User("admin", "123", 3);
            user.setFms(this);
            users.add(user);
        }
        this.name = "FMS";
        this.currentVersion = "V1.0";
    }
    public FinanceManagementSystem(String name, String version) {
        this.name = name;
        this.currentVersion = version;
    }

    public List<User> getAllUsers(){
        return users;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    private void setCurrentUser(User user) {
        this.activeUser = user;
    }
    public void setUserListFromDb() {
        this.users = userHibernateControl.findUserEntities();
    }
    public void setCategoriesListFromDb() {
        this.topCategories = categoriesHibernateControl.findTopCategoryEntities();
    }

    public List<User> getUsers(){
        return users;
    }

    public Category getCategory(String id) {
        Category category = new Category();
        for (topCategory cat : topCategories) {
            if (id.equals(String.valueOf(cat.getId()))) {    //first checks if top category's ID matches with entered
                category = cat;
                break;
            } else {
                category = cat.getCategory(id, category); //else enters recursion to check sub categories
            }
        }
        return category;
    }

    public List<Expenses> getExpenseListById(String id) {
        List<Expenses> expenses = new ArrayList<Expenses>();
        for (topCategory cat : topCategories) {
            if (id.equals(String.valueOf(cat.getCatId()))) {    //first checks if top category's ID matches with entered
                expenses = cat.getThisExpenseList();
                break;
            } else {
                expenses = cat.getExpenseListById(id, expenses); //else enters recursion to check sub categories
            }
        }
        return expenses;
    }

    public List<Income> getIncomeListById(String id) {
        List<Income> income = new ArrayList<Income>();
        for (topCategory cat : topCategories) {
            if (id.equals(String.valueOf(cat.getCatId()))) {    //first checks if top category's ID matches with entered
                income = cat.getThisIncomeList();
                break;
            } else {
                income = cat.getIncomeListById(id, income); //else enters recursion to check sub categories
            }
        }
        return income;
    }

    public void addTopCategory(String name, String description) throws Exception {
        topCategory newCategory = new topCategory(name, description);
        newCategory.setFms(this);
        categoriesHibernateControl.createTopCategory(newCategory);
        topCategories.add(newCategory);
    }

    public void addSubCategory(String SelectedCategoryId, String categoryName, String categoryDis) throws Exception {
        subCategory newCategory = new subCategory(categoryName, categoryDis);
        for (topCategory cat : topCategories) {
            cat.addSubCategory(SelectedCategoryId, newCategory, cat);
        }
    }

    public void editDescriptionOfSelCat(String id, String newDis) throws Exception {

        for (topCategory cat : topCategories) {
            if (id.equals(String.valueOf(cat.getId()))){
                cat.setDescription(newDis);
                categoriesHibernateControl.editTopCategory(cat);
                break;
            } else {
                cat.editDescriptionOfSelCatSub(id, newDis);
            }
        }
    }

    public void editNameOfSelCat(String id, String newName) throws Exception {
        for (topCategory cat : topCategories) {
            if (id.equals(String.valueOf(cat.getId()))){
                cat.setName(newName);
                categoriesHibernateControl.editTopCategory(cat);
                break;
            } else {
                cat.editNameOfSelCatSub(id, newName);
            }
        }
    }

    public void removeCategory(String idd) throws Exception {
        if(!checkAccessLevel(manager))
          return;
        boolean deleteTopCategory = false;
        Category toDelete = null;
        for (topCategory cat : topCategories) {
            if (idd.equals(String.valueOf(cat.getCatId()))) {
                toDelete = cat;
                deleteTopCategory = true;
                break;
            } else {
                cat.removeCategory(idd);
            }
        }
        if (deleteTopCategory){
            topCategories.remove(toDelete);
            this.FMSHibernateControl.removeTopCategory(this, toDelete);
        }
        setCategoriesListFromDb();
    }

    public void editExpenseName(String catId,String selectedExpenseId, String newName) throws Exception {
        for (topCategory cat : topCategories) {
            cat.editNameOfExpense(catId,selectedExpenseId,newName);
        }

    }
    public void editExpenseAmount(String catId,String selectedExpenseId, String newAmount) throws Exception {
        for (topCategory cat : topCategories) {
            cat.editAmountOfExpense(catId, selectedExpenseId, newAmount);
        }
    }
    public void editIncomeName(String catId,String selectedIncomeId, String newName) throws Exception {
        for (topCategory cat : topCategories) {
            cat.editNameOfIncome(catId,selectedIncomeId,newName);
        }

    }
    public void editIncomeAmount(String catId,String selectedIncomeId, String newAmount) throws Exception {
        for (topCategory cat : topCategories) {
            cat.editAmountOfIncome(catId,selectedIncomeId,newAmount);
        }

    }

    public void addIncomeToCat(String id, String name, String amountString) throws Exception {

        double amount = Double.parseDouble(amountString);

        Income newIncome = new Income(amount, name);
        for (topCategory cat : topCategories) {
                cat.addIncome(newIncome, id);
        }
        System.out.println("Added income");
    }
    public void addExpenseToCat(String id, String name, String amountString) throws Exception {
        double amount = Double.parseDouble(amountString);

        Expenses newExp = new Expenses(amount, name);
        for (topCategory cat : topCategories) {
            cat.addExpense(newExp, id);
        }
        System.out.println("Added Expnese");
    }
    public void removeIncomefromCat(String catid, String delInc) throws Exception {

        for (topCategory cat : topCategories) {
            cat.removeIncome(catid, delInc);
        }
    }
    public void removeExpensefromCat(String catid, String delExp) throws Exception {
        for (topCategory cat : topCategories) {
            cat.removeExpense(catid, delExp);
        }
    }

    public void addUser(User newUser) throws Exception {
        newUser.setFms(this);
        userHibernateControl.create(newUser);
        users.add(newUser);
    }
    public void removeUser(String userName,String userPassword, int userAccessLvl) throws Exception {
        if(!checkAccessLevel(admin))
            return;
        if(activeUser.getName().equals(userName) && activeUser.getAccessLevel() == userAccessLvl){
            System.out.println("You can't delete your own user");
            return;
        }
        for (User user : users){
            if(user.getName().equals(userName) && user.getAccessLevel() == userAccessLvl && user.getPassword().equals(userPassword)){
                userHibernateControl.destroy(user.getId());
                System.out.println("User has been REMOVED");
                break;
            }
        }
        setUserListFromDb();
    }
    public void editUser (User selectedUser, User edditedUser) throws Exception {
        for(User user : users){
            if (selectedUser.getName().equals(user.getName()) && selectedUser.getPassword().equals(user.getPassword()) && selectedUser.getAccessLevel() == user.getAccessLevel()){
                edditedUser.setId(user.getId());
                userHibernateControl.edit(edditedUser);
                break;
            }
        }
        setUserListFromDb();
    }

    public boolean login(String userName, String password) {
        boolean isCorrect = false;
        setUserListFromDb();
        for (User user : users) {
            if (userName.equals(user.getName()) && password.equals(user.getPassword())) {
                setCurrentUser(user);
                isCorrect = true;
            }
        }
        return isCorrect;
    }

    public boolean checkAccessLevel(int accessLevel){
        if(activeUser.getAccessLevel() >= accessLevel){
            return true;
        } else {
            System.out.println("Access Denied. You do not have permission for this task.");
            return false;
        }
    }

    public boolean checkIfIdExists(String id) {
        boolean existance = false;
        for (topCategory cat: topCategories) { // checks is it's not topCat
            if (id.equals(String.valueOf(cat.getId()))){
                existance = true;
                break;
            } else {
                existance = cat.chekIfIdExistsSub(id, existance); // Checks subCategories
                if (existance)
                    break;
            }
        }
        return existance;
    }
}
