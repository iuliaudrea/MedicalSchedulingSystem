package ro.unibuc.pao.services;

import ro.unibuc.pao.domain.*;
import ro.unibuc.pao.persistence.*;

import java.util.Vector;

public class AppointmentServices {

    private AppointmentRepository appointmentRepository = new AppointmentRepository();
    private ClientRepository clientRepo = new ClientRepository();
    private ServiceRepository serviceRepo = new ServiceRepository();
    private MedicRepository medicRepo = new MedicRepository();


    public boolean addAppointment(Appointment appointment) {
        // se returneaza true daca programarea poate fi adaugata cu succes
        if(isOverlapping(appointment) || appointment == null)
            return false; // nu se poate adauga programarea
        appointmentRepository.add(appointment);
        return true;
    }

    public void printAllAppointments() {
        for(int i = 0; i < appointmentRepository.getSize(); i++)
            System.out.println(appointmentRepository.get(i));
    }

    public boolean addAppointment(int clientIndex, int medicIndex, int serviceIndex, DateTime date) {
        //daca clientul/medicul/serivciul nu exista in repository-uri, nu se poate crea programarea
        if(clientIndex <0 || medicIndex < 0 || serviceIndex < 0 || clientIndex >= clientRepo.getSize()
            || medicIndex >= medicRepo.getSize() || serviceIndex >= serviceRepo.getSize())
            return false;

        Client client = clientRepo.get(clientIndex);
        Medic medic = medicRepo.get(medicIndex);
        Service service = serviceRepo.get(serviceIndex);

        //daca programarea se intersecteaza cu alta programare se returneaza false
        Appointment temp = new Appointment(client,medic, service, date); // obiect temporar de tipul Appointment
        if(isOverlapping(temp)) return false;

        // daca medicul nu are aceeasi specializare ca serviciul pentru care se face programarea, se returneaza false
        if(medic.getSpeciality() != service.getSpeciality())
            return false;

        // altfel se adauga programarea
        Appointment appointment = new Appointment(client,medic,service,date);
        appointmentRepository.add(appointment);
        return true;
    }

    // se returneaza si se afiseaza programarile unui anumit client
    public Vector<Appointment> getAppointmentsByClient(int index) {
        Vector<Appointment> result = new Vector<Appointment>();
        for(int i = 0; i < appointmentRepository.getSize(); i++)
            if(appointmentRepository.get(i).getClient().equals(clientRepo.get(index))) {
                result.add(appointmentRepository.get(i));
                System.out.println(appointmentRepository.get(i));
            }

        return result;
    }

    //se returneaza si se afiseaza programarile unui anumit medic
    public Vector<Appointment> getAppointmentsByMedic(int index) {
        Vector<Appointment> result = new Vector<Appointment>();
        for(int i = 0; i < appointmentRepository.getSize(); i++)
            if(appointmentRepository.get(i).getMedic().equals(medicRepo.get(index))) {
                result.add(appointmentRepository.get(i));
                System.out.println(appointmentRepository.get(i));
            }
        return result;
    }

//    //returneaza programarile unui medic primit ca parametru la o anumita data
//    public Vector<Appointment> getAppointmentsByMedicAndDate(int index, Date date) {
//        Vector<Appointment> result = new Vector<Appointment>();
//        for(int i = 0; i < appointmentRepository.getSize(); i++) {
//            Date appDate = new Date(appointmentRepository.get(i).getDateTime().getYear(),
//                                    appointmentRepository.get(i).getDateTime().getMonth(),
//                                    appointmentRepository.get(i).getDateTime().getDay());
//
//            if (appointmentRepository.get(i).getMedic().equals(medicRepo.get(index)) &&
//                    appDate.equals(date)) {
//                result.add(appointmentRepository.get(i));
//                System.out.println(appointmentRepository.get(i));
//            }
//        }
//        return result;
//    }

    // metode pentru modificarea programarilor
    // se returneaza true daca programarea poate fi schimbata cu succes
    public boolean updateDate(int index, DateTime newDate) {
        if(index <0 || index >= appointmentRepository.getSize())
            return false;

        Appointment appointment = appointmentRepository.get(index);
        appointment.setDateTime(newDate);
        return true;
    }

    public boolean updateService(int appIndex, int serviceIndex) {
        if(appIndex <0 || appIndex >= appointmentRepository.getSize())
            return false;

        Appointment appointment = appointmentRepository.get(appIndex);
        appointment.setService(serviceRepo.get(serviceIndex));
        return true;
    }

    public boolean updateMedic(int appIndex, int medicIndex) {
        if(appIndex <0 || appIndex >= appointmentRepository.getSize())
            return false;

        Appointment appointment = appointmentRepository.get(appIndex);
        appointment.setMedic(medicRepo.get(medicIndex));
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

    // verificarea faptului ca o programare nu se suprapune cu programarile deja existente
    private boolean isOverlapping(Appointment appointment) {
        if(appointment == null) return true; // de tratat exceptii

        // inceputul si sfarsitul programarii pe care vrem sa o adaugam
        DateTime appStart, appFin;
        appStart = appointment.getDateTime();
        appFin = getEndDate(appointment);
        //TODO appStart si appFin pot sa fie null

        for(int i = 0; i < appointmentRepository.getSize(); i++) {
            Appointment ap = appointmentRepository.get(i);

            // data si ora la care incepe, respectiv se termina programarea cu care comparam
            DateTime begin = new DateTime(ap.getDateTime());
            DateTime end = new DateTime(getEndDate(ap));
            // verificarea faptului ca cele doua intervale nu se suprapun
            // daca appStart este dupa end sau daca appFin este inaintea lui begin, atunci datele nu se suprapun
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

}
