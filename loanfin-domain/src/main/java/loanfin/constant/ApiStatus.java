package loanfin.constant;

import lombok.Getter;

@Getter
public enum ApiStatus {
    LOGIN_SUCCESSFUL("Login successful!"),
    LOAN_APPLICATION_SUCCESSFUL("Loan application submitted sucessfully"),
    LOAN_APPLICATION_REVIEW_SUCCESSFUL("Loan application reviewed succesfully");

    private final String message;

    ApiStatus(String message){this.message = message;}
}
