import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.PrintWriter;


class Salon {
    private Map<String, ArrayList<String>> schedule = new HashMap<>();
    private ArrayList<Appointment> appointments = new ArrayList<>();

    public boolean isValidDate(String date) {
        return date.matches("\\d{4}-\\d{2}-\\d{2}");
    }



    public ArrayList<String> getAvailableTimes(String date) {
        if (!schedule.containsKey(date)) {
            ArrayList<String> allTimes = new ArrayList<>();
            allTimes.add("10:00");
            allTimes.add("11:00");
            allTimes.add("12:00");
            allTimes.add("13:00");
            allTimes.add("14:00");
            allTimes.add("15:00");
            allTimes.add("16:00");
            allTimes.add("17:00");
            return allTimes;
        } else {
            ArrayList<String> bookedTimes = schedule.get(date);
            ArrayList<String> allTimes = new ArrayList<>();
            allTimes.add("10:00");
            allTimes.add("11:00");
            allTimes.add("12:00");
            allTimes.add("13:00");
            allTimes.add("14:00");
            allTimes.add("15:00");
            allTimes.add("16:00");
            allTimes.add("17:00");
            allTimes.removeAll(bookedTimes);
            return allTimes;
        }
    }

    public void addAppointment(Appointment appointment) {
        String date = appointment.getDate();
        String time = appointment.getTime();
        String name = appointment.getCustomerName();

        if (!schedule.containsKey(date)) {
            schedule.put(date, new ArrayList<>());
            schedule.put(name, new ArrayList<>());
        }

        if (schedule.get(date).contains(time)) {
            System.out.println("Appointment slot is already booked.");
        } else {
            schedule.get(date).add(time);
            appointments.add(appointment);
            System.out.println("Appointment added successfully.");
        }
    }

    public void deleteAppointment(String date, String time) {
        for (Appointment appointment : appointments) {
            if (appointment.getDate().equals(date) && appointment.getTime().equals(time)) {
                schedule.get(date).remove(time);
                appointments.remove(appointment);
                System.out.println("Appointment deleted successfully.");
                return;
            }
        }
        System.out.println("Appointment not found.");
    }

    public ArrayList<String> getAppointmentsList() {
        ArrayList<String> appointmentList = new ArrayList<>();
        for (Appointment appointment : appointments) {
            appointmentList.add(appointment.getDate() + " - " + appointment.getTime() + " - " + appointment.getCustomerName());
        }
        return appointmentList;
    }


    public double markAppointmentAsCompleted(String date, String time) {
        for (Appointment appointment : appointments) {
            if (appointment.getDate().equals(date) && appointment.getTime().equals(time)) {
                appointments.remove(appointment);
                return 150.0;
            }
        }
        return 0.0;
    }

    public void viewAppointmentsByDate(String date) {
        System.out.println("Appointments on " + date + ":");
        if (schedule.containsKey(date)) {
            ArrayList<String> times = schedule.get(date);
            for (String time : times) {
                System.out.print(time + " - ");
                for (Appointment appointment : appointments) {
                    if (appointment.getDate().equals(date) && appointment.getTime().equals(time)) {
                        System.out.println(appointment.getCustomerName());
                    }
                }
                System.out.println();
            }
        } else {
            System.out.println("No appointments found for this date.");
        }
    }
    public void saveAppointmentsToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(filename)) {
            for (Appointment appointment : appointments) {
                String line = appointment.getDate() + " - " + appointment.getTime() + " - " + appointment.getCustomerName();
                writer.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void loadAppointmentsFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" - ");
                if (parts.length == 3) {
                    String date = parts[0];
                    String time = parts[1];
                    String customerName = parts[2];
                    addLoadedAppointment(date, time, customerName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void addLoadedAppointment(String date, String time, String customerName) {
        appointments.add(new Appointment(customerName, date, time));
        if (!schedule.containsKey(date)) {
            schedule.put(date, new ArrayList<>());
        }
        schedule.get(date).add(time);
    }
}