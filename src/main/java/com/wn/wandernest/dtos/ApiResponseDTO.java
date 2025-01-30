package com.wn.wandernest.dtos;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponseDTO {

    private Status status; // Enum for status
    private String message;
    private Object data;
    private List<FieldError> errors;

    // Constructor for success, warning and info with data
    public ApiResponseDTO(Status status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.errors = null;
    }

    // Constructor for error
    public ApiResponseDTO(Status status, String message, List<FieldError> errors) {
        this.status = status;
        this.message = message;
        this.data = null;
        this.errors = errors;
    }

    // Enum for Status
    public enum Status {
        SUCCESS("success"),
        ERROR("error"),
        WARNING("warning"),
        INFO("info");

        private String value;

        Status(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @Getter
    @Setter
    public static class FieldError {
        private String field;
        private String error;
    }
}
