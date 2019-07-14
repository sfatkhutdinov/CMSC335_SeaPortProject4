package edu.umuc.cmsc335;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

final class SeaPort extends Thing {

    private ArrayList<Dock> docks;
    private ArrayList<Ship> que, ships;
    private ArrayList<Person> persons;
    private HashMap<String, ResourcePool> resourcePools;

    protected SeaPort(Scanner scannerContents) {
        super(scannerContents);
        this.setDocks(new ArrayList<>());
        this.setQue(new ArrayList<>());
        this.setShips(new ArrayList<>());
        this.setPersons(new ArrayList<>());
        this.setResourcePools(new HashMap<>());
    }

    private void setDocks(ArrayList<Dock> docks) {
        this.docks = docks;
    }

    private void setQue(ArrayList<Ship> que) {
        this.que = que;
    }

    private void setShips(ArrayList<Ship> ships) {
        this.ships = ships;
    }

    private void setPersons(ArrayList<Person> persons) {
        this.persons = persons;
    }

    private void setResourcePools(HashMap<String, ResourcePool> resourcePools) {
        this.resourcePools = resourcePools;
    }

    protected ArrayList<Dock> getDocks() {
        return this.docks;
    }

    protected ArrayList<Ship> getQue() {
        return this.que;
    }

    protected ArrayList<Ship> getShips() {
        return this.ships;
    }

    protected ArrayList<Person> getPersons() {
        return this.persons;
    }

    protected HashMap<String, ResourcePool> getResourcePools() {
        return this.resourcePools;
    }

    protected synchronized ArrayList<Person> getResources(Job job) {

        ResourcePool skillGroup;
        ArrayList<Person> candidates;
        boolean areAllRequirementsMet;
        String workerLogLine;
        Person worker;
        HashMap<String, Integer> mapOfNeededSkills;
        candidates = new ArrayList<>();
        areAllRequirementsMet = true;
        workerLogLine = "";
        mapOfNeededSkills = new HashMap<>();

        job.getRequirements().forEach((String skill) -> {
            mapOfNeededSkills.merge(skill, 1, Integer::sum);
        });

        outerLoop:
        for (String skill : job.getRequirements()) {

            skillGroup = this.getResourcePools().get(skill);

            if (skillGroup == null) {
                job.getStatusLog().append("No qualified workers found for " + job.getName()
                        + " (" + job.getParentShip().getName() + ")\n");

                this.returnResources(candidates);
                job.endJob();
                return new ArrayList<>();

            } else if (skillGroup.getPersonsInPool().size() < mapOfNeededSkills.get(skill)) {
                job.getStatusLog().append("Not enough qualified workers found for " + job.getName()
                        + " (" + job.getParentShip().getName() + ")\n");

                this.returnResources(candidates);
                job.endJob();
                return new ArrayList<>();

            } else {

                for (Person person : skillGroup.getPersonsInPool()) {

                    if (!person.getIsWorking()) {
                        skillGroup.reservePerson(person);
                        candidates.add(person);
                        continue outerLoop;
                    }
                }

                areAllRequirementsMet = false;
                break;
            }
        }
        if (areAllRequirementsMet) {
            workerLogLine += job.getName() + " (" + job.getParentShip().getName() + ") reserving";

            for (int i = 0; i < candidates.size(); i++) {
                worker = candidates.get(i);

                if (i == 0) {
                    workerLogLine += " ";
                } else if (i < candidates.size() - 1) {
                    workerLogLine += ", ";
                } else {
                    workerLogLine += " & ";
                }

                workerLogLine += worker.getName();
            }
            job.getStatusLog().append(workerLogLine + "\n");

            return candidates;
        } else {

            this.returnResources(candidates);
            return null;
        }
    }

    protected synchronized void returnResources(ArrayList<Person> resources) {
        resources.forEach((Person worker) -> {
            this.getResourcePools().get(worker.getSkill()).returnPerson(worker);
        });
    }

    protected void divideWorkersBySkill() {
        ResourcePool myResourcePool;

        for (Person person : this.getPersons()) {
            myResourcePool = this.getResourcePools().get(person.getSkill());

            if (myResourcePool == null) {
                myResourcePool = new ResourcePool(new ArrayList<>(), person.getSkill(),
                        this.getName());
                this.getResourcePools().put(person.getSkill(), myResourcePool);
            }

            myResourcePool.addPerson(person);
        }
    }

    @Override
    public String toString() {
        String stringOutput;

        stringOutput = "\n\nSeaPort: " + super.toString();
        for (Dock dock: this.getDocks()) {
            stringOutput += "\n> " + dock.toString();
        }

        stringOutput += "\n\n --- List of all ships in que:";
        for (Ship shipQue: this.getQue()) {
            stringOutput += "\n> " + shipQue.toString();
        }

        stringOutput += "\n\n --- List of all ships:";
        for (Ship shipAll: this.getShips()) {
            stringOutput += "\n> " + shipAll.getName() + " " + shipAll.getIndex() + " ("
                    + shipAll.getClass().getSimpleName() + ")";
        }

        stringOutput += "\n\n --- List of all persons:";
        for (Person person: this.getPersons()) {
            stringOutput += "\n> " + person.toString();
        }

        return stringOutput;
    }
}