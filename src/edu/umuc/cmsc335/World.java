package edu.umuc.cmsc335;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import javax.swing.tree.DefaultMutableTreeNode;

final class World extends Thing {

    // Rubric-required fields
    private ArrayList<Thing> allThings;
    private ArrayList<SeaPort> ports;
    private PortTime time;
    protected World(Scanner scannerContents) {
        super(scannerContents);
        this.setAllThings(new ArrayList<>());
        this.setPorts(new ArrayList<>());
        this.process(scannerContents);
    }

    private void setAllThings(ArrayList<Thing> allThings) {
        this.allThings = allThings;
    }

    private void setPorts(ArrayList<SeaPort> ports) {
        this.ports = ports;
    }

    private void setTime(PortTime time) {
        this.time = time;
    }

    protected ArrayList<Thing> getAllThings() {
        return this.allThings;
    }

    protected ArrayList<SeaPort> getPorts() {
        return this.ports;
    }

    protected PortTime getTime() {
        return this.time;
    }

    private void process(Scanner scannerContents) {

        String lineString;
        Scanner lineContents;
        HashMap<Integer, SeaPort> portsMap;
        HashMap<Integer, Dock> docksMap;
        HashMap<Integer, Ship> shipsMap;
        portsMap = new HashMap<>();
        docksMap = new HashMap<>();
        shipsMap = new HashMap<>();

        while (scannerContents.hasNextLine()) {
            lineString = scannerContents.nextLine().trim();

            if (lineString.length() == 0) {
                continue;
            }

            lineContents = new Scanner(lineString);

            if (lineContents.hasNext()) {

                switch(lineContents.next().trim()) {
                    case "port":
                        SeaPort newSeaPort = new SeaPort(lineContents);
                        this.getAllThings().add(newSeaPort);
                        this.getPorts().add(newSeaPort);
                        portsMap.put(newSeaPort.getIndex(), newSeaPort);
                        break;
                    case "dock":
                        Dock newDock = new Dock(lineContents);
                        this.getAllThings().add(newDock);
                        this.addThingToList(portsMap, newDock, "getDocks");
                        docksMap.put(newDock.getIndex(), newDock);
                        break;
                    case "pship":
                        PassengerShip newPassengerShip = new PassengerShip(lineContents, docksMap,
                                portsMap);
                        this.getAllThings().add(newPassengerShip);
                        this.addShipToParent(newPassengerShip, docksMap, portsMap);
                        shipsMap.put(newPassengerShip.getIndex(), newPassengerShip);
                        break;
                    case "cship":
                        CargoShip newCargoShip = new CargoShip(lineContents, docksMap, portsMap);
                        this.getAllThings().add(newCargoShip);
                        this.addShipToParent(newCargoShip, docksMap, portsMap);
                        shipsMap.put(newCargoShip.getIndex(), newCargoShip);
                        break;
                    case "person":
                        Person newPerson = new Person(lineContents);
                        this.getAllThings().add(newPerson);
                        this.addThingToList(portsMap, newPerson, "getPersons");
                        break;
                    case "job":
                        Job newJob = new Job(lineContents, shipsMap);
                        this.getAllThings().add(newJob);
                        this.addJobToShip(newJob, shipsMap, docksMap);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Thing> void addThingToList(HashMap<Integer, SeaPort> portsMap, T newThing,
                                                  String methodName) {


        SeaPort newPort;
        ArrayList<T> thingsList;
        Method getList;


        newPort = portsMap.get(newThing.getParent());

        try {

            getList = SeaPort.class.getDeclaredMethod(methodName);


            thingsList = (ArrayList<T>) getList.invoke(newPort);

            if (newPort != null) {
                thingsList.add(newThing);
            }
        } catch (
                NoSuchMethodException |
                        SecurityException |
                        IllegalAccessException |
                        IllegalArgumentException |
                        InvocationTargetException ex
        ) {
            System.out.println("Error: " + ex);
        }
    }

    private void addJobToShip(Job newJob, HashMap<Integer, Ship> shipsMap,
                              HashMap<Integer, Dock> docksMap) {

        Dock newDock;
        Ship newShip = shipsMap.get(newJob.getParent());

        if (newShip != null) {
            newShip.getJobs().add(newJob);
        } else {
            newDock = docksMap.get(newJob.getParent());
            newDock.getShip().getJobs().add(newJob);
        }
    }

    private void addShipToParent(Ship newShip, HashMap<Integer, Dock> docksMap,
                                 HashMap<Integer, SeaPort> portsMap) {

        SeaPort myPort;
        Dock myDock = docksMap.get(newShip.getParent());

        if (myDock == null) {
            myPort = portsMap.get(newShip.getParent());
            myPort.getShips().add(newShip);
            myPort.getQue().add(newShip);
        } else {
            myPort = portsMap.get(myDock.getParent());
            myDock.setShip(newShip);
            myPort.getShips().add(newShip);
        }
    }

    @SuppressWarnings("unchecked")
    protected <T extends Thing> DefaultMutableTreeNode toTree() {

        DefaultMutableTreeNode mainNode, parentNode, childNode;
        Method getList;
        HashMap<String, String> classMethodMap;
        ArrayList<T> thingsList;


        mainNode = new DefaultMutableTreeNode("World");
        classMethodMap = new HashMap<String, String>() {{
            put("Docks", "getDocks");
            put("Ships", "getShips");
            put("Que", "getQue");
            put("Persons", "getPersons");
        }};

        for (SeaPort newPort : this.getPorts()) {
            parentNode = new DefaultMutableTreeNode(newPort.getName() + " (" + newPort.getIndex()
                    + ")");
            mainNode.add(parentNode);

            for (HashMap.Entry<String, String> pair : classMethodMap.entrySet()) {
                try {

                    getList = SeaPort.class.getDeclaredMethod(pair.getValue());
                    thingsList = (ArrayList<T>) getList.invoke(newPort);


                    childNode = this.addBranch(pair.getKey(), thingsList);
                    parentNode.add(childNode);
                } catch (
                        NoSuchMethodException |
                                SecurityException |
                                IllegalAccessException |
                                IllegalArgumentException |
                                InvocationTargetException ex
                ) {
                    System.out.println("Error: " + ex);
                }
            }
        }

        return mainNode;
    }

    private <T extends Thing> DefaultMutableTreeNode addBranch(String title,
                                                               ArrayList<T> thingsList) {


        String newThingName, childTitle;
        DefaultMutableTreeNode parentNode, childNode;
        Dock thisDock;
        Ship mooredShip, newShip;


        parentNode = new DefaultMutableTreeNode(title);

        for (T newThing : thingsList) {
            newThingName = newThing.getName() + " (" + newThing.getIndex() + ")";
            childNode = new DefaultMutableTreeNode(newThingName);

            if (newThing instanceof Dock) {
                thisDock = (Dock) newThing;
                mooredShip = thisDock.getShip();

                if (thisDock.getShip() != null) {
                    childTitle = mooredShip.getName() + " (" + mooredShip.getIndex() + ")";
                    childNode.add(new DefaultMutableTreeNode(childTitle));
                }
            } else if (newThing instanceof Ship) {
                newShip = (Ship) newThing;

                if (!newShip.getJobs().isEmpty()) {
                    for (Job newJob : newShip.getJobs()) {
                        childTitle = newJob.getName();
                        childNode.add(new DefaultMutableTreeNode(childTitle));
                    }
                }
            }

            parentNode.add(childNode);
        }

        return parentNode;
    }

    @Override
    public String toString() {
        String stringOutput = ">>>>> The world:\n";

        for (SeaPort seaPort : this.getPorts()) {
            stringOutput += seaPort.toString() + "\n";
        }
        return stringOutput;
    }
}