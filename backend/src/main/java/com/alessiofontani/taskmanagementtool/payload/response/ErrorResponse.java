package com.alessiofontani.taskmanagementtool.payload.response;

import com.alessiofontani.taskmanagementtool.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {

    private int code;
    private String message;

    public ErrorResponse(ErrorCode errorCode, String message) {
        this.code = errorCode.getCode();
        this.message = message;
    }

}
