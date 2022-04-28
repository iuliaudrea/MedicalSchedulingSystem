package ro.unibuc.pao.services.csv;
import ro.unibuc.pao.domain.Service;
import ro.unibuc.pao.domain.Speciality;

import java.io.*;
import java.util.Vector;

public class ServiceCSVServices implements GenericCSVServices<Service> {

    private static final ServiceCSVServices INSTANCE = new ServiceCSVServices();

    private ServiceCSVServices() {}

    public static ServiceCSVServices getInstance() {
        return INSTANCE;
    }

    @Override
    public Vector<Service> read() {
        Vector<Service> services = new Vector<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("files/services.csv"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split(",");
                Service service = new Service(tokens[0], Double.parseDouble(tokens[1]),
                        Integer.parseInt(tokens[2]), Speciality.valueOf(tokens[3]));
                services.add(service);
            }
        } catch(IOException ex) {
            System.out.println("Eroare la citirea din fisier!");
        }
        return services;
    }


    @Override
    public void write(Service service) {
        try(FileWriter fileWriter = new FileWriter("files/services.csv", true)) {
            fileWriter.write(service.getName() + "," + service.getPrice() + ","
                    + service.getDuration() + "," + service.getSpeciality().name() + "\n");
            fileWriter.flush();
        } catch (IOException ex) {
            System.out.println("Error writing to file!");
        }
    }

}
