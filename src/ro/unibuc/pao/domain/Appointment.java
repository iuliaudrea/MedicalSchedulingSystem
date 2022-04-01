package ro.unibuc.pao.domain;

import java.util.Objects;

public class Appointment {
    private Client client;
    private Medic medic;
    private Service service;
    private DateTime dateTime;

    public Appointment(Client client, Medic medic, Service service, DateTime dateTime) {
        //daca sterg o programare, nu se vor sterge clientul, medicul, seriviciul sau data => agregare
        this.client = client;
        this.medic = medic;
        this.service = service;
        this.dateTime = dateTime;
    }

    public Appointment(Appointment appointment) {
        if(appointment != null) {
            this.client = appointment.getClient();
            this.medic = appointment.getMedic();
            this.service = appointment.getService();
            this.dateTime = appointment.getDateTime();
        }
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "client=" + client +
                ", medic=" + medic +
                ", service=" + service +
                ", dateTime=" + dateTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return Objects.equals(client, that.client) && Objects.equals(medic, that.medic) &&
                Objects.equals(service, that.service) && Objects.equals(dateTime, that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(client, medic, service, dateTime);
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Medic getMedic() {
        return medic;
    }

    public void setMedic(Medic medic) {
        this.medic = medic;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }
}
