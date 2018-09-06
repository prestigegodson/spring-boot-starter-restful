package com.prestigegodson.starter.restfulservice.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Date timestamp;
    private int status;
    private String error;
    private String message;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}