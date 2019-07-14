package edu.umuc.cmsc335;

import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class SeaPortProgram extends JFrame {

    private World world;


    private String title;
    private int width, height;

    private JFrame mainFrame;
    private JTree mainTree;
    private JTextArea searchResultsTextArea, jobsStatusTextArea;
    private JScrollPane treeScrollPane, searchResultsScrollPane, jobsScrollPane,
            jobsStatusScrollPane, jobsPoolScrollPane;
    private JPanel mainPanel, optionsPanel, worldPanel, treePanel, treeButtonsPanel, jobsPanel,
            jobsScrollPanePanel, jobsLogsPanel, jobsPoolTablePanel;
    private JButton readButton, searchButton, sortButton, treeDetailsButton, treeExpandButton,
            treeCollapseButton;
    private JLabel searchTextLabel, sortTextLabel;
    private JTextField searchTextField;
    private String[] searchComboBoxValues, sortPortComboBoxValues, sortTargetComboBoxValues,
            sortTypeComboBoxValues;
    private JComboBox<String> searchComboBox, sortPortComboBox, sortTargetComboBox,
            sortTypeComboBox;

    private JFileChooser fileChooser;
    private Scanner scannerContents;

    protected SeaPortProgram() {
        super("SeaPortProgram");
        this.setWindowTitle("SeaPortProgram");
        this.setWindowWidth(1280);
        this.setWindowHeight(720);
    }

    protected SeaPortProgram(String title, int width, int height) {
        super(title);
        this.setWindowTitle(title);
        this.setWindowWidth(width);
        this.setWindowHeight(height);
    }

    private void setWindowTitle(String title) {
        this.title = title;
    }

    private void setWindowWidth(int width) {
        if (width < 1280) {
            this.width = 1280;
        } else {
            this.width = width;
        }
    }

    private void setWindowHeight(int height) {
        if (height < 720) {
            this.height = 720;
        } else {
            this.height = height;
        }
    }

    protected String getWindowTitle() {
        return this.title;
    }

    protected int getWindowWidth() {
        return this.width;
    }

    protected int getWindowHeight() {
        return this.height;
    }

    private void constructGUI() {

        this.mainPanel = new JPanel(new BorderLayout());
        this.optionsPanel = new JPanel(new GridLayout(1, 10, 5, 5));
        this.worldPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        this.treePanel = new JPanel(new BorderLayout());
        this.treeButtonsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        this.jobsPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        this.jobsScrollPanePanel = new JPanel(new GridLayout(0, 1));
        this.jobsLogsPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        this.jobsPoolTablePanel = new JPanel(new GridLayout(0, 1));

        this.readButton = new JButton("Read");
        this.searchButton = new JButton("Search");
        this.sortButton = new JButton("Sort");

        this.searchTextField = new JTextField("", 10);
        this.searchTextLabel = new JLabel("Search:", JLabel.RIGHT);

        this.sortTextLabel = new JLabel("Sort:", JLabel.RIGHT);

        this.sortPortComboBoxValues = new String[] {
                "All ports"
        };
        this.sortPortComboBox = new JComboBox<>(this.sortPortComboBoxValues);

        this.searchComboBoxValues = new String[] {
                "By name",
                "By index",
                "By skill"
        };
        this.searchComboBox = new JComboBox<>(this.searchComboBoxValues);

        this.sortTargetComboBoxValues = new String[] {
                "Que",
                "Ships",
                "Docks",
                "Persons",
                "Jobs"
        };
        this.sortTargetComboBox = new JComboBox<>(this.sortTargetComboBoxValues);

        this.sortTypeComboBoxValues = new String[] {
                "By name",
                "By weight",
                "By length",
                "By width",
                "By draft"
        };
        this.sortTypeComboBox = new JComboBox<>(this.sortTypeComboBoxValues);

        this.optionsPanel.add(this.readButton);
        this.optionsPanel.add(this.searchTextLabel);
        this.optionsPanel.add(this.searchTextField);
        this.optionsPanel.add(this.searchComboBox);
        this.optionsPanel.add(this.searchButton);
        this.optionsPanel.add(this.sortTextLabel);
        this.optionsPanel.add(this.sortPortComboBox);
        this.optionsPanel.add(this.sortTargetComboBox);
        this.optionsPanel.add(this.sortTypeComboBox);
        this.optionsPanel.add(this.sortButton);

        this.mainTree = new JTree();
        this.mainTree.setModel(null);
        this.mainTree.getSelectionModel().setSelectionMode(
                TreeSelectionModel.SINGLE_TREE_SELECTION);

        this.treeScrollPane = new JScrollPane(this.mainTree);

        this.treeExpandButton = new JButton("Expand all");
        this.treeCollapseButton = new JButton("Collapse all");
        this.treeDetailsButton = new JButton("More info");

        this.treeButtonsPanel.add(this.treeExpandButton);
        this.treeButtonsPanel.add(this.treeCollapseButton);
        this.treeButtonsPanel.add(this.treeDetailsButton);

        this.treePanel.add(this.treeScrollPane, BorderLayout.CENTER);
        this.treePanel.add(this.treeButtonsPanel, BorderLayout.SOUTH);

        this.searchResultsTextArea = new JTextArea();
        this.searchResultsTextArea.setEditable(false);
        this.searchResultsTextArea.setFont(new Font("Monospaced", 0, 12));
        this.searchResultsTextArea.setLineWrap(true);

        this.searchResultsScrollPane = new JScrollPane(this.searchResultsTextArea);

        this.worldPanel.add(this.treePanel);
        this.worldPanel.add(this.searchResultsScrollPane);

        this.jobsStatusTextArea = new JTextArea();
        this.jobsStatusTextArea.setEditable(false);
        this.jobsStatusTextArea.setFont(new Font("Monospaced", 0, 11));
        this.jobsStatusTextArea.setLineWrap(true);
        this.jobsScrollPane = new JScrollPane(this.jobsScrollPanePanel);
        this.jobsStatusScrollPane = new JScrollPane(this.jobsStatusTextArea);
        this.jobsPoolScrollPane = new JScrollPane(this.jobsPoolTablePanel);

        this.jobsLogsPanel.add(this.jobsStatusScrollPane);
        this.jobsLogsPanel.add(this.jobsPoolScrollPane);
        this.jobsPanel.add(this.jobsScrollPane);
        this.jobsPanel.add(this.jobsLogsPanel);

        this.mainPanel.add(this.optionsPanel, BorderLayout.NORTH);
        this.mainPanel.add(this.worldPanel, BorderLayout.WEST);
        this.mainPanel.add(this.jobsPanel, BorderLayout.CENTER);

        this.optionsPanel.setBorder(BorderFactory.createTitledBorder("Options"));
        this.treePanel.setBorder(BorderFactory.createTitledBorder("World tree"));
        this.jobsScrollPane.setBorder(BorderFactory.createTitledBorder("Jobs Listing"));
        this.searchResultsScrollPane.setBorder(BorderFactory.createTitledBorder("Search/sort log"));
        this.jobsStatusScrollPane.setBorder(BorderFactory.createTitledBorder("Job status log"));
        this.jobsPoolScrollPane.setBorder(BorderFactory.createTitledBorder("Job resource pool"));
        this.mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.jobsStatusTextArea.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        this.searchResultsTextArea.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        this.jobsScrollPanePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        this.jobsScrollPanePanel.setBackground(Color.WHITE);
        this.jobsPoolTablePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        this.jobsPoolTablePanel.setBackground(Color.WHITE);

        this.sortTargetComboBox.addActionListener((ActionEvent e) -> {
            this.provideProperSortOptions();
        });

        this.readButton.addActionListener((ActionEvent e) -> {
            this.readFileContents();
        });

        this.searchButton.addActionListener((ActionEvent e) -> {
            this.searchWorldContents();
        });

        this.sortButton.addActionListener((ActionEvent e) -> {
            this.sortWorldContents();
        });

        this.treeExpandButton.addActionListener((ActionEvent e) -> {
            this.toggleNodes("expandRow");
        });

        this.treeCollapseButton.addActionListener((ActionEvent e) -> {
            this.toggleNodes("collapseRow");
        });

        this.treeDetailsButton.addActionListener((ActionEvent e) -> {
            this.displaySelectionDetails();
        });

        this.mainFrame = new JFrame(this.getWindowTitle());
        this.mainFrame.setContentPane(this.mainPanel);
        this.mainFrame.setSize(this.getWindowWidth(), this.getWindowHeight());
        this.mainFrame.setVisible(true);
        this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void provideProperSortOptions() {
        this.sortTypeComboBox.removeAllItems();
        this.sortTypeComboBox.addItem("By name");
        if (this.sortTargetComboBox.getSelectedIndex() == 0) {
            this.sortTypeComboBox.addItem("By weight");
            this.sortTypeComboBox.addItem("By width");
            this.sortTypeComboBox.addItem("By length");
            this.sortTypeComboBox.addItem("By draft");
        }
    }

    private void provideProperSeaPortSortOptions() {
        this.sortPortComboBox.removeAllItems();
        this.sortPortComboBox.addItem("All ports");
        Collections.sort(this.world.getPorts(), new ThingComparator("By name"));
        if (this.world.getPorts().size() > 1) {
            for (SeaPort newPort : this.world.getPorts()) {
                this.sortPortComboBox.addItem(newPort.getName());
            }
        }
    }

    private void readFileContents() {


        int selection;
        FileReader fileReader;
        FileNameExtensionFilter filter;


        this.fileChooser = new JFileChooser(".");
        fileReader = null;

        filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        this.fileChooser.setFileFilter(filter);

        selection = this.fileChooser.showOpenDialog(new JFrame());

        if (selection == JFileChooser.APPROVE_OPTION) {
            try {
                fileReader = new FileReader(this.fileChooser.getSelectedFile());
                this.scannerContents = new Scanner(fileReader);
            } catch (FileNotFoundException ex) {
                this.displayErrorPopup("Error: No such file found. Please try again.");
            }
        }

        if (fileReader == null) {
            return;
        }

        if (this.world != null) {
            this.removeOldData();
            this.clearAllJobs();
        }

        this.world = new World(this.scannerContents);

        if (this.world.getAllThings().isEmpty()) {
            this.removeOldData();
            this.clearAllJobs();
            this.world = null;
            this.displayErrorPopup("Error: File data may be empty or corrupted. Please try again.");
        } else {
            this.mainTree.setModel(new DefaultTreeModel(this.world.toTree()));
            this.provideProperSeaPortSortOptions();
            this.addAllResourcePools();
            this.startAllJobs();
        }
    }

    private void removeOldData() {
        this.jobsStatusTextArea.setText("");
        this.searchResultsTextArea.setText("");
        this.jobsScrollPanePanel.removeAll();
        this.jobsPoolTablePanel.removeAll();
        this.mainTree.setModel(null);
    }

    private void searchWorldContents() {

        if (this.world == null || this.scannerContents == null) {
            this.displayErrorPopup("Error: No world initialized. Please try again.");
            return;
        }

        String resultsString, searchText;
        int dropdownSelection;

        resultsString = "";
        searchText = this.searchTextField.getText().trim();
        dropdownSelection = this.searchComboBox.getSelectedIndex();

        if (searchText.equals("")) {
            this.displayErrorPopup("Error: No search terms included. Please try again.");
            return;
        }


        switch(dropdownSelection) {
            case 0:
            case 1:
                resultsString = this.assembleResults(dropdownSelection, searchText);
                this.displayStatus(resultsString, searchText);
                break;
            case 2:
                for (SeaPort port : this.world.getPorts()) {
                    for (Person person : port.getPersons()) {
                        if (person.getSkill().equals(searchText)) {
                            resultsString += "> " + person.getName() + " (id #" + person.getIndex()
                                    + ")\n";
                        }
                    }
                }
                this.displayStatus(resultsString, searchText);
                break;
            default:
                break;
        }
    }

    private String assembleResults(int index, String target) {

        Method getParam;
        String parameter, methodName;
        String resultsString = "";

        methodName = (index == 0) ? "getName" : "getIndex";

        try {
            getParam = Thing.class.getDeclaredMethod(methodName);

            for (Thing item : this.world.getAllThings()) {

                parameter = "" + getParam.invoke(item);

                if (parameter.equals(target)) {
                    resultsString += "> " + item.getName() + " " + item.getIndex() + " ("
                            + item.getClass().getSimpleName() + ")\n";
                }
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
        return resultsString;
    }

    private void displayStatus(String resultsString, String target) {
        if (resultsString.equals("")) {
            this.searchResultsTextArea.append("Warning: '" + target + "' not found.\n\n");
        } else {
            this.searchResultsTextArea.append("Search results for '" + target + "'\n"
                    + resultsString + "\n");
        }
    }


    @SuppressWarnings("unchecked")
    private void sortWorldContents() {

        if (this.world == null || this.scannerContents == null) {
            this.displayErrorPopup("Error: No world initialized. Please try again.");
            return;
        }

        String sortPort, sortTarget, sortType, result, fieldMethodName, listMethodName;
        Method getField, getList;
        ArrayList<Thing> thingsList, gottenList;
        HashMap<String, String> typeMethodMap, targetMethodMap;

        typeMethodMap = new HashMap<String, String>() {{
            put("By name", "getIndex");
            put("By weight", "getWeight");
            put("By length", "getLength");
            put("By width", "getWidth");
            put("By draft", "getDraft");
        }};

        targetMethodMap = new HashMap<String, String>() {{
            put("Que", "getQue");
            put("Ships", "getShips");
            put("Docks", "getDocks");
            put("Persons", "getPersons");
            put("Jobs", "getShips");
        }};

        // Definitions
        sortPort = this.sortPortComboBox.getSelectedItem().toString();
        sortTarget = this.sortTargetComboBox.getSelectedItem().toString();
        sortType = this.sortTypeComboBox.getSelectedItem().toString();
        fieldMethodName = typeMethodMap.get(sortType);
        listMethodName = targetMethodMap.get(sortTarget);
        result = "";
        thingsList = new ArrayList<>();

        try {
            getList = SeaPort.class.getDeclaredMethod(listMethodName);

            if (sortTarget.equals("Que") && !sortType.equals("By name")) {
                getField = Ship.class.getDeclaredMethod(fieldMethodName);
            } else {
                getField = Thing.class.getDeclaredMethod(fieldMethodName);
            }

            if (sortPort.equals("All ports")) {
                sortPort = sortPort.toLowerCase();
                for (SeaPort newPort : world.getPorts()) {
                    gottenList = (ArrayList<Thing>) getList.invoke(newPort);
                    thingsList.addAll(gottenList);
                }
            } else {
                for (SeaPort newPort : this.world.getPorts()) {
                    if (newPort.getName().equals(sortPort)) {
                        gottenList = (ArrayList<Thing>) getList.invoke(newPort);
                        thingsList.addAll(gottenList);
                    }
                }
            }

            if (sortTarget.equals("Jobs")) {
                ArrayList<Job> jobsList = new ArrayList<>();

                for (Iterator<Thing> iterator = thingsList.iterator(); iterator.hasNext();) {
                    Ship newShip = (Ship) iterator.next();
                    for (Job newJob : newShip.getJobs()) {
                        jobsList.add(newJob);
                    }
                }
                thingsList.clear();
                thingsList.addAll(jobsList);
            }


            if (thingsList.isEmpty()) {
                result += "> No results found.\n";
            } else {

                Collections.sort(thingsList, new ThingComparator(sortType));

                for (Thing newThing : thingsList) {
                    result += "> " + newThing.getName() + " (" + getField.invoke(newThing) + ")\n";
                }
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

        this.searchResultsTextArea.append("Sort results for '" + sortTarget + " "
                + sortType.toLowerCase() + " in " + sortPort + "'\n" + result + "\n");
    }

    private void toggleNodes(String methodName) {

        if (this.world == null || this.scannerContents == null) {
            this.displayErrorPopup("Error: No world initialized. Please try again.");
            return;
        }


        Method toggleRow;

        try {
            toggleRow = JTree.class.getDeclaredMethod(methodName, Integer.TYPE);

            for (int i = 0; i < this.mainTree.getRowCount(); i++) {
                toggleRow.invoke(this.mainTree, i);
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

    private void displaySelectionDetails() {

        if (this.world == null || this.scannerContents == null) {
            this.displayErrorPopup("Error: No world initialized. Please try again.");
            return;
        } else if (this.mainTree.getLastSelectedPathComponent() == null) {
            this.displayErrorPopup("Error: Please select an element from the tree above.");
            return;
        } else if (Arrays.asList(new String[] {"Que", "Docks", "Ships", "Persons", "World"})
                .contains(this.mainTree.getLastSelectedPathComponent().toString())) {
            this.displayErrorPopup("Error: Improper selection. Please select a different node.");
            return;
        }

        String userSelection;
        JTable resultsTable;
        HashMap<String, String> defaultValues, shipValues, passengerShipValues, cargoShipValues,
                personValues;
        LinkedHashMap<String, String> applicableFields;
        Object[][] results;
        String[] columnNames, nameIndexArray;
        int counter;

        userSelection = this.mainTree.getLastSelectedPathComponent().toString();
        nameIndexArray = userSelection.split(" ");
        userSelection = nameIndexArray[0].trim();
        applicableFields = new LinkedHashMap<>();
        columnNames = new String[] {"Field", "Value"};
        counter = 0;

        defaultValues = new HashMap<String, String>() {{
            put("ID", "getIndex");
            put("Name", "getName");
        }};

        shipValues = new HashMap<String, String>() {{
            put("Weight", "getWeight");
            put("Length", "getLength");
            put("Width", "getWidth");
            put("Draft", "getDraft");
        }};

        passengerShipValues = new HashMap<String, String>() {{
            put("Total rooms", "getNumberOfRooms");
            put("Occupied rooms", "getNumberOfOccupiedRooms");
            put("Passengers", "getNumberOfPassengers");
        }};

        cargoShipValues = new HashMap<String, String>() {{
            put("Cargo volume", "getCargoVolume");
            put("Cargo value", "getCargoValue");
            put("Cargo weight", "getCargoWeight");
        }};

        personValues = new HashMap<String, String>() {{
            put("Occupation", "getSkill");
        }};

        for (Thing newThing : this.world.getAllThings()) {
            if (newThing.getName().equals(userSelection)) {
                applicableFields.putAll(this.constructResults(newThing, Thing.class,
                        defaultValues));

                if (newThing instanceof Ship) {
                    applicableFields.putAll(this.constructResults(newThing, Ship.class,
                            shipValues));

                    if (newThing instanceof PassengerShip) {
                        applicableFields.putAll(this.constructResults(newThing, PassengerShip.class,
                                passengerShipValues));
                    } else if (newThing instanceof CargoShip) {
                        applicableFields.putAll(this.constructResults(newThing, CargoShip.class,
                                cargoShipValues));
                    }
                } else if (newThing instanceof Person) {
                    applicableFields.putAll(this.constructResults(newThing, Person.class,
                            personValues));
                }
            }
        }

        results = new Object[applicableFields.size()][2];
        for (HashMap.Entry<String,String> entry : applicableFields.entrySet()) {
            results[counter][0] = entry.getKey();
            results[counter][1] = entry.getValue();
            counter++;
        }
        resultsTable = new JTable(results, columnNames);
        resultsTable.setPreferredScrollableViewportSize(resultsTable.getPreferredSize());
        resultsTable.setFillsViewportHeight(true);
        JOptionPane.showMessageDialog(this.mainFrame, new JScrollPane(resultsTable), userSelection,
                JOptionPane.PLAIN_MESSAGE);
    }

    @SuppressWarnings("unchecked")
    private <T extends Thing> HashMap<String, String> constructResults(T newThing,
                                                                       Class className, HashMap<String, String> values) {

        HashMap<String, String> resultsMap;
        Method getAttribute;

        resultsMap = new HashMap<>();

        try {
            for (HashMap.Entry<String, String> row : values.entrySet()) {

                String displayText, methodName, methodResult;
                Object getAttributeResult;
                displayText = row.getKey();
                methodName = row.getValue();

                getAttribute = className.getDeclaredMethod(methodName);
                getAttributeResult = getAttribute.invoke(newThing);

                if (getAttributeResult instanceof String) {
                    methodResult = (String) getAttributeResult;
                } else {
                    methodResult = String.valueOf(getAttributeResult);
                }

                resultsMap.put(displayText, methodResult);
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

        return resultsMap;
    }

    private void startAllJobs() {
        for (SeaPort port : this.world.getPorts()) {
            for (Dock dock : port.getDocks()) {
                if (dock.getShip().getJobs().isEmpty()) {
                    this.jobsStatusTextArea.append("Unmooring " + dock.getShip().getName()
                            + " from " + dock.getName() + " in " + port.getName() + "\n");
                    dock.setShip(null);

                    while (!port.getQue().isEmpty()) {
                        Ship newShip = port.getQue().remove(0);
                        if (!newShip.getJobs().isEmpty()) {
                            dock.setShip(newShip);
                            this.jobsStatusTextArea.append("Mooring " + dock.getShip().getName()
                                    + " at " + dock.getName() + " in " + port.getName() + "\n");
                            break;
                        }
                    }
                }

                dock.getShip().setDock(dock);
            }

            for (Ship ship : port.getShips()) {
                if (!ship.getJobs().isEmpty()) {
                    for (Job job : ship.getJobs()) {
                        this.jobsScrollPanePanel.add(job.getJobAsPanel());
                        this.jobsScrollPanePanel.revalidate();
                        this.jobsScrollPanePanel.repaint();

                        job.setStatusLog(this.jobsStatusTextArea);
                        job.startJob();
                    }
                }
            }
        }
    }

    private void clearAllJobs() {
        this.jobsScrollPanePanel.removeAll();
        this.world.getAllThings().forEach((thing) -> {
            if (thing instanceof Job) {
                ((Job) thing).endJob();
            }
        });
    }

    private void addAllResourcePools() {
        for (SeaPort port : this.world.getPorts()) {
            port.divideWorkersBySkill();
            for (HashMap.Entry<String, ResourcePool> pair : port.getResourcePools().entrySet()) {
                JPanel row = pair.getValue().getPoolAsPanel();
                this.jobsPoolTablePanel.add(row);
            }
        }
    }

    private void displayErrorPopup(String message) {
        JOptionPane.showMessageDialog(this.mainFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SeaPortProgram newCollection = new SeaPortProgram();
        newCollection.constructGUI();
    }
}