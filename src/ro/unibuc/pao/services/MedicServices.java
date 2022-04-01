package ro.unibuc.pao.services;

import ro.unibuc.pao.domain.*;
import ro.unibuc.pao.persistence.MedicRepository;
import ro.unibuc.pao.persistence.RoomRepository;

public class MedicServices {

    private MedicRepository medicRepository= new MedicRepository();
    private RoomRepository roomRepository = new RoomRepository();

    public void printAllMedics() {
        for(int i = 0; i < medicRepository.getSize(); i++)
            System.out.println(medicRepository.get(i));
    }

    public void listMedicsWithSpeciality(Speciality speciality) {
        for(int i = 0; i < medicRepository.getSize(); i++)
            if(medicRepository.get(i).getSpeciality().equals(speciality))
                System.out.println(medicRepository.get(i));
    }

    public void addNewMedic(String firstName, String lastName, Date birthDate, String phoneNumber, String email, Speciality speciality, Room room) {
        Medic newMedic = new Medic(firstName, lastName, birthDate, phoneNumber, email, speciality, room);
        medicRepository.add(newMedic);
    }

    public void addNewMedic(Medic medic) {
        Medic newMedic = new Medic(medic);
        medicRepository.add(newMedic);
    }

    public void addNewMedic(String firstName, String lastName, Date birthDate, String phoneNumber, String email, Speciality speciality, int roomIndex) {
        Medic newMedic = new Medic(firstName, lastName, birthDate, phoneNumber, email, speciality, roomRepository.get(roomIndex));
        medicRepository.add(newMedic);
    }


    public void deleteMedic(int index) {
        medicRepository.delete(index);
    }

    public void addRoom(Room room) {
        roomRepository.add(room);
    }

}
