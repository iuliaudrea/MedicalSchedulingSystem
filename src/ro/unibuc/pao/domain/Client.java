package ro.unibuc.pao.domain;

import java.util.Objects;

public class Client extends Person{

    public int id;
    public static int _id = 101;

    public Client(String firstName, String lastName, Date birthDate, String phoneNumber, String email) {
        super(firstName, lastName, birthDate, phoneNumber, email);
        this.id = ++_id;
    }

    public Client() {}

    public Client(Client client) {
        if(client != null) {
            this.id = client.id;
            this.firstName = client.firstName;
            this.lastName = client.lastName;
            this.birthDate = client.birthDate;
            this.phoneNumber = client.phoneNumber;
            this.email = client.email;
        }
    }

    @Override
    public String toString() {
        return "Client{id=" + id +", " + super.toString() + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Client client = (Client) o;
        return Objects.equals(firstName, client.firstName) &&
                Objects.equals(lastName, client.lastName) &&
                Objects.equals(birthDate, client.birthDate) &&
                Objects.equals(phoneNumber, client.phoneNumber) &&
                Objects.equals(email, client.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), firstName, lastName, birthDate, phoneNumber, email);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
