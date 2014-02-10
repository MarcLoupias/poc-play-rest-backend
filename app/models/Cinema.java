package models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.myweb.db.DaoObject;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@SuppressWarnings("UnusedDeclaration")
@Entity
@NamedQueries({
        @NamedQuery(name = "Cinema.findByLikeName", query = "SELECT c FROM Cinema c WHERE c.name LIKE :name"),
        @NamedQuery(
                name = "Cinema.findByCountyName",
                query = "SELECT c FROM Cinema c JOIN c.county co WHERE co.name LIKE :name"
        ),
        @NamedQuery(
                name = "Cinema.countFindByCountyName",
                query = "SELECT COUNT(c) FROM Cinema c JOIN c.county co WHERE co.name LIKE :name"
        )
})
public class Cinema extends DaoObject {

    @NotNull
    @Constraints.Required
    @Constraints.MinLength(value = 1)
    @Constraints.MaxLength(value = 255)
    private String name;

    @NotNull
    @Constraints.Required
    @Constraints.Min(value = 1)
    @Constraints.Max(value = 200)
    private Integer screen;

    @NotNull
    @Constraints.Required
    @Constraints.Min(value = 1)
    @Constraints.Max(value = 10000)
    private Integer seat;

    @Nullable
    @ManyToOne
    private County county;

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    public Integer getScreen() {
        return screen;
    }

    public void setScreen(@NotNull Integer screen) {
        this.screen = screen;
    }

    @NotNull
    public Integer getSeat() {
        return seat;
    }

    public void setSeat(@NotNull Integer seat) {
        this.seat = seat;
    }

    @Nullable
    public County getCounty() {
        return county;
    }

    public void setCounty(@Nullable County county) {
        this.county = county;
    }

    public Cinema() {
    }

    public Cinema(@NotNull String name, @NotNull Integer screen, @NotNull Integer seat, @Nullable County county) {
        this.name = name;
        this.screen = screen;
        this.seat = seat;
        this.county = county;
    }
}
