package ro.unibuc.pao.view;

import ro.unibuc.pao.domain.*;
import ro.unibuc.pao.exceptions.InvalidDataException;
import ro.unibuc.pao.services.*;
//import ro.unibuc.pao.services.csv.AuditServices;
import ro.unibuc.pao.services.database.*;
import ro.unibuc.pao.services.database.AuditServices;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

public class ConsoleApp {
    private Scanner s = new Scanner(System.in);
    private ClientServices clientService = new ClientServices();
    private AppointmentServices appService = new AppointmentServices();
    private MedicServices medicService = new MedicServices();
    private ServiceServices serviceService = new ServiceServices();



    public static void main(String args[]) throws ClassNotFoundException, SQLException , InvalidDataException {
        ConsoleApp app = new ConsoleApp();
        app.loadCSVFiles();
        Room room1 = new Room(3, 1, 40);
        Room room2 = new Room(4, 1, 35);
        app.medicService.addRoom(room1);
        app.medicService.addRoom(room2);

        ConnectionManager man = new ConnectionManager("jdbc:mysql://localhost:3306/java_db","root","iulia");

        while (true) {
            app.showMenu();
            int option = app.readOption();
            app.execute(option);
        }

    }

    private void audit(ConnectionManager con, String action){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        AuditServices auditServices = new AuditServices();
        auditServices.write(con, action, dtf.format(now));
    }

    private void loadCSVFiles() {
        serviceService.loadFromCSVFile();
        clientService.loadFromCSVFile();
        medicService.loadFromCSVFile();
        appService.loadFromCSVFile();
    }

    private void showMenu() {
        System.out.println("---------------------");
        System.out.println("What do you want to do?");
        System.out.println("1. add client");
        System.out.println("2. add medic");
        System.out.println("3. add service");
        System.out.println("4. add appointment");
        System.out.println("5. list all appointments (or filter by client/medic)");
        System.out.println("6. list all clients");
        System.out.println("7. list all medics");
        System.out.println("8. list all services");
        System.out.println("9. update appointment (change date/service/medic)");
        System.out.println("10. delete appointment");
        System.out.println("13 - 16: Add/Update/Delete/Get from/to client database");
        System.out.println("17 - 20: Add/Update/Delete/Get from/to medic database");
        System.out.println("21 - 24: Add/Update/Delete/Get from/to service database");
        System.out.println("25 - 28: Add/Update/Delete/Get from/to appointment database");
        System.out.println("0. exit");
        System.out.print("Option:");
    }

    private int readOption() {
        try {
            int option = readInt();
            if (option >= 0 && option <= 28) {
                return option;
            }
        } catch (InvalidDataException invalid) {
            // nothing to do, as it's handled below
        }
        System.out.println("Invalid option. Try again");
        return readOption();
    }

    private int readInt() throws InvalidDataException {
        String line = s.nextLine();
        if (line.matches("^\\d+$")) {
            return Integer.parseInt(line);
        } else {
            throw new InvalidDataException("Invalid number");
        }
    }

    private void execute(int option) throws ClassNotFoundException, SQLException {
        ConnectionManager man = new ConnectionManager("jdbc:mysql://localhost:3306/java_db","root","iulia");
        switch (option) {
            case 1:
                addClientToRepo();
                audit(man,"Added new client");
                break;
            case 2:
                addMedicToRepo();
                audit(man,"Added new medic");
                break;
            case 3:
                addServiceToRepo();
                audit(man,"Added new service");
                break;
            case 4:
                addAppointmentToRepo();
                audit(man,"Added new appointment");
                break;
            case 5:
                listAppointments();
                audit(man,"Printed all appointments");
                break;
            case 6:
                printAllClients();
                audit(man,"Printed all clients");
                break;
            case 7:
                printAllMedics();
                audit(man,"Printed all medics");
                break;
            case 8:
                printAllServices();
                audit(man,"Printed all services");
                break;
            case 9:
                updateAppointment();
                audit(man,"Updated appointment");
                break;
            case 10:
                System.out.print("Enter the index of the appointment to delete:");
                int index = s.nextInt();
                s.nextLine();
                appService.deleteAppointment(index);
                audit(man,"Deleted appointment");
                break;
            case 12:
                System.out.print("Appointment index:");
                int ind = s.nextInt();
                s.nextLine();
                System.out.println(appService.getAppointmentFinDate(ind));
                break;
            case 13:
                addClientToDB(man);
                audit(man,"Added client to DB");
                break;
            case 14:
                updateClientInDB(man);
                audit(man,"Updated client in DB");
                break;
            case 15:
                deleteClientFromDB(man);
                audit(man, "Added client to DB");
                break;
            case 16:
                getAllClientsFromDB(man);
                audit(man,"Loaded clients from DB");
                break;
            case 17:
                addMedicToDB(man);
                audit(man,"Added medic to DB");
                break;
            case 18:
                updateMedicInDB(man);
                audit(man, "Updated medic in DB");
                break;
            case 19:
                deleteMedicFromDB(man);
                audit(man,"Deleted medic from DB");
                break;
            case 20:
                getAllMedicsFromDB(man);
                audit(man, "Loaded medics from DB");
                break;
            case 21:
                addServiceToDB(man);
                audit(man, "Added service to DB");
                break;
            case 22:
                updateServiceInDB(man);
                audit(man, "Updated service in DB");
                break;
            case 23:
                deleteServiceFromDB(man);
                audit(man, "Deleted service from DB");
                break;
            case 24:
                getServicesFromDB(man);
                audit(man, "Loaded services from DB");
                break;
            case 25:
                addAppointmentToDB(man);
                audit(man, "Added appointment to DB");
                break;
            case 26:
                updateAppointmentInDB(man);
                audit(man, "Updated appointment in DB");
                break;
            case 27:
                deleteAppointmentFromDB(man);
                audit(man, "Deleted appointment from DB");
                break;
            case 28:
                getAppointmentsFromDB(man);
                audit(man, "Loaded appointments from DB");
                break;
            case 0:
                System.exit(0);
        }
    }

    void listAppointments() {
        System.out.print("Filter appointments by 0-none, 1-client, 2-medic:");
        int option = s.nextInt();
        s.nextLine();
        // TODO: daca nu sunt programari se afiseaza un mesaj
        switch(option) {
            case 0:
                appService.printAllAppointments();
                break;
            case 1:
                try {
                    System.out.print("Please enter client index:");
                    int index = s.nextInt();
                    s.nextLine();
                    printAppointmentsByClient(index);
                } catch (InvalidDataException invalidData){
                    System.out.println(invalidData.getMessage());
                }
                break;
            case 2:
                try {
                    System.out.print("Please enter medic index:");
                    int index2 = s.nextInt();
                    s.nextLine();
                    printAppointmentsByMedic(index2);
                } catch (InvalidDataException invalidData){
                    System.out.println(invalidData.getMessage());
                }
                break;
            default:
                break;
        }
    }

    void printAppointmentsByMedic(int index) throws InvalidDataException {
        Vector<Appointment> appointments = appService.getAppointmentsByMedic(index);
        if(appointments.size() == 0) {
            System.out.println("No appointments found!");
        }
        for(Appointment appointment : appointments) {
            System.out.println(appointment);
        }
    }

    void printAppointmentsByClient(int index) throws InvalidDataException {
        Vector<Appointment> appointments = appService.getAppointmentsByClient(index);
        if(appointments.size() == 0) {
            System.out.println("No appointments found!");
        }
        for(Appointment appointment : appointments) {
            System.out.println(appointment);
        }
    }

    void printAllClients() {
        Vector<Client> clients = clientService.getAllClients();
        if(clients.size() == 0) {
            System.out.println("No clients found!");
        }
        for(Client client : clients) {
            System.out.println(client);
        }
    }

    void printAllMedics() {
        Vector<Medic> medics = medicService.getAllMedics();
        if(medics.size() == 0) {
            System.out.println("No medics found!");
        }
        for(Medic medic : medics) {
            System.out.println(medic);
        }
    }

    void printAllServices() {
        Vector<Service> services = serviceService.getAllServices();
        if(services.size() == 0) {
            System.out.println("No services found!");
        }
        for(Service service : services) {
            System.out.println(service);
        }
    }

    Client readClient() {
        try {
            System.out.print("First name:");
            String firstName = s.nextLine();
            System.out.print("Last name:");
            String lastName = s.nextLine();

            System.out.println("Birthdate:");
            System.out.print("Day:");
            int day = Integer.parseInt(s.nextLine());
            System.out.print("Month:");
            int month = Integer.parseInt(s.nextLine());
            System.out.print("Year:");
            int year = Integer.parseInt(s.nextLine());
            Date birthday = new Date(day, month, year);

            System.out.print("Phone:");
            String phone = s.nextLine();
            System.out.print("Email:");
            String email = s.nextLine();

            return new Client(firstName, lastName, birthday, phone, email);

        } catch (InvalidDataException invalidData) {
            System.out.println(invalidData.getMessage());
        } catch (NumberFormatException numberFormatException) {
            System.out.println("Invalid date!");
        }
        return null;
    }

    void addClientToRepo() {
        try {
            clientService.addNewClient(readClient());
        } catch (InvalidDataException invalidData) {
            System.out.println(invalidData.getMessage());
        }
    }

    void addClientToDB(ConnectionManager con) {
        try{
            Client client = readClient();
            clientService.addClientToDB(con, client);
        } catch (InvalidDataException invalidData) {
            System.out.println(invalidData.getMessage());
        }
    }

    void updateClientInDB(ConnectionManager con) {
        try {
            System.out.print("Id:");
            int id = Integer.parseInt(s.nextLine());
            Client client = readClient();
            clientService.updateClientInDB(con, id, client);
        } catch (InvalidDataException invalidData) {
            System.out.println(invalidData.getMessage());
        }
    }

    void deleteClientFromDB(ConnectionManager con) {
        System.out.print("Id:");
        int id = Integer.parseInt(s.nextLine());
        clientService.deleteClientFromDB(con, id);
    }

    private void getAllClientsFromDB(ConnectionManager con){
        ArrayList<Client> clients = clientService.getClientsFromDB(con);
        for (Client client : clients) {
            System.out.println(client);
        }
    }

    Service readService() {
        try {
            System.out.print("Name:");
            String name = s.nextLine();
            System.out.print("Price:");
            double price = Double.parseDouble(s.nextLine());
            System.out.print("Duration:");
            int duration = Integer.parseInt(s.nextLine());
            System.out.print("Speciality:");
            String spec = s.nextLine();

            return new Service(name, price, duration,Speciality.valueOf(spec));

        } catch (NumberFormatException numberFormat) {
            System.out.println("Invalid price or duration!");
        }
        return null;
    }

    void addServiceToRepo() {
        serviceService.addNewService(readService());
    }

    void addServiceToDB(ConnectionManager con) {
        try {
            Service service = readService();
            serviceService.addServiceToDB(con, service);
        } catch (InvalidDataException invalidData) {
            System.out.println(invalidData.getMessage());
        }
    }

    void updateServiceInDB(ConnectionManager con) {
        try {
            System.out.print("Id:");
            int id = Integer.parseInt(s.nextLine());
            Service service = readService();
            serviceService.updateServiceInDB(con, id, service);
        } catch (InvalidDataException invalidData) {
            System.out.println(invalidData.getMessage());
        }
    }

    void deleteServiceFromDB(ConnectionManager con) {
        System.out.print("Id:");
        int id = Integer.parseInt(s.nextLine());
        serviceService.deleteServiceFromDB(con, id);
    }

    void getServicesFromDB(ConnectionManager con) {
        ArrayList<Service> services = serviceService.getServicesFromDB(con);
        for (Service service : services) {
            System.out.println(service);
        }
    }

    Medic readMedic() {
        try {
            System.out.print("First name:");
            String firstName = s.nextLine();
            System.out.print("Last name:");
            String lastName = s.nextLine();

            System.out.println("Birthdate:");
            System.out.print("Day:");
            int day = Integer.parseInt(s.nextLine());
            System.out.print("Month:");
            int month = Integer.parseInt(s.nextLine());
            System.out.print("Year:");
            int year = Integer.parseInt(s.nextLine());
            Date birthday = new Date(day, month, year);

            System.out.print("Phone:");
            String phone = s.nextLine();
            System.out.print("Email:");
            String email = s.nextLine();

            System.out.print("Speciality:");
            String spec = s.nextLine();
            System.out.print("Room index(for repo)/id(for database):");
            int roomId = Integer.parseInt(s.nextLine());

            Medic medic = new Medic(firstName, lastName, birthday, phone, email, Speciality.valueOf(spec), roomId);
            return medic;
        } catch (InvalidDataException invalidData) {
            System.out.println(invalidData.getMessage());
        } catch (NumberFormatException numberFormat) {
            System.out.println("Invalid date or room id!");
        }
        return null;
    }

    void addMedicToRepo() {
        try {
            Medic medic = readMedic();
            medicService.addNewMedic(medic.getFirstName(), medic.getLastName(), medic.getBirthDate(),
                    medic.getPhoneNumber(), medic.getEmail(), medic.getSpeciality().name(), medic.getCabinet().getNumber());
        } catch (InvalidDataException invalidData) {
            System.out.println(invalidData.getMessage());
        }
    }

    void addMedicToDB(ConnectionManager con) {
        try {
            Medic medic = readMedic();
            medicService.addMedicToDB(con, medic);
        } catch (InvalidDataException invalidData) {
            System.out.println(invalidData.getMessage());
        }
    }

    void updateMedicInDB(ConnectionManager con) {
        try {
            System.out.print("Id:");
            int id = Integer.parseInt(s.nextLine());
            Medic medic = readMedic();
            medicService.updateMedicInDB(con, id, medic);
        } catch (InvalidDataException invalidData) {
            System.out.println(invalidData.getMessage());
        }
    }

    void deleteMedicFromDB(ConnectionManager con) {
        System.out.print("Id:");
        int id = Integer.parseInt(s.nextLine());
        medicService.deleteMedicFromDB(con, id);
    }

    void getAllMedicsFromDB(ConnectionManager con) {
        ArrayList<Medic> medics = medicService.getMedicsFromDB(con);
        for (Medic medic : medics) {
            System.out.println(medic);
        }
    }

    void addAppointmentToRepo() {
        try {
            System.out.print("Client index:");
            int clientIndex = Integer.parseInt(s.nextLine());

            System.out.print("Medic index:");
            int medicIndex = Integer.parseInt(s.nextLine());

            System.out.println("Date:");
            System.out.print("Day:");
            int day = Integer.parseInt(s.nextLine());
            System.out.print("Month:");
            int month = Integer.parseInt(s.nextLine());
            System.out.print("Year:");
            int year = Integer.parseInt(s.nextLine());
            System.out.println("Time:");
            System.out.print("Hour:");
            int hour = Integer.parseInt(s.nextLine());
            System.out.print("Minute:");
            int minute = Integer.parseInt(s.nextLine());
            DateTime date = new DateTime(day, month, year, hour, minute);

            System.out.print("Service index:");
            int serviceIndex = Integer.parseInt(s.nextLine());

            appService.addAppointment(clientIndex, medicIndex, serviceIndex, date);

        } catch (InvalidDataException invalidData) {
            System.out.println(invalidData.getMessage());
        } catch (NumberFormatException numberFormat) {
            System.out.println("Invalid date or indexes!");
        }
    }

    void addAppointmentToDB(ConnectionManager con) {
        try {
            System.out.print("Client id:");
            int clientIndex = Integer.parseInt(s.nextLine());

            System.out.print("Medic id:");
            int medicIndex = Integer.parseInt(s.nextLine());

            System.out.print("Service id:");
            int serviceIndex = Integer.parseInt(s.nextLine());

            System.out.println("Date:");
            System.out.print("Day:");
            int day = Integer.parseInt(s.nextLine());
            System.out.print("Month:");
            int month = Integer.parseInt(s.nextLine());
            System.out.print("Year:");
            int year = Integer.parseInt(s.nextLine());
            System.out.println("Time:");
            System.out.print("Hour:");
            int hour = Integer.parseInt(s.nextLine());
            System.out.print("Minute:");
            int minute = Integer.parseInt(s.nextLine());
            DateTime date = new DateTime(day, month, year, hour, minute);

            appService.addAppointmentToDB(con, clientIndex, medicIndex, serviceIndex, date);

        } catch (InvalidDataException invalidData) {
            System.out.println(invalidData.getMessage());
        } catch (NumberFormatException numberFormat) {
            System.out.println("Invalid date or indexes!");
        }
    }

    void updateAppointmentInDB(ConnectionManager con) {
        try {
            System.out.print("Appointment Id:");
            int id = Integer.parseInt(s.nextLine());

            System.out.print("Client id:");
            int clientIndex = Integer.parseInt(s.nextLine());

            System.out.print("Medic id:");
            int medicIndex = Integer.parseInt(s.nextLine());

            System.out.print("Service id:");
            int serviceIndex = Integer.parseInt(s.nextLine());

            System.out.println("Date:");
            System.out.print("Day:");
            int day = Integer.parseInt(s.nextLine());
            System.out.print("Month:");
            int month = Integer.parseInt(s.nextLine());
            System.out.print("Year:");
            int year = Integer.parseInt(s.nextLine());
            System.out.println("Time:");
            System.out.print("Hour:");
            int hour = Integer.parseInt(s.nextLine());
            System.out.print("Minute:");
            int minute = Integer.parseInt(s.nextLine());
            DateTime date = new DateTime(day, month, year, hour, minute);

            appService.updateAppointmentInDB(con, id, clientIndex, medicIndex, serviceIndex, date);

        } catch (InvalidDataException invalidData) {
            System.out.println(invalidData.getMessage());
        } catch (NumberFormatException numberFormat) {
            System.out.println("Invalid date or indexes!");
        }
    }

    void deleteAppointmentFromDB(ConnectionManager con) {
        try {
            System.out.print("Appointment id:");
            int id = Integer.parseInt(s.nextLine());
            appService.deleteAppointmentFromDB(con, id);
        } catch (NumberFormatException numberFormat) {
            System.out.println("Invalid index!");
        }
    }

    void getAppointmentsFromDB(ConnectionManager con) {
        ArrayList<Appointment> appts = appService.getAppointmentsFromDB(con);
        for (Appointment app : appts) {
            System.out.println(app);
        }
    }

    void updateAppointment() {
        System.out.println("What do you want to update?  1-medic, 2-date, 3-service");
        System.out.print("Option:");
        int option = s.nextInt();
        s.nextLine();
        System.out.print("Appointment index:");
        int index = s.nextInt();
        s.nextLine();
        switch (option) {
            case 1:
                System.out.print("Medic index:");
                int medicIndex = s.nextInt();
                s.nextLine();
                appService.updateMedic(index, medicIndex);
                break;
            case 2:
                try {
                    System.out.println("Date:");
                    System.out.print("Day:");
                    int day = s.nextInt();
                    System.out.print("Month:");
                    int month = s.nextInt();
                    System.out.print("Year:");
                    int year = s.nextInt();
                    System.out.println("Time:");
                    System.out.print("Hour:");
                    int hour = s.nextInt();
                    System.out.print("Minute:");
                    int minute = s.nextInt();
                    s.nextLine();
                    DateTime date = new DateTime(day, month, year, hour, minute);
                    appService.updateDate(index, date);
                } catch (InvalidDataException invalidData) {
                    System.out.println(invalidData.getMessage());
                }
                break;
            case 3:
                System.out.print("Service index:");
                int serviceIndex = s.nextInt();
                s.nextLine();
                appService.updateService(index, serviceIndex);
                break;
            default:
                System.out.println("Invalid option");
                break;
        }

    }

    private static void addSampleData() {
        ConsoleApp app = new ConsoleApp();
        try {
            Date date1 = new Date(20, 10, 2001);
            Client client1 = new Client("Iulia", "Udrea", date1, "0722333444", "iulia.udr@gmail.ro");
            app.clientService.addNewClient(client1);


            Date date2 = new Date(26, 5, 1980);
            Client client2 = new Client("Ioana", "Dobre", date2, "0722678908", "dobre.ioana@ceva.ro");
            app.clientService.addNewClient(client2);

            DateTime dateTime = new DateTime(30, 3, 2022, 12, 30);
            DateTime dateTime2 = new DateTime(4, 4, 2022, 14, 40);
            DateTime dateTime3 = new DateTime(14, 5, 2022, 9, 25);

            Room room1 = new Room(3, 1, 40);
            Room room2 = new Room(4, 1, 35);
            app.medicService.addRoom(room1);
            app.medicService.addRoom(room2);

            Medic medic1 = new Medic("Mihai", "Popescu", dateTime,
                    "0722678908", "pmihai@mail.com", Speciality.GENERAL, room1);
            Medic medic2 = new Medic("Matei", "Popa", dateTime2,
                    "0744555666", "medic2@mail.com", Speciality.ORTHODONTICS, room2);
            Medic medic3 = new Medic("Gigel", "Costel", dateTime2,
                    "0711122333", "gigel@gmail.com", Speciality.GENERAL, room2);
            app.medicService.addNewMedic(medic1);
            app.medicService.addNewMedic(medic2);
            app.medicService.addNewMedic(medic3);

            Service service1 = new Service("Consultatie", 100, 10, Speciality.GENERAL);
            Service service2 = new Service("Montare Aparat Dentar", 200, 30, Speciality.ORTHODONTICS);
            Service service3 = new Service("Obturatie", 150, 30, Speciality.GENERAL);
            app.serviceService.addNewService(service1);
            app.serviceService.addNewService(service2);
            app.serviceService.addNewService(service3);

            Appointment appointment = new Appointment(client1, medic1, service1, dateTime);
            Appointment appointment2 = new Appointment(client1, medic2, service2, dateTime2);
            Appointment appointment3 = new Appointment(client2, medic2, service2, dateTime3);

            app.appService.addAppointment(appointment);
            app.appService.addAppointment(appointment2);
            app.appService.addAppointment(appointment3);
        }
        catch (InvalidDataException invalidData) {
            System.out.println(invalidData.getMessage());
        }
    }

}