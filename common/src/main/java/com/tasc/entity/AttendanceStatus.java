package com.tasc.entity;

public enum AttendanceStatus {
    PENDING(0, "Check in without checkout"),
    PRESENT(1, "Attendance success"),
    ABSENT(0, "Attendance fail")
    ;

    public int code;
    public String message;

    AttendanceStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
