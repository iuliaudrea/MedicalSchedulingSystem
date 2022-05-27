package ro.unibuc.pao.services.database;

import ro.unibuc.pao.domain.*;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AppointmentDBServices implements GenericDBServices<Appointment> {
    private ConnectionManager connectionManager;

    public AppointmentDBServices(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public void insertItem(Appointment item) {}

    public void insertItem(int clientId, int medicId, int serviceId, String dateTime) {
        try {
            PreparedStatement stm = connectionManager.prepareStatement
                    ("INSERT INTO appointments VALUES (null, ?, ?, ?, STR_TO_DATE(?, '%d/%m/%Y %H:%i'))");
            stm.setInt(1, clientId);
            stm.setInt(2, medicId);
            stm.setInt(3, serviceId);
            stm.setString(4, dateTime);
            stm.executeUpdate();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateItem(int id, Appointment item) {
        try {
            PreparedStatement stm = connectionManager.prepareStatement
                    ("UPDATE appointments SET id_client = ?, id_medic = ?, id_serv = ? " +
                            "app_date = STR_TO_DATE(?, '%d-%m-%Y %H:%i') WHERE id_app = ?");
            stm.setInt(1, item.getClient().getId());
            stm.setInt(2, item.getMedic().getId());
            stm.setInt(3, item.getService().getId());
            stm.setString(4, item.getDateTime().toString());
            stm.setInt(5, id);
            stm.executeUpdate();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateItem(int id, int clientId, int medicId, int serviceId, DateTime dateTime) {
        try {
            PreparedStatement stm = connectionManager.prepareStatement
                    ("UPDATE appointments SET id_client = ?, id_medic = ?, id_serv = ?, " +
                            "app_date = STR_TO_DATE(?, '%d/%m/%Y %H:%i') WHERE id_app = ?");
            stm.setInt(1, clientId);
            stm.setInt(2, medicId);
            stm.setInt(3, serviceId);
            stm.setString(4, dateTime.toString());
            stm.setInt(5, id);
            stm.executeUpdate();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteItem(int id) {
        try {
            PreparedStatement stm = connectionManager.prepareStatement("DELETE FROM appointments WHERE id_app = ?");
            stm.setInt(1, id);
            stm.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<Appointment> getAllItems() {
        try {
            PreparedStatement stm = connectionManager.prepareStatement("SELECT * FROM appointments");

            ResultSet res = stm.executeQuery();
            ArrayList<Appointment> appointments = new ArrayList<>();

            ClientDBServices clientServ = new ClientDBServices(connectionManager);
            ArrayList<Client> allClients = clientServ.getAllItems();

            ServiceDBServices serviceServ = new ServiceDBServices(connectionManager);
            ArrayList<Service> allServices = serviceServ.getAllItems();

            MedicDBServices medicServ = new MedicDBServices(connectionManager);
            ArrayList<Medic> allMedics = medicServ.getAllItems();

            while(res.next()){
                Appointment appointment = new Appointment();

                for(Client client : allClients) {
                    if(client.getId() == res.getInt("id_client")) {
                        appointment.setClient(client);
                    }
                }

                for(Medic medic : allMedics) {
                    if(medic.getId() == res.getInt("id_medic")) {
                        appointment.setMedic(medic);
                    }
                }

                for(Service service : allServices) {
                    if(service.getId() == res.getInt("id_serv")) {
                        appointment.setService(service);
                    }
                }

                String date = res.getString("app_date");
                String[] dateParts = date.split("-");
                int year = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                String[] timeParts = dateParts[2].split(" ");
                int day = Integer.parseInt(timeParts[0]);
                String[] time = timeParts[1].split(":");
                int hour = Integer.parseInt(time[0]);
                int minute = Integer.parseInt(time[1]);
                DateTime app_date = new DateTime(day, month, year, hour, minute);
                appointment.setDateTime(app_date);

                appointments.add(appointment);
            }
            return appointments;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
