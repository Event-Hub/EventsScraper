package hub.event.users.user;

import java.util.List;

import com.sun.istack.NotNull;
import hub.event.users.filter.Filter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "USER_TABLE")
@SequenceGenerator(name = "USER_ID_SEQ", allocationSize = 1, initialValue = 1)
public class User {
    @Id
    @GeneratedValue(generator = "USER_ID_SEQ", strategy = GenerationType.SEQUENCE)
    @Column(name="user_id", nullable = false)
    private Long userId;
    private String username;

    private String email;
    @Column(name = "registration_date",columnDefinition = "DATE")
    private LocalDate registrationDate;
    @Column(name = "birth_date",columnDefinition = "DATE")
    private LocalDate birthDate;
    @OneToMany
    @JoinColumn(name = "userId")
    private List<Filter> filters;



    public User() { }

    public User(Long userId, String username, String email, LocalDate registrationDate, LocalDate birthDate) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.registrationDate = registrationDate;
        this.birthDate = birthDate;
    }

    public User( String username, String email, LocalDate registrationDate, LocalDate birthDate) {
        this.username = username;
        this.email = email;
        this.registrationDate = registrationDate;
        this.birthDate = birthDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", registrationDate=" + registrationDate +
                ", birthDate=" + birthDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) && Objects.equals(username, user.username) && Objects.equals(email, user.email) && Objects.equals(registrationDate, user.registrationDate) && Objects.equals(birthDate, user.birthDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, email, registrationDate, birthDate);
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }
}
