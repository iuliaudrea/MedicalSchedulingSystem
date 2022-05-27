package ro.unibuc.pao.services;

import ro.unibuc.pao.domain.Service;
import ro.unibuc.pao.domain.Speciality;
import ro.unibuc.pao.persistence.ServiceRepository;
import ro.unibuc.pao.exceptions.InvalidDataException;
import ro.unibuc.pao.services.csv.ServiceCSVServices;
import ro.unibuc.pao.services.database.ConnectionManager;
import ro.unibuc.pao.services.database.ServiceDBServices;

import java.util.ArrayList;
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

    private boolean isValidService(String name, double price, int duration, String spec) throws InvalidDataException {
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

        return true;
    }

    public void addNewService(String name, double price, int duration, String spec) throws InvalidDataException{

        if(isValidService(name, price, duration, spec)) {
            Speciality speciality = Speciality.valueOf(spec);
            Service service = new Service(name, price, duration, speciality);
            serviceRepository.add(service);
        }
    }

    public void addNewService(Service service) {
        serviceRepository.add(service);
    }

    public void loadFromCSVFile(){
        ServiceCSVServices csvFileService = ServiceCSVServices.getInstance();
        Vector<Service> services = new Vector<>(csvFileService.read());
        for(Service service : services) {
            serviceRepository.add(service);
        }
    }

    public ArrayList<Service> getServicesFromDB(ConnectionManager conn) {
        ServiceDBServices dbService = new ServiceDBServices(conn);
        ArrayList<Service> dbServices = dbService.getAllItems();
        return dbServices;
    }

    public void addServiceToDB(ConnectionManager conn, Service service) throws InvalidDataException {
        ServiceDBServices dbService = new ServiceDBServices(conn);
        if(isValidService(service.getName(), service.getPrice(), service.getDuration(), service.getSpeciality().toString()))
            dbService.insertItem(service);
    }

    public void updateServiceInDB(ConnectionManager conn, int id, Service service) throws InvalidDataException {
        ServiceDBServices dbService = new ServiceDBServices(conn);
        if(isValidService(service.getName(), service.getPrice(), service.getDuration(), service.getSpeciality().toString()))
            dbService.updateItem(id, service);
    }

    public void deleteServiceFromDB(ConnectionManager conn, int id)  {
        ServiceDBServices dbService = new ServiceDBServices(conn);
        dbService.deleteItem(id);
    }
}
