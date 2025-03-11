package org.easydate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindDate {
    private static DateTimeFormatter TEXT_DATE_FORMAT = new DateTimeFormatterBuilder()
            .appendPattern("MMMM d")
            .parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear())
            .toFormatter(Locale.ENGLISH);

    /**
     * getting simple text based date formattter.
     * @return
     */
    public static DateTimeFormatter getFormatter() {
        return TEXT_DATE_FORMAT;
    }

    /**
     * parse text based date string.
     * @param dateString
     * @return
     */
    public static LocalDate parse(String dateString) {
        String numerized = Numerizer.numerize(dateString);
        LocalDate date = LocalDate.parse(numerized, TEXT_DATE_FORMAT);
        return date;
    }

    /**
     * replace all date strings with DateTimeFormatter.ISO_LOCAL_DATE format in given text.
     * @param text
     * @return
     */
    public static String datize(String text) {
        String source = Numerizer.numerize(text);
        List<String> words = new ArrayList<>();
        List<Integer> starts = new ArrayList<>();
        Pattern pattern = Pattern.compile("(january|february|march|april|may|june|july|august|september|october|november|december)[\\s]+([0-9]+)");
        Matcher matcher = pattern.matcher(source.toLowerCase());

        while (matcher.find()) {
            words.add(matcher.group(1) + " " + matcher.group(2));
            starts.add(matcher.start(1));
        }
        StringBuilder builder = new StringBuilder();
        int beg = 0;
        String word = "";
        int start = beg;
        for (int i = 0; i < words.size(); ++i) {
            word = words.get(i);
            start = starts.get(i);
            if (beg < start) {
                builder.append(source.substring(beg, start));
            }
            beg = start + word.length();
            word = word.substring(0, 1).toUpperCase() + word.substring(1);
            LocalDate d = parse(word);
            builder.append(d.format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
        if (beg < source.length()) {
            builder.append(source.substring(beg));
        }
        return builder.toString();
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
