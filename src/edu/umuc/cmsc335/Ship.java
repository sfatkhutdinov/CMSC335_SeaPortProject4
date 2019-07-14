package edu.umuc.cmsc335;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class Ship extends Thing {

    private PortTime arrivalTime, dockTime;
    private double draft, length, weight, width;
    private ArrayList<Job> jobs;
    private SeaPort port;
    private Dock dock;
    private HashMap<Integer, Dock> docksMap;

    protected Ship(Scanner scannerContents, HashMap<Integer, Dock> docksMap,
                   HashMap<Integer, SeaPort> portsMap) {

        super(scannerContents);

        if (scannerContents.hasNextDouble()) {
            this.setWeight(scannerContents.nextDouble());
        }

        if (scannerContents.hasNextDouble()) {
            this.setLength(scannerContents.nextDouble());
        }

        if (scannerContents.hasNextDouble()) {
            this.setWidth(scannerContents.nextDouble());
        }

        if (scannerContents.hasNextDouble()) {
            this.setDraft(scannerContents.nextDouble());
        }

        this.setJobs(new ArrayList<>());
        this.setPort(docksMap, portsMap);
        this.setDocksMap(docksMap);
        this.setDock();
    }

    private void setArrivalTime(PortTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    private void setDockTime(PortTime dockTime) {
        this.dockTime = dockTime;
    }

    private void setWeight(double weight) {
        this.weight = weight;
    }

    private void setLength(double length) {
        this.length = length;
    }

    private void setWidth(double width) {
        this.width = width;
    }

    private void setDraft(double draft) {
        this.draft = draft;
    }

    private void setJobs(ArrayList<Job> jobs) {
        this.jobs = jobs;
    }

    private void setPort(HashMap<Integer, Dock> docksMap, HashMap<Integer, SeaPort> portsMap) {
        this.port = portsMap.get(this.getParent());

        if (this.port == null) {
            Dock pier = docksMap.get(this.getParent());
            this.port = portsMap.get(pier.getParent());
        }
    }

    private void setDocksMap(HashMap<Integer, Dock> docksMap) {
        this.docksMap = docksMap;
    }

    private void setDock() {
        if (this.getDocksMap().containsKey(this.getParent())) {
            this.dock = this.getDocksMap().get(this.getParent());
        } else {
            this.dock = null;
        }
    }

    protected void setDock(Dock dock) {
        this.dock = dock;
    }

    protected PortTime getArrivalTime() {
        return this.arrivalTime;
    }

    protected PortTime getDockTime() {
        return this.dockTime;
    }

    protected double getWeight() {
        return this.weight;
    }

    protected double getLength() {
        return this.length;
    }

    protected double getWidth() {
        return this.width;
    }

    protected double getDraft() {
        return this.draft;
    }

    protected ArrayList<Job> getJobs() {
        return this.jobs;
    }

    protected SeaPort getPort() {
        return this.port;
    }

    private HashMap<Integer, Dock> getDocksMap() {
        return this.docksMap;
    }

    protected Dock getDock() {
        return this.dock;
    }

    @Override
    public String toString() {
        String stringOutput;

        stringOutput = super.toString() + "\n\tWeight: " + this.getWeight() + "\n\tLength: "
                + this.getLength() + "\n\tWidth: " + this.getWidth() + "\n\tDraft: " + this.getDraft()
                + "\n\tJobs:";

        if (this.getJobs().isEmpty()){
            stringOutput += " None";
        } else {
            for (Job newJob : this.getJobs()) {
                stringOutput += "\n" + newJob.toString();
            }
        }

        return stringOutput;
    }
}