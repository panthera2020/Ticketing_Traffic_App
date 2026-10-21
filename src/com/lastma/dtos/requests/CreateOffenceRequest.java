package com.lastma.dtos.requests;

import com.lastma.data.models.ViolationType;

public class CreateOffenceRequest {
    private ViolationType violationType;
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ViolationType getViolationType() {
        return violationType;
    }

    public void setViolationType(ViolationType violationType) {
        this.violationType = violationType;
    }



}
