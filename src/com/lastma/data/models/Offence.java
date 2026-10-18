package com.lastma.data.models;

public class Offence {
    private int id;
    private String code;
    private String description;
    private ViolationType violationType;
    private int penaltyPoints;
    private int firstOffenceFine;
    private int subsequentOffenceFine;
    private String additionalPenaltyDescription;
    private boolean active;

    public Offence() {}
    public Offence(ViolationType violationType) {
        this.violationType = violationType;
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

    public String getDescription() {
        return violationType.getDescription();
    }

    public ViolationType getViolationType() {
        return violationType;
    }

    public void setViolationType(ViolationType violationType) {
        this.violationType = violationType;
    }

    public int getPenaltyPoints() {
        return violationType.getPoints();
    }

    public int getFirstOffenceFine() {
        return violationType.getFirstOffenceFine();
    }

    public int getSubsequentOffenceFine() {
        return violationType.getSubsequentOffenceFine();
    }

    public String getAdditionalPenaltyDescription() {
        return violationType.getAdditionalPenalty();
    }

    public boolean isActive() {
        return active;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }



}
