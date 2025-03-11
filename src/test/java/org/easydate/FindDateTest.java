package org.easydate;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class FindDateTest {
    @Test
    void parseTextDate() {
        String[] ds = {"March tenth", "February second", "December twenty fifth"};
        for (String d : ds) {
            System.out.println(FindDate.parse(d));
        }
    }

    @Test
    void datize() {
        String source = "Do you have tickets for March tenth, June twenty first, January 1, 2025-12-25 for two adults and 1 kid?";
        System.out.println(FindDate.datize(source));
    }

    @Test
    void findDates() {
        String source = "Do you have tickets for March tenth, June twenty first, January 1, 2025-12-25 for two adults and 1 kid?";
        System.out.println(FindDate.findDates(source));
    }

    @Test
    void formatter() {
        LocalDate date = LocalDate.parse("March 10", FindDate.getFormatter());
        System.out.println(date);
    }
}