package ro.unibuc.pao.domain;

import java.util.Objects;

public class Service {
    private String name;
    private double price;
    private int duration;
    private Speciality speciality;

    private int id;
    private static int _id = 10;

    public Service(String name, double price, int duration, Speciality speciality) {
        this.name = name;
        this.price = price;
        this.duration = duration;
        this.speciality = speciality;
        this.id = ++_id;
    }

    public Service() {}

    public Service(Service service) {
        if(service != null) {
            this.name = service.name;
            this.price = service.price;
            this.duration = service.duration;
            this.speciality = service.speciality;
            this.id = service.id;
        }
    }

    @Override
    public String toString() {
        return "Service{id=" + id +
                ", name=" + name +
                ", price=" + price +
                ", duration=" + duration +
                ", speciality=" + speciality +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, duration);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Service service = (Service) obj;
        return price == service.price &&
                duration == service.duration &&
                Objects.equals(name, service.name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }
}
