package ro.unibuc.pao.view;

import ro.unibuc.pao.domain.*;
import ro.unibuc.pao.services.*;

import java.sql.SQLOutput;
import java.util.Scanner;

public class ConsoleApp {
    private Scanner s = new Scanner(System.in);
    private ClientServices clientService = new ClientServices();
    private AppointmentServices appService = new AppointmentServices();
    private MedicServices medicService = new MedicServices();
    private ServiceServices serviceService = new ServiceServices();

    public static void main(String args[]) {
        ConsoleApp app = new ConsoleApp();
        addSampleData();
        while (true) {
            app.showMenu();
            int option = app.readOption();
            app.execute(option);
        }
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
        System.out.println("11. exit");
        System.out.print("Option:");
    }

    private int readOption(){
        int option = readInt();
        if (option >= 1 )//&& option <= 11)
            return option;

        System.out.println("Invalid option. Try again");
        return readOption();
    }

    private int readInt() {
        String line = s.nextLine();
        return Integer.parseInt(line);
    }

    private void execute(int option) {
        switch (option) {
            case 1:
                readClient();
                break;
            case 2:
                readMedic();
                break;
            case 3:
                readService();
                break;
            case 4:
                readAppointment();
                break;
            case 5:
                listAppointments();
                break;
            case 6:
                clientService.printAllClients();
                break;
            case 7:
                medicService.printAllMedics();
                break;
            case 8:
                serviceService.printAllServices();
                break;
            case 9:
                updateAppointment();
                break;
            case 10:
                System.out.print("Enter the index of the appointment to delete:");
                int index = s.nextInt();
                s.nextLine();
                appService.deleteAppointment(index);
                break;
            case 12:
                System.out.print("Appointment index:");
                int ind = s.nextInt();
                s.nextLine();
                System.out.println(appService.getAppointmentFinDate(ind));
                break;
            case 11:
                System.exit(0);
        }
    }

    void listAppointments() {
        System.out.print("Filter appointments by 0-none, 1-client, 2-medic:");
        int option = s.nextInt();
        s.nextLine();
        switch(option) {
            case 0:
                appService.printAllAppointments();
                break;
            case 1:
                System.out.print("Please enter client index:");
                int index = s.nextInt();
                s.nextLine();
                appService.printAppointmentsByClient(index);
                break;
            case 2:
                System.out.print("Please enter medic index:");
                int index2 = s.nextInt();
                s.nextLine();
                appService.printAppointmentsByMedic(index2);
                break;
//            case 3:
//                System.out.print("Please enter medic index:");
//                int index3 = s.nextInt();
//                s.nextLine();
//                System.out.print("Please enter date:");
//                int day = s.nextInt();
//                int month = s.nextInt();
//                int year = s.nextInt();
//                Date date = new Date(day, month, year);
//                s.nextLine();
//                appService.getAppointmentsByMedicAndDate(index3, date);
            default:
                break;
        }
    }

    void readClient() {
        System.out.print("First name:");
        String firstName = s.nextLine();
        System.out.print("Last name:");
        String lastName = s.nextLine();

        System.out.println("Birthdate:");
        System.out.print("Day:");
        int day = s.nextInt();
        System.out.print("Month:");
        int month = s.nextInt();
        System.out.print("Year:");
        int year = s.nextInt();
        Date birthday = new Date(day, month, year);

        s.nextLine();
        System.out.print("Phone:");
        String phone = s.nextLine();
        System.out.print("Email:");
        String email = s.nextLine();

        clientService.addNewClient(firstName, lastName, birthday, phone, email);
    }

    void readService() {
        System.out.print("Name:");
        String name = s.nextLine();
        System.out.print("Price:");
        double price = s.nextDouble();
        System.out.print("Duration:");
        int duration = s.nextInt();
        s.nextLine();
        System.out.print("Speciality:");
        Speciality speciality = Speciality.valueOf(s.nextLine());

        serviceService.addNewService(name, price, duration, speciality);
    }

    void readMedic() {
        System.out.print("First name:");
        String firstName = s.nextLine();
        System.out.print("Last name:");
        String lastName = s.nextLine();

        System.out.println("Birthdate:");
        System.out.print("Day:");
        int day = s.nextInt();
        System.out.print("Month:");
        int month = s.nextInt();
        System.out.print("Year:");
        int year = s.nextInt();
        Date birthday = new Date(day, month, year);

        s.nextLine();
        System.out.print("Phone:");
        String phone = s.nextLine();
        System.out.print("Email:");
        String email = s.nextLine();

        System.out.print("Speciality:");
        Speciality speciality = Speciality.valueOf(s.nextLine());

        System.out.print("Room index:");
        int roomIndex = s.nextInt();
        s.nextLine();

        medicService.addNewMedic(firstName, lastName, birthday, phone, email, speciality, roomIndex);
    }

    void readAppointment() {
        System.out.print("Client index:");
        int clientIndex = s.nextInt();
        s.nextLine();

        System.out.print("Medic index:");
        int medicIndex = s.nextInt();
        s.nextLine();

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

        System.out.print("Service index:");
        int serviceIndex = s.nextInt();
        s.nextLine();
        appService.addAppointment(clientIndex, medicIndex, serviceIndex, date);
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
        Date date1 = new Date(20,10,2001);
        Client client1 = new Client("Iulia", "Udrea", date1, "0722333444", "iulia.udr@gmail.ro");
        app.clientService.addNewClient(client1);


        Date date2 = new Date(26,5,1980);
        Client client2 = new Client("Ioana", "Dobre", date2, "0722678908", "dobre.ioana@ceva.ro");
        app.clientService.addNewClient(client2);

        DateTime dateTime = new DateTime(30,3,2022,12,30);
        DateTime dateTime2 = new DateTime(4,4,2022,14,40);
        DateTime dateTime3 = new DateTime(14,5,2022,9,25);

        Room room1 = new Room(3,1,40);
        Room room2 = new Room(4,1,35);
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

//        app.appService.printAllAppointments();
    }

}