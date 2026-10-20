package com.sathish.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class CalculatorServiceTest {
    private final CalculatorService calculatorService = new CalculatorService();

    @Test void addsNumbers() { assertEquals(new BigDecimal("5"), calculatorService.add(new BigDecimal("2"), new BigDecimal("3"))); }
    @Test void subtractsNumbers() { assertEquals(new BigDecimal("-1"), calculatorService.subtract(new BigDecimal("2"), new BigDecimal("3"))); }
    @Test void multipliesNumbers() { assertEquals(new BigDecimal("6"), calculatorService.multiply(new BigDecimal("2"), new BigDecimal("3"))); }
    @Test void dividesNumbers() { assertEquals(new BigDecimal("2.5"), calculatorService.divide(new BigDecimal("5"), new BigDecimal("2"))); }
    @Test void rejectsDivisionByZero() { assertThrows(DivisionByZeroException.class, () -> calculatorService.divide(BigDecimal.ONE, BigDecimal.ZERO)); }
}
