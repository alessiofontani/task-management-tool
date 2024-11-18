package com.alessiofontani.taskmanagementtool.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {

    GENERIC(0),
    USER_NOT_FOUND(1001),
    INVALID_PASSWORD(1002),
    EMAIL_ALREADY_EXISTS(1003),
    USERNAME_ALREADY_EXISTS(1004),
    PASSWORD_COMPLEXITY_ERROR(1005),
    PASSWORD_MISMATCH_ERROR(1006);

    final int code;

    ErrorCode(int value) {
        this.code = value;
    }

}
