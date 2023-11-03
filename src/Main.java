import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Salon salon = new Salon();
        salon.loadAppointmentsFromFile("appointments.txt");
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;
        double totalMoney = loadTotalMoney("totalMoney.txt");;


        while (isRunning) {
            System.out.println("Welcome to our Salon Booking System. What would you like to do?");
            System.out.println("1. Add Appointment");
            System.out.println("2. Delete Appointment");
            System.out.println("3. Mark appointment as Completed");
            System.out.println("4. View Appointments");
            System.out.println("5. Calculate Total Money earned (Password Required)");
            System.out.println("6. Exit");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter customer name: ");
                    String customerName = scanner.nextLine();
                    System.out.print("Enter date (yyyy-dd-mm): ");
                    String dateStr = scanner.nextLine();

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-dd-mm");
                    Date date = null;
                    try {
                        date = dateFormat.parse(dateStr);
                    } catch (java.text.ParseException e) {
                        System.out.println("Invalid date format. Please enter a valid date (yyyy-dd-mm).");
                        break;
                    }

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                    if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                        System.out.println("Weekend appointments are not allowed. Please choose a weekday.");
                    } else if (salon.isValidDate(dateStr)) {
                        ArrayList<String> availableTimes = salon.getAvailableTimes(dateStr);
                        System.out.println("Available Times for " + dateStr + ":");
                        for (String time : availableTimes) {
                            System.out.println(time);
                        }

                        System.out.print("Please, select a time from the list: ");
                        String selectedTime = scanner.nextLine();

                        if (availableTimes.contains(selectedTime)) {
                            Appointment newAppointment = new Appointment(customerName, dateStr, selectedTime);
                            salon.addAppointment(newAppointment);
                        } else {
                            System.out.println("I am sorry, but that isn't valid, please try again.");
                        }
                    } else {
                        System.out.println("Invalid date. Please enter a valid date (yyyy-dd-mm).");
                    }
                    break;


                case 2:
                    System.out.println("Appointments to Delete:");
                    ArrayList<String> appointmentsToDelete = salon.getAppointmentsList();

                    if (appointmentsToDelete.isEmpty()) {
                        System.out.println("No appointments to delete.");
                    } else {
                        System.out.println("Select an appointment to delete:");
                        for (int i = 0; i < appointmentsToDelete.size(); i++) {
                            System.out.println((i + 1) + ". " + appointmentsToDelete.get(i));
                        }

                        System.out.print("Enter the number of the appointment to delete: ");
                        int selection = scanner.nextInt();

                        if (selection >= 1 && selection <= appointmentsToDelete.size()) {
                            String[] selectedAppointmentParts = appointmentsToDelete.get(selection - 1).split(" - ");
                            String deleteDate = selectedAppointmentParts[0];
                            String deleteTime = selectedAppointmentParts[1];

                            salon.deleteAppointment(deleteDate, deleteTime);
                            System.out.println("Appointment deleted successfully.");
                        } else {
                            System.out.println("Invalid selection. Please try again.");
                        }
                    }
                    salon.saveAppointmentsToFile("appointments.txt");
                    break;

                case 3:
                    System.out.print("Enter the date of the appointment to mark as completed (yyyy-dd-mm): ");
                    String completedDate = scanner.nextLine();
                    System.out.print("Enter the time of the appointment to mark as completed (HH:mm): ");
                    String completedTime = scanner.nextLine();

                    double moneyEarned = salon.markAppointmentAsCompleted(completedDate, completedTime);

                    if (moneyEarned > 0.0) {
                        System.out.println("Appointment marked as completed.");
                        System.out.println("You earned 150 Danish Crowns for this appointment.");

                        System.out.println("Would you like to buy items?");
                        System.out.println("1. Shampoo");
                        System.out.println("2. Brush");
                        System.out.println("3. Both");
                        System.out.println("4. Neither");
                        System.out.print("Select an option: ");
                        int buyOption = scanner.nextInt();

                        double costOfShampoo = 50.0;
                        double costOfBrush = 35.0;

                        switch (buyOption) {
                            case 1:
                                System.out.println("The customer bought Shampoo.");
                                totalMoney += costOfShampoo;
                                break;
                            case 2:
                                System.out.println("The customer bought a Brush.");
                                totalMoney += costOfBrush;
                                break;
                            case 3:
                                System.out.println("The customer bought Shampoo and a Brush.");
                                totalMoney += (costOfShampoo + costOfBrush);
                                break;
                            case 4:
                                System.out.println("You chose not to buy any items.");
                                break;
                            default:
                                System.out.println("Invalid choice. No items purchased.");
                                break;
                        }
                    } else {
                        System.out.println("Appointment not found.");
                    }
                    break;

                case 4:
                    System.out.print("Enter date (yyyy-dd-mm): ");
                    String viewDate = scanner.nextLine();
                    salon.viewAppointmentsByDate(viewDate);
                    break;
                case 5:
                    System.out.print("Enter the password to calculate the total money: ");
                    String password = scanner.nextLine();
                    if (password.equals("HairyHarry")) {
                        System.out.println("Total Money Made: " + totalMoney + " Danish Crowns");
                    } else {
                        System.out.println("Incorrect password. Access denied.");
                    }
                    break;
                case 6:
                    System.out.println("Exiting Salon, have a fantastic day!");
                    scanner.close();
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    private static void saveTotalMoney(double totalMoney, String filename) {
        try (PrintWriter writer = new PrintWriter(filename)) {
            writer.println(totalMoney);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static double loadTotalMoney(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            if ((line = reader.readLine()) != null) {
                return Double.parseDouble(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}