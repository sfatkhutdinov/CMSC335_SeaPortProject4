package edu.umuc.cmsc335;

final class PortTime {

    private int time;

    protected PortTime(int time) {
        this.setTime(time);
    }

    private void setTime(int time) {
        this.time = time;
    }

    protected int getTime() {
        return this.time;
    }

    @Override
    public String toString() {
        return "Time: " + this.getTime();
    }
}