package com.example;

import org.junit.Test;
import static org.junit.Assert.*;

public class CalculatorTest {
    
    @Test
    public void testAdd() {
        Calculator calc = new Calculator();
        assertEquals(15, calc.add(10, 5));
    }
    
    @Test
    public void testSubtract() {
        Calculator calc = new Calculator();
        assertEquals(5, calc.subtract(10, 5));
    }
}