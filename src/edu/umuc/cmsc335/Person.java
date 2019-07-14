package edu.umuc.cmsc335;


import java.util.Scanner;

final class Person extends Thing {

    private String skill;

    private volatile boolean isWorking;

    protected Person(Scanner scannerContents) {
        super(scannerContents);

        if (scannerContents.hasNext()) {
            this.setSkill(scannerContents.next());
        } else {
            this.setSkill("Error");
        }

        this.setIsWorking(false);
    }

    private void setSkill(String skill) {
        this.skill = skill;
    }

    protected void setIsWorking(boolean isWorking) {
        this.isWorking = isWorking;
    }

    protected String getSkill() {
        return this.skill;
    }

    protected boolean getIsWorking() {
        return this.isWorking;
    }

    @Override
    public String toString() {
        return "Person: " + super.toString() + " " + this.getSkill();
    }
}