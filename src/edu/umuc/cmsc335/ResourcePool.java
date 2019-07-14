package edu.umuc.cmsc335;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

final class ResourcePool {

    private ArrayList<Person> personsInPool;
    private int availablePersons, totalPersons;
    private String skill, parentPort;

    private JPanel rowPanel;
    private JLabel portLabel, skillLabel, countLabel, totalLabel;
    protected ResourcePool(ArrayList<Person> personsInPool, String skill, String parentPort) {
        this.setPersonsInPool(personsInPool);
        this.setTotalPersons(this.getPersonsInPool().size());
        this.setAvailablePersons(this.getPersonsInPool().size());
        this.setSkill(skill);
        this.setParentPort(parentPort);
    }

    private void setPersonsInPool(ArrayList<Person> personsInPool) {
        this.personsInPool = personsInPool;
    }

    private void setTotalPersons(int totalPersons) {
        this.totalPersons = totalPersons;
    }

    private void setAvailablePersons(int availablePersons) {
        this.availablePersons = availablePersons;
    }

    private void setSkill(String skill) {
        this.skill = skill;
    }

    private void setParentPort(String parentPort) {
        this.parentPort = parentPort;
    }

    protected ArrayList<Person> getPersonsInPool() {
        return this.personsInPool;
    }

    protected int getTotalPersons() {
        return this.totalPersons;
    }

    protected int getAvailablePersons() {
        return this.availablePersons;
    }

    protected String getSkill() {
        return this.skill;
    }

    protected String getParentPort() {
        return this.parentPort;
    }

    protected JPanel getPoolAsPanel() {

        String job = this.getSkill().substring(0, 1).toUpperCase() + this.getSkill().substring(1);
        this.rowPanel = new JPanel(new GridLayout(1, 3));
        this.skillLabel = new JLabel(job, JLabel.CENTER);
        this.portLabel = new JLabel(this.getParentPort(), JLabel.CENTER);
        this.countLabel = new JLabel("Available: "
                + String.valueOf(this.getAvailablePersons()), JLabel.CENTER);
        this.totalLabel = new JLabel("Total: "
                + String.valueOf(this.getTotalPersons()), JLabel.CENTER);
        this.skillLabel.setOpaque(true);
        this.portLabel.setOpaque(true);
        this.countLabel.setOpaque(true);
        this.totalLabel.setOpaque(true);
        this.skillLabel.setBackground(Color.WHITE);
        this.portLabel.setBackground(Color.WHITE);
        this.countLabel.setBackground(Color.WHITE);
        this.totalLabel.setBackground(Color.WHITE);
        this.skillLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        this.portLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        this.countLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        this.totalLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        this.rowPanel.add(this.skillLabel);
        this.rowPanel.add(this.portLabel);
        this.rowPanel.add(this.countLabel);
        this.rowPanel.add(this.totalLabel);

        return this.rowPanel;
    }

    protected void addPerson(Person person) {
        this.getPersonsInPool().add(person);
        this.setAvailablePersons(this.getPersonsInPool().size());
        this.setTotalPersons(this.getPersonsInPool().size());
    }

    protected void reservePerson(Person person) {
        person.setIsWorking(true);
        this.setAvailablePersons(this.getAvailablePersons() - 1);
        this.countLabel.setText("Available: " + String.valueOf(this.getAvailablePersons()));
    }


    protected void returnPerson(Person person) {
        person.setIsWorking(false);
        this.setAvailablePersons(this.getAvailablePersons() + 1);
        this.countLabel.setText("Available: " + String.valueOf(this.getAvailablePersons()));
    }
}