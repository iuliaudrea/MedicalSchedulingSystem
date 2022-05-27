package ro.unibuc.pao.services.database;

import ro.unibuc.pao.domain.Client;
import ro.unibuc.pao.domain.Date;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientDBServices implements GenericDBServices<Client> {
    private ConnectionManager connectionManager;

    public ClientDBServices(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public void insertItem(Client client) {
        try {
            PreparedStatement stm = connectionManager.prepareStatement
            ("INSERT INTO clients VALUES (?, ?, ?, STR_TO_DATE(?,'%d/%m/%Y'), ?, ?)");
            stm.setInt(1, client.getId());
            stm.setString(2, client.getFirstName());
            stm.setString(3, client.getLastName());
            stm.setString(4, client.getBirthDate().toString());
            stm.setString(5, client.getEmail());
            stm.setString(6, client.getPhoneNumber());
            stm.executeUpdate();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateItem(int id, Client item) {
        try {
            PreparedStatement stm = connectionManager.prepareStatement
            ("UPDATE clients SET first_name = ?, last_name = ?, birth_date = STR_TO_DATE(?, '%d/%m/%Y')," +
                    " email = ?, phone = ? WHERE id_client = ?");
            stm.setString(1, item.getFirstName());
            stm.setString(2, item.getLastName());
            stm.setString(3, item.getBirthDate().toString());
            stm.setString(4, item.getEmail());
            stm.setString(5, item.getPhoneNumber());
            stm.setInt(6, id);
            stm.executeUpdate();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteItem(int id) {
        try {
            PreparedStatement stm = connectionManager.prepareStatement("DELETE FROM clients WHERE id_client = ?");
            stm.setInt(1, id);
            stm.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<Client> getAllItems() {
        try {
            PreparedStatement stm = connectionManager.prepareStatement("SELECT * FROM clients");
            ResultSet res = stm.executeQuery();
            ArrayList<Client> clients = new ArrayList<>();
            while(res.next()){
                Client client = new Client();
                client.setId(res.getInt("id_client"));
                client.setFirstName(res.getString("first_name"));
                client.setLastName(res.getString("last_name"));

                String date = res.getString("birth_date");
                String[] dateParts = date.split("-");
                int year = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                int day = Integer.parseInt(dateParts[2]);
                Date birthDate = new Date(day, month, year);
                client.setBirthDate(birthDate);

                client.setEmail(res.getString("email"));
                client.setPhoneNumber(res.getString("phone"));
                clients.add(client);
            }
            return clients;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
