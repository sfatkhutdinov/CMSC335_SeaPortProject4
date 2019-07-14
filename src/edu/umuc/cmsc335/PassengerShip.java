package edu.umuc.cmsc335;


import java.util.HashMap;
import java.util.Scanner;

final class PassengerShip extends Ship {

    private int numberOfOccupiedRooms, numberOfPassengers, numberOfRooms;

    protected PassengerShip(Scanner scannerContents, HashMap<Integer, Dock> docksMap,
                            HashMap<Integer, SeaPort> portsMap) {

        super(scannerContents, docksMap, portsMap);

        if (scannerContents.hasNextInt()) {
            this.setNumberOfPassengers(scannerContents.nextInt());
        }

        if (scannerContents.hasNextInt()) {
            this.setNumberOfRooms(scannerContents.nextInt());
        }

        if (scannerContents.hasNextInt()) {
            this.setNumberOfOccupiedRooms(scannerContents.nextInt());
        }
    }

    private void setNumberOfPassengers(int numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
    }

    private void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    private void setNumberOfOccupiedRooms(int numberOfOccupiedRooms) {
        this.numberOfOccupiedRooms = numberOfOccupiedRooms;
    }

    protected int getNumberOfPassengers() {
        return this.numberOfPassengers;
    }

    protected int getNumberOfRooms() {
        return this.numberOfRooms;
    }

    protected int getNumberOfOccupiedRooms() {
        return this.numberOfOccupiedRooms;
    }

    @Override
    public String toString() {
        String stringOutput;

        stringOutput = "Passenger Ship: " + super.toString() + "\n\tPassengers: "
                + this.getNumberOfPassengers() + "\n\tRooms: " + this.getNumberOfRooms()
                + "\n\tOccupied Rooms: " + this.getNumberOfOccupiedRooms();

        return stringOutput;
    }
}