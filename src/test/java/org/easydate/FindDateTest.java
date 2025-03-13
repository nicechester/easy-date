package org.easydate;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

class FindDateTest {
    @Test
    void parseTextDate() {
        String[] ds = {"March tenth", "February second", "December twenty fifth", "Fourth July",
                "today", "tomorrow", "yesterday", "next week", "next month"};
        for (String d : ds) {
            System.out.println(FindDate.parse(d));
        }
    }

    @Test
    void datize() {
        String source = "Do you have tix on tomorrow, april fifth, june twenty fifth and fourth of july with two adults and five kids?";
        System.out.println(FindDate.datize(source));
    }

    @Test
    void findDates() {
        String source = "Do you have tickets for March tenth, June twenty first, first January, Fourth of July,"
                + " 2025-12-25, today and tomorrow for two adults and 1 kid?";
        List<LocalDate> dates = FindDate.findDates(source);
        System.out.println(dates);
    }
}