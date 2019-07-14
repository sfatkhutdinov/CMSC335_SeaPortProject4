package edu.umuc.cmsc335;

import java.util.HashMap;
import java.util.Scanner;

final class CargoShip extends Ship {

    private double cargoValue, cargoVolume, cargoWeight;

    protected CargoShip(Scanner scannerContents, HashMap<Integer, Dock> docksMap,
                        HashMap<Integer, SeaPort> portsMap) {

        super(scannerContents, docksMap, portsMap);

        if (scannerContents.hasNextDouble()) {
            this.setCargoWeight(scannerContents.nextDouble());
        }

        if (scannerContents.hasNextDouble()) {
            this.setCargoVolume(scannerContents.nextDouble());
        }

        if (scannerContents.hasNextDouble()) {
            this.setCargoValue(scannerContents.nextDouble());
        }
    }

    private void setCargoWeight(double cargoWeight) {
        this.cargoWeight = cargoWeight;
    }

    private void setCargoVolume(double cargoVolume) {
        this.cargoVolume = cargoVolume;
    }

    private void setCargoValue(double cargoValue) {
        this.cargoValue = cargoValue;
    }

    protected double getCargoWeight() {
        return this.cargoWeight;
    }

    protected double getCargoVolume() {
        return this.cargoVolume;
    }

    protected double getCargoValue() {
        return this.cargoValue;
    }

    @Override
    public String toString() {
        String stringOutput;

        stringOutput =  "Cargo Ship: " + super.toString() + "\n\tCargo Weight: "
                + this.getCargoWeight() + "\n\tCargo Volume: " + this.getCargoVolume()
                + "\n\tCargo Value: " + this.getCargoValue();

        return stringOutput;
    }
}