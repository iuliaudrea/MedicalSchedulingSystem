package ro.unibuc.pao.services.csv;
import ro.unibuc.pao.domain.Client;
import ro.unibuc.pao.domain.Date;
import ro.unibuc.pao.exceptions.InvalidDataException;
import ro.unibuc.pao.services.ClientServices;
import java.io.*;
import java.util.Vector;

public class ClientCSVServices implements GenericCSVServices<Client> {

    private static final ClientCSVServices INSTANCE = new ClientCSVServices();

    private ClientCSVServices() {}

    public static ClientCSVServices getInstance() {
        return INSTANCE;
    }

    @Override
    public Vector<Client> read() {
        Vector<Client> clients = new Vector<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("files/clients.csv"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split(",");
                Date date = new Date(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]));
                Client client = new Client(tokens[0], tokens[1], date, tokens[5], tokens[6]);
                clients.add(client);
            }
        } catch(IOException ex) {
            System.out.println("Error reading from file!");
        } catch(InvalidDataException ex) {
            System.out.println("Invalid date!");
        }
        return clients;
    }


    @Override
    public void write(Client client) {
        try(FileWriter fileWriter = new FileWriter("files/clients.csv", true)) {
            fileWriter.write(client.getFirstName() + "," + client.getLastName() + ","
                    + client.getBirthDate().getDay() + "," + client.getBirthDate().getMonth() + ","
                    + client.getBirthDate().getYear() + "," + client.getPhoneNumber() + ","
                    + client.getEmail() + "\n");
            fileWriter.flush();
        } catch (IOException ex) {
            System.out.println("Error writing to file!");
        }
    }

}
