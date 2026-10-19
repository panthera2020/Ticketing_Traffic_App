package com.lastma.data.models;

public class Offence {
    private int id;
    private String code;
    private String description;
    private int penaltyPoints;
    private int firstOffenceFine;
    private ViolationType violationType;
    private int subsequentOffenceFine;
    private String additionalPenaltyDescription;
    private boolean active;

    public Offence() {}
    public Offence(ViolationType violationType) {
        this.violationType = violationType;
    }
    public ViolationType getViolationType() {
        return violationType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription() {
        this.description = violationType.getDescription();
    }

    public int getPenaltyPoints() {
        return penaltyPoints;
    }

    public void setPenaltyPoints() {
        this.penaltyPoints = violationType.getPoints();
    }

    public int getFirstOffenceFine() {
        return firstOffenceFine;
    }

    public void setFirstOffenceFine() {
        this.firstOffenceFine = violationType.getFirstOffenceFine();
    }

    public int getSubsequentOffenceFine() {
        return subsequentOffenceFine;
    }

    public void setSubsequentOffenceFine() {
        this.subsequentOffenceFine = violationType.getSubsequentOffenceFine();
    }

    public String getAdditionalPenaltyDescription() {
        return additionalPenaltyDescription;
    }

    public void setAdditionalPenaltyDescription() {
        this.additionalPenaltyDescription = violationType.getAdditionalPenalty();
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }



    public boolean isActive() {
        return active;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {this.active = false;}

}
