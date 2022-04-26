package ro.unibuc.pao.services;

import ro.unibuc.pao.domain.*;
import ro.unibuc.pao.exceptions.InvalidDataException;
import ro.unibuc.pao.persistence.MedicRepository;
import ro.unibuc.pao.persistence.RoomRepository;

import java.util.Vector;

public class MedicServices {

    private MedicRepository medicRepository= new MedicRepository();
    private RoomRepository roomRepository = new RoomRepository();

    public Vector<Medic> getAllMedics() {
        Vector<Medic> medics = new Vector<>();
        for(int i = 0; i < medicRepository.getSize(); i++)
            medics.add(medicRepository.get(i));
        return medics;
    }

    public Vector<Medic> getMedicsWithSpeciality(Speciality speciality) {
        Vector<Medic> medics = new Vector<>();
        for(int i = 0; i < medicRepository.getSize(); i++)
            if(medicRepository.get(i).getSpeciality().equals(speciality))
                medics.add(medicRepository.get(i));
        return medics;
    }

    public void addNewMedic(Medic medic) throws InvalidDataException {
        if(medic == null)
            throw new InvalidDataException("Medicul nu poate fi null!");

        Medic newMedic = new Medic(medic);
        medicRepository.add(newMedic);
    }

    public void addNewMedic(String firstName, String lastName, Date birthDate, String phoneNumber,
                String email, String spec, int roomIndex) throws InvalidDataException {

        if(firstName == null || lastName == null || firstName.equals("") || lastName.equals(""))
            throw new InvalidDataException("Invalid name!");

        if(phoneNumber.length() != 10 || !phoneNumber.matches("0[0-9]+"))
            throw new InvalidDataException("Phone number must be 10 digits long and start with 0!");

        if(!email.matches("[a-zA-Z0-9]+@[a-zA-Z0-9]+.[a-zA-Z]+"))
            throw new InvalidDataException("Email is not valid!");

        boolean ok = false;
        for(Speciality speciality : Speciality.values()) {
            if(speciality.toString().equals(spec)) { ok = true; }
        }
        if(!ok) {
            throw new InvalidDataException("Invalid speciality!");
        }
        Speciality speciality = Speciality.valueOf(spec);

        if(roomIndex < 0 || roomIndex >= roomRepository.getSize())
            throw new InvalidDataException("Invalid room index!");

        Medic newMedic = new Medic(firstName, lastName, birthDate, phoneNumber, email, speciality, roomRepository.get(roomIndex));
        medicRepository.add(newMedic);
    }

    public void deleteMedic(int index) throws InvalidDataException {
        if(index < 0 || index >= medicRepository.getSize())
            throw new InvalidDataException("Invalid medic index!");

        medicRepository.delete(index);
    }

    public void addRoom(Room room) {
        roomRepository.add(room);
    }

}
