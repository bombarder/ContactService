package entity;

import annotations.CustomDateFormat;
import annotations.JsonValue;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

public class Customer {

    private String firstName;
    private String lastName;

    @JsonValue(name ="fun")
    private String hobby;

    @CustomDateFormat(format = "dd-MM-yyyy")
    private LocalDate birthDate;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    @PostConstruct
    public void init(){

    }

    @Override
    public String toString() {
        return "entity.Customer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", hobby='" + hobby + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}