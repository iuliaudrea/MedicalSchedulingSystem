package ro.unibuc.pao.services;

import com.sun.jdi.InvalidLineNumberException;
import ro.unibuc.pao.domain.*;
import ro.unibuc.pao.persistence.*;
import ro.unibuc.pao.exceptions.InvalidDataException;
import java.util.Vector;

public class AppointmentServices {

    private AppointmentRepository appointmentRepository = new AppointmentRepository();
    private ClientRepository clientRepo = new ClientRepository();
    private ServiceRepository serviceRepo = new ServiceRepository();
    private MedicRepository medicRepo = new MedicRepository();


    public void addAppointment(Appointment appointment) throws InvalidDataException{
        if(appointment == null)
            throw new InvalidDataException("Appointment is null");
        if(isOverlapping(appointment))
            throw new InvalidDataException("Appointment is overlapping with other appointments!");

        appointmentRepository.add(appointment);
    }

    public void addAppointment(int clientIndex, int medicIndex, int serviceIndex, DateTime date) throws InvalidDataException {
        //daca clientul/medicul/serivciul nu exista in repository-uri, nu se poate crea programarea
        if(clientIndex <0 || clientIndex >= clientRepo.getSize())
            throw new InvalidDataException("Client index is invalid!");
        if(medicIndex < 0 ||  medicIndex >= medicRepo.getSize())
            throw new InvalidDataException("Medic index is invalid!");
        if(serviceIndex < 0 || serviceIndex >= serviceRepo.getSize())
            throw new InvalidDataException("Service index is invalid!");

        Client client = clientRepo.get(clientIndex);
        Medic medic = medicRepo.get(medicIndex);
        Service service = serviceRepo.get(serviceIndex);

        //daca programarea se intersecteaza cu alta programare
        Appointment temp = new Appointment(client,medic, service, date); // obiect temporar de tipul Appointment
        if(isOverlapping(temp))
            throw new InvalidDataException("Appointment is overlapping with other appointments!");

        // daca medicul nu are aceeasi specializare ca serviciul pentru care se face programarea
        if(medic.getSpeciality() != service.getSpeciality())
            throw new InvalidDataException("Medic does not have the same speciality as the service!");

        // daca ora de inceput a programarii incepe inainte de ora 9:00 sau se termina dupa ora 18:00
        if(date.getHour() < 9 || getEndDate(temp).getHour() > 18)
            throw new InvalidDataException("Appointment is not in the working hours!");

        Appointment appointment = new Appointment(client,medic,service,date);
        appointmentRepository.add(appointment);
    }

    public void printAllAppointments() {
        for(int i = 0; i < appointmentRepository.getSize(); i++)
            System.out.println(appointmentRepository.get(i));
    }

    // se returneaza programarile unui anumit client
    public Vector<Appointment> getAppointmentsByClient(int index) throws InvalidDataException {
        if(index < 0 || index >= clientRepo.getSize())
            throw new InvalidDataException("Client index is invalid!");

        Vector<Appointment> result = new Vector<Appointment>();
        for(int i = 0; i < appointmentRepository.getSize(); i++)
            if(appointmentRepository.get(i).getClient().equals(clientRepo.get(index))) {
                result.add(appointmentRepository.get(i));
            }
        return result;
    }

    //se returneaza programarile unui anumit medic
    public Vector<Appointment> getAppointmentsByMedic(int index) throws InvalidDataException {
        if(index < 0 || index >= medicRepo.getSize())
            throw new InvalidDataException("Medic index is invalid!");

        Vector<Appointment> result = new Vector<Appointment>();
        for(int i = 0; i < appointmentRepository.getSize(); i++)
            if(appointmentRepository.get(i).getMedic().equals(medicRepo.get(index))) {
                result.add(appointmentRepository.get(i));
            }
        return result;
    }

    private Vector<Appointment> getAppointmentsByMedic(Medic medic){
        Vector<Appointment> result = new Vector<Appointment>();
        for(int i = 0; i < appointmentRepository.getSize(); i++)
            if(appointmentRepository.get(i).getMedic().equals(medic)) {
                result.add(appointmentRepository.get(i));
            }
        return result;
    }

    // metode pentru modificarea programarilor
    // se returneaza true daca programarea poate fi schimbata cu succes
    public void updateDate(int index, DateTime newDate) throws InvalidDataException {
        if(index <0 || index >= appointmentRepository.getSize())
            throw new InvalidDataException("Appointment index is invalid!");

        Appointment oldApp = new Appointment(appointmentRepository.get(index));
        Appointment newApp = new Appointment(oldApp);
        newApp.setDateTime(newDate);
        appointmentRepository.delete(index);

        if(isOverlapping(newApp)) {
            // daca data se suprapune cu alta programare, programarea ramane la fel
            appointmentRepository.add(oldApp);
            throw new InvalidDataException("The new date is overlapping with another appointment!");
        }

        // daca data se schimba inainte de ora 9:00 sau dupa ora 18:00, programarea ramane la fel
        if(newDate.getHour() < 9 || getEndDate(newApp).getHour() > 18) {
            appointmentRepository.add(oldApp);
            throw new InvalidDataException("The new date is invalid!");
        }

        appointmentRepository.add(newApp);
    }

    public boolean updateService(int appIndex, int serviceIndex) {
        if(appIndex <0 || appIndex >= appointmentRepository.getSize())
            return false;

        Appointment oldApp = new Appointment(appointmentRepository.get(appIndex));
        Appointment newApp = new Appointment(oldApp);
        newApp.setService(serviceRepo.get(serviceIndex));
        appointmentRepository.delete(appIndex);

        if(isOverlapping(newApp) || !newApp.getService().getSpeciality().equals(newApp.getMedic().getSpeciality())){
            // daca se schimba serviciul, durata poate fi diferita de durata serviciului precedent
            // deci este nevoie sa verificam ca noua programare nu se suprapune cu alte programari
            // si ca noul serviciu face parte din acceasi specializare ca cea a medicului
            appointmentRepository.add(oldApp);
            return false;
        }

        appointmentRepository.add(newApp);
        return true;
    }

    public boolean updateMedic(int appIndex, int medicIndex) {
        if(appIndex <0 || appIndex >= appointmentRepository.getSize())
            return false;

        Appointment oldApp = new Appointment(appointmentRepository.get(appIndex));
        Appointment newApp = new Appointment(oldApp);
        newApp.setMedic(medicRepo.get(medicIndex));
        appointmentRepository.delete(appIndex);

        if(isOverlapping(newApp) || !newApp.getMedic().getSpeciality().equals(newApp.getService().getSpeciality())) {
//            System.out.println("Specializarea medicului e diferita");
            appointmentRepository.add(oldApp);
            return false;
        }

        appointmentRepository.add(newApp);
        return true;
    }

    public boolean deleteAppointment(int index) {
        // se returneaza true daca programarea exista si poate fi stearsa
        if(index >=0 && index < appointmentRepository.getSize()) {
            appointmentRepository.delete(index);
            return true;
        }
        return false;
    }

    public Room getAppRoom(int index) { //cabinetul in care are loc programarea
//        if(index < 0 || index >= appointmentRepository.getSize()) //TODO
        return appointmentRepository.get(index).getMedic().getCabinet();
    }

    // verificarea faptului ca o programare nu se suprapune cu programarile deja existente la acelasi medic
    private boolean isOverlapping(Appointment appointment) {
        if(appointment == null) return true; // de tratat exceptii

        // inceputul si sfarsitul programarii pe care vrem sa o adaugam
        DateTime appStart, appFin;
        appStart = new DateTime(appointment.getDateTime());
        appFin = new DateTime(getEndDate(appointment));
        //TODO appStart si appFin pot sa fie null

        Vector<Appointment> appts = getAppointmentsByMedic(appointment.getMedic()); // programarile deja existente la acelasi medic

        for(int i = 0; i < appts.size(); i++) {
            Appointment ap = appts.get(i);

            // data si ora la care incepe, respectiv se termina programarea cu care comparam
            DateTime begin = new DateTime(ap.getDateTime());
            DateTime end = new DateTime(getEndDate(ap));

            // daca appStart este dupa end sau daca appFin este inaintea lui begin, atunci intervalele nu se suprapun
            if(appStart.compareTo(end) == 1 || appFin.compareTo(begin) == -1); // cazul ok
            else return true;
        }
        return false;
    }

    // calcularea orei la care se termina o programare
    private DateTime getEndDate(Appointment appointment) {
        if(appointment == null) return null;

        DateTime startDate = new DateTime(appointment.getDateTime());
        DateTime endDate = new DateTime(appointment.getDateTime());
        int duration = appointment.getService().getDuration(); // durata serviciului pentru care s-a facut programarea

        //TODO exceptii: finDate nu trebuie sa depaseasca ora la care se incheie programul clinicii

        if((startDate.getMinutes() + duration) >= 60)
            endDate.setHour(startDate.getHour() + (startDate.getMinutes() + duration) / 60);

        endDate.setMinutes((startDate.getMinutes() + duration) % 60);

        // (startDate.getMinutes() + duration) / 60) reprezinta ora la care se termina programarea, iar
        // (startDate.getMinutes() + duration) % 60) reprezinta minutul la care se termina programarea

        return endDate;
    }

    public DateTime getAppointmentFinDate(int index) {
        return new DateTime(getEndDate(appointmentRepository.get(index)));
    }

}
