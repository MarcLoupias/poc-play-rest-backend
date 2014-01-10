package models;

import org.jetbrains.annotations.NotNull;
import org.myweb.db.DaoObject;
import play.data.validation.Constraints;

import javax.persistence.Entity;

@SuppressWarnings("UnusedDeclaration")
@Entity
public class County extends DaoObject {

    @NotNull
    @Constraints.Required
    @Constraints.MinLength(value = 1)
    @Constraints.MaxLength(value = 255)
    private String code;

    @NotNull
    @Constraints.Required
    @Constraints.MinLength(value = 1)
    @Constraints.MaxLength(value = 255)
    private String name;

    @NotNull
    public String getCode() {
        return code;
    }

    public void setCode(@NotNull String code) {
        this.code = code;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public County() {
    }

    public County(@NotNull String code, @NotNull String name) {
        this.code = code;
        this.name = name;
    }
}
