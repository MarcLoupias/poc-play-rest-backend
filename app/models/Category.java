package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.myweb.db.DaoObject;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Collection;

@SuppressWarnings("UnusedDeclaration")
@Entity
public class Category extends DaoObject {

    @NotNull
    @Constraints.Required
    @Constraints.MinLength(value = 1)
    @Constraints.MaxLength(value = 255)
    private String name;
    @Nullable
    @OneToMany(mappedBy = "category")
    private Collection<Product> productCollection;

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @Nullable
    @JsonIgnore
    public Collection<Product> getProductCollection() {
        return productCollection;
    }

    @JsonIgnore
    public void setProductCollection(@Nullable Collection<Product> productCollection) {
        this.productCollection = productCollection;
    }

    public Category() {
    }

    public Category(@NotNull String name) {
        this.name = name;
    }

    public Category(@NotNull String name, @Nullable Collection<Product> productCollection) {
        this.name = name;
        this.productCollection = productCollection;
    }

}
