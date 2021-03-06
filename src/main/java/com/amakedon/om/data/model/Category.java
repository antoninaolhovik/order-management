package com.amakedon.om.data.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "Category")
@Table(name = "category")
public class Category extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @OneToMany(
            mappedBy = "category",
            cascade = CascadeType.ALL//,
            //orphanRemoval = true
    )
    @JsonManagedReference
    private List<Product> products = new ArrayList<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product) {
        if (product.isNew()) {
            getProducts().add(product);
        }
        product.setCategory(this);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return getId() == category.getId() &&
                Objects.equals(name, category.name)
                &&
                Objects.equals(products, category.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), name, products);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", products=" + products.size() +
                '}';
    }
}

