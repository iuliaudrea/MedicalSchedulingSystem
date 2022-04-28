package ro.unibuc.pao.services.csv;
import ro.unibuc.pao.domain.Medic;
import ro.unibuc.pao.domain.Date;
import ro.unibuc.pao.domain.Room;
import ro.unibuc.pao.domain.Speciality;
import ro.unibuc.pao.exceptions.InvalidDataException;
import java.io.*;
import java.util.Vector;

public class MedicCSVServices implements GenericCSVServices<Medic> {

    private static final MedicCSVServices INSTANCE = new MedicCSVServices();

    private MedicCSVServices() {}

    public static MedicCSVServices getInstance() {
        return INSTANCE;
    }

    @Override
    public Vector<Medic> read() {
        Vector<Medic> medics = new Vector<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("files/medics.csv"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split(",");
                Date date = new Date(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]));
                Room room = new Room(Integer.parseInt(tokens[8]), Integer.parseInt(tokens[9]), Double.parseDouble(tokens[10]));
                Medic medic = new Medic(tokens[0], tokens[1], date, tokens[5], tokens[6], Speciality.valueOf(tokens[7]), room);
                medics.add(medic);
            }
        } catch(IOException ex) {
            System.out.println("Error reading from file!");
        } catch(InvalidDataException ex) {
            System.out.println("Invalid date!");
        }
        return medics;
    }

    @Override
    public void write(Medic medic) {
        try(FileWriter fileWriter = new FileWriter("files/medics.csv", true)) {
            fileWriter.write(medic.getFirstName() + "," + medic.getLastName() + ","
                    + medic.getBirthDate().getDay() + "," + medic.getBirthDate().getMonth() + ","
                    + medic.getBirthDate().getYear() + "," + medic.getPhoneNumber() + ","
                    + medic.getEmail() + "," + medic.getSpeciality().name() + ","
                    + medic.getCabinet().getNumber() + "," + medic.getCabinet().getFloor()+ ","
                    + medic.getCabinet().getSurface() +"\n");
            fileWriter.flush();
        } catch (IOException ex) {
            System.out.println("Error writing to file!");
        }
    }

}
