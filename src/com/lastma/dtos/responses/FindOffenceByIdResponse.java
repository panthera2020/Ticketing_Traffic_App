package com.lastma.dtos.responses;

import com.lastma.data.models.ViolationType;

public class FindOffenceByIdResponse {
    private int id;
    private String code;
    private String description;
    private int penaltyPoints;
    private int firstOffenceFine;
    private ViolationType violationType;
    private int subsequentOffenceFine;
    private String additionalPenaltyDescription;
    private boolean active;

    public ViolationType getViolationType() {
        return violationType;
    }

    public void setViolationType(ViolationType violationType) {
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
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPenaltyPoints() {
        return penaltyPoints;
    }

    public void setPenaltyPoints(int penaltyPoints) {
        this.penaltyPoints = penaltyPoints;
    }

    public int getFirstOffenceFine() {
        return firstOffenceFine;
    }

    public void setFirstOffenceFine(int firstOffenceFine) {
        this.firstOffenceFine = firstOffenceFine;
    }

    public int getSubsequentOffenceFine() {
        return subsequentOffenceFine;
    }

    public void setSubsequentOffenceFine(int subsequentOffenceFine) {
        this.subsequentOffenceFine = subsequentOffenceFine;
    }

    public String getAdditionalPenaltyDescription() {
        return additionalPenaltyDescription;
    }

    public void setAdditionalPenaltyDescription(String additionalPenaltyDescription) {
        this.additionalPenaltyDescription = additionalPenaltyDescription;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


}
