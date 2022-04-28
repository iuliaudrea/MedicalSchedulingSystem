package ro.unibuc.pao.services.csv;
import ro.unibuc.pao.domain.*;
import ro.unibuc.pao.exceptions.InvalidDataException;

import java.io.*;
import java.util.Vector;

public class AppointmentCSVServices implements GenericCSVServices<Appointment> {

    private static final AppointmentCSVServices INSTANCE = new AppointmentCSVServices();

    private AppointmentCSVServices() {}

    public static AppointmentCSVServices getInstance() {
        return INSTANCE;
    }

    @Override
    public Vector<Appointment> read() {
        Vector<Appointment> appointments = new Vector<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("files/appointments.csv"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split(",");
                Date clientDate = new Date(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]));
                Client client = new Client(tokens[0], tokens[1], clientDate, tokens[5], tokens[6]);

                Date medicDate = new Date(Integer.parseInt(tokens[9]), Integer.parseInt(tokens[10]), Integer.parseInt(tokens[11]));
                Room medicCabinet = new Room(Integer.parseInt(tokens[15]), Integer.parseInt(tokens[16]), Double.parseDouble(tokens[17]));
                Medic medic = new Medic(tokens[7], tokens[8], medicDate, tokens[12], tokens[13], Speciality.valueOf(tokens[14]), medicCabinet);

                Service service = new Service(tokens[18], Double.parseDouble(tokens[19]), Integer.parseInt(tokens[20]), Speciality.valueOf(tokens[21]));
                DateTime date = new DateTime(Integer.parseInt(tokens[22]), Integer.parseInt(tokens[23]), Integer.parseInt(tokens[24]),
                        Integer.parseInt(tokens[25]), Integer.parseInt(tokens[26]));

                Appointment appointment = new Appointment(client, medic, service, date);
                appointments.add(appointment);
            }
        } catch(IOException ex) {
            System.out.println("Error reading from file!");
        } catch(InvalidDataException ex) {
            System.out.println("Invalid date!");
        } catch(NumberFormatException ex) {
            System.out.println("Invalid number!");
        }
        return appointments;
    }

    @Override
    public void write(Appointment appointment) {
        try(FileWriter fileWriter = new FileWriter("files/appointments.csv", true)) {
            Client client = new Client(appointment.getClient());
            Medic medic = new Medic(appointment.getMedic());
            Service service = new Service(appointment.getService());
            DateTime date = new DateTime(appointment.getDateTime());
            fileWriter.write(
                    client.getFirstName() + "," + client.getLastName() + ","
                    + client.getBirthDate().getDay() + "," + client.getBirthDate().getMonth() + ","
                    + client.getBirthDate().getYear() + "," + client.getPhoneNumber() + ","  + client.getEmail() + ","

                    + medic.getFirstName() + "," + medic.getLastName() + "," + medic.getBirthDate().getDay() + ","
                    + medic.getBirthDate().getMonth() + "," + medic.getBirthDate().getYear() + ","
                    + medic.getPhoneNumber() + "," + medic.getEmail() + "," + medic.getSpeciality().name() + ","
                    + medic.getCabinet().getNumber() + "," + medic.getCabinet().getFloor()+ "," + medic.getCabinet().getSurface() + ","

                    + service.getName() + "," + service.getPrice() + ","
                    + service.getDuration() + "," + service.getSpeciality().name() + ","

                    + date.getDay() + "," + date.getMonth() + "," + date.getYear() + ","
                    + date.getHour() + "," + date.getMinutes() + "\n");
            fileWriter.flush();
        } catch (IOException ex) {
            System.out.println("Error writing to file!");
        }
    }

}
