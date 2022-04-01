package ro.unibuc.pao.domain;

import java.util.Objects;

public class Date {
    protected int day;
    protected int month;
    protected int year;

    public Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public Date(Date date) {
        if(date != null) {
            this.day = date.day;
            this.month = date.month;
            this.year = date.year;
        }
    }

    public Date() {}

    @Override
    public String toString() {
        return "{" +
                "day=" + day +
                ", month=" + month +
                ", year=" + year +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Date date = (Date) o;
        return day == date.day && month == date.month && year == date.year;
    }


    public int compareTo(Date date) {
        if(date == null) {
            return 1;
        }
        // daca cele doua date sunt egale, se returneaza 0
        if(this.year == date.year && this.month == date.month && this.day == date.day) return 0;

        // daca data care apeleaza functia e mai recenta decat data primita ca param se returneaza 1
        if(this.year > date.year || (this.year == date.year && this.month > date.month) ||
                (this.year == date.year && this.month == date.month && this.day > date.month)) return 1;

        // altfel se returneaza -1
        return -1;
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, month, year);
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
