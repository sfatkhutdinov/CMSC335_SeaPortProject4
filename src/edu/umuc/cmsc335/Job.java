package edu.umuc.cmsc335;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;

final class Job extends Thing implements Runnable {

    private double duration;
    private ArrayList<String> requirements;
    private enum Status {RUNNING, SUSPENDED, WAITING, DONE}
    private Ship parentShip;
    private SeaPort parentPort;
    private boolean isSuspended, isCancelled, isFinished, isEndex;
    private Status status;
    private Thread jobThread;
    private ArrayList<Person> workers;
    private JTextArea statusLog;
    private JButton cancelButton, suspendButton;
    private JProgressBar jobProgress;
    private JPanel rowPanel;
    private JLabel rowLabel;
    protected Job(Scanner scannerContents, HashMap<Integer, Ship> shipsMap) {
        super(scannerContents);
        if (scannerContents.hasNextDouble()) {
            this.setDuration(scannerContents.nextDouble());
        }

        this.setRequirements(new ArrayList<>());
        while (scannerContents.hasNext()) {
            this.getRequirements().add(scannerContents.next());
        }

        this.setParentShip(shipsMap.get(this.getParent()));
        this.setParentPort(this.getParentShip().getPort());
        this.setIsSuspended(false);
        this.setIsCancelled(false);
        this.setIsFinished(false);
        this.setStatus(Status.SUSPENDED);
        this.setJobThread(new Thread(this, this.getName() + " (" + this.getParentShip().getName()
                + ")"));
        this.setIsEndex(false);
    }

    private void setDuration(double duration) {
        this.duration = duration;
    }

    private void setRequirements(ArrayList<String> requirements) {
        this.requirements = requirements;
    }

    private void setParentShip(Ship parentShip) {
        this.parentShip = parentShip;
    }

    private void setParentPort(SeaPort parentPort) {
        this.parentPort = parentPort;
    }

    protected void setStatusLog(JTextArea statusLog) {
        this.statusLog = statusLog;
    }

    private void setWorkers(ArrayList<Person> workers) {
        this.workers = workers;
    }

    private void setIsSuspended(boolean isSuspended) {
        this.isSuspended = isSuspended;
    }

    private void setIsCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    private void setIsFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    private void setStatus(Status status) {
        this.status = status;
    }

    private void setJobThread(Thread jobThread) {
        this.jobThread = jobThread;
    }

    private void setIsEndex(boolean isEndex) {
        this.isEndex = isEndex;
    }

    protected double getDuration() {
        return this.duration;
    }

    protected ArrayList<String> getRequirements() {
        return this.requirements;
    }

    protected Ship getParentShip() {
        return this.parentShip;
    }

    protected SeaPort getParentPort() {
        return this.parentPort;
    }

    protected JTextArea getStatusLog() {
        return this.statusLog;
    }

    protected ArrayList<Person> getWorkers() {
        return this.workers;
    }

    protected boolean getIsSuspended() {
        return this.isSuspended;
    }

    protected boolean getIsCancelled() {
        return this.isCancelled;
    }

    protected boolean getIsFinished() {
        return this.isFinished;
    }

    protected Status getStatus() {
        return this.status;
    }

    protected Thread getJobThread() {
        return this.jobThread;
    }

    private boolean getIsEndex() {
        return this.isEndex;
    }

    protected JPanel getJobAsPanel() {

        this.rowPanel = new JPanel(new GridLayout(1, 4));

        this.rowLabel = new JLabel(this.getName() + " (" + this.getParentShip().getName() + ")",
                JLabel.CENTER);
        this.rowLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        this.jobProgress = new JProgressBar();
        this.jobProgress.setStringPainted(true);

        this.suspendButton = new JButton("Suspend");
        this.cancelButton = new JButton("Cancel");

        this.suspendButton.addActionListener((ActionEvent e) -> {
            this.toggleSuspendFlag();
        });

        this.cancelButton.addActionListener((ActionEvent e) -> {
            this.toggleCancelFlag();
        });

        this.rowPanel.add(this.rowLabel);
        this.rowPanel.add(this.jobProgress);
        this.rowPanel.add(this.suspendButton);
        this.rowPanel.add(this.cancelButton);

        return this.rowPanel;
    }

    protected void startJob() {
        this.setIsFinished(false);
        this.getJobThread().start();
    }

    protected void endJob() {
        this.toggleCancelFlag();
        this.setIsEndex(true);
    }

    private void toggleSuspendFlag() {
        this.setIsSuspended(!this.getIsSuspended());
    }

    private void toggleCancelFlag() {
        this.setIsCancelled(true);
        this.setIsFinished(true);
    }

    private void showStatus(Status status) {
        switch(status){
            case RUNNING:
                this.suspendButton.setBackground(Color.GREEN);
                this.suspendButton.setText("Running");
                break;
            case SUSPENDED:
                this.suspendButton.setBackground(Color.YELLOW);
                this.suspendButton.setText("Suspended");
                break;
            case WAITING:
                this.suspendButton.setBackground(Color.ORANGE);
                this.suspendButton.setText("Waiting");
                break;
            case DONE:
                this.suspendButton.setBackground(Color.RED);
                this.suspendButton.setText("Done");
                break;
        }
    }

    private synchronized boolean isWaiting() {

        ArrayList<Person> dockWorkers;

        if (this.getParentPort().getQue().contains(this.getParentShip())) {
            return true;
        } else {
            if (!this.getRequirements().isEmpty()) {
                dockWorkers = this.getParentPort().getResources(this);
                if (dockWorkers == null) {
                    return true;
                } else {
                    this.setWorkers(dockWorkers);
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    @Override
    public void run() {

        long time, startTime, stopTime;
        double timeNeeded;
        ArrayList<Boolean> statusList;
        Ship newShipToMoor;
        Dock parentDock;
        String workerLogLine;

        time = System.currentTimeMillis();
        startTime = time;
        stopTime = time + 1000 * (long) this.getDuration();
        timeNeeded = stopTime - time;
        workerLogLine = "";

        synchronized(this.getParentPort()) {
            while (this.isWaiting()) {
                try {
                    this.showStatus(Status.WAITING);
                    this.getParentPort().wait();
                } catch (InterruptedException e) {
                    System.out.println("Error: " + e);
                }
            }
        }

        while (time < stopTime && !this.getIsCancelled()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Error: " + e);
            }

            if (!this.getIsSuspended()) {
                this.showStatus(Status.RUNNING);
                time += 100;
                this.jobProgress.setValue((int) (((time - startTime) / timeNeeded) * 100));
            } else {
                this.showStatus(Status.SUSPENDED);
            }

            if (this.getIsEndex()) {
                return;
            }
        }

        if (!this.getIsSuspended()) {
            this.jobProgress.setValue(100);
            this.showStatus(Status.DONE);
            this.setIsFinished(true);
        }

        synchronized(this.getParentPort()) {

            if (!this.getRequirements().isEmpty() && !this.getWorkers().isEmpty()) {
                workerLogLine += this.getName() + " (" + this.getParentShip().getName()
                        + ") returning";

                for (int i = 0; i < this.getWorkers().size(); i++) {
                    if (i == 0) {
                        workerLogLine += " ";
                    } else if (i < this.getWorkers().size() - 1) {
                        workerLogLine += ", ";
                    } else {
                        workerLogLine += " & ";
                    }
                    workerLogLine += this.getWorkers().get(i).getName();
                }
                this.getStatusLog().append(workerLogLine + "\n");

                this.getParentPort().returnResources(this.getWorkers());
            }

            statusList = new ArrayList<>();
            this.getParentShip().getJobs().forEach((job) -> {
                statusList.add(job.getIsFinished());
            });

            if (!statusList.contains(false)) {
                this.getStatusLog().append("Unmooring " + this.getParentShip().getName() + " from "
                        + this.getParentShip().getDock().getName() + " in "
                        + this.getParentPort().getName()+ "\n");

                while (!this.getParentPort().getQue().isEmpty()) {
                    newShipToMoor = this.getParentPort().getQue().remove(0);

                    if (!newShipToMoor.getJobs().isEmpty()) {
                        parentDock = this.getParentShip().getDock();
                        parentDock.setShip(newShipToMoor);
                        newShipToMoor.setDock(parentDock);

                        this.getStatusLog().append("Mooring " + newShipToMoor.getName() + " at "
                                + parentDock.getName() + " in " + this.getParentPort().getName()
                                + "\n");
                        break;
                    }
                }
            }

            this.getParentPort().notifyAll();
        }
    }

    @Override
    public String toString() {
        String stringOutput;

        stringOutput = "\t\t" + super.toString() + "\n\t\tDuration: " + this.getDuration()
                + "\n\t\tRequirements:";

        if (this.getRequirements().isEmpty()) {
            stringOutput += "\n\t\t\t - None";
        } else {
            for(String requiredSkill : this.getRequirements()){
                stringOutput += "\n\t\t\t - " + requiredSkill;
            }
        }

        return stringOutput;
    }
}