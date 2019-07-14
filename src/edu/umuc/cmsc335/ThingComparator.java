package edu.umuc.cmsc335;

import java.util.Comparator;

final class ThingComparator implements Comparator<Thing> {

    private String target;

    protected ThingComparator(String target) {
        this.setTarget(target);
    }

    private void setTarget(String target) {
        this.target = target;
    }

    protected String getTarget() {
        return this.target;
    }


    @Override
    public int compare(Thing thing1, Thing thing2) {

        switch (this.getTarget()) {
            case "By weight":
                if (((Ship) thing1).getWeight() == ((Ship) thing2).getWeight()) {
                    return 0;
                } else if (((Ship) thing1).getWeight() > ((Ship) thing2).getWeight()) {
                    return 1;
                } else {
                    return -1;
                }
            case "By length":
                if (((Ship) thing1).getLength() == ((Ship) thing2).getLength()) {
                    return 0;
                } else if (((Ship) thing1).getLength() > ((Ship) thing2).getLength()) {
                    return 1;
                } else {
                    return -1;
                }
            case "By draft":
                if (((Ship) thing1).getDraft() == ((Ship) thing2).getDraft()) {
                    return 0;
                } else if (((Ship) thing1).getDraft() > ((Ship) thing2).getDraft()) {
                    return 1;
                } else {
                    return -1;
                }
            case "By width":
                if (((Ship) thing1).getWidth() == ((Ship) thing2).getWidth()) {
                    return 0;
                } else if (((Ship) thing1).getWidth() > ((Ship) thing2).getWidth()) {
                    return 1;
                } else {
                    return -1;
                }
            case "By name":
                return thing1.getName().compareTo(thing2.getName());
            default:
                return -1;
        }
    }
}