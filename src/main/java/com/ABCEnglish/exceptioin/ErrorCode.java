package com.ABCEnglish.exceptioin;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
@Getter

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(1005,"Role not found", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated Wrong Password", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1009, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    COURSE_NOT_FOUND(1005, "Course not found",HttpStatus.NOT_FOUND),
    DOC_NOT_FOUND(1005, "Doc not found",HttpStatus.NOT_FOUND),
    LESSON_NOT_FOUND(1005, "Lesson not found",HttpStatus.NOT_FOUND),
    EXERCISE_NOT_FOUND(1005, "exercise not found",HttpStatus.NOT_FOUND),
    QUESTION_NOT_FOUND(1005, "question not found",HttpStatus.NOT_FOUND),
    ANSWER_M_CHOICE_NOT_FOUND(1005, "answer not found",HttpStatus.NOT_FOUND),
    FORBIDDEN(1004, "forbidden",HttpStatus.FORBIDDEN),
    SOCIAL_NOT_FOUND(1005, "social not found",HttpStatus.NOT_FOUND),
    TEST_NOT_FOUND(1005, "test not found",HttpStatus.NOT_FOUND),
    COMMENT_NOT_FOUND(1005, "comment not found",HttpStatus.NOT_FOUND),
    UNEXPECTED_ERROR(1006,"Unexpected error data", HttpStatus.BAD_REQUEST ),
    INVALID_DATE_RANGE(1006,"Error Date", HttpStatus.BAD_REQUEST),
    PAYMENT_URL_CREATION_FAILED(1006,"Error creating VNPay payment URL", HttpStatus.BAD_REQUEST),

    ACCOUNT_BANED(1007,"Account Baned please try again after 24h",HttpStatus.NOT_FOUND),
    ACCOUNT_NOT_VERIFIED(1007,"Account not verified please check your email",HttpStatus.NOT_FOUND),
    NOT_APPECT_ROLE(1008,"Not Appect Role",HttpStatus.NOT_FOUND),
    NOT_FOUND_CODE(1008,"Code vetion not found",HttpStatus.NOT_FOUND),
    NOT_VERIFIED(1008,"Not Verified Code",HttpStatus.NOT_FOUND),
    ACCESS_DENIED(1007, "Access denied: You do not have permission to perform this action", HttpStatus.FORBIDDEN),
    ACCOUNT_NOT_VERIFICATION(1007,"Account not verify, please check your email",HttpStatus.BAD_REQUEST),
    RESULT_NOT_FOUND(1005, "result not found",HttpStatus.NOT_FOUND),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
