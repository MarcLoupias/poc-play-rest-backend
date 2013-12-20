package models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.myweb.db.DaoObject;
import play.data.format.Formats;
import play.data.validation.Constraints;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

@SuppressWarnings("UnusedDeclaration")
@Entity
public class Product extends DaoObject {

    @NotNull
    @Constraints.Required
    @Constraints.MinLength(value = 5)
    @Constraints.MaxLength(value = 20)
    private String name;
    @Nullable
    @Formats.DateTime(pattern = "yyyy/MM/dd HH:mm:ss")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy/MM/dd HH:mm:ss", timezone="CET")
    private Date date;
    @NotNull
    @Constraints.Required
    @Constraints.Min(value = 1)
    @Constraints.Max(value = 10)
    private Integer numberInteger;
    @Nullable
    @Constraints.Min(value = 10)
    @Constraints.Max(value = 20)
    private Long numberLong;

    @Nullable
    @ManyToOne
    private Category category;

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @Nullable
    public Date getDate() {
        return date;
    }

    public void setDate(@Nullable Date date) {
        this.date = date;
    }

    @NotNull
    public Integer getNumberInteger() {
        return numberInteger;
    }

    public void setNumberInteger(@NotNull Integer numberInteger) {
        this.numberInteger = numberInteger;
    }

    @Nullable
    public Long getNumberLong() {
        return numberLong;
    }

    public void setNumberLong(@Nullable Long numberLong) {
        this.numberLong = numberLong;
    }

    @Nullable
    @JsonIgnore
    public Category getCategory() {
        return category;
    }

    @JsonIgnore
    public void setCategory(@Nullable Category category) {
        this.category = category;
    }

    public Product() {
    }

    public Product(@NotNull String name, @Nullable Date date, @NotNull Integer numberInteger, @Nullable Category category) {
        this.name = name;
        this.date = date;
        this.numberInteger = numberInteger;
        this.category = category;
    }

    public Product(
            @NotNull String name, @Nullable Date date, @NotNull Integer numberInteger, @Nullable Long numberLong,
            @Nullable Category category) {
        this.name = name;
        this.date = date;
        this.numberInteger = numberInteger;
        this.numberLong = numberLong;
        this.category = category;
    }
}
