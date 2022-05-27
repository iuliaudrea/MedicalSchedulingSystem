package ro.unibuc.pao.domain;

import java.util.Objects;

public class Medic extends Person {
    private Speciality speciality;
    private Room cabinet; // cabinetul in care lucreaza medicul

    public int id;
    public static int _id = 1;

    public Medic(String firstName, String lastName, Date birthDate, String phoneNumber, String email, Speciality speciality, Room cabinet) {
        super(firstName, lastName, birthDate, phoneNumber, email);
        this.speciality = speciality;
        this.cabinet = cabinet;
        this.id = ++_id;
    }

    public Medic() {}

    public Medic(String firstName, String lastName, Date birthDate, String phoneNumber, String email, Speciality speciality, int cabinetNumber) {
        super(firstName, lastName, birthDate, phoneNumber, email);
        this.speciality = speciality;
        this.id = ++_id;
        this.cabinet = new Room();
        this.cabinet.setNumber(cabinetNumber);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //generate constructor with Medic object
    public Medic(Medic medic) {
        this.firstName = medic.firstName;
        this.lastName = medic.lastName;
        this.birthDate = medic.birthDate;
        this.phoneNumber = medic.phoneNumber;
        this.email = medic.email;
        this.speciality = medic.speciality;
        this.cabinet = medic.cabinet;
        this.id = medic.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Medic medic = (Medic) o;
        return speciality == medic.speciality && Objects.equals(cabinet, medic.cabinet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), speciality, cabinet);
    }

    @Override
    public String toString() {
        return "Medic{id=" + id + ", " + super.toString() +
                ", speciality=" + speciality +
                ", cabinet=Room#" + cabinet.getNumber() +
                '}';
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }

    public Room getCabinet() {
        return cabinet;
    }

    public void setCabinet(Room cabinet) {
        this.cabinet = cabinet;
    }
}
