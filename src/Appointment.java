import java.io.Serializable;

class Appointment implements Serializable {
    private String customerName;
    private String date;
    private String time;

    public Appointment(String customerName, String date, String time) {
        this.customerName = customerName;
        this.date = date;
        this.time = time;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}