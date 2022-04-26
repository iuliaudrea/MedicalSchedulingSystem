package ro.unibuc.pao.services;

import ro.unibuc.pao.domain.Service;
import ro.unibuc.pao.domain.Speciality;
import ro.unibuc.pao.persistence.ServiceRepository;
import ro.unibuc.pao.exceptions.InvalidDataException;
import java.util.Vector;

public class ServiceServices {
    //stiu ca numele clasei nu este prea inspirat, dar atat s-a putut

    private ServiceRepository serviceRepository = new ServiceRepository();

    public Vector<Service> getAllServices() {
        Vector<Service> services = new Vector<>();
        for(int i = 0; i < serviceRepository.getSize(); i++) {
            services.add(serviceRepository.get(i));
        }
        return services;
    }

    public void addNewService(String name, double price, int duration, String spec) throws InvalidDataException{
        if(price < 0 || duration < 0)
            throw new InvalidDataException("Invalid price or duration!");
        if(name.equals(""))
            throw new InvalidDataException("Invalid name!");

        boolean ok = false;
        for(Speciality speciality : Speciality.values()) {
            if(speciality.toString().equals(spec)) { ok = true; }
        }
        if(!ok) {
            throw new InvalidDataException("Invalid speciality!");
        }
        Speciality speciality = Speciality.valueOf(spec);

        Service service = new Service(name, price, duration, speciality);
        serviceRepository.add(service);
    }

    public void addNewService(Service service) {
        serviceRepository.add(service);
    }
}
