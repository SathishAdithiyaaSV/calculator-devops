package com.sathish.calculator;

public class DivisionByZeroException extends RuntimeException {
    public DivisionByZeroException() {
        super("Division by zero is not allowed");
    }
}
