package org.easydate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindDate {
    private static DateTimeFormatter TEXT_DATE_FORMAT1 = new DateTimeFormatterBuilder()
            .appendPattern("MMMM d")
            .parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear())
            .toFormatter(Locale.ENGLISH);
    private static DateTimeFormatter TEXT_DATE_FORMAT2 = new DateTimeFormatterBuilder()
            .appendPattern("d MMMM")
            .parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear())
            .toFormatter(Locale.ENGLISH);
    private static final String MONTHS="(january|february|march|april|may|june|july|august|september|october|november|december)";

    /**
     * parse text based date string.
     * @param dateString
     * @return
     */
    public static LocalDate parse(String dateString) {
        String numerized = Numerizer.numerize(dateString);
        switch (dateString.toLowerCase()) {
            case "tomorrow":
                return LocalDate.now().plusDays(1);
            case "yesterday":
                return LocalDate.now().minusDays(1);
            case "today":
                return LocalDate.now();
            case "next week":
                return LocalDate.now().plusWeeks(1);
            case "next month":
                return LocalDate.now().plusMonths(1);
        }
        DateTimeFormatter[] formatters = { TEXT_DATE_FORMAT1, TEXT_DATE_FORMAT2 };
        for (DateTimeFormatter dateFormat: formatters) {
            try {
                LocalDate date = LocalDate.parse(numerized, dateFormat);
                return date;
            } catch (DateTimeParseException e) {
                //System.err.println("Try different format: " + dateString);
            }
        }
        return null;
    }

    /**
     * replace all date strings with DateTimeFormatter.ISO_LOCAL_DATE format in given text.
     * @param text
     * @return
     */
    public static String datize(String text) {
        String source = Numerizer.numerize(text);
        List<String> words = new ArrayList<>();
        List<String> dateTexts = new ArrayList<>();
        List<Integer> starts = new ArrayList<>();
        Pattern pattern = Pattern.compile(MONTHS + "[\\s]+([0-9]+)");
        Matcher matcher = pattern.matcher(source.toLowerCase());

        while (matcher.find()) {
            String dateText = capitalize(matcher.group(1)) + " " + matcher.group(2);
            dateTexts.add(dateText);
            words.add(matcher.group());
            starts.add(matcher.start());
        }
        pattern = Pattern.compile("([0-9]+)\\s+(of\\s+)?" + MONTHS);
        matcher = pattern.matcher(source.toLowerCase());

        while (matcher.find()) {
            String dateText = matcher.group(1) + " " + capitalize(matcher.group(3));
            dateTexts.add(dateText);
            words.add(matcher.group());
            starts.add(matcher.start());
        }

        pattern = Pattern.compile("(yesterday|today|tomorrow|next week|next month)");
        matcher = pattern.matcher(source.toLowerCase());

        while (matcher.find()) {
            dateTexts.add(matcher.group());
            words.add(matcher.group());
            starts.add(matcher.start());
        }

        StringBuilder builder = new StringBuilder();
        int beg = 0;
        String word = "";
        String dateText = "";
        int start = beg;
        for (int i = 0; i < words.size(); ++i) {
            word = words.get(i);
            dateText = dateTexts.get(i);
            start = starts.get(i);
            if (beg < start) {
                builder.append(source.substring(beg, start));
            }
            beg = start + word.length();
            LocalDate d = parse(dateText);
            builder.append(d.format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
        if (beg < source.length()) {
            builder.append(source.substring(beg));
        }
        return builder.toString();
    }

    private static String capitalize(String month) {
        month = month.substring(0, 1).toUpperCase() + month.substring(1);
        return month;
    }

    /**
     * Find and return all dates as LocalDate from given text.
     * @param text
     * @return
     */
    public static List<LocalDate> findDates(String text) {
        String source = datize(text);
        Pattern pattern = Pattern.compile("(\\d+-\\d+-\\d+)");
        Matcher matcher = pattern.matcher(source);
        List<LocalDate> dates = new ArrayList<>();
        while (matcher.find()) {
            dates.add(LocalDate.parse(matcher.group(1), DateTimeFormatter.ISO_LOCAL_DATE));
        }
        return dates;
    }
}
