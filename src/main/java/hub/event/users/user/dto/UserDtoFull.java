package hub.event.users.user.dto;

import hub.event.users.filter.Filter;
import hub.event.users.filter.dto.FilterDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class UserDtoFull {
    private Long id;
    private String username;
    private String email;
    private LocalDate registrationDate;
    private LocalDate birthDate;
    private List<FilterDto> filterDtos;

    public UserDtoFull() {
    }

    public UserDtoFull(Long id, String username, String email, LocalDate registrationDate, LocalDate birthDate, List<FilterDto> filterDtos) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.registrationDate = registrationDate;
        this.birthDate = birthDate;
        this.filterDtos =  filterDtos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return "UpdateUserDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", registrationDate=" + registrationDate +
                ", birthDate=" + birthDate +
                '}';
    }

    public List<FilterDto> getFilterDtos() {
        return filterDtos;
    }

    public void setFilterDtos(List<FilterDto> filterDtos) {
        this.filterDtos = filterDtos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDtoFull that = (UserDtoFull) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(email, that.email) && Objects.equals(registrationDate, that.registrationDate) && Objects.equals(birthDate, that.birthDate) && Objects.equals(filterDtos, that.filterDtos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, registrationDate, birthDate, filterDtos);
    }
}
