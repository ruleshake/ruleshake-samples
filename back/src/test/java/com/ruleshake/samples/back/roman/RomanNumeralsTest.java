package com.ruleshake.samples.back.roman;

import org.junit.jupiter.api.Test;

class RomanNumeralsTest {

    @Test
    void test() {

        for (int i = 1; i <= 100; i++) {

            String roman = RomanNumerals.convert(i);
            System.err.println(i + " => " + roman);
        }

    }

    @Test
    void test3() {
        int i = 1030;
        String roman = RomanNumerals.convert(i);
        System.err.println(i + " => " + roman);
    }

    @Test
    void test4() {
        String roman = "DCLXVI";

        int number = RomanNumerals.convert(roman);

        System.err.println(roman + " => " + number);

    }

}