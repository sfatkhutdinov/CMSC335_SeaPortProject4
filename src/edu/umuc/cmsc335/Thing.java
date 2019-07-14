package edu.umuc.cmsc335;

import java.util.Scanner;

class Thing implements Comparable<Thing> {

    private int index, parent;
    private String name;

    protected Thing(Scanner scannerContents) {
        if (scannerContents.hasNext()) {
            this.setName(scannerContents.next());
        }

        if (scannerContents.hasNextInt()) {
            this.setIndex(scannerContents.nextInt());
        }

        if (scannerContents.hasNextInt()) {
            this.setParent(scannerContents.nextInt());
        }
    }

    private void setIndex(int index) {
        this.index = index;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setParent(int parent) {
        this.parent = parent;
    }

    protected int getIndex() {
        return this.index;
    }

    protected String getName() {
        return this.name;
    }

    protected int getParent() {
        return this.parent;
    }

    @Override
    public String toString() {

        return this.getName() + " " + this.getIndex();
    }

    @Override
    public int compareTo(Thing thingInstance) {
        if (
                (thingInstance.getIndex() == this.getIndex()) &&
                        (thingInstance.getName().equals(this.getName())) &&
                        (thingInstance.getParent() == this.getParent())
        ) {
            return 1;
        } else {
            return 0;
        }
    }
}