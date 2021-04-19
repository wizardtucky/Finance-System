package fms.model.Categories;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import java.io.Serializable;
@Entity
public class subCategory extends Category implements Serializable {
    @JsonIgnore
    @ManyToOne
    Category parentCategory;

    public subCategory() {}

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    public subCategory(String name, String description) {
        super(name, description);
    }
}
