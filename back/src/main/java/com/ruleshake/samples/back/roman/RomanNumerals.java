package com.ruleshake.samples.back.roman;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class RomanNumerals {

    public static int convert(String roman) {
        if (StringUtils.isBlank(roman)) {
            return 0;
        }
        List<Symbol> symbols = roman.codePoints()
            .mapToObj(Character::toString)
            .map(Symbol::valueOf)
            .toList();

        int result = symbols.get(0).getNumber();
        for (int i = 1; i < symbols.size(); i++) {
            Symbol previous = symbols.get(i - 1);
            Symbol current = symbols.get(i);
            Symbol next = i < symbols.size() - 1 ? symbols.get(i + 1) : null;
            if (current.ordinal() <= previous.ordinal() && (next == null || current.ordinal() >= next.ordinal())) {
                result += current.getNumber();
            } else if (current.ordinal() > previous.ordinal()) {
                if (i == 1) {
                    result = 0;
                }
                result += current.getNumber() - previous.getNumber();
            }
        }
        return result;
    }

    public static String convert(int number) {
        if (number <= 0) {
            return "";
        }
        Number data = parse(number);
        int maxUnit = data.maxUnit();
        return getRoman(maxUnit) + convert(number - maxUnit);
    }

    private static String getRoman(int number) {
        Symbol topSymbol = getTopSymbol(number);
        if (topSymbol == null) {
            return Symbol.M.name() + convert(number - Symbol.M.getNumber());
        }

        if (topSymbol.getNumber() == number) {
            return topSymbol.name();
        }

        Symbol downSymbol = topSymbol.previous();
        String partialRoman = downSymbol.name();
        int numberToSubstract = downSymbol.getNumber();

        Symbol previous = downSymbol;
        while (number > topSymbol.getNumber() - previous.getNumber() && previous != Symbol.I) {
            previous = previous.previous();
        }

        if (number == topSymbol.getNumber() - previous.getNumber()) {
            partialRoman = previous.name() + topSymbol.name();
            numberToSubstract = topSymbol.getNumber() - previous.getNumber();
        }

        return partialRoman + convert(number - numberToSubstract);
    }

    private static Symbol getTopSymbol(int number) {
        return Arrays.stream(Symbol.values())
            .filter(s -> s.getNumber() >= number)
            .findFirst()
            .orElse(null);
    }

    private static Number parse(int number) {
        int thousands = number / 1000;
        int hundreds = (number % 1000) / 100;
        int decades = (number % 100) / 10;
        int units = number % 10;
        return new Number(number, thousands * 1000, hundreds * 100, decades * 10, units);
    }

    @Getter
    public enum Symbol {

        I(1),
        V(5),
        X(10),
        L(50),
        C(100),
        D(500),
        M(1000);

        private final int number;

        Symbol(int number) {
            this.number = number;
        }

        public static Symbol getByNumber(int number) {
            return Arrays.stream(values())
                .filter(s -> s.getNumber() == number)
                .findFirst()
                .orElse(null);
        }

        public Symbol previous() {
            return values()[ordinal() - 1];
        }

        public Symbol next() {
            return values()[ordinal() + 1];
        }
    }

    public static record Number(int self, int thousand, int hundred, int decade, int unit) {

        public int maxUnit() {
            if (thousand != 0) {
                return thousand;
            }
            if (hundred != 0) {
                return hundred;
            }
            if (decade != 0) {
                return decade;
            }
            return self;
        }

    }
}
