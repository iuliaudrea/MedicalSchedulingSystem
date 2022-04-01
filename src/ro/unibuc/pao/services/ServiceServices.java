package ro.unibuc.pao.services;

import ro.unibuc.pao.domain.Service;
import ro.unibuc.pao.domain.Speciality;
import ro.unibuc.pao.persistence.ServiceRepository;

public class ServiceServices {
    //stiu ca numele clasei nu este prea inspirat, dar atat s-a putut

    private ServiceRepository serviceRepository = new ServiceRepository();

    public void printAllServices() {
        for(int i = 0; i < serviceRepository.getSize(); i++) {
            System.out.println(serviceRepository.get(i));
        }
    }

    public void addNewService(String name, double price, int duration, Speciality speciality) {
        Service service = new Service(name, price, duration, speciality);
        serviceRepository.add(service);
    }

    public void addNewService(Service service) {
        serviceRepository.add(service);
    }
}
