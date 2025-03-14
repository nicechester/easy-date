package org.easydate;

import java.io.IOException;
import java.io.InputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FindDate {
    private static DateTimeFormatter TEXT_DATE_FORMAT1 = new DateTimeFormatterBuilder()
            .appendPattern("MMMM d")
            .parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear())
            .toFormatter(Locale.ENGLISH);
    private static DateTimeFormatter TEXT_DATE_FORMAT2 = new DateTimeFormatterBuilder()
            .appendPattern("d MMMM")
            .parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear())
            .toFormatter(Locale.ENGLISH);
    private static DateTimeFormatter HOLIDAY_FORMAT = new DateTimeFormatterBuilder()
            .appendPattern("MM-dd")
            .parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear())
            .toFormatter(Locale.ENGLISH);
    private static final String MONTHS="(january|february|march|april|may|june|july|august|september|october|november|december)";
    private static final HashMap<String, String> HOLIDAYS_ABSOLUTE;
    private static final HashMap<String, RelativeDate> HOLIDAYS_RELATIVE;
    private static final Collection<String> SUPPORTED_HOLIDAYS;
    static {
        SUPPORTED_HOLIDAYS = new HashSet<>();
        HOLIDAYS_ABSOLUTE = new HashMap<>();
        Properties prop = loadProperties("holidays-absolute.properties");
        for (String key : prop.stringPropertyNames()) {
            String holiday = key.toLowerCase().replace('_', ' ');
            SUPPORTED_HOLIDAYS.add(holiday);
            HOLIDAYS_ABSOLUTE.put(holiday, prop.getProperty(key));
        }
        HOLIDAYS_RELATIVE = new HashMap<>();
        prop = loadProperties("holidays-relative.properties");
        for (String key : prop.stringPropertyNames()) {
            String holiday = key.toLowerCase().replace('_', ' ');
            SUPPORTED_HOLIDAYS.add(holiday);
            String[] values = prop.getProperty(key).split(",");
            HOLIDAYS_RELATIVE.put(holiday, RelativeDate.of(values));
        }
    }

    private static Properties loadProperties(String filename) {
        Properties prop = new Properties();
        try (InputStream input = FindDate.class.getClassLoader().getResourceAsStream(filename)) {
            prop.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return prop;
    }

    private static class RelativeDate {
        Month month;
        DayOfWeek dayOfWeek;
        int weekOffset;
        static RelativeDate of(String[] values) {
            RelativeDate relativeDate = new RelativeDate();
            relativeDate.month = Month.valueOf(values[0]);
            relativeDate.dayOfWeek = DayOfWeek.valueOf(values[1]);
            relativeDate.weekOffset = Integer.valueOf(values[2]);
            return relativeDate;
        }
    }

    private static class CharPos {
        int size;
        String dateText;
        static CharPos of(int size, String dateText) {
            CharPos charPos = new CharPos();
            charPos.size = size;
            charPos.dateText = dateText;
            return charPos;
        }
    }

    /**
     * parse text based date string.
     * @param dateString
     * @return
     */
    public static LocalDate parse(String dateString) {
        Optional<LocalDate> holiday = parseHoliday(dateString);
        if (holiday.isPresent()) {
            return holiday.get();
        }
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
        String numerized = Numerizer.numerize(dateString);
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

    private static Optional<LocalDate> parseHoliday(String dateText) {
        String text = dateText.toLowerCase();
        if (HOLIDAYS_ABSOLUTE.containsKey(text)) {
            String holiday = HOLIDAYS_ABSOLUTE.get(text);
            return Optional.of(LocalDate.parse(holiday, HOLIDAY_FORMAT));
        }
        if (HOLIDAYS_RELATIVE.containsKey(text)) {
            RelativeDate relativeDate = HOLIDAYS_RELATIVE.get(text);
            LocalDate date = LocalDate.of(LocalDate.now().getYear(), relativeDate.month, 1)
                    .with(TemporalAdjusters.firstInMonth(relativeDate.dayOfWeek))
                    .plusWeeks(relativeDate.weekOffset);
            return Optional.of(date);
        }
        return Optional.empty();
    }

    public static Collection<String> supportedHolidays() {
        return SUPPORTED_HOLIDAYS;
    }

    /**
     * replace all date strings with DateTimeFormatter.ISO_LOCAL_DATE format in given text.
     * @param text
     * @return
     */
    public static String datize(String text) {
        String source = Numerizer.numerize(text);
        SortedMap<Integer, CharPos> charPos = new TreeMap<>();
        Pattern pattern = Pattern.compile(MONTHS + "[\\s]+([0-9]+)");
        Matcher matcher = pattern.matcher(source.toLowerCase());

        while (matcher.find()) {
            String dateText = capitalize(matcher.group(1)) + " " + matcher.group(2);
            charPos.put(matcher.start(), CharPos.of(matcher.group().length(), dateText));
        }
        pattern = Pattern.compile("([0-9]+)\\s+(of\\s+)?" + MONTHS);
        matcher = pattern.matcher(source.toLowerCase());

        while (matcher.find()) {
            String dateText = matcher.group(1) + " " + capitalize(matcher.group(3));
            charPos.put(matcher.start(), CharPos.of(matcher.group().length(), dateText));
        }

        pattern = Pattern.compile("(yesterday|today|tomorrow|next week|next month|"
                + SUPPORTED_HOLIDAYS.stream().collect(Collectors.joining("|")) + ")");
        matcher = pattern.matcher(source.toLowerCase());

        while (matcher.find()) {
            charPos.put(matcher.start(), CharPos.of(matcher.group().length(), matcher.group()));
        }

        StringBuilder builder = new StringBuilder();
        int beg = 0;
        for (Map.Entry<Integer, CharPos> entry : charPos.entrySet()) {
            int start = entry.getKey();
            if (beg < start) {
                builder.append(source.substring(beg, start));
            }
            beg = start + entry.getValue().size;
            LocalDate d = parse(entry.getValue().dateText);
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
