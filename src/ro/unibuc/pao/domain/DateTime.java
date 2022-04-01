package ro.unibuc.pao.domain;

import java.util.Objects;

public class DateTime extends Date{
    private int hour;
    private int minutes;

    public DateTime(int day, int month, int year, int hour, int minutes) {
        super(day, month, year);
        this.hour = hour;
        this.minutes = minutes;
    }

    public DateTime (DateTime dateTime){
        if(dateTime != null){
            this.day = dateTime.day;
            this.month = dateTime.month;
            this.year = dateTime.year;
            this.hour = dateTime.hour;
            this.minutes = dateTime.minutes;
        }
    }

    @Override
    public String toString() {
        return " DateTime{" + super.toString() +
                "hour=" + hour +
                ", minutes=" + minutes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DateTime dateTime = (DateTime) o;
        return hour == dateTime.hour && minutes == dateTime.minutes;
    }

    public int compareTo(DateTime datetime) {
        Date date = (Date)this;
        int result = date.compareTo((Date)datetime);
        if(result == 0) {
            if(this.hour == datetime.hour && this.minutes == datetime.minutes) return 0;
            if(this.hour > datetime.hour || (this.hour == datetime.hour && this.minutes > datetime.minutes)) return 1;
            return -1;
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), hour, minutes);
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
}
