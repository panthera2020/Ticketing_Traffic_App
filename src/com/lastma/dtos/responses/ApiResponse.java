package com.lastma.dtos.responses;

public class ApiResponse {
    private boolean status;
    private Object data;

    public ApiResponse(boolean status, Object data) {
        this.status = status;
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
