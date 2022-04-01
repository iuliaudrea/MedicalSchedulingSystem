package ro.unibuc.pao.domain;

import java.util.Objects;

public class Person {
    protected String firstName;
    protected String lastName;
    protected Date birthDate;
    protected String phoneNumber;
    protected String email;

    public Person(String firstName, String lastName, Date birthDate, String phoneNumber, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = new Date(birthDate);
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public Person() {}

//    public Person(Person person) {
//        if(person != null) {
//            this.firstName = person.firstName;
//            this.lastName = person.lastName;
//            this.birthDate = new Date(birthDate);
//            this.phoneNumber = person.phoneNumber;
//            this.email = person.email;
//        }
//    }

    @Override
    public String toString() {
        return "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(firstName, person.firstName) &&
                Objects.equals(lastName, person.lastName) &&
                Objects.equals(birthDate, person.birthDate) &&
                Objects.equals(phoneNumber, person.phoneNumber) &&
                Objects.equals(email, person.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, birthDate, phoneNumber, email);
    }

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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
