package ro.unibuc.pao.persistence;

import ro.unibuc.pao.domain.Appointment;
import ro.unibuc.pao.domain.*;

import java.util.Vector;

public class AppointmentRepository implements GenericRepository<Appointment> {

    private static final Vector<Appointment> storage = new Vector<Appointment>();

    @Override
    public void add(Appointment entity) {
        storage.add(entity);
    }

    @Override
    public Appointment get(int id) {
        return storage.get(id);
    }

    @Override
    public void update(int index, Appointment entity) {
        storage.set(index, entity);
    }

    public void updateDate(int index, DateTime date) {
        storage.get(index).setDateTime(date);
    }

    public void updateMedic(int index, Medic medic) {
        storage.get(index).setMedic(medic);
    }

    public void updateService(int index, Service service) {
        storage.get(index).setService(service);
    }

    @Override
    public void delete(int index) {
        storage.remove(index);
    }

    @Override
    public int getSize() {
        return storage.size();
    }
}
