package org.easydate;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

class FindDateTest {
    @Test
    void parseTextDate() {
        String[] ds = {"New year day", "Martin Luther King Junior day", "Thanksgiving", "Christmas",
                "March tenth", "February second", "December twenty fifth", "Fourth July",
                "today", "tomorrow", "yesterday", "next week", "next month"};
        for (String d : ds) {
            System.out.println(d + ": " + FindDate.parse(d));
        }
    }

    @Test
    void datize() {
        String source = "Do you have tix on tomorrow, april fifth, june twenty fifth, fourth of july and christmas with two adults and five kids?";
        System.out.println(FindDate.datize(source));
    }

    @Test
    void findDates() {
        String source = "Do you have tickets for March tenth, June twenty first, first January, Fourth of July,"
                + "Thanksgiving, Christmas day, 2025-12-31, today and tomorrow for two adults and 1 kid?";
        List<LocalDate> dates = FindDate.findDates(source);
        System.out.println(dates);
    }

    @Test
    void supportedHolidays() {
        System.out.println(FindDate.supportedHolidays());
    }
}
