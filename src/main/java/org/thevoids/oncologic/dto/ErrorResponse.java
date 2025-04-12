package org.thevoids.oncologic.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    public String error;
    public String details;

    public ErrorResponse(String error, String details) {
        this.error = error;
        this.details = details;
    }
}
