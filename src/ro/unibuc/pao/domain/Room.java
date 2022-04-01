package ro.unibuc.pao.domain;

import java.util.Objects;

public class Room {
    private int number;
    private int floor;
    private double surface;

    public Room(int number, int floor, double surface) {
        this.number = number;
        this.floor = floor;
        this.surface = surface;
    }

    public Room(Room room) {
        if(room != null) {
            this.number = room.number;
            this.floor = room.floor;
            this.surface = room.surface;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return number == room.number && floor == room.floor && Double.compare(room.surface, surface) == 0;
    }

    @Override
    public String toString() {
        return "Room{" +
                "number=" + number +
                ", floor=" + floor +
                ", surface=" + surface +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, floor, surface);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public double getSurface() {
        return surface;
    }

    public void setSurface(double surface) {
        this.surface = surface;
    }
}
