package ro.unibuc.pao.services.database;

import ro.unibuc.pao.domain.*;
import ro.unibuc.pao.domain.Medic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MedicDBServices implements GenericDBServices<Medic> {
    private ConnectionManager connectionManager;

    public MedicDBServices(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public void insertItem(Medic medic) {
        try {
            PreparedStatement stm = connectionManager.prepareStatement
                    ("INSERT INTO medics VALUES (?, ?, ?, STR_TO_DATE(?,'%d/%m/%Y'), ?, ?, ?, ?)");
            stm.setInt(1, medic.getId());
            stm.setString(2, medic.getFirstName());
            stm.setString(3, medic.getLastName());
            stm.setString(4, medic.getBirthDate().toString());
            stm.setString(5, medic.getEmail());
            stm.setString(6, medic.getPhoneNumber());
            stm.setInt(7, medic.getCabinet().getNumber());
            stm.setString(8, medic.getSpeciality().name());
            stm.executeUpdate();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateItem(int id, Medic item) {
        try {
            PreparedStatement stm = connectionManager.prepareStatement
            ("UPDATE medics SET first_name = ?, last_name = ?, birth_date = STR_TO_DATE(?, '%d/%m/%Y')," +
            " email = ?, phone = ?, id_cabinet = ?, speciality = ? WHERE id_medic = ?");
            stm.setString(1, item.getFirstName());
            stm.setString(2, item.getLastName());
            stm.setString(3, item.getBirthDate().toString());
            stm.setString(4, item.getEmail());
            stm.setString(5, item.getPhoneNumber());
            stm.setInt(6, item.getCabinet().getNumber());
            stm.setString(7, item.getSpeciality().name());
            stm.setInt(8, id);
            stm.executeUpdate();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteItem(int id) {
        try {
            PreparedStatement stm = connectionManager.prepareStatement("DELETE FROM medics WHERE id_medic = ?");
            stm.setInt(1, id);
            stm.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<Medic> getAllItems() {
        try {
            PreparedStatement stm = connectionManager.prepareStatement("SELECT * FROM medics");
            PreparedStatement stm2 = connectionManager.prepareStatement("SELECT * FROM rooms WHERE id_room = ?");

            ResultSet res = stm.executeQuery();
            ArrayList<Medic> medics = new ArrayList<>();
            while(res.next()){
                Medic medic = new Medic();
                medic.setId(res.getInt("id_medic"));
                medic.setFirstName(res.getString("first_name"));
                medic.setLastName(res.getString("last_name"));

                String date = res.getString("birth_date");
                String[] dateParts = date.split("-");
                int year = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                int day = Integer.parseInt(dateParts[2]);
                Date birthDate = new Date(day, month, year);
                medic.setBirthDate(birthDate);

                medic.setEmail(res.getString("email"));
                medic.setPhoneNumber(res.getString("phone"));

                stm2.setInt(1, res.getInt("id_cabinet"));
                ResultSet res2 = stm2.executeQuery();
                if(res2.next()){
                    Room room = new Room();
                    room.setNumber(res2.getInt("id_room"));
                    room.setFloor(res2.getInt("floor"));
                    room.setSurface(res2.getDouble("surface"));
                    medic.setCabinet(room);
                }
                medic.setSpeciality(Speciality.valueOf(res.getString("speciality")));
                medics.add(medic);
            }
            return medics;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}

