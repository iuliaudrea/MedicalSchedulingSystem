package ro.unibuc.pao.services;

import ro.unibuc.pao.domain.Client;
import ro.unibuc.pao.domain.Date;
import ro.unibuc.pao.domain.Service;
import ro.unibuc.pao.exceptions.InvalidDataException;
import ro.unibuc.pao.persistence.ClientRepository;
import ro.unibuc.pao.services.csv.ClientCSVServices;
import ro.unibuc.pao.services.database.ClientDBServices;
import ro.unibuc.pao.services.database.ConnectionManager;

import java.util.ArrayList;
import java.util.Vector;

public class ClientServices {
    private ClientRepository clientRepository = new ClientRepository();

    public Vector<Client> getAllClients() {
        Vector<Client> clients = new Vector<>();
        for(int i = 0; i < clientRepository.getSize(); i++)
            clients.add(clientRepository.get(i));
        return clients;
    }

    public void addNewClient(Client client) throws InvalidDataException {
        if(client == null)
            throw new InvalidDataException("Client is null");

        Client newClient = new Client(client);
        clientRepository.add(newClient);
    }

    private boolean isValidClient(String firstName, String lastName, Date birthDate, String phoneNumber, String email)
    throws InvalidDataException {
        if(firstName == null || lastName == null || firstName.equals("") || lastName.equals(""))
            throw new InvalidDataException("Invalid name!");

        if(phoneNumber.length() != 10 || !phoneNumber.matches("0[0-9]+"))
            throw new InvalidDataException("Phone number must be 10 digits long and start with 0!");

        if(!email.matches("[a-zA-Z0-9]+@[a-zA-Z0-9]+.[a-zA-Z]+"))
            throw new InvalidDataException("Email is not valid!");

        return true;
    }

    public void addNewClient(String firstName, String lastName, Date birthDate, String phoneNumber, String email) throws InvalidDataException {

        if(isValidClient(firstName, lastName, birthDate, phoneNumber, email)) {
            Client newClient = new Client(firstName, lastName, birthDate, phoneNumber, email);
            clientRepository.add(newClient);
        }
    }

    public void updateClient(int index, Client client) throws InvalidDataException {
        if(index < 0 || index >= clientRepository.getSize())
            throw new InvalidDataException("Index is out of bounds");
        clientRepository.update(index, client);
    }

    public void deleteClient(int index) throws InvalidDataException {
        if(index < 0 || index >= clientRepository.getSize())
            throw new InvalidDataException("Index is out of bounds");
        clientRepository.delete(index);
    }

    public void loadFromCSVFile(){
        ClientCSVServices csvFileService = ClientCSVServices.getInstance();
        Vector<Client> clients = new Vector<>(csvFileService.read());
        for(Client client : clients) {
            clientRepository.add(client);
        }
    }

    public ArrayList<Client> getClientsFromDB(ConnectionManager conn) {
        ClientDBServices dbService = new ClientDBServices(conn);
        ArrayList<Client> dbClients = dbService.getAllItems();
        return dbClients;
    }

    public void addClientToDB(ConnectionManager conn, Client client) throws InvalidDataException {
        ClientDBServices dbService = new ClientDBServices(conn);
        if(isValidClient(client.getFirstName(), client.getLastName(), client.getBirthDate(),
                client.getPhoneNumber(), client.getEmail()))
            dbService.insertItem(client);
    }

    public void updateClientInDB(ConnectionManager conn, int id, Client client) throws InvalidDataException {
        ClientDBServices dbService = new ClientDBServices(conn);
        if(isValidClient(client.getFirstName(), client.getLastName(), client.getBirthDate(),
                client.getPhoneNumber(), client.getEmail()))
            dbService.updateItem(id, client);
    }

    public void deleteClientFromDB(ConnectionManager conn, int id)  {
        ClientDBServices dbService = new ClientDBServices(conn);
        dbService.deleteItem(id);
    }

}
