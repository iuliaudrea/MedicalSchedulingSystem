package ro.unibuc.pao.services.database;

import ro.unibuc.pao.domain.Service;
import ro.unibuc.pao.domain.Date;
import ro.unibuc.pao.domain.Service;
import ro.unibuc.pao.domain.Speciality;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServiceDBServices implements GenericDBServices<Service> {
    private ConnectionManager connectionManager;

    public ServiceDBServices(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public void insertItem(Service item) {
        try {
            PreparedStatement stm = connectionManager.prepareStatement
                    ("INSERT INTO services VALUES (?, ?, ?, ?, ?)");
            stm.setInt(1, item.getId());
            stm.setString(2, item.getName());
            stm.setDouble(3, item.getPrice());
            stm.setInt(4, item.getDuration());
            stm.setString(5, item.getSpeciality().name());
            stm.executeUpdate();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateItem(int id, Service item) {
        try {
            PreparedStatement stm = connectionManager.prepareStatement
                    ("UPDATE services SET name = ?, price = ?, duration = ?, speciality = ? WHERE id_serv = ?");
            stm.setString(1, item.getName());
            stm.setDouble(2, item.getPrice());
            stm.setInt(3, item.getDuration());
            stm.setString(4, item.getSpeciality().name());
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
            PreparedStatement stm = connectionManager.prepareStatement("DELETE FROM services WHERE id_serv = ?");
            stm.setInt(1, id);
            stm.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<Service> getAllItems() {
        try {
            PreparedStatement stm = connectionManager.prepareStatement("SELECT * FROM services");
            ResultSet res = stm.executeQuery();
            ArrayList<Service> services = new ArrayList<>();
            while(res.next()){
                Service service = new Service();
                service.setId(res.getInt("id_serv"));
                service.setName(res.getString("name"));
                service.setPrice(res.getDouble("price"));
                service.setDuration(res.getInt("duration"));
                String spec = res.getString("speciality");
                service.setSpeciality(Speciality.valueOf(spec));
                services.add(service);
            }
            return services;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
