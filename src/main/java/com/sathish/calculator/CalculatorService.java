package com.sathish.calculator;

import java.math.BigDecimal;
import java.math.MathContext;
import org.springframework.stereotype.Service;

@Service
public class CalculatorService {
    public BigDecimal add(BigDecimal a, BigDecimal b) {
        return a.add(b);
    }

    public BigDecimal subtract(BigDecimal a, BigDecimal b) {
        return a.subtract(b);
    }

    public BigDecimal multiply(BigDecimal a, BigDecimal b) {
        return a.multiply(b);
    }

    public BigDecimal divide(BigDecimal a, BigDecimal b) {
        if (BigDecimal.ZERO.compareTo(b) == 0) {
            throw new DivisionByZeroException();
        }
        return a.divide(b, MathContext.DECIMAL64).stripTrailingZeros();
    }
}
