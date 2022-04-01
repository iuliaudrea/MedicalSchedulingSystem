package ro.unibuc.pao.services;

import ro.unibuc.pao.domain.Client;
import ro.unibuc.pao.domain.Date;
import ro.unibuc.pao.persistence.ClientRepository;

public class ClientServices {
    private ClientRepository clientRepository = new ClientRepository();

    public void printAllClients() {
        for(int i = 0; i < clientRepository.getSize(); i++)
            System.out.println(clientRepository.get(i));
    }

    public void addNewClient(Client client) {
        Client newClient = new Client(client);
        clientRepository.add(newClient);
    }

    public void addNewClient(String firstName, String lastName, Date birthDate, String phoneNumber, String email) {
        Client newClient = new Client(firstName, lastName, birthDate, phoneNumber, email);
        clientRepository.add(newClient);
    }

    public void updateClient(int index, Client client) {
        // exceptie index out of bounds
        clientRepository.update(index, client);
    }

    public void deleteClient(int index) {
        clientRepository.delete(index);
    }

}
